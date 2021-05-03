package com.example.BlogDemo.controller;

import com.example.BlogDemo.entities.Post;
import com.example.BlogDemo.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {

    private final PostService postService;

    @Autowired
    public HomeController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/home")
    public String home(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int perPage, Model model, Authentication authentication) {
        Page<Post> postPage = postService.findAllPage(page,perPage);
        int totalPage = postPage.getTotalPages();
        List<Post> posts = postPage.getContent();
        model.addAttribute("posts", posts);
        model.addAttribute("page", page);
        model.addAttribute("perPage", perPage);
        model.addAttribute("totalPage", totalPage);
        return "/home";
    }

    @GetMapping("/login")
    public String showLoginForm(@RequestParam(defaultValue = "false") boolean error, Model model) {
        model.addAttribute("error", error);
        return "/login";
    }

}
