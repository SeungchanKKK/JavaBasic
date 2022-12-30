package com.sparta.jpa.repository;

import com.sparta.jpa.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, String> {
}
