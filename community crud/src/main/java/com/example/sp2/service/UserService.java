package com.example.sp2.service;

import com.example.sp2.dto.SignupRequestDto;
import com.example.sp2.model.Users;
import com.example.sp2.repository.UserRepository;
import com.example.sp2.security.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final int SEC = 1;
    private static final int MINUTE = 60 * SEC;
    private static final int HOUR = 60 * MINUTE;
    private static final int DAY = 24 * HOUR;
    // JWT 토큰의 유효기간: 3일 (단위: seconds)
    private static final int JWT_TOKEN_VALID_SEC = 3 * DAY;
    // JWT 토큰의 유효기간: 3일 (단위: milliseconds)
    private static final int JWT_TOKEN_VALID_MILLI_SEC = JWT_TOKEN_VALID_SEC * 1000;
    public static final String CLAIM_USER_NAME = "USER_NAME";
    public static final String JWT_SECRET = "jwt_secret_!@#$%";
    private static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;


    public UserService(UserRepository userRepository,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String registerUser(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        // 회원 ID 중복 확인
        Optional<Users> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            return "중복된 사용자 ID 가 존재합니다.";
        }

        if(!Pattern.matches("^([a-zA-Z0-9]{3,9})$", username)){
            return "최소 3자 이상,9자 이하 알파벳 대소문자(a~z, A~Z), 숫자(0~9)로 해야합니다";
        }

        //비밀번호 확인절차
        String ps= requestDto.getPassword();
        String psC=requestDto.getPasswordCheck();

        if(ps.length()<4){
            return "비밀번호는 4글자이상이어야합니다";
        }

        if(ps.equals(username)){
            return "아이디와같으면안됩니다";
        }

        if(!ps.equals(psC)){
            return "비밀번호가 일치하지않습니다";
        }

// 패스워드 암호화
        String password = passwordEncoder.encode(requestDto.getPassword());

// 사용자 ROLE 확인


        Users user = new Users(username,password);
        userRepository.save(user);
        return "회원가입완료!";
    }

    public String jwt(UserDetailsImpl userDetails) {

        String token = null;
        Map<String, Object> payloads = new HashMap<>();
        payloads.put(CLAIM_USER_NAME, userDetails.getUsername());
        try {
            token = Jwts.builder()
                    .setIssuer("sparta")
                    .setSubject(CLAIM_USER_NAME)
                    .setClaims(payloads)
                    .signWith(signatureAlgorithm, getSecretKeySpec(DatatypeConverter.parseBase64Binary(JWT_SECRET)))
                    .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALID_MILLI_SEC))
                    .compact();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return token;
    }
    public String decodeUsername(String token) {
        Claims claimMap = null;
        try {
            claimMap = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(JWT_SECRET)) // Set Key
                    .parseClaimsJws(token) // 파싱 및 검증, 실패 시 에러
                    .getBody();

            //Date expiration = claims.get("exp", Date.class);
            //String data = claims.get("data", String.class);
        } catch (ExpiredJwtException e) { // 토큰이 만료되었을 경우
            System.out.println(e);
        } catch (Exception e) { // 그외 에러났을 경우
            System.out.println(e);
        }
        System.out.println(claimMap.get(CLAIM_USER_NAME).toString());
        return claimMap.get(CLAIM_USER_NAME).toString();
    }

    public static SecretKeySpec getSecretKeySpec(byte[] secretKeyBytes) {
        return new SecretKeySpec(secretKeyBytes, signatureAlgorithm.getJcaName());
    }

}


