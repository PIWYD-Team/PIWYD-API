package com.piwyd.web.rs;

import com.piwyd.file.*;
import com.piwyd.file.FileNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public FileDto uploadFile(@RequestParam("file") MultipartFile file) throws IOException, NoSuchAlgorithmException {
        return fileService.addFile(file);
    }

    @GetMapping("/{fileId}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public FileDto getFile(@PathVariable("fileId") Long id) throws FileNotFoundException {
        return fileService.getFileById(id);
    }

    @GetMapping("/users/{userId}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<FileDto> getFiles(@PathVariable("userId") Long id) {
        return fileService.getAllFilesByUser(id);
    }

    @GetMapping("download/{fileId}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void downloadFile(@PathVariable("fileId") Long id,HttpServletResponse response) throws IOException, NoSuchAlgorithmException, com.piwyd.file.FileNotFoundException {
        final InputStream inputStream = fileService.getFileForDownload(id);
        final FileDto file = fileService.getFileById(id);
        response.setHeader("Content-Disposition", "attachment; filename=" + file.getFileName());
        FileCopyUtils.copy(inputStream, response.getOutputStream());
    }

    @DeleteMapping(value = "/{fileId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFile(@PathVariable("fileId") Long id) throws FileNotFoundException {
        fileService.removeFile(id);
    }
}
