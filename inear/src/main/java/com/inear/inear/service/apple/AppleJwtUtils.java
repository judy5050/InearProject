package com.inear.inear.service.apple;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inear.inear.exception.AppleLoginException;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.*;


@RequiredArgsConstructor
@Service
public class AppleJwtUtils {

    private final AppleClient appleClient;

    public Claims getClaimsBy(String identityToken) throws AppleLoginException {

        try {
            ApplePublicKeyResponse response = appleClient.getAppleAuthPublicKey();

            String headerOfIdentityToken = identityToken.substring(0, identityToken.indexOf("."));
            Map<String, String> header = new ObjectMapper().readValue(new String(Base64.getDecoder().decode(headerOfIdentityToken), "UTF-8"), Map.class);
            ApplePublicKeyResponse.Key key = response.getMatchedKeyBy(header.get("kid"), header.get("alg"))
                    .orElseThrow(() -> new NullPointerException("Failed get public key from apple's id server."));

            byte[] nBytes = Base64.getUrlDecoder().decode(key.getN());
            byte[] eBytes = Base64.getUrlDecoder().decode(key.getE());

            BigInteger n = new BigInteger(1, nBytes);
            BigInteger e = new BigInteger(1, eBytes);

            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
            KeyFactory keyFactory = KeyFactory.getInstance(key.getKty());
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(identityToken);

            return claimsJws.getBody();

        } catch (NoSuchAlgorithmException e) {
            throw new AppleLoginException("NoSuchAlgorithmException : " + e.getMessage());
        } catch (InvalidKeySpecException e) {
            throw new AppleLoginException("InvalidKeySpecException : " + e.getMessage());
        } catch (SignatureException | MalformedJwtException e){
            throw new AppleLoginException("토큰 서명 검증 or 구조 문제 (Invalid token) : " + e.getMessage());
        } catch ( ExpiredJwtException e){
            throw new AppleLoginException("토큰이 만료됐기 때문에 클라이언트는 토큰을 refresh 해야함. : " + e.getMessage());
        } catch(Exception e){
            throw new AppleLoginException("Exception : " + e.getMessage());
        }
    }
}
