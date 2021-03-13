package com.example.BlogDemo.service;

import com.example.BlogDemo.Dao.CommentRepository;
import com.example.BlogDemo.entities.Comment;
import com.example.BlogDemo.entities.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Optional<Comment> findById(long id) {
        return commentRepository.findById(id);
    }

    public Page<Comment> findByPost(Post post, int page, int perPage) {
        return commentRepository.findByPostOrderByCreateDate(post, PageRequest.of(page, perPage));
    }

    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    public void delete(Comment comment) {
        commentRepository.delete(comment);
    }

}
