package com.green.greengream4.dm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class DmInsDto {

    //오토 인크리먼트 값 가겨오기위해 제이슨 이그노어 함
    @JsonIgnore
    private int idm;

    private int loginedIuser;
    private int otherPersonIuser;


}
