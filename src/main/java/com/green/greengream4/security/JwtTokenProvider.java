package com.green.greengream4.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.greengream4.common.AppProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
@Slf4j
// 빈등록
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    // ObjectMapper : 제이슨 객체 객체에서 제이슨 바까줘는거
    private final ObjectMapper om;
    private final AppProperties appProperties;
    private SecretKeySpec secretKeySpec;


    // secret 암호화 할떄 쓰는키 문자열 넣어줄거임
//    private final String secret;
//    private final String headerSchemeName;
//    private final String tokenType;
//    private Key key;
//
//    public JwtTokenProvider(@Value("${springboot.jwt.secret}") String secret
//            , @Value("${springboot.jwt.header-scheme-name}") String headerSchemeName
//            , @Value("${springboot.jwt.token-type}") String tokenType
//    ) {
//        this.secret = secret;
//        this.headerSchemeName = headerSchemeName;
//        this.tokenType = tokenType;
//    }

    //  사용할려면 빈등록이 된애만 쓸수있다   빈등록 = 객체화 스프링 컨테이너보고 해달라 하는거
    // 호출하기전에 di부터 이루어진다 그리고 스프링 컨테이너가 호출한다
    // inti 는 호출당한다   메서드 호출하고 싶을때 쓰는거
    @PostConstruct
    public void init() {
        this.secretKeySpec=new SecretKeySpec(appProperties.getJwt().getSecret().getBytes()
        , SignatureAlgorithm.HS256.getJcaName());
    }
    public String grnerateAccessToken(MyPrincipal principal){
       return generateToken(principal, appProperties.getJwt().getAccessTokenExpiry());
    }
    public String grnerateRefreshToken(MyPrincipal principal){
        return generateToken(principal, appProperties.getJwt().getRefreshTokenExpiry());

    }


    // jwt 만드는거
    // 엑세스 토큰 리플레이션 토큰 두개 받는다
    // 로그인 하는 부분에서 사용하기
    private String generateToken(MyPrincipal principal, long tokenValidMs) {
        return Jwts.builder()
                .claims(createClaims(principal))
                .issuedAt(new Date(System.currentTimeMillis()))
                // 현재시간 expiration 만료시간 tokenValidMs
                .expiration(new Date(System.currentTimeMillis() + tokenValidMs))
                // 토큰 담기
                .signWith(secretKeySpec)
                .compact();
    }

    // Jwt 클레임을 생성한다
    // Claims 해쉬맵 스타일
    private Claims createClaims(MyPrincipal principal) {
        // writeValueAsString 에러 발생 할수있으니까 뜨로우 던질거다
        // 그러니까 다음 새끼가 예왜 처리해라
        try {
            String json = om.writeValueAsString(principal);
            return Jwts.claims()
                    .add("user", json)
                    .build();

        } catch (Exception e) {
            return null;
        }

    }

    // Jwt 토큰을 추출하는 메서드
    public String resolveToken(HttpServletRequest req) {
        String auth = req.getHeader(appProperties.getJwt().getHeaderSchemeName());
        if (auth == null) {
            return null;
        }

        //Bearer Askladjflaksjfw309jasdklfj
        // 베얼어로 시작하면 트루 시작안하면 펄스
        // appProperties.getJwt().getTokenType() 이부분이 Bearer 부분 그래서  Bearer  가 6개
        // 또 .trim() . 을찍어서 . 은 객체주소값 .만있어서 래퍼런스 변수 리턴한다
        // substring 문자열 짜르기  시작 부터 끝까지
        // .trim 문자열 공백 양쪽 앞에꺼 뒤에꺼 제거
        if (auth.startsWith(appProperties.getJwt().getTokenType())) {
            return auth.substring(appProperties.getJwt().getTokenType().length()).trim();
        }
        return null;
        //return auth == null ? null : auth.startsWith();
    }

    public boolean isValidateToken(String token) {
        try {
            // 만료기간 현재시간보다 전이면 False, 현재시간이 만료기간보다 후이면 false
            // 만료기간 현재시간보다 후면 true, 현재시간이 만료기간보다 전이면 true
            // 현재시간 new Date 이 getExpiration 만료기간 보다 전before
            return !getAllClamims(token).getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    private Claims getAllClamims(String token) {
        return Jwts
                .parser()
                .verifyWith(secretKeySpec)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Authentication getAuthentication(String token){
        UserDetails userDetails = getUserDetailsFromToken(token);


        return  userDetails == null
                ? null
                : new UsernamePasswordAuthenticationToken(userDetails, "",userDetails.getAuthorities());

    }


    public   UserDetails getUserDetailsFromToken(String token) {
        try {
            Claims claims = getAllClamims(token);
            String json = (String) claims.get("user");
            MyPrincipal myPrincipal = om.readValue(json, MyPrincipal.class);
            return  MyUserDatails.builder()
                    .myPrincipal(myPrincipal)
                    .build();
        } catch (Exception e){
            return  null;
        }

    }
}
