package com.green.greengream4.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.K;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
// 빈등록
@Component
public class JwtTokenProvider {
    // secret 암호화 할떄 쓰는키 문자열 넣어줄거임
    private final String secret;
    private final String headerSchemeName;
    private final String tokenType;
    private Key key;

    public JwtTokenProvider(@Value("${springboot.jwt.secret}") String secret
            , @Value("${springboot.jwt.header-scheme-name}") String headerSchemeName
            , @Value("${springboot.jwt.token-type}") String tokenType
    ) {
        this.secret = secret;
        this.headerSchemeName = headerSchemeName;
        this.tokenType = tokenType;
    }

    //  사용할려면 빈등록이 된애만 쓸수있다   빈등록 = 객체화 스프링 컨테이너보고 해달라 하는거
    // 호출하기전에 di부터 이루어진다 그리고 스프링 컨테이너가 호출한다
    // inti 는 호출당한다   메서드 호출하고 싶을때 쓰는거
    @PostConstruct
    public void init() {
        log.info("secret: {}", secret);
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // jwt 만드는거
    public String generateToken(MyPrincipal principal, long tokenValidMs){
        return Jwts.builder()
                .claims(createClaims(principal))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+tokenValidMs))
                .signWith(this.key)
                .compact();
    }
    private Claims createClaims(MyPrincipal principal){
        return  Jwts.claims()
                .add("iuser",principal.getIuser())
                .build();
    }

    public String resolveToken(HttpServletRequest req) {
        String auth = req.getHeader(headerSchemeName);
        if(auth == null) { return null; }

        //Bearer Askladjflaksjfw309jasdklfj
        if(auth.startsWith(tokenType)) {
            return auth.substring(tokenType.length()).trim();
        }
        return null;
        //return auth == null ? null : auth.startsWith();
    }
}
