package com.example.sp2.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommentsRequestDto {
    private String username;
    private String contents;
    private String title;
    private Long num;
}

