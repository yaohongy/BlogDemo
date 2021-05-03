package com.example.BlogDemo.APIcontroller;


import com.example.BlogDemo.config.MyUserDetails;
import com.example.BlogDemo.entities.Post;
import com.example.BlogDemo.entities.User;
import com.example.BlogDemo.service.PostService;
import com.example.BlogDemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/posts")
public class PostAPIController {

    private final PostService postService;
    private final UserService userService;

    @Autowired
    public PostAPIController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping("")
    public ResponseEntity<?> getAllPost(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = Integer.MAX_VALUE + "") int perPage) {
        Page<Post> posts= postService.findAllPage(page, perPage);
        return new ResponseEntity<>(posts.getContent(), HttpStatus.OK);
    }

    @GetMapping("/{post_id}")
    public ResponseEntity<?> getPost(@PathVariable long post_id) {
        Optional<Post> optionalPost = postService.findById(post_id);
        if (optionalPost.isPresent()) {
            return new ResponseEntity<>(optionalPost.get(), HttpStatus.OK);
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not exist");
        }
    }

    @GetMapping("/{username}/posts")
    public ResponseEntity<?> getPostByUser(@PathVariable String username, @RequestParam int page, @RequestParam int perPage) {
        Optional<User> optionalUser = userService.findByUsername(username);
        if (optionalUser.isPresent()) {
            Page<Post> posts = postService.findAllPageByUser(optionalUser.get(), page, perPage);
            return new ResponseEntity<>(posts, HttpStatus.OK);
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not exist");
        }
    }

    @PostMapping("")
    public ResponseEntity<?> savePost(@Valid @RequestBody Post post, Authentication authentication) {
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        User user = myUserDetails.getUser();
        post.setUser(user);
        post.setCreateDate(new Timestamp(System.currentTimeMillis()));
        post = postService.save(post);
        return new ResponseEntity<>(post, HttpStatus.CREATED);
    }

    @SuppressWarnings("ConstantConditions")
    @PutMapping("/{username}/{post_id}")
    @PreAuthorize("(authentication.principal.username == #username) || hasAuthority('ADMIN')")
    public ResponseEntity<?> updatePost(@Valid @RequestBody Post post, @PathVariable String username, @PathVariable long post_id) {
        if (postValidation(username, post_id).getStatusCode() == HttpStatus.OK) {
            Post getPost = (Post) postValidation(username, post_id).getBody();
            post.setId(getPost.getId());
            post.setUser(getPost.getUser());
            post = postService.save(post);
            return new ResponseEntity<>(post, HttpStatus.OK);
        }
        else {
            return postValidation(username, post_id);
        }

    }

    @DeleteMapping("/{username}/{post_id}")
    @PreAuthorize("(authentication.principal.username == #username) || hasAuthority('ADMIN')")
    public ResponseEntity<?> deletePost(@PathVariable String username, @PathVariable long post_id) {
        if (postValidation(username, post_id).getStatusCode() == HttpStatus.OK) {
            Post getPost = (Post) postValidation(username, post_id).getBody();
            postService.delete(getPost);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Post deleted");
        }
        else {
            return postValidation(username, post_id);
        }
    }

    private ResponseEntity<?> postValidation (String username, long post_id) {
        Optional<User> optionalUser = userService.findByUsername(username);
        Optional<Post> optionalPost = postService.findById(post_id);

        if (!optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not exist");
        }
        else if (!optionalPost.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not exist");
        }
        else if (!optionalPost.get().getUser().equals(optionalUser.get())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized operation");
        }
        else {
            Post post = optionalPost.get();
            return new ResponseEntity<>(post, HttpStatus.OK);
        }
    }

}