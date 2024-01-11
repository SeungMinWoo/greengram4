package com.green.greengream4.user;

import com.green.greengream4.common.AppProperties;
import com.green.greengream4.common.Const;
import com.green.greengream4.common.CookieUtils;
import com.green.greengream4.common.ResVo;
import com.green.greengream4.security.JwtTokenProvider;
import com.green.greengream4.security.MyPrincipal;
import com.green.greengream4.security.MyUserDatails;
import com.green.greengream4.user.model.*;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AppProperties appProperties;
    private final CookieUtils cookieUtils;

    public ResVo signup(UserSignupDto dto) {
//        String salt = BCrypt.gensalt();
//        String hashedPw = BCrypt.hashpw(dto.getUpw(), salt);

        String hashedPw = passwordEncoder.encode(dto.getUpw());
                //비밀번호 암호화

        UserSignupProcDto pDto = new UserSignupProcDto();
        pDto.setUid(dto.getUid());
        pDto.setUpw(hashedPw);
        pDto.setNm(dto.getNm());
        pDto.setPic(dto.getPic());

        log.info("before - pDto.iuser : {}", pDto.getIuser());
        int affectedRows = mapper.insUser(pDto);
        log.info("after - pDto.iuser : {}", pDto.getIuser());

        return new ResVo(pDto.getIuser()); //회원가입한 iuser pk값이 리턴
    }

    public UserSigninVo signin(HttpServletRequest req, HttpServletResponse res, UserSigninDto dto) {


        UserSelDto sDto = new UserSelDto();
        sDto.setUid(dto.getUid());

        UserEntity entity = mapper.selUser(sDto);
        if (entity == null) {
            return UserSigninVo.builder().result(Const.LOGIN_NO_UID).build();
            //  } else if (!BCrypt.checkpw(dto.getUpw(), entity.getUpw())) {
        }   else  if (!passwordEncoder.matches(dto.getUpw(), entity.getUpw())){
            return UserSigninVo.builder().result(Const.LOGIN_DIFF_UPW).build();
        }
        MyPrincipal myPrincipal = MyPrincipal.builder()
                                            .iuser(entity.getIuser())
                                            .build();

//        String at = jwtTokenProvider.generateToken(myPrincipal, appProperties.getJwt().getAccessTokenExpiry());

//        String rt = jwtTokenProvider.generateToken(myPrincipal, appProperties.getJwt().getRefresgTokenExpiry());


        String at = jwtTokenProvider.grnerateAccessToken(myPrincipal);
        String rt = jwtTokenProvider.grnerateRefreshToken(myPrincipal);

        //rt > cookie에 담을꺼임
        int rtCookieMaxAge = (int)appProperties.getJwt().getRefreshTokenExpiry() / 1000;
        cookieUtils.deleteCookie( res, "rt");
        cookieUtils.setCookie(res,"rt",rt ,rtCookieMaxAge);


        return UserSigninVo.builder()
                .result(Const.SUCCESS)
                .iuser(entity.getIuser())
                .nm(entity.getNm())
                .pic(entity.getPic())
                .accessToken(at)
                .build();
    }
    public ResVo signout( HttpServletResponse res){
       cookieUtils.deleteCookie(res,"rt");
        return new ResVo(1);
    }

    public  UserSigninVo getRefreshToken(HttpServletRequest req){
        Cookie cookie = cookieUtils.getCookie(req,"rt");
        if (cookie ==null){
            return  UserSigninVo.builder()
                    .result(Const.FAIL)
                    .accessToken(null)
                    .build();
        }
        String token = cookie.getValue();
        if (!jwtTokenProvider.isValidateToken(token)){
            return UserSigninVo.builder()
                    .result(Const.FAIL)
                    .accessToken(null)
                    .build();

        }

        MyUserDatails myUserDatails = (MyUserDatails) jwtTokenProvider.getUserDetailsFromToken(token);
        //엑세스 토큰 만들려면 마이프리서플 만들어야함
        MyPrincipal myPrincipal = myUserDatails.getMyPrincipal();
        String at = jwtTokenProvider.grnerateAccessToken(myPrincipal);


        return UserSigninVo.builder()
                .result(Const.SUCCESS)
                .accessToken(at)
                .build();
    }


    public UserInfoVo getUserInfo(UserInfoSelDto dto) {
        return mapper.selUserInfo(dto);
    }

    public ResVo patchUserFirebaseToken( UserFirebaseTokenPatchDto dto) {
        int affectedRows = mapper.updUserFirebaseToken(dto);
        return new ResVo(affectedRows);
    }

    public ResVo patchUserPic(UserPicPatchDto dto) {
        int affectedRows = mapper.updUserPic(dto);
        return new ResVo(affectedRows);
    }

    public ResVo toggleFollow(UserFollowDto dto) {
        int delAffectedRows = mapper.delUserFollow(dto);
        if (delAffectedRows == 1) {
            return new ResVo(Const.FAIL);
        }
        int insAffectedRows = mapper.insUserFollow(dto);
        return new ResVo(Const.SUCCESS);
    }
}
