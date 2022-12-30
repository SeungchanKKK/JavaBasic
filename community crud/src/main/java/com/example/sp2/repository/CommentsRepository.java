package com.example.sp2.repository;

import com.example.sp2.model.Comments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentsRepository extends JpaRepository<Comments, Long> {
    List<Comments> findAllByOrderByModifiedAtDesc();

}