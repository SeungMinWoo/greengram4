package com.green.greengream4;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
//같은내용이있어서  상속 받아서 쓸라고
@Slf4j
@Import(CharEncodingConfig.class)
//통합 테스트
//@MockMvcConfig
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
//DB를 H2로 안하고 기존꺼로한다
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BaseIntegrationTest   {
    // protected : 같은 패키지 상속관계면 다른패키지도 가능
    // MockMvc = 포스트맨 통신 시도 해보는거
    @Autowired protected MockMvc mvc;
    // 화면 있으면 안해도됨 화면이 없어서 ObjectMapper 쓰는거
    // ObjectMapper : 객체에서 제이슨 제이슨에서 객체 서로해주는거
    @Autowired protected ObjectMapper om;

}
