package com.green.greengream4.feed;

import com.green.greengream4.feed.model.FeedCommentInsDto;
import com.green.greengream4.feed.model.FeedCommentSelDto;
import com.green.greengream4.feed.model.FeedCommentSelVo;
import com.green.greengream4.feed.model.FeedDelDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FeedCommentMapper {
    int insFeedComment(FeedCommentInsDto dto);
    List<FeedCommentSelVo> selFeedCommentAll(FeedCommentSelDto dto);
    int delFeedCommentAll(FeedDelDto dto);
}
