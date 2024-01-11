package com.green.greengream4.feed;

import com.green.greengream4.common.Const;
import com.green.greengream4.common.ResVo;
import com.green.greengream4.feed.model.*;
import com.green.greengream4.security.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedService {
    private final FeedMapper mapper;
    private final FeedPicsMapper picsMapper;
    private final FeedFavMapper favMapper;
    private final FeedCommentMapper commentMapper;
    private final AuthenticationFacade authenticationFacade;

    public ResVo postFeed(FeedInsDto dto) {


        dto.setIuser(authenticationFacade.getLoginUserPk());
        int feedAffectedRows = mapper.insFeed(dto);
        int feedPicsAffectedRows = picsMapper.insFeedPics(dto);
        return new ResVo(dto.getIfeed());
    }

    public List<FeedSelVo> getFeedAll(FeedSelDto dto) {
        List<FeedSelVo> list = mapper.selFeedAll(dto);

        //페이징 처리 하기위해 객체를생성해서 메서드를 불러오기위한애
        FeedCommentSelDto fcDto = new FeedCommentSelDto();
        fcDto.setStartIdx(0);
        fcDto.setRowCount(Const.FEED_COMMENT_FIRST_CNT);

        for(FeedSelVo vo : list) {
            List<String> pics = picsMapper.selFeedPicsAll(vo.getIfeed());
            // 각객체안에 사진들을 담기위해서
            vo.setPics(pics);

            //댓글의 내용의 ifeed 를 게시글 내용의 ifeed를 가져온다
            fcDto.setIfeed(vo.getIfeed());
            //저기안에 fcDto 를 넣는 이유는 메서드 호출했을때 그매개변수 타입이 같은거 받기위해
            // 메서드를 통해 조회된 피드 댓글 목록이 들어가게 됩니다.

            List<FeedCommentSelVo> comments = commentMapper.selFeedCommentAll(fcDto);
            vo.setComments(comments);

            if(comments.size() == Const.FEED_COMMENT_FIRST_CNT) {
                //댓글이 더 있는지 여부를 나타내는 플래그
                vo.setIsMoreComment(1);
                // 더보기를 눌렀을떄 가상의 페이지가 추가되서 그페이지를 삭제하기위해
                comments.remove(comments.size() - 1);
            }
        }
        return list;
    }

    public ResVo delFeed(FeedDelDto dto) {
        //1 이미지
        int picsAffectedRows = picsMapper.delFeedPicsAll(dto);
        if(picsAffectedRows == 0) {
            return new ResVo(Const.FAIL);
        }

        //2 좋아요
        int favAffectedRows = favMapper.delFeedFavAll(dto);

        //3 댓글
        int commentAffectedRows = commentMapper.delFeedCommentAll(dto);

        //4 피드
        int feedAffectedRows = mapper.delFeed(dto);
        return new ResVo(Const.SUCCESS);
    }

    //--------------- t_feed_fav
    public ResVo toggleFeedFav(FeedFavDto dto) {
        //ResVo - result값은 삭제했을 시 (좋아요 취소) 0, 등록했을 시 (좋아요 처리) 1
        int delAffectedRows = favMapper.delFeedFav(dto);
        if(delAffectedRows == 1) {
            return new ResVo(Const.FEED_FAV_DEL);
        }
        int insAffectedRows = favMapper.insFeedFav(dto);
        return new ResVo(Const.FEED_FAV_ADD);
    }
}
