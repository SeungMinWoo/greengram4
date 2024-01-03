package com.green.greengream4.feed;

import com.green.greengream4.BaseIntegrationTest;
import com.green.greengream4.common.ResVo;
import com.green.greengream4.feed.model.FeedInsDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FeedIntegrationTest extends BaseIntegrationTest {

    private Object assertEquals;

    @Test
    public void postFeed() throws Exception {

        // @RequestBody 제이슨 형태로만 받는거
        // 통합 테스스 진짜로 이제는 dto에 값이 들어가야함
        // 제이슨 { "iuser" : 1, "contents": "", "pics" : ["a.jpg,b.jpg"]
        FeedInsDto dto = new FeedInsDto();
        dto.setIuser(2);
        dto.setContents("통합 테스트 작업 중");
        dto.setLocation("그린컴퓨터학원");

        //인터페이스 객체화 불가능

        List<String> pics = new ArrayList<>();
        pics.add("https://p4.wallpaperbetter.com/wallpaper/551/184/835/wings-the-demon-rage-horns-wallpaper-preview.jpg");
        pics.add("https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMzAzMDFfMTM0%2FMDAxNjc3Njc5NDYzOTA3.MBqhAJXMNGjhMJhGxD6Ee94Lcp86rgc4rU7qC5l6qscg.QhgUkFgAcgrbKoRGRVkqP35Qulwa_zZDF9_inEGyoCUg.JPEG.aimpain%2F%25BD%25BA%25C6%25F9%25C1%25F6%25B9%25E4.JPG&type=sc960_832");
        dto.setPics(pics);
        String json = om.writeValueAsString(dto);
        System.out.println("json: " + json);

        //mvc통해 요청
        MvcResult mr = mvc.perform(
                        MockMvcRequestBuilders
                                //체이닝 기법 호출뒤에 . 있는건 객체 주소값이 리턴
                                // 1. 리턴메서드 레퍼런스.찍을수있는건 무조건 레퍼런스
                                // 주소 가보니 객체 메서드가 있다 또호출
                                .post("/api/feed")

                                // 제이슨형태로 날림
                                .contentType(MediaType.APPLICATION_JSON)
                                //바디 로 날림contentType
                                // 제이슨 형태라는거 알려줌
                                //재이슨형 태로 받아서 바디로 넘김
                                .content(json)

                )

                //상태코드를 확인하는거 상태코드 200 통신성공 뜻 isok 200
                //덴
                .andExpect(status().isOk())
                // 호출했더니 임무 준거 서비스한테 리턴한게 응답이된다
                //바디에 담겨있는거 내용이 머냐 문자열로 받은 제이슨형태를 기대하고있다

                .andDo(print())
                .andReturn();

        //{"result" : 5}문자열로 확인하는거  문자열 끼리 이게 왔는지
//   mr.getResponse()는 생성된 MVC 테스트 중에 발생한 HTTP 응답을 나타내는 MockHttpServletResponse 객체를 반환합니다.
//   getContentAsString()은 MockHttpServletResponse의 메서드로 응답 내용을 문자열로 반환합니다.
        //객최하시키고 문자열로 변화한다 그걸 스트링 넣는다
        // 프라머티브라 mr.getResponse() 이거뒤에.찍는거 리턴타입이라 . 보이드면 불가
        // String content 여기에있는 스트링은 전체의 결과물
        String content = mr.getResponse().getContentAsString();

        // 스프링 di, ioc  di =  디펜시브 인젝션 di가 할수있는구조가 ioc
        // @Autowired protected ObjectMapper om; 빈등록이 되어 있기떄문에
        // 빈등록 스프링 컨테이너 보고 객체화 하라고 시키고 내가쓰고 싶을떄 스는거
        // 편하게 내가쓸라고 객체화 다시 시키는거

        // content 이거를 제이슨이 멀로 바꼇으면 조켓냐고 하는거
        // 바꾸고 싶은 ResVo.class 이거객체로 바꾼다
        ResVo vo = om.readValue(content, ResVo.class);
        assertEquals(true, vo.getResult() > 0);

        // json 이라는걸 쓰는이유는  내컴퓨터는 자바 요청받고 응답 할껀데 대상이 브라우저 일수도 혹은 서버 일수도있따
        // 부라우저에 들어가는건 자바 스크립트 문자열로 변화해서 주기위해
    }

    @Test
    //false 트랜잭션이 롤백되지 않고 커밋되도록 설정합니다.
    @Rollback(value = true)
    public void delFeed() throws Exception {
        ResVo vo = new ResVo(2);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("ifeed", "16");
        params.add("iuser", "2");

        MvcResult mr = mvc.perform(
                        MockMvcRequestBuilders
                                //.delete("/api/feed?ifeed={ifeed}&iuser={iuser}","220","3")
                                .delete("/api/feed")
                                .params(params))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        // 받는건 무조건 제이슨으로 받기떄문에 제이슨을 스트링으로 바까써 비교함
        String result = mr.getResponse().getContentAsString();
        ResVo vo1 = om.readValue(result, ResVo.class);
        assertEquals(1, vo1.getResult());

    }


}
