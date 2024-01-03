package com.green.greengream4.feed;

import com.green.greengream4.common.ResVo;
import com.green.greengream4.feed.model.FeedCommentInsDto;
import com.green.greengream4.feed.model.FeedCommentSelDto;
import com.green.greengream4.feed.model.FeedCommentSelVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feed/comment")
public class FeedCommentController {
    private final FeedCommentService service;

    @PostMapping
    public ResVo postFeedComment(@RequestBody FeedCommentInsDto dto) {
        log.info("dto: {}", dto);
        return service.postFeedComment(dto);
    }

    @GetMapping
    public List<FeedCommentSelVo> getFeedCommentAll(FeedCommentSelDto dto) { //4~999까지의 레코드만 리턴 될 수 있도록
        return service.getFeedCommentAll(dto);
    }

}
