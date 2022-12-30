package com.example.sp2.controller;

import com.example.sp2.dto.CommentsRequestDto;
import com.example.sp2.model.Comments;
import com.example.sp2.repository.CommentsRepository;
import com.example.sp2.repository.MemoRepository;
import com.example.sp2.security.UserDetailsImpl;
import com.example.sp2.service.CommentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class CommentsController {

    private final MemoRepository memoRepository;
    private final CommentsRepository commentsRepository;
    private final CommentsService commentsService;

    @Controller
    class CommentsViewController {

        @GetMapping("/memo/comments/{id}")
        public String readMemo(Model model, @PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
            if(Optional.ofNullable(userDetails).isPresent()){
                model.addAttribute("memo", memoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지않는 글입니다")));
                model.addAttribute("username",userDetails.getUsername());}
            else {
                model.addAttribute("memo", memoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지않는 글입니다")));
                model.addAttribute("username","guest");
            }
            return "comments";
        }
    }

    @PostMapping("/memo/comments/{id}")
    public String createComments(@RequestBody CommentsRequestDto requestDto, @RequestHeader(value = "token", defaultValue = "token") String token){
        return commentsService.post(requestDto,token);
    }

    @GetMapping("/memo/comments")
    public List<Comments> readComment() {
        return commentsRepository.findAllByOrderByModifiedAtDesc();
    }

    @PutMapping("/memo/comments/{id}")
    public String updateMemo(@PathVariable Long id, @RequestBody CommentsRequestDto requestDto,  @RequestHeader(value = "token", defaultValue = "token") String token) {
        return  commentsService.update(id, requestDto, token);
    }

    @DeleteMapping("/memo/comments/{id}")
    public String deleteMemo(@PathVariable Long id, @RequestBody CommentsRequestDto requestDto,  @RequestHeader(value = "token", defaultValue = "token") String token){
        return commentsService.delete(id,requestDto,token);
    }

}
