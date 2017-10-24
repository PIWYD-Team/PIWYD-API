package com.piwyd.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    FileRepository fileRepository;

    @Autowired
    FileAdapter fileAdapter;

    @Value("${application.file-storage-path}")
    String storagePath;

    @Override
    public FileDto addFile(byte[] file, FileDto fileDto) throws IOException {
        //todo encryption

        String path = storagePath + "/" + fileDto.getFileName() + ".fouan";

        FileOutputStream fos = new FileOutputStream(path);
        fos.write(file);
        fos.close();

        fileDto.setFilePath(path);

        FileEntity fileEntity = fileAdapter.dtoToFile(fileDto);
        FileEntity entitySave = fileRepository.save(fileEntity);

        return fileAdapter.fileToDto(entitySave);
    }

    @Override
    public List<FileDto> getAllFilesByUser(Long id) {
        List<FileDto> filesByUser = fileRepository.findAllByIdOwner(id)
                        .stream()
                .map(u -> fileAdapter.fileToDto(u))
                .collect(Collectors.toList());
        return filesByUser;
    }

    @Override
    public FileDto getFileById(Long id) {
        FileEntity fileEntity = fileRepository.findOneById(id);
        return fileAdapter.fileToDto(fileEntity);
    }
}
