package com.green.greengream4.dm;

import com.green.greengream4.dm.model.*;
import com.green.greengream4.user.model.UserEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DmMapper {
    //----------------------------- t_dm
    int insDm(DmInsDto dto);
    List<DmSelVo> selDmAll(DmSelDto dto);
    UserEntity selOtherPersonByLoginUser(DmMsgInsDto dto);
    int updDmLastMsg(DmMsgInsDto dto);
    int updDmLastMsgAfterDelByLastMsg(DmMsgDelDto dto);
    //------------------------------ t_dm_user
    int insDmUser(DmUserInsDto dto);
    Integer selDmUserCheck(DmInsDto dto);
    //------------------------------ t_dm_msg
    int insDmMsg(DmMsgInsDto dto);
    List<DmMsgSelVo> selDmMsgAll(DmMsgSelDto dto);
    //------------------------------ del
    int delMsg(DmMsgDelDto dto);



}
