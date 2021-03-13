package com.example.BlogDemo;

import com.example.BlogDemo.service.PostService;
import com.example.BlogDemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class test {
    @Autowired
    UserService userService;
    @Autowired
    PostService postService;

    public static void getAll() {
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        String body = "";

        HttpEntity<String> requestEntity = new HttpEntity<String>(body, headers);
        ResponseEntity<String> responseEntity = rest.exchange("http://localhost:8080/posts", HttpMethod.GET, requestEntity, String.class);
        HttpStatus httpStatus = responseEntity.getStatusCode();
        int status = httpStatus.value();
        String response = responseEntity.getBody();
        System.out.println("Response status: " + status);
        System.out.println(response);
    }

    public static void add(String title, String contain, int user_id) {
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        StringBuilder sb = new StringBuilder();
        sb.append("{\"title\":\"" + title + "\",\"contain\":\"" + contain + "\",\"user\":{\"id\":\""+ user_id + "\"}}");
        String body = sb.toString();

        HttpEntity<String> requestEntity = new HttpEntity<String>(body, headers);
        ResponseEntity<String> responseEntity = rest.exchange("http://localhost:8080/posts", HttpMethod.POST, requestEntity, String.class);
        HttpStatus httpStatus = responseEntity.getStatusCode();
        int status = httpStatus.value();
        String response = responseEntity.getBody();
        System.out.println("Response status: " + status);
        System.out.println(response);
    }


    public static void main(String[] args) {
        /*
        for (int i = 0; i < 20; i++) {
            String title = "title" + i;
            String contain = "contain" + i;
            add(title, contain,i%2 + 1);
        }
        */

    }

}
