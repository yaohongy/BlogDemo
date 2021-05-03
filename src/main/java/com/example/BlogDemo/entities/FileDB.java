package com.example.BlogDemo.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "files")
public class FileDB {
    @Id
    @Column(name = "file_id", nullable = false)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "type", nullable = false)
    private String type;
    @Column(name = "category", nullable = false)
    private FileCategory category;

    public FileDB() {
    }

    public FileDB(String name, String type, FileCategory category, User user, byte[] data) {
        this.name = name;
        this.type = type;
        this.category = category;
        this.user = user;
        this.data = data;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Lob
    @Column(name = "data", nullable = false)
    private byte[] data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public FileCategory getCategory() {
        return category;
    }

    public void setCategory(FileCategory category) {
        this.category = category;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
