package com.example.BlogDemo.controller;

import com.example.BlogDemo.config.MyUserDetails;
import com.example.BlogDemo.entities.FileCategory;
import com.example.BlogDemo.entities.FileDB;
import com.example.BlogDemo.entities.Post;
import com.example.BlogDemo.entities.User;
import com.example.BlogDemo.service.FileDBService;
import com.example.BlogDemo.service.PostService;
import com.example.BlogDemo.service.UserService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    private final UserService userService;
    private final PostService postService;
    private final FileDBService fileDBService;

    @Autowired
    public UserController(UserService userService, PostService postService, FileDBService fileDBService) {
        this.userService = userService;
        this.postService = postService;
        this.fileDBService = fileDBService;
    }

    @GetMapping("/users/{username}")
    @PreAuthorize("(authentication.principal.username == #username) || hasAuthority('ADMIN')")
    public String getUser(@PathVariable String username, Model model) {
        Optional<User> userOptional = userService.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Page<Post> postPage = postService.findAllPageByUser(user, 0, 10);
            List<Post> posts = postPage.getContent();
            model.addAttribute("posts", posts);
            model.addAttribute("user", user);
            return "/users";
        }
        else {
            model.addAttribute("error", "User not exist");
            return "/404";
        }
    }

    @PostMapping("/users/{username}")
    @PreAuthorize("(authentication.principal.username == #username) || hasAuthority('ADMIN')")
    public String editUser(@PathVariable String username, @ModelAttribute("user") User editUser, Model model) {

        Optional<User> userOptional = userService.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (editUser.getEmail() != null) user.setEmail(editUser.getEmail());
            if (editUser.getPassword() != null)user.setPassword(editUser.getPassword());
            System.out.println(user);
            userService.save(user);
            model.addAttribute("message", "User info updated");
            return "/message";
        }
        else {
            model.addAttribute("error", "User not exist");
            return "/404";
        }
    }

    @GetMapping("/users/new")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "/register";
    }

    @PostMapping("/users/new")
    public String createUser(@Valid @ModelAttribute User user, BindingResult bindingResult, Model model)  {
        if (bindingResult.hasErrors()) {
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            model.addAttribute("errors", allErrors);
            return "/register";
        }
        user.setActive(1);
        user = userService.save(user);
        model.addAttribute("user", user);
        return "/users";
    }

    @RequestMapping("/username_check")
    @ResponseBody
    public String usernameCheck(@RequestParam String username) {
        if (userService.usernameCheck(username)) {
            return "Username is exist, choose another name";
        }
        else {
            return "Username is available";
        }
    }

    @GetMapping("/posts/new")
    @PreAuthorize("hasAuthority('USER')")
    public String newPost(Model model) {
        model.addAttribute("post", new Post());
        return "/newpost";
    }

    @GetMapping("/users/{userName}/gallery")
    public String userGallery(@PathVariable String userName, Model model) {
        Optional<User> optionalUser = userService.findByUsername(userName);
        if (optionalUser.isPresent()) {
            List<FileDB> files = fileDBService.getFile(optionalUser.get(), FileCategory.GALLERY);
            List<String> ids = new ArrayList<>();
            for (FileDB file : files) {
                ids.add(file.getId());
            }
            model.addAttribute("user", optionalUser.get());
            model.addAttribute("image_id", ids);
            return "/gallery";
        }
        else {
            model.addAttribute("message", "User not exist");
            return "/message";
        }
    }

    @PostMapping("/posts/new")
    @PreAuthorize("hasAuthority('USER')")
    public String postPost(@Valid @ModelAttribute Post post, Authentication authentication, Model model) {
        MyUserDetails myUserDetails = (MyUserDetails)authentication.getPrincipal();
        User user = myUserDetails.getUser();
        post.setUser(user);
        post.setCreateDate(new Timestamp(System.currentTimeMillis()));
        postService.save(post);
        model.addAttribute("message", "Posted with title " + post.getTitle());
        return "/message";
    }

}
