package com.inear.model;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;
import java.util.ArrayList;

@Getter
@NoArgsConstructor
public class PatchAlarmReq {
    private ArrayList<String> message;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime alarmTime;

    private ArrayList<String> alarmDate;

    private int term;

    private int repeat;

    private VoiceType voiceType;

    private String name;
}
