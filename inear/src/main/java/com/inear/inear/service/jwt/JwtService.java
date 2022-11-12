package com.inear.inear.service.jwt;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static com.inear.inear.config.Secret.JWT_SECRET_KEY;

@Service
public class JwtService {

    public static final String X_ACCESS_TOKEN = "X-ACCESS-TOKEN";
    private static final String USER_ID = "userId";
    private static final String EMPTY_JWT_ERROR = "JWT를 입력해주세요";
    private static final String INVALID_JWT = "유효하지 않은 JWT : ";

    public String createJwt(Long userId) {
        Date now = new Date();
        return Jwts.builder()
                .claim(USER_ID, userId)
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET_KEY)
                .compact();
    }

    public String getJwt() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader(X_ACCESS_TOKEN);
    }

    public Long getUserId() throws JwtException {

        String accessToken = getJwt();
        if (accessToken == null || accessToken.length() == 0) {
            throw new JwtException(EMPTY_JWT_ERROR);
        }

        Jws<Claims> claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(JWT_SECRET_KEY)
                    .parseClaimsJws(accessToken);
        } catch (Exception ignored) {
            throw new JwtException(INVALID_JWT  + accessToken);
        }

        Long userId = Long.valueOf(claims.getBody().get(USER_ID, Integer.class));

        return userId;
    }

}
