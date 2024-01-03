package com.green.greengream4.dm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.green.greengream4.common.Const;
import com.green.greengream4.common.ResVo;
import com.green.greengream4.dm.model.*;
import com.green.greengream4.user.UserMapper;
import com.green.greengream4.user.model.UserEntity;
import com.green.greengream4.user.model.UserSelDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class DmService {

 private final DmMapper mapper;
 private final UserMapper userMapper;
 private final ObjectMapper objMapper;
    public  List<DmSelVo> getDmAll(DmSelDto dto){
        return mapper.selDmAll(dto);
    }

    public ResVo postDmMsg(DmMsgInsDto dto) {
        int insAffectedRows = mapper.insDmMsg(dto);
        //last msg update
        if(insAffectedRows == 1) {
            int updAffectedRows = mapper.updDmLastMsg(dto);
        }
        LocalDateTime now = LocalDateTime.now(); // 현재 날짜 구하기
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // 포맷 정의
        String createdAt = now.format(formatter); // 포맷 적용

        //상대방의 firebaseToken값 필요. 나의 pic, iuser값 필요.
        UserEntity otherPerson = mapper.selOtherPersonByLoginUser(dto);

        try {

            if(otherPerson.getFirebaseToken() != null) {
                DmMsgPushVo pushVo = new DmMsgPushVo();
                pushVo.setIdm(dto.getIdm());
                pushVo.setSeq(dto.getSeq());
                pushVo.setWriterIuser(dto.getLoginedIuser());
                pushVo.setWriterPic(dto.getLoginedPic());
                pushVo.setMsg(dto.getMsg());
                pushVo.setCreatedAt(createdAt);

                //object to json
                String body = objMapper.writeValueAsString(pushVo);
                log.info("body: {}", body);
                Notification noti = Notification.builder()
                        .setTitle("dm")
                        .setBody(body)
                        .build();

                Message message = Message.builder()
                        .setToken(otherPerson.getFirebaseToken())

                        .setNotification(noti)
                        .build();

                FirebaseMessaging.getInstance().sendAsync(message);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResVo(dto.getSeq());
    }

    public DmSelVo postDm(DmInsDto dto){
        // 빈 방 만들기 idm 값 생성
        mapper.insDm(dto);
        // 로그인한 유저 해당 방에 참여 시키는 과정
        mapper.insDmUser(DmUserInsDto.builder()
                // 빈 방 생성시 만들어진 idm값 가져옴
                        .idm(dto.getIdm())
                        .iuser(dto.getLoginedIuser())
                .build());
        log.info("dto{}",dto);

        // 다른 유저 해당 방에 참여
        mapper.insDmUser(DmUserInsDto.builder()
                // 빈 방 생성시 만들어진 idm값 가져옴
                        .idm(dto.getIdm())
                //postman에서 입력받은 상대 iuser값 가져옴
                        .iuser(dto.getOtherPersonIuser())
                // 여기까지 새로 생성된 채팅방에 유저 집어넣기완료
                .build());

        // UserMapper.xml에 있는 selUser를 실행하기 위해 객체생성
// 이녀석이 없으면 쿼리문이 인식을 못해서 오류가난다 유저정보를 알아내려면 누구인지 특정이 되어야한다.
        UserSelDto usDto = new UserSelDto();
        // 새로운객체 usDto에 dto에서 입력받은 상대 iuser값 가져옴
        usDto.setIuser(dto.getOtherPersonIuser());

        // 다른 유저의 정보 얻어오기
        UserEntity entity = userMapper.selUser(usDto);
        // 빌더로 값을 불러온다.
        return DmSelVo.builder()
                // 위에서 생성된 방번호 가져오기
                .idm(dto.getIdm())
                // 다른 유저의 iuser 가져오기
                .otherPersonIuser(entity.getIuser())
                // 다른 유저의 nm 가져오기
                .otherPersomNm(entity.getNm())
                // 다른 유저의 사진 가져오기
                .otherPersonPic(entity.getPic())
                .build();
    }


    public List<DmMsgSelVo> getMsgAll(DmMsgSelDto dto){
        return mapper.selDmMsgAll(dto);
    }

    public ResVo delDmMsg(DmMsgDelDto dto){
        int result= mapper.delMsg(dto);
        if (result==0){
            return new ResVo(Const.FAIL);
        }   return new ResVo(Const.SUCCESS);

//        int delAffectedRows = mapper.delMsg(dto);
//        if(delAffectedRows == 1) {
//            int updAffectedRows = mapper.updDmLastMsgAfterDelByLastMsg(dto);
//        }
//        return new ResVo(delAffectedRows);
    }
}