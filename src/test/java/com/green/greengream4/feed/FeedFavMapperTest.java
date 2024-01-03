package com.green.greengream4.feed;

import com.green.greengream4.feed.model.FeedDelDto;
import com.green.greengream4.feed.model.FeedFavDto;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
//이거 붙여주면 스프링이랑 연동 을시킴
@MybatisTest

// h2로하지말고 기존꺼로 하자
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FeedFavMapperTest {
    //테스트 떄는 생성자 주입 X
    //DI 주소값 내놓으라는거 (bin 객채화 한거 주소값) 주소값 받고싶으면 오토와이어드
    @Autowired
    //싱글톤이라서 단 하나의 유일한 객체를 만들기 위한 코드 패턴
    // MybatisTest 떄문에 mapper들은 전부다 객체화
    private FeedFavMapper mapper;

    @Test
    public void insFeedFav(){
        FeedFavDto dto = new FeedFavDto();
        dto.setIfeed(6);
        dto.setIuser(2);

        // 없었는지 확인하고  셀렉트먼저하고 테이블에 암것도 없어야 인설트 됫는지 확인
        List<FeedFavDto> preResult = mapper.selFeedFavForTest(dto);
        assertEquals(0,preResult.size(),"첫번쨰 insert전  미리 확인");


        //영양받은 행수가 1인지 확인하는거 인설트 딜리트 업데이트 는 영양받은 행수
        int affectedRows1 = mapper.insFeedFav(dto);
        assertEquals(1,affectedRows1,"첫번쨰 insert");

        List<FeedFavDto> result = mapper.selFeedFavForTest(dto);
            assertEquals(1,result.size(),"첫번쨰 insert 확인");


        dto.setIfeed(12);
        dto.setIuser(3);

        int affectedRows2 = mapper.insFeedFav(dto);
        assertEquals(1,affectedRows2);

        List<FeedFavDto> result2 = mapper.selFeedFavForTest(dto);
        assertEquals(1,result2.size());

    }

    @Test
    public void delFeedFav(){

    }


    @Test
    public void delFeedFavTest(){
        FeedFavDto dto = new FeedFavDto();
        dto.setIfeed(8);
        dto.setIuser(2);

        int affectedRows1 = mapper.delFeedFav(dto);
        assertEquals(1,affectedRows1);

        int affectedRows2 = mapper.delFeedFav(dto);
        assertEquals(0,affectedRows2);

        //경우의 수가 2가지
        List<FeedFavDto> list = mapper.selFeedFavForTest(dto);
        assertEquals(0,list.size());
        assertTrue(list.size()==0);
    }

    @Test
    public void delFeedFavAllTest(){
        final int ifeed = 12;

        FeedFavDto selDto = new FeedFavDto();
        selDto.setIfeed(12);

        // 몇개있었는지 체크 확인
        List<FeedFavDto>selList = mapper.selFeedFavForTest(selDto);

        FeedDelDto dto = new FeedDelDto();
        dto.setIfeed(ifeed);
        int delFeedFavAll=mapper.delFeedFavAll(dto);
        assertEquals(selList.size(),delFeedFavAll);

        List<FeedFavDto> selList2 =mapper.selFeedFavForTest(selDto);
        assertEquals(0,selList2.size());


    }

}