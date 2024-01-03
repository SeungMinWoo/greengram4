package com.green.greengream4.feed;

import com.green.greengream4.common.Const;
import com.green.greengream4.common.ResVo;
import com.green.greengream4.feed.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//스프링 컨테이너 올라오게끔 하기위해
// 이거 안하면 오토와이어드 해서 못씀
@ExtendWith(SpringExtension.class)
// 빈등록 시켜주기위해서 서비스할떄는 아래꺼만 수정하면됨
@Import({FeedService.class})
class FeedServiceTest {

    // 가짜 빈 주소값이 있는것처럼 속인다
    @MockBean
    private FeedMapper mapper;
    @MockBean
    private FeedPicsMapper feedPicsMapper;
    @MockBean
    private FeedFavMapper favMapper;
    @MockBean
    private FeedCommentMapper commentMapper;
    // 이건 진짜 빈 아래꺼 객체화할려면 다른mapper의 주소값이 필요함 그래서 가짜만듬
    @Autowired
    private FeedService service;

    @Test
    void postFeed() {
        //  진짜로 할필요없고 가짜야 any(애니 )먼가 들어갈거다 주입이 될거다
        when(mapper.insFeed(any())).thenReturn(1);
        when(feedPicsMapper.insFeedPics(any())).thenReturn(2);

        FeedInsDto dto = new FeedInsDto();
        // 값을비교할떄 저거안넣으면 비교를못해서
        dto.setIfeed(100);
        ResVo vo = service.postFeed(dto);
        assertEquals(dto.getIfeed(), vo.getResult());

        // 메퍼에있는 메서드를 호출했는지 any = 아무파라미터나
        verify(mapper).insFeed(any());
        verify(feedPicsMapper).insFeedPics(any());


        FeedInsDto dto2 = new FeedInsDto();
        dto2.setIfeed(200);
        ResVo vo2 = service.postFeed(dto2);
        assertEquals(dto2.getIfeed(), vo2.getResult());


    }

    @Test
    public void getFeedAll() {
        //1. 가짜들 임무부여부터
        FeedSelVo feedSelVo1 = new FeedSelVo();
        feedSelVo1.setIfeed(1);
        feedSelVo1.setContents("일번 feedSelVo");

        FeedSelVo feedSelVo2 = new FeedSelVo();
        feedSelVo2.setIfeed(2);
        feedSelVo2.setContents("이번 feedSelVo");


        // FeedSelVo 객체 주소값 담을수 있는애
        // 주소값은 8바이트
        List<FeedSelVo> list = new ArrayList<>();
        list.add(feedSelVo1);
        list.add(feedSelVo2);


        // 경비원 한테 시범으로 총으로 쏴바라한거 검증
        // 이거하는순간 가짜방에 쏘는게아니라 리스트방에 쏘는게된다
        when(mapper.selFeedAll(any())).thenReturn(list);


        //배열을 리스트로 바꿀떄 스트림쓴다
        List<String> feed1Pics = Arrays.stream(new String[]{"a.jpg", "b.jpg"}).toList();

        List<String> feed2Pics = new ArrayList<>();
        feed2Pics.add("가.jpg");
        feed2Pics.add("나.jpg");

        List<List<String>> picsList = new ArrayList<>();
        picsList.add(feed1Pics);
        picsList.add(feed2Pics);


        List<String>[] picsArr = new List[2];
        picsArr[0] = feed1Pics;
        picsArr[1] = feed2Pics;


        when(feedPicsMapper.selFeedPicsAll(1)).thenReturn(feed1Pics);
        when(feedPicsMapper.selFeedPicsAll(2)).thenReturn(feed2Pics);

        //--------------------------ifeed(1) 댓글


        List<FeedCommentSelVo> cmtsFeed = new ArrayList<>();


        FeedCommentSelVo feedCommentSelVo = new FeedCommentSelVo();
        feedCommentSelVo.setIfeedComment(1);
        feedCommentSelVo.setComment("살라살라");

        FeedCommentSelVo feedCommentSelVo2 = new FeedCommentSelVo();
        feedCommentSelVo.setIfeedComment(2);
        feedCommentSelVo.setComment("누네즈");

        // 2개 주소값 담아서 cmtsFeed 이거 안에 2개있다.
        cmtsFeed.add(feedCommentSelVo);
        cmtsFeed.add(feedCommentSelVo2);
        //commentMapper.selFeedCommentAll(fcDto); 이거담실수있는값이 3개
        // 파라미터로 나오는값이 똑같아야함

        FeedCommentSelDto fcDto1 = new FeedCommentSelDto();



        //--------------------------ifeed(2) 댓글

        List<FeedCommentSelVo> cmtsFeed2 = new ArrayList<>();


        FeedCommentSelVo feedCommentSelVo3 = new FeedCommentSelVo();
        feedCommentSelVo.setIfeedComment(3);
        feedCommentSelVo.setComment("덕배");

        FeedCommentSelVo feedCommentSelVo4 = new FeedCommentSelVo();
        feedCommentSelVo.setIfeedComment(4);
        feedCommentSelVo.setComment("홀란");

        FeedCommentSelVo feedCommentSelVo5 = new FeedCommentSelVo();
        feedCommentSelVo.setIfeedComment(5);
        feedCommentSelVo.setComment("마레즈");

        FeedCommentSelVo feedCommentSelVo6 = new FeedCommentSelVo();
        feedCommentSelVo.setIfeedComment(6);
        feedCommentSelVo.setComment("디아스");



        cmtsFeed2.add(feedCommentSelVo3);
        cmtsFeed2.add(feedCommentSelVo4);
        cmtsFeed2.add(feedCommentSelVo5);
        cmtsFeed2.add(feedCommentSelVo6);

        // 니가받았을떄 리턴은 4개짜리해돌라





        FeedCommentSelDto dto1 = new FeedCommentSelDto();
        dto1.setStartIdx(0);
        dto1.setRowCount(Const.FEED_COMMENT_FIRST_CNT);
        dto1.setIfeed(2);
        when(commentMapper.selFeedCommentAll(dto1)).thenReturn(cmtsFeed2);

        FeedSelDto dto = new FeedSelDto();
        List<FeedSelVo> result = service.getFeedAll(dto);


        //본인이 시킨일 을 똑바로했는지 보는거list테스트 result실제값
        assertEquals(list, result);


        // 위에꼐 트루라서 아래꼐 무의미 해짐
        for (int i = 0; i < list.size(); i++) {
            //리절트가 참이기떄문에 굳이 필요없다
            FeedSelVo pVo = list.get(i);
            assertNotNull(pVo.getPics());

            List<String> pics = picsList.get(i);
            assertEquals(pVo.getPics(), pics);


            List<String> pics2 = picsArr[i];
            assertEquals(pVo.getPics(), pics2);
        }
        List<FeedCommentSelVo> commentsResult1 = list.get(0).getComments();
        assertEquals(cmtsFeed,commentsResult1,"ifeed(1)댓글 체크");
        assertEquals(0,list.get(0).getIsMoreComment(),"ifeed(1)isMoreComment 체크");
        assertTrue(commentsResult1.size()==2);

        List<FeedCommentSelVo> commentsResult2 = list.get(1).getComments();
        assertEquals(cmtsFeed2,commentsResult2,"ifeed(2) 댓글 체크");
        assertEquals(1,list.get(1).getIsMoreComment(),"ifeed(2) isMoreComment 체크");
        assertTrue(commentsResult2.size()==3);

    }

    @Test
    void delFeed() {
    }

    @Test
    void toggleFeedFav() {
    }
}