package com.example.BlogDemo.controller;

import com.example.BlogDemo.entities.FileCategory;
import com.example.BlogDemo.entities.FileDB;
import com.example.BlogDemo.entities.User;
import com.example.BlogDemo.service.FileDBService;
import com.example.BlogDemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.IOUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Controller
public class FileDBController {

    private final FileDBService fileDBService;
    private final UserService userService;

    @Autowired
    public FileDBController(FileDBService fileDBService, UserService userService) {
        this.fileDBService = fileDBService;
        this.userService = userService;
    }

    @PostMapping("/users/{userName}/portrait/upload")
    public String uploadPortrait(@PathVariable String userName, @RequestParam("file") MultipartFile file, Model model) {
        Optional<User> optionalUser = userService.findByUsername(userName);
        if (optionalUser.isPresent()) {
            List<FileDB> files = fileDBService.getFile(optionalUser.get(), FileCategory.PORTRAIT);
            FileDB fileDB;
            try {
                if (files.size() > 0) {
                    fileDBService.delete(files.get(0));
                }
                fileDB = fileDBService.save(file, FileCategory.PORTRAIT, optionalUser.get());
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("message", "Could not upload file");
                return "message";
            }
            return "redirect:/users/" + userName;
        }
        else {
            model.addAttribute("message", "Could not find user");
            return "message";
        }
    }

    @PostMapping("/users/{userName}/gallery/upload")
    public String uploadGallery(@PathVariable String userName, @RequestParam("file") MultipartFile file, Model model) {
        Optional<User> optionalUser = userService.findByUsername(userName);
        if (optionalUser.isPresent()) {
            try {
                fileDBService.save(file, FileCategory.GALLERY, optionalUser.get());
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("message", "Could not upload file");
                return "message";
            }
            return "redirect:/users/" + userName + "/gallery";
        }
        else {
            model.addAttribute("message", "Could not find user");
            return "message";
        }
    }

    @GetMapping(value = "/users/{userName}/portrait", consumes = MediaType.ALL_VALUE, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> showPortraitImg(@PathVariable String userName) {
        Optional<User> optionalUser = userService.findByUsername(userName);
        if (optionalUser.isPresent()) {
            List<FileDB> files = fileDBService.getFile(optionalUser.get(), FileCategory.PORTRAIT);
            if (files.size() > 0) {
                return ResponseEntity.ok(files.get(0).getData());
            }
            else {
                return ResponseEntity.ok(new ClassPathResource("static/img/default-portrait.jpg"));
            }

        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such user");
        }
    }

    @GetMapping(value = "/users/{userName}/gallery/{id}", consumes = MediaType.ALL_VALUE, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> showImage(@PathVariable String userName, @PathVariable String id) {
        Optional<User> optionalUser = userService.findByUsername(userName);
        if (optionalUser.isPresent()) {
            FileDB file = fileDBService.getFile(id);
            return ResponseEntity.ok(file.getData());
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such user");
        }
    }
}
