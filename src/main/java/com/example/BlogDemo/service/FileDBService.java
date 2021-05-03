package com.example.BlogDemo.service;


import com.example.BlogDemo.Dao.FileDBRepository;
import com.example.BlogDemo.entities.FileCategory;
import com.example.BlogDemo.entities.FileDB;
import com.example.BlogDemo.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class FileDBService {

    private final FileDBRepository fileDBRepository;

    @Autowired
    public FileDBService(FileDBRepository fileDBRepository) {
        this.fileDBRepository = fileDBRepository;
    }



    public FileDB save(MultipartFile file, FileCategory fileCategory, User user) throws IOException {
        assert(file.getOriginalFilename() != null);
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        FileDB fileDB = new FileDB(fileName, file.getContentType(), fileCategory, user, file.getBytes());
        return fileDBRepository.save(fileDB);
    }

    public List<FileDB> getFile(User user, FileCategory category) {
        Optional<List<FileDB>> optionalFileDB = fileDBRepository.findALLByUserAndCategory(user, category);
        return optionalFileDB.orElse(null);
    }

    public FileDB getFile(String id) {
        Optional<FileDB> optionalFileDB = fileDBRepository.findById(id);
        return optionalFileDB.orElse(null);
    }

    public void delete(FileDB fileDB) {
        if (fileDB != null) {
            fileDBRepository.delete(fileDB);
        }
    }
}
