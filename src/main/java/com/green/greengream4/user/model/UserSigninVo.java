package com.green.greengream4.user.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSigninVo {
    private final int result;
    private int iuser;
    private String nm;
    private String pic;
    private String firebaseToken;
    // 에세스 토큰만들어서 프론트에 알려주기위해
    private String accessToken;
}