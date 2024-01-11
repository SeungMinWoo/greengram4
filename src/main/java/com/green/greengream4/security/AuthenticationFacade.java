package com.green.greengream4.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component

public class AuthenticationFacade {
    public MyUserDatails getLoginUser() {
        //이건 두줄로 한거 밑에거는 한줄로 줄인거 저런방법이 있따 그런거
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        return   (MyUserDatails) auth.getPrincipal();

        return (MyUserDatails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    public int getLoginUserPk() {

        return getLoginUser().getMyPrincipal().getIuser();
    }
}
