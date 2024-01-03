package com.green.greengream4.dm.model;

import lombok.Data;

@Data
public class DmMsgDelDto {
    private int idm;
    private int iuser;
    //내가 쓴글인지 알고싶어서
    private int seq;
}
