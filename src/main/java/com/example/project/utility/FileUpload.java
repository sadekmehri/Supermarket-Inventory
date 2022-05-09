package com.example.project.utility;

import com.example.project.exception.ApiRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;


public class FileUpload {

    private final String pathDirectory;
    private final MultipartFile file;
    private final String fileName;

    public FileUpload(String pathDirectory, MultipartFile file) {
        checkFileValidity(file);

        this.file = file;
        this.fileName = file.getOriginalFilename();
        this.pathDirectory = pathDirectory;
    }

    /* Check if the file is not empty and is not null */
    private void checkFileValidity(MultipartFile file) {
        if (file == null)
            throw new ApiRequestException("File not found", HttpStatus.BAD_REQUEST);

        if (file.isEmpty())
            throw new ApiRequestException("The Uploaded file should not be empty ", HttpStatus.BAD_REQUEST);
    }

    /* Generate random name for the existing file */
    private String generateRandomFileName() {
        return fileName.replace(fileName, generateRandomString()) + getFileExtension();
    }

    /* Generate random name */
    private String generateRandomString() {
        return UUID.randomUUID().toString();
    }

    /* Get file extension */
    private String getFileExtension() {
        int lastIndex = fileName.lastIndexOf('.');
        return fileName.substring(lastIndex);
    }

    /* Check allowed file extension from a given list */
    private void isAllowedFileExtension(String allowedFileExtensions) {
        String substring = getFileExtension();

        if (!allowedFileExtensions.contains(substring.toLowerCase()))
            throw new ApiRequestException("Accepted extensions are " + allowedFileExtensions, HttpStatus.BAD_REQUEST);
    }

    /* Save file */
    private void saveFile(String generatedName) {
        try {
            File dir = new File(pathDirectory);
            if (!dir.exists()) dir.mkdir();

            File myFile = new File(pathDirectory + generatedName);
            FileOutputStream fos = new FileOutputStream(myFile);
            fos.write(file.getBytes());
            fos.close();
        } catch (IOException ex) {
            throw new ApiRequestException("Something wrong happened while uploading file", HttpStatus.BAD_REQUEST);
        }
    }

    /* Save file process and return the generated name */
    public String saveProcess(String allowedFileExtensions) {
        isAllowedFileExtension(allowedFileExtensions);
        String generatedName = generateRandomFileName();
        saveFile(generatedName);
        return generatedName;
    }

}
