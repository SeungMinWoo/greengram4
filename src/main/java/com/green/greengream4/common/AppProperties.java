package com.green.greengream4.common;

import io.jsonwebtoken.Jwt;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
// prefix 야물 파일에 있는 제일 앞에있는거
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private final Jwt jwt = new Jwt();

    @Getter
    @Setter
    // 클래스 안에 있는 클래스  이너클래스 내부에만 쓰는 클래스
    public class Jwt {
        private String secret;
        private String headerSchemeName;
        private String tokenType;
        private long accessTokenExpiry;
        private long refreshTokenExpiry;
        private int refershTokenCookieMaxAge;
        private int setRefershTokenCookieMaxAge;

        public void  setRefreshTokenExpiry(long refreshTokenExpiry){
            this.refreshTokenExpiry = refreshTokenExpiry;
            this.setRefershTokenCookieMaxAge=(int)refreshTokenExpiry/1000;
        }
    }
}
