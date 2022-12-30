package com.sparta.springassign.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor // 기본생성자를 만듭니다.
@Getter
@Entity // 테이블과 연계됨을 스프링에게 알려줍니다.
public class Memo extends Timestamped { // 생성,수정 시간을 자동으로 만들어줍니다.
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private int pw;

    @Column(nullable = false)
    private String title;


    public Memo(String username, String contents ,int pw, String title) {
        this.username = username;
        this.contents = contents;
        this.pw=pw;
        this.title=title;
    }

    public Memo(com.sparta.springassign.domain.MemoRequestDto requestDto) {
        this.username = requestDto.getUsername();
        this.contents = requestDto.getContents();
        this.pw=requestDto.getPw();
        this.title=requestDto.getTitle();
    }

    public void update(com.sparta.springassign.domain.MemoRequestDto requestDto) {
        this.username = requestDto.getUsername();
        this.pw=requestDto.getPw();
        this.contents = requestDto.getContents();
        this.title=requestDto.getTitle();
    }
}