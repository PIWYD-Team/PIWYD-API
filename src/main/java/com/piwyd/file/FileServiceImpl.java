package com.piwyd.file;

import com.piwyd.crypto.CBCService;
import com.piwyd.crypto.CBCServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.*;
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

    @Autowired
    private HttpSession httpSession;

    @Value("${application.file-storage-path}")
    String storagePath;

    @Value("${spring.jpa.database}")
    String s;

    @Transactional
    @Override
    public FileDto addFile(MultipartFile file) throws IOException, NoSuchAlgorithmException {
        String fileName = file.getOriginalFilename();

        //todo remove commentaire, utiliser userPrivateKey en guise de password
        //String userPrivateKey = (String) httpSession.getAttribute("userPrivateKey");
        String userPrivateKey = "Default password 12345$";

        byte[] process = cbcService.process(CBCServiceImpl.CBCTask.ENCRYPTION, userPrivateKey, file.getInputStream());

        FileOutputStream fos = new FileOutputStream(storagePath + "/" + fileName + ".enc");
        fos.write(process);
        fos.close();

        //todo remove le commentaire -> idOwner
        //Long userId = (Long) httpSession.getAttribute("userId");
        Long userId = 1L;

        FileEntity entityRegistry = fileRepository.save(
                FileEntity.builder()
                        .id(UUID.randomUUID().getMostSignificantBits())
                        .fileName(fileName)
                        .filePath(storagePath + "/" + fileName + ".enc")
                        .idOwner(userId)
                        .build());

        return fileAdapter.fileToDto(entityRegistry);
    }

    @Transactional
    @Override
    public InputStream getFileForDownload(Long id) throws IOException, NoSuchAlgorithmException, FileNotFoundException {
        FileEntity fileEntity= fileRepository.findOneById(id);
        if (fileEntity == null) {
            throw new FileNotFoundException();
        }

        File file = new File(fileEntity.getFilePath());
        InputStream inputStream = new FileInputStream(file);

        //todo remove commentaire, utiliser la user.privateKey pour déchiffré ses fichiers
        //String userPrivateKey = (String) httpSession.getAttribute("userPrivateKey");
        String userPrivateKey = "Default password 12345$";

        byte[] process = cbcService.process(CBCServiceImpl.CBCTask.DECRYPTION, userPrivateKey, inputStream);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(process);

        FileOutputStream fos = new FileOutputStream(storagePath + "/" + "test.txt");
        fos.write(process);
        fos.close();

        return byteArrayInputStream;
    }

    @Override
    public void removeFile(Long id) throws FileNotFoundException {
        FileEntity fileEntity= fileRepository.findOneById(id);
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
        FileEntity fileEntity = fileRepository.findOneById(id);

        if (fileEntity == null) {
            throw new FileNotFoundException();
        }

        return fileAdapter.fileToDto(fileEntity);
    }
}
