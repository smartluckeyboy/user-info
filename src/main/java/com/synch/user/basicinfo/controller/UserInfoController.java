package com.synch.user.basicinfo.controller;


import com.synch.user.basicinfo.dto.UploadFileResponse;
import com.synch.user.basicinfo.dto.UserDetailsDTO;
import com.synch.user.basicinfo.model.User;
import com.synch.user.basicinfo.service.FileStorageService;
import com.synch.user.basicinfo.service.UserService;
import com.synch.user.basicinfo.util.StaticClass;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static com.synch.user.basicinfo.mapper.UserMapper.toAPI;

@RestController
@RequestMapping("/user-info")
@Log4j2
public class UserInfoController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/register")
    public User register(@RequestBody UserDetailsDTO userDetailsDTO){

        return userService.register(userDetailsDTO);
    }

    @GetMapping
    public UserDetailsDTO getUserDetails(){
       long userId= StaticClass.getThreadLocalUserId().get();
        User user=userService.getUserDetails(userId);
        return toAPI(user,fileStorageService.fileLocation());
    }

    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();

        return new UploadFileResponse(fileName, fileDownloadUri,
                file.getContentType(), file.getSize());
    }

    @DeleteMapping("/remove-file/{filePath:.+}")
    public boolean removeFile(@PathVariable String filePath) {
        return fileStorageService.loadFileAsResource(filePath);
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileStorageService.downloadFileResource(StaticClass.getThreadLocalUserId().get()+"\\"+fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
