package com.inear.inear.service;



import com.inear.inear.controller.model.CheckJwtRes;
import com.inear.inear.controller.model.PostKakaoSignUpAndSignInReq;
import com.inear.inear.controller.model.PostKakaoSignUpAndSignInRes;
import com.inear.inear.model.Users;
import com.inear.inear.repository.UserRepository;
import io.jsonwebtoken.JwtException;
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

    public PostKakaoSignUpAndSignInRes kakaoSignUpAndSignIn(PostKakaoSignUpAndSignInReq postKakaoSignUpAndSignInReq) throws IOException {

        KakaoAccessToken accessToken = new KakaoAccessToken(postKakaoSignUpAndSignInReq.getAccessToken());

        return getPostKakaoSignUpAndSignInRes(accessToken.getSnsId());
    }

    private PostKakaoSignUpAndSignInRes getPostKakaoSignUpAndSignInRes(String snsId) {
        Users user = getSnsIdInDB(snsId);
        if(user != null){
            return new PostKakaoSignUpAndSignInRes(jwtService.createJwt(user.getUserId()));
        }
        return new PostKakaoSignUpAndSignInRes(jwtService.createJwt(userRepository.save(new Users(snsId)).getUserId()));
    }

    private Users getSnsIdInDB(String snsId) {
        return userRepository.findBySnsId(snsId);
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
