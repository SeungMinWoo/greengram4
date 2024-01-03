package com.green.greengream4.feed;

import com.green.greengream4.feed.model.FeedDelDto;
import com.green.greengream4.feed.model.FeedInsDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FeedPicsMapper {
    int insFeedPics(FeedInsDto p);
    List<String> selFeedPicsAll(int p);
    int delFeedPicsAll(FeedDelDto dto);
}
