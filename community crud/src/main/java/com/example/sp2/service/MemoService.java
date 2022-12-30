package com.example.sp2.service;

import com.example.sp2.dto.MemoRequestDto;
import com.example.sp2.model.Memo;
import com.example.sp2.repository.MemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemoService {

    private final MemoRepository memoRepository;

    private final UserService userService;

    //게시글수정
    @Transactional // db 에 꼭 반영되어야함 이라는뜻
    public String update(Long id, MemoRequestDto requestDto, String token) {
        Memo memo = memoRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
        if(token.equals("token")){
            return "로그인이 필요한 서비스입니다";
        }
        if(userService.decodeUsername(token).equals(requestDto.getUsername())) {
            memo.update(requestDto);
            return "수정완료";
        }
        else {
            return "작성자가아닙니다!";
        }

    }

    //게시글작성
    public String post(MemoRequestDto requestDto, String token) {
        if(token.equals("token")){
            return "로그인이 필요한 서비스입니다";
        }

        if(requestDto.getContents().equals("")){
            return "내용을 입력해주세요!";
        }
        //무작위 토큰인지 확인하는법
        if(userService.decodeUsername(token).equals(requestDto.getUsername())){
            Memo memo = new Memo(requestDto);
            memoRepository.save(memo);
            return  "작성완료!";
        }
        else {
            return "로그인이 필요합니다";
        }
    }

    //게시글삭제
    public String delete(Long id, MemoRequestDto requestDto, String token) {
        if(token.equals("token")){
            return "로그인이 필요한 서비스입니다";
        }
        if(userService.decodeUsername(token).equals(requestDto.getUsername())) {
            memoRepository.deleteById(id);
            return "삭제완료!";
        }
        else {
            return "작성자가아닙니다!";
        }
    }
}