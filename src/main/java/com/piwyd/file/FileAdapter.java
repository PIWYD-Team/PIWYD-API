package com.piwyd.file;

import org.springframework.stereotype.Component;

@Component
public class FileAdapter {

    public FileDto fileToDto(FileEntity fileEntity) {
        return FileDto.builder()
                .id(fileEntity.getId().toString())
                .name(fileEntity.getName())
                .idOwner(fileEntity.getIdOwner().toString())
                .build();
    }

    public FileEntity dtoToFile(FileDto fileDto) {
        return FileEntity.builder()
                .id(Long.parseLong(fileDto.getId()))
                .name(fileDto.getName())
                .idOwner(Long.parseLong(fileDto.getIdOwner()))
                .build();
    }
}
