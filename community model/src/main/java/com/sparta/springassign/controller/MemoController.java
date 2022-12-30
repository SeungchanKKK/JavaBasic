package com.sparta.springassign.controller;

import com.sparta.springassign.domain.Memo;
import com.sparta.springassign.domain.MemoRepository;
import com.sparta.springassign.domain.MemoRequestDto;
import com.sparta.springassign.service.MemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class MemoController {
    private final MemoRepository memoRepository;
    private final MemoService memoService;

    @PostMapping("/api/memos")
    public Memo createMemo(@RequestBody  MemoRequestDto requestDto){
        Memo memo = new Memo(requestDto);
        return memoRepository.save(memo);
    }

    @GetMapping("/api/memos")
    public List<Memo> readMemo() {
        return memoRepository.findAllByOrderByModifiedAtDesc();
    }


    @DeleteMapping("/api/memos/{id}")
    public Long deleteMemo(@PathVariable Long id ){
        memoRepository.deleteById(id);
        return id;
    }

    @GetMapping("/api/memos/{id}")
    public int bringpw(@PathVariable Long id){
        Memo memo =memoRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
        int pw= memo.getPw();
        return pw;
    }

    @PutMapping("/api/memos/{id}")
    public Long updateMemo(@PathVariable Long id, @RequestBody MemoRequestDto requestDto) {
        return memoService.update(id, requestDto);
    }
}
