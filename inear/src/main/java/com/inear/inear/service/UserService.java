package com.inear.inear.service;



import com.inear.inear.controller.model.*;
import com.inear.inear.exception.AppleLoginException;
import com.inear.inear.model.Users;
import com.inear.inear.repository.UserRepository;
import com.inear.inear.service.apple.AppleJwtUtils;
import com.inear.inear.service.jwt.JwtService;
import com.inear.inear.service.kakao.KakaoAccessToken;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;



@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AppleJwtUtils appleJwtUtils;

    public PostSignUpAndSignInRes kakaoSignUpAndSignIn(PostKakaoSignUpAndSignInReq postKakaoSignUpAndSignInReq) throws IOException {

        KakaoAccessToken accessToken = new KakaoAccessToken(postKakaoSignUpAndSignInReq.getAccessToken());

        return new PostSignUpAndSignInRes(dbInsertAndGetJwt(accessToken.getSnsId()));
    }

    private String dbInsertAndGetJwt(String snsId) {
        Users user = getSnsIdInDB(snsId);
        if(user != null){
            return jwtService.createJwt(user.getUserId());
        }
        return jwtService.createJwt(userRepository.save(new Users(snsId)).getUserId());
    }

    private Users getSnsIdInDB(String snsId) {
        return userRepository.findBySnsId(snsId);
    }


    public PostSignUpAndSignInRes appleSignUpAndSignIn(PostAppleSignUpAndSignInReq postAppleSignUpAndSignInReq) throws AppleLoginException {

        Claims token = this.appleJwtUtils.getClaimsBy(postAppleSignUpAndSignInReq.getIdToken());

        return new PostSignUpAndSignInRes(dbInsertAndGetJwt(String.valueOf(token.get("sub"))));

    }


    public CheckJwtRes checkJwt() {
        try{
            jwtService.getUserId();
            return new CheckJwtRes(true);
        } catch (JwtException e) {
            log.info(e.getMessage());
            return new CheckJwtRes(false);
        }
    }
}
