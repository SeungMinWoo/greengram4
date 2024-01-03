package com.green.greengream4.feed;

import com.green.greengream4.feed.model.FeedDelDto;
import com.green.greengream4.feed.model.FeedFavDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FeedFavMapper {



    int insFeedFav(FeedFavDto dto);

    //널인지 아닌지만 체크 내부적으로 테스트 하는 용도
    List<FeedFavDto> selFeedFavForTest(FeedFavDto dto);

    int delFeedFav(FeedFavDto dto);
    int delFeedFavAll(FeedDelDto dto);
}
