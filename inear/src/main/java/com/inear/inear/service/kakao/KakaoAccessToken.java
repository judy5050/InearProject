package com.inear.inear.service.kakao;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;


public class KakaoAccessToken {

    private final String accessToken;
    private static final String KAKAO_USER_INFO_API = "https://kapi.kakao.com/v2/user/me";

    public KakaoAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getSnsId() throws IOException {
        URL url = new URL(KAKAO_USER_INFO_API);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        //    요청에 필요한 Header에 포함될 내용
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        String line = "";
        String result = "";

        while ((line = br.readLine()) != null) {
            result += line;
        }

        JsonElement jsonElement = JsonParser.parseString(result);

        return jsonElement.getAsJsonObject().get("id").getAsString();
    }
}
