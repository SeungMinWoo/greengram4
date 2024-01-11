package com.green.greengream4.security;

import lombok.*;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Builder
// 제이슨에서 객체화 가 되는 친구가 MyPrincipal 이거니까 기본생성자가 필요함
@NoArgsConstructor
// 빌드까지 쓰고싶으면  AllArgsConstructor 이거쓰면됨
@AllArgsConstructor

// 토큰에 집어 넣을려고 쓰는거
public class MyPrincipal {
    private int iuser;
}
