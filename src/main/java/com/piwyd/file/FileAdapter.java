package com.piwyd.file;

import org.springframework.stereotype.Component;

@Component
public class FileAdapter {

    public FileDto fileToDto(FileEntity fileEntity) {
        return FileDto.builder()
                .id(fileEntity.getId())
                .fileName(fileEntity.getFileName())
                .idOwner(fileEntity.getIdOwner())
                .build();
    }

    public FileEntity dtoToFile(FileDto fileDto) {
        return FileEntity.builder()
                .id(fileDto.getId())
                .fileName(fileDto.getFileName())
                .idOwner(fileDto.getIdOwner())
                .build();
    }
}
