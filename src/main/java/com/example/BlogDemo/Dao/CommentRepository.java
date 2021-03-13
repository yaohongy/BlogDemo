package com.example.BlogDemo.Dao;

import com.example.BlogDemo.entities.Comment;
import com.example.BlogDemo.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByPostOrderByCreateDate(Post post, Pageable pageable);

}

