package com.green.greengream4.common;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.InputStream;

@Slf4j
@Configuration
public class FirebaseConfiguration {

    @Value("${fcm.certification}")
    private String googleApplicationCredentials;

    @PostConstruct
    public void init() {
        //파일을 읽어 온다음에 스트림으로 불러온다
        //밑에는 파이어베이스 옵션으로 빌더를 이용해 내용을 집어넣는다
        //파이어베이스 연결된 앱을 가져오고 넣는다
        try {
            InputStream serviceAccount =
                    new ClassPathResource(googleApplicationCredentials).getInputStream();
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            if (FirebaseApp.getApps().isEmpty()) {
                log.info("FirebaseApp Initialization Complete !!!");
                FirebaseApp.initializeApp(options);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}