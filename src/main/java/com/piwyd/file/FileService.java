package com.piwyd.file;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

public interface FileService {
    FileDto addFile(byte[] file, FileDto fileDto) throws IOException;

    @Transactional(readOnly = true)
    List<FileDto> getAllFilesByUser(Long id);

    FileDto getFileById(Long id);
}
