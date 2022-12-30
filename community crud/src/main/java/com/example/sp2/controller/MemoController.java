package com.example.sp2.controller;

import com.example.sp2.dto.MemoRequestDto;
import com.example.sp2.model.Memo;
import com.example.sp2.repository.MemoRepository;
import com.example.sp2.service.MemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class MemoController {
    private final MemoRepository memoRepository;
    private final MemoService memoService;

    @PostMapping("/api/memos")
    public String createMemo(@RequestBody MemoRequestDto requestDto, @RequestHeader(value = "token", defaultValue = "token") String token){
        return memoService.post(requestDto,token);
    }

    @GetMapping("/api/memos")
    public List<Memo> readMemo() {
        return memoRepository.findAllByOrderByModifiedAtDesc();
    }

    @DeleteMapping("/api/memos/{id}")
    public String deleteMemo(@PathVariable Long id, @RequestBody MemoRequestDto requestDto,  @RequestHeader(value = "token", defaultValue = "token") String token){
        return memoService.delete(id,requestDto,token);
    }

    @PutMapping("/api/memos/{id}")
    public String updateMemo(@PathVariable Long id, @RequestBody MemoRequestDto requestDto,  @RequestHeader(value = "token", defaultValue = "token") String token) {
        return  memoService.update(id, requestDto, token);
    }
}
