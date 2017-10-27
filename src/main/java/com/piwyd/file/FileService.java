package com.piwyd.file;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface FileService {
    @Transactional(readOnly = true)
    List<FileDto> getAllFilesByUser(Long id);

    FileDto getFileById(Long id) throws com.piwyd.file.FileNotFoundException;

    FileDto addFile(MultipartFile file) throws IOException, NoSuchAlgorithmException;

    InputStream getFileForDownload(Long id) throws IOException, NoSuchAlgorithmException, com.piwyd.file.FileNotFoundException;

    void removeFile(Long id) throws com.piwyd.file.FileNotFoundException;
}
