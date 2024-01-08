package com.green.greengream4.security;

import jakarta.servlet.http.HttpServlet;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@RequiredArgsConstructor
public class SecutiyConfiguration {

    @Bean
    // 인가  특정 리소스에 접근할 수 있는 권한을 부여하는 것
    // httpServlet 객체 주소값 들어옴 스프링 컨테이너가 알아서해줌
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(http -> http.disable())
                //화면이없으면 필요가없다 그래서 끈다 프론트에 데이터만 주기떄문에 필요없어
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.requestMatchers("/api/user/sign-in"
                        , "/api/user/sign_up"
                        , "/eroor"
                        , "/err"
                        ,"/"
                        ,"/index.html"
                        ,"/static/**"
                        ,"/swagger.html"
                        ,"/swagger-ui/**"
                        ,"/v3/api-docs/**"
                ).permitAll()
                                //.anyRequest().authenticated()
                )
                .build();


    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        // BCryptPasswordEncoder 이거말고 다른게 나왔으면 이거뺴고 수정하면됨
        return new BCryptPasswordEncoder();

    }
}
