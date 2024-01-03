package com.green.greengream4.feed;

import com.green.greengream4.feed.model.FeedDelDto;
import com.green.greengream4.feed.model.FeedInsDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

class FeedPicsMapperTest {

    private  FeedInsDto dto;
    public FeedPicsMapperTest(){
        this.dto=new FeedInsDto();
        this.dto.setIfeed(6);

        List<String> pics = new ArrayList<>();
        pics.add("a.jpg");
        pics.add("b.jpg");
        this.dto.setPics(pics);

// List<String> pics 래퍼런스 변수 주소값이라서
        // pics.add("a.jpg");
        // pics.add("b.jpg"); 이래뒤에있어도 같음

    }


    @Autowired
    private FeedPicsMapper mapper;

    @BeforeEach
    public void  beforeEach(){
        FeedDelDto delDto = new FeedDelDto();
        delDto.setIfeed(this.dto.getIfeed());
        delDto.setIuser(2);
        int affectedRows = mapper.delFeedPicsAll(delDto);
        System.out.println("delRows : "+affectedRows);

    }


    @Test
    @DisplayName("이거하나로 인설트 셀렉트 딜리트 전부한거")
    void insFeedPicsTest() {

        List<String> preList = mapper.selFeedPicsAll(dto.getIfeed());
        assertEquals(0,preList.size(),"첫번쨰 insert전 미리 확인");



        int insAffectedRows = mapper.insFeedPics(dto);
        assertEquals(dto.getPics().size(),insAffectedRows);
        assertTrue(insAffectedRows==2);

        List<String> afterList = mapper.selFeedPicsAll(dto.getIfeed());
        assertEquals(dto.getPics().size(),afterList.size());

//        assertEquals(dto.getPics().get(0),afterList.get(0));
//        assertEquals(dto.getPics().get(1),afterList.get(1));

        for (int i = 0; i <dto.getPics().size() ; i++) {
            assertEquals(dto.getPics().get(i),afterList.get(i));
        }

    }


}