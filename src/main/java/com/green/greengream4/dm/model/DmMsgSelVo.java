package com.green.greengream4.dm.model;

import lombok.Data;

@Data
public class DmMsgSelVo {
    // 여러사람이 쓰는정보라서idm은 쓸수없다
    // idm 은 방 seq 은 글
    // 둘다 삭제하고싶을떄는 idm eq둘다 보내야함
    // 프론트에서 알고있어서 이루볼 idm은뻇음
    private int seq;
    private int writerIuser;  //칼럼명 iuser
    private String writerPic; //작성자 프로필사진
    private String msg;
    private String createdAt;
}

