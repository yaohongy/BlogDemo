package com.example.BlogDemo.service;

import com.example.BlogDemo.Dao.PostRepository;
import com.example.BlogDemo.entities.Post;
import com.example.BlogDemo.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service("postService")
public class PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Optional<Post> findById(long id) {
        return postRepository.findById(id);
    }

    public Post save(Post post) {
        return postRepository.save(post);
    }

    public Page<Post> findAllPage(int page, int perPage) {
        return postRepository.findAllByOrderByCreateDateDesc(PageRequest.of(page, perPage));
    }

    public Page<Post> findAllPageByUser(User user, int page, int perPage) {
        return postRepository.findByUserOrderByCreateDateDesc(
                user, PageRequest.of(page, perPage)
        );
    }

    public void delete(Post post) {
        postRepository.delete(post);;
    }

}
