package com.green.greengream4.security;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Builder

public class MyPrincipal {
    private int iuser;
}
