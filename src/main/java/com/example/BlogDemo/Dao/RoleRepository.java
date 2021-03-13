package com.example.BlogDemo.Dao;

import com.example.BlogDemo.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByRole(@Param("role") String role);
}
