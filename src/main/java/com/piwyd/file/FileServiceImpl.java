package com.piwyd.file;

import com.piwyd.crypto.CBCService;
import com.piwyd.crypto.CBCServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private FileAdapter fileAdapter;

    @Autowired
    private CBCService cbcService;

    @Value("${application.file-storage-path}")
    String storagePath;

    @Value("${application.encryption-extension}")
	String encryptionExtension;

    @Value("${spring.jpa.database}")
    String s;

    @Transactional
    @Override
    public FileDto addFile(MultipartFile file, Long idUser, String userPrivateKey) throws IOException, NoSuchAlgorithmException {
        String filename = file.getOriginalFilename();
        String path = storagePath + "/" + filename + encryptionExtension;
        File newFile = new File(path);

        if (newFile.exists()) {
        	throw new InvalidParameterException("Un fichier avec le même nom existe déjà !");
		}

        byte[] process = cbcService.process(CBCServiceImpl.CBCTask.ENCRYPTION, userPrivateKey, file.getInputStream());

        File uploadDir = new File(storagePath);
        if (!uploadDir.exists() && !uploadDir.mkdir()) {
        	throw new IOException("Une erreur technique est survenue");
		}

        FileOutputStream fos = new FileOutputStream(path);
        fos.write(process);
        fos.close();

        FileEntity entityRegistry = fileRepository.save(
                FileEntity.builder()
                        .id(UUID.randomUUID().getMostSignificantBits())
                        .name(filename)
                        .path(path)
                        .idOwner(idUser)
                        .build());

        return fileAdapter.fileToDto(entityRegistry);
    }

    @Transactional
    @Override
    public InputStream getFileForDownload(Long id, String userPrivateKey) throws IOException, NoSuchAlgorithmException, FileNotFoundException {
        FileEntity fileEntity = fileRepository.findOne(id);
        if (fileEntity == null) {
            throw new FileNotFoundException();
        }

        File file = new File(fileEntity.getPath());
        InputStream inputStream = new FileInputStream(file);

        byte[] process = cbcService.process(CBCServiceImpl.CBCTask.DECRYPTION, userPrivateKey, inputStream);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(process);

        /** DEBUG **
        FileOutputStream fos = new FileOutputStream(storagePath + "/" + "test.txt");
        fos.write(process);
        fos.close();
		** FIN DEBUG */

        return byteArrayInputStream;
    }

    @Override
    public void removeFile(Long id) throws FileNotFoundException {
        FileEntity fileEntity= fileRepository.findOne(id);
        if (fileEntity == null) {
            throw new FileNotFoundException();
        }
        fileRepository.delete(fileEntity);
    }

    @Transactional
    @Override
    public List<FileDto> getAllFilesByUser(Long id) {
        List<FileDto> filesByUser = fileRepository.findAllByIdOwner(id)
				.stream()
                .map(u -> fileAdapter.fileToDto(u))
                .collect(Collectors.toList());
        return filesByUser;
    }

    @Transactional
    @Override
    public FileDto getFileById(Long id) throws FileNotFoundException {
        FileEntity fileEntity = fileRepository.findOne(id);

        if (fileEntity == null) {
            throw new FileNotFoundException();
        }

        return fileAdapter.fileToDto(fileEntity);
    }
}
