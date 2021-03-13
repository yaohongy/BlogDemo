package com.example.BlogDemo.crudTest;

import com.example.BlogDemo.entities.Post;
import com.example.BlogDemo.entities.User;
import com.example.BlogDemo.service.PostService;
import com.example.BlogDemo.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class PostTest {

    @Autowired
    PostService postService;
    @Autowired
    UserService userService;

    @Test
    public void createAndGetTest() {
        User user = new User();
        user.setUsername("test_user");
        user.setPassword("1234567890");
        user.setEmail("test@gmail.com");
        user.setActive(1);
        user = userService.save(user);

        Post post = new Post();
        post.setTitle("test title");
        post.setContent("test content");
        post.setUser(user);
        post = postService.save(post);

        Optional<Post> optionalPost = postService.findById(post.getId());

        Post getPost = null;
        if (optionalPost.isPresent()) {
            getPost = optionalPost.get();
        }

        Assertions.assertEquals(post, getPost);

        postService.delete(post);
        userService.delete(user);
    }
}
