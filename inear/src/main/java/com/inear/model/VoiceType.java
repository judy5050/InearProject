package com.inear.model;


import lombok.Getter;

@Getter
public enum VoiceType {
    여자1("ko-KR-Standard-A","FEMALE"),
    여자2("ko-KR-Standard-B","FEMALE"),
    남자1("ko-KR-Standard-C","MALE"),
    남자2("ko-KR-Standard-D","MALE");

    private String voiceName;
    private String gender;

    VoiceType(String voiceName, String gender) {
        this.voiceName = voiceName;
        this.gender = gender;
    }


}
