package com.example.BlogDemo.Dao;

import com.example.BlogDemo.entities.FileCategory;
import com.example.BlogDemo.entities.FileDB;
import com.example.BlogDemo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileDBRepository extends JpaRepository<FileDB, String> {

    Optional<List<FileDB>> findALLByUserAndCategory(User user, FileCategory category);

}
