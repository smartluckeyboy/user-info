package com.synch.user.basicinfo.service;

import com.synch.user.basicinfo.dto.FileStorageProperties;
import com.synch.user.basicinfo.exceptions.FileStorageException;
import com.synch.user.basicinfo.exceptions.MyFileNotFoundException;
import com.synch.user.basicinfo.util.StaticClass;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class FileStorageService {

    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        long userId=0;
        File directory=null;
        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            log.debug("file location : "+fileStorageLocation);
            if(StaticClass.getThreadLocalUserId().get()!=null){
                 userId=StaticClass.getThreadLocalUserId().get();
                String fullLocation = fileStorageLocation + "\\" + userId;
                 directory = new File(fullLocation);
            }
            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(userId+"\\"+fileName);
            if (! directory.exists()){
                directory.mkdir();
            }
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public boolean loadFileAsResource(String fileName)  {
        long userId=0;
        Path filePath=null;
        try {
            if(StaticClass.getThreadLocalUserId().get()!=null){
                userId=StaticClass.getThreadLocalUserId().get();
                 filePath = this.fileStorageLocation.resolve(userId+"\\"+fileName).normalize();
            }else {
                 filePath = this.fileStorageLocation.resolve(fileName).normalize();
            }

            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return Files.deleteIfExists(filePath);
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        } catch (IOException e) {
            log.error("File does not exist");
        }
        return false;
    }

    public Resource downloadFileResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }

    public String fileLocation()  {
      long userId=StaticClass.getThreadLocalUserId().get();
        File directoryPath = new File(this.fileStorageLocation.toUri()+String.valueOf(userId)+"\\");
        //List of all files and directories
        File filesPath = directoryPath.getAbsoluteFile();
        List<File> files = null;
        Path filePath = this.fileStorageLocation.resolve(userId+"\\").normalize();
        try {
            files = Files.list(filePath)
                    .map(Path::toFile)
                    .filter(File::isFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return files.get(0).toString();
    }
}