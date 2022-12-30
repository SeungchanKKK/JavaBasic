package com.sparta.springassign.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemoRepository extends JpaRepository<com.sparta.springassign.domain.Memo, Long> {
    List<com.sparta.springassign.domain.Memo> findAllByOrderByModifiedAtDesc();

}
