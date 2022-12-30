package com.sparta.springassign.service;


import com.sparta.springassign.domain.Memo;
import com.sparta.springassign.domain.MemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemoService {

    private final MemoRepository memoRepository;

    @Transactional // db 에 꼭 반영되어야함 이라는뜻
    public Long update(Long id, com.sparta.springassign.domain.MemoRequestDto requestDto) {
        Memo memo = memoRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
        memo.update(requestDto);
        return memo.getId();
    }
}