package com.inear.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.inear.inear.model.Alarm;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;
import java.util.ArrayList;

@Getter
@AllArgsConstructor
public class GetAlarmRes {

    private Long alarmId;
    private ArrayList<String> message;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime alarmTime;
    private ArrayList<String> alarmDate;
    private int term;
    private int repeat;

    private  VoiceType voiceType;

    private String name;
    private String active;

    public GetAlarmRes(Alarm alarm) {
        this.alarmId = alarm.getAlarmId();
        this.message = alarm.convertStringMsgToArrayMsg(alarm.getMessage());
        this.alarmTime = alarm.getAlarmTime();
        this.alarmDate = alarm.convertStringMsgToArrayMsg(alarm.getAlarmDate());
        this.term = alarm.getTerm();
        this.repeat = alarm.getRepeat();
        this.voiceType = alarm.getVoiceType();
        this.active = alarm.getActive();
        this.name = alarm.getName();
    }
}
