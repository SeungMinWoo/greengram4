package com.green.greengream4.dm;

import com.green.greengream4.common.ResVo;
import com.green.greengream4.dm.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dm")
public class DmController {
    private final  DmService service;

    @GetMapping
     public  List<DmSelVo> getDmAll(DmSelDto dto){
        return service.getDmAll(dto);
    }

    @PostMapping
    public DmSelVo postDm(@RequestBody DmInsDto dto){
        return service.postDm(dto);
    }
    @PostMapping("/msg")
    public ResVo postDmMsg(@RequestBody DmMsgInsDto dto){
        return service.postDmMsg(dto);
    }

    @GetMapping("/msg")
    // 리퀘스트 파랑 리콰이어드 트루가 기본값 객체로 받을떄는 트루 가되면 안됨
    // 객체가 있을떄는 리퀘스트 파람 못씁니다
    // 객채 가 있을떄는 객채앞에 리퀘스트 앞에 쓰면안된다
    // int String 인경우에는 @RequestParm 있는거랑 같은효과)
    // 겟 매핑일시 객체일경우 리퀘스트 쓰지마라
    public List<DmMsgSelVo> getMsgAll(DmMsgSelDto dto){
        log.info("dot:{}",dto);
        return service.getMsgAll(dto);

    }

    @DeleteMapping("/msg")
    public ResVo delDmMsg(DmMsgDelDto dto){
        return service.delDmMsg(dto);
    }
}
