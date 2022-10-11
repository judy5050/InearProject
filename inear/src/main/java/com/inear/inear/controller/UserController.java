package com.inear.inear.controller;

import com.inear.inear.controller.model.*;
import com.inear.inear.exception.AppleLoginException;
import com.inear.inear.exception.AuthenticationException;
import com.inear.inear.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "카카오 로그인/회원가입 API", description = "기존 회원 -> jwt 리턴 , 신규 회원 -> 회원가입 후 jwt 리턴")
    @PostMapping("/login/kakao")
    public ResponseEntity<PostSignUpAndSignInRes> postKakaoSignUpAndSignIn(@RequestBody PostKakaoSignUpAndSignInReq postKakaoSignUpAndSignInReq) throws IOException {

        PostSignUpAndSignInRes postKakaoSignUpAndSignInRes = userService.kakaoSignUpAndSignIn(postKakaoSignUpAndSignInReq);

        return new ResponseEntity(postKakaoSignUpAndSignInRes, HttpStatus.OK);
    }

    @Operation(summary = "애플 로그인/회원가입 API", description = "기존 회원 -> jwt 리턴 , 신규 회원 -> 회원가입 후 jwt 리턴")
    @PostMapping("/login/apple")
    public ResponseEntity<PostSignUpAndSignInRes> postKakaoSignUpAndSignIn(@RequestBody PostAppleSignUpAndSignInReq postAppleSignUpAndSignInReq) throws AppleLoginException {

        PostSignUpAndSignInRes postappleSignUpAndSignInRes = userService.appleSignUpAndSignIn(postAppleSignUpAndSignInReq);

        return new ResponseEntity(postappleSignUpAndSignInRes, HttpStatus.OK);
    }

    @Operation(summary = "자동 로그인 API", description = "jwt 유효 -> true, jwt 오류 -> false")
    @GetMapping("/check/jwt")
    public ResponseEntity<CheckJwtRes> getJwt() {

        return new ResponseEntity(userService.checkJwt(), HttpStatus.OK);
    }


}
