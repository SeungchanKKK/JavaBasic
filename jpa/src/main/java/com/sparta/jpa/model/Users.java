package com.sparta.jpa.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Setter
@Getter // get 함수를 일괄적으로 만들어줍니다.
@NoArgsConstructor // 기본 생성자를 만들어줍니다.
@Entity // DB 테이블 역할을 합니다.
public class Users {
    // nullable: null 허용 여부
// unique: 중복 허용 여부 (false 일때 중복 허용)
    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = false)
    private String nickname;

    @Column(nullable = false, unique = false)
    private String favoriteFood;

    public Users(String username, String nickname, String favoriteFood) {
        this.username = username;
        this.nickname = nickname;
        this.favoriteFood = favoriteFood;
    }
}
