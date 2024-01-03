package com.green.greengream4.feed;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.greengream4.CharEncodingConfig;
import com.green.greengream4.common.ResVo;
import com.green.greengream4.feed.model.FeedInsDto;
import com.green.greengream4.feed.model.FeedSelVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//이걸로인해서 객체가 메모리에 올라와있음
//@MockMvcConfig
@Import(CharEncodingConfig.class)

@WebMvcTest(FeedController.class)
class FeedControllerTest {

    //제이슨 형태의문자열을 다시 객체 제이슨형태로
    @Autowired private MockMvc mvc;
    // 가짜 주소값이 들어가야한다 (주소값이 들어와야 오류가안뜸)
    @MockBean private FeedService service;
    @Autowired private ObjectMapper mapper;

    @Test
    void postFeed() throws Exception {
        //when
        ResVo result = new ResVo(5);
       // when(service.postFeed(any())).thenReturn(result);
        //가짜한테 임무준거
        // ResVO를담고있는거5 리턴
        given(service.postFeed(any())).willReturn(result);

        FeedInsDto dto = new FeedInsDto();
        String json = mapper.writeValueAsString(dto);
        System.out.println("json: " +json);

        //메서드 호출
        mvc.perform(
                MockMvcRequestBuilders
                        .post("/api/feed")
                        //체이닝 기법 호출뒤에 . 있는건 객체 주소값이 리턴
                        // 1. 리턴메서드 레퍼런스.찍을수있는건 무조건 레퍼런스
                        // 주소 가보니 객체 메서드가 있다 또호출
                        .contentType(MediaType.APPLICATION_JSON)
                        //바디 로 날림
                        .content(mapper.writeValueAsString(dto))

        )
                //재이슨형 태로 받아서 바디로 넘김
                //상태코드를 확인하는거 상태코드 200 통신성공 뜻 isok 200
                //덴
                .andExpect(status().isOk())
                // 호출했더니 임무 준거 서비스한테 리턴한게 응답이된다
                //바디에 담겨있는거 내용이 머냐 문자열로 받은 제이슨형태를 기대하고있다

                //{"result" : 5}문자열로 확인하는거  문자열 끼리 이게 왔는지
                .andExpect(content().string(mapper.writeValueAsString(result)))
                .andDo(print());

                verify(service).postFeed(any());
    }


    @Test
    void getFeedAll()throws  Exception {
        // Controller에 있는 getFeedAll메서드의 리턴타입 맞추기위해 객체생성
        List<FeedSelVo> voList = new ArrayList<>();
        // FeedSelVo에 있는 Ifeed랑 Contents를 담아서 voList에 넣기위해 객체생성
        FeedSelVo vo = new FeedSelVo();
        // ifeed에 7 보냄
        vo.setIfeed(7);
        // contents에 "7"을 보냄
        vo.setContents("안녕하세여ㅛ");

        FeedSelVo vo1 = new FeedSelVo();
        vo1.setIfeed(1);
        vo1.setContents("jkl");

        // voList에 ifeed랑 contents 값넣은거 vo주소값 를 넣어줌
        voList.add(vo);
        voList.add(vo1);

        // service.getFeedAll이 실행되면 voList가 리턴되게함
        given(service.getFeedAll(any())).willReturn(voList);

        // Controller에 있는 getFeedAll에 있는 파라미터 FeedSelDto 접근하기위한 params생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page","3");  // 입력
        params.add("loginedIuser","4"); // 입력

        mvc.perform(
                    MockMvcRequestBuilders
                            .get("/api/feed") //메서드주소
                            .params(params)) //파라미터값
                // 상태 200 응답했을 때 = 응답성공 200
                .andExpect(status().isOk())
                // 결과 72번에 있는 VoList와 같아야한다.
                .andExpect(content().string(mapper.writeValueAsString(voList)))
                .andDo(print());  // 아래 결과창에 출력문 프린트

        // Controller에 있는 return service.FeedSelAll이 실행되면 성공.
        verify(service).getFeedAll(any());

    }

    @Test
    void delFeed() throws Exception {
        ResVo vo = new ResVo(4);
        // 가짜로 이게 되는지 안되는지 확인하기위해
        given(service.delFeed(any())).willReturn(vo);

        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("ifeed","1");
        params.add("iuser","2");

        mvc.perform(
                        MockMvcRequestBuilders
                                .delete("/api/feed") //메서드주소
                                .params(params)) //파라미터값
                // 상태 200 응답했을 때 = 응답성공 200
                .andExpect(status().isOk())
                // 결과 72번에 있는 VoList와 같아야한다.
                .andExpect(content().string(mapper.writeValueAsString(vo)))
                .andDo(print());  // 아래 결과창에 출력문 프린트
        verify(service).delFeed(any());
    }

    @Test
    void toggleFeedFav() {
    }
}