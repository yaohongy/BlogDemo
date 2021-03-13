package com.example.BlogDemo.crudTest;

import com.example.BlogDemo.service.PostService;
import com.example.BlogDemo.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserTest {

    @Autowired
    UserService userService;
    @Autowired
    PostService postService;

    @Test
    public void createAndGetTest() {
    }


}
