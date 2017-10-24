package com.piwyd.web.rs;

import com.piwyd.file.FileDto;
import com.piwyd.file.FileService;
import com.piwyd.web.rs.form.FileTransfert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
@RequestMapping("/api/files")
public class FileController {

    private Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    FileService fileService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public FileDto uploadFile(@RequestBody @Valid FileTransfert fileTransfert) throws IOException {

        FileDto newFile = fileService.addFile(fileTransfert.getFile(),
                FileDto.builder()
                        .fileName(fileTransfert.getFileName())
                        .idOwner(fileTransfert.getIdOwner())
                        .build());

        return newFile;
    }

    @GetMapping("/{fileId}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public FileDto getUser(@PathVariable("fileId") Long id) {
        final FileDto fileById = fileService.getFileById(id);
        return fileById;
    }

    @GetMapping("/users/{userId}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<FileDto> getFiles(@PathVariable("userId") Long id) {
        List<FileDto> filesByUser = fileService.getAllFilesByUser(id);
        return filesByUser;
    }
}
