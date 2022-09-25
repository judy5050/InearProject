package com.inear.inear.controller;

import com.inear.inear.controller.model.CheckJwtRes;
import com.inear.inear.controller.model.PostKakaoSignUpAndSignInReq;
import com.inear.inear.controller.model.PostKakaoSignUpAndSignInRes;
import com.inear.inear.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login/kakao")
    public ResponseEntity<PostKakaoSignUpAndSignInRes> postKakaoSignUpAndSignIn(PostKakaoSignUpAndSignInReq postKakaoSignUpAndSignInReq) throws IOException {

        PostKakaoSignUpAndSignInRes postKakaoSignUpAndSignInRes = userService.kakaoSignUpAndSignIn(postKakaoSignUpAndSignInReq);

        return new ResponseEntity(postKakaoSignUpAndSignInRes, HttpStatus.OK);
    }

    @GetMapping("/check/jwt")
    public ResponseEntity<CheckJwtRes> getJwt() {

        return new ResponseEntity(userService.checkJwt(), HttpStatus.OK);
    }
}
