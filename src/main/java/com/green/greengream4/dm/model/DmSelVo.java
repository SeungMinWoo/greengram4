package com.green.greengream4.dm.model;

import lombok.Builder;
import lombok.Data;
@Builder
@Data
public class DmSelVo {
    private int idm;
    private String lastMsg;
    private String lastMsgAt;
    private int otherPersonIuser;
    private String otherPersomNm;
    private String otherPersonPic;
}
