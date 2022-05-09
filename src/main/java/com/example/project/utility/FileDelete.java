package com.example.project.utility;

import com.example.project.exception.ApiRequestException;
import org.springframework.http.HttpStatus;

import java.io.File;


public class FileDelete {

    private final String pathDirectory;
    private final String fileName;

    public FileDelete(String pathDirectory, String fileName) {
        this.pathDirectory = pathDirectory;
        this.fileName = fileName;
    }

    /* Delete file process */
    public void deleteProcess() {
        File file = new File(pathDirectory + fileName);

        if (!file.delete())
            throw new ApiRequestException("Something wrong happened while uploading file", HttpStatus.BAD_REQUEST);
    }

}
