package com.inear.inear.model;

import com.inear.model.PatchAlarmReq;
import com.inear.model.PostAlarmReq;
import com.inear.model.VoiceType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

@Getter
@Entity
@NoArgsConstructor
@Table(name="Alarm")
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "alarm_id")
    private Long alarmId;

    @ManyToOne
    @JoinColumn(name="user_id")
    private Users userID;

    @Column(name = "alarm_time")
    private LocalTime alarmTime;

    @Column(name = "alarmDate")
    private String alarmDate;

    @Column(name = "repeat")
    private int repeat;

    @Column(name = "term")
    private int term;

    @Enumerated(EnumType.STRING)
    @Column(name = "voice_type")
    private VoiceType voiceType;

    @Column(name = "name")
    private String name;

    @Column(name = "message")
    private String message;

    @Column(name = "path")
    private String path;

    @Column(name="active")
    private String active;


    public Alarm(final PostAlarmReq postAlarmReq,final Users users) {
        this.alarmTime = postAlarmReq.getAlarmTime();
        this.alarmDate = convertArrayMsgToStringMsg(postAlarmReq.getAlarmDate());
        this.repeat = postAlarmReq.getRepeat();
        this.term = postAlarmReq.getTerm();
        this.voiceType = postAlarmReq.getVoiceType();
        this.name = postAlarmReq.getName();
        this.message =  addFixAlarmMsg(convertArrayMsgToStringMsg(postAlarmReq.getMessage()));
        this.userID = users;
        this.active = "Y";
    }

    public Alarm(Alarm alarm, PatchAlarmReq patchAlarmReq) {
        if(alarm.alarmTime != null) {
            alarm.alarmTime = patchAlarmReq.getAlarmTime();
        }
        if(!isNullArray(patchAlarmReq.getAlarmDate())){
            alarm.alarmDate = convertArrayMsgToStringMsg(patchAlarmReq.getAlarmDate());
        }
        alarm.repeat = patchAlarmReq.getRepeat();
        System.out.println(patchAlarmReq.getTerm());
        alarm.term = patchAlarmReq.getTerm();
        if(alarm.voiceType != null) {
            alarm.voiceType = patchAlarmReq.getVoiceType();
        }
        if(alarm.name != null) {
            patchAlarmReq.getName();
        }
        if(!isNullArray(patchAlarmReq.getMessage())) {
            alarm.message =  addFixAlarmMsg(convertArrayMsgToStringMsg(patchAlarmReq.getMessage()));
        }
    }

    /**
     * ?????? active ?????? ??????
     * @param alarm
     */
    public Alarm(Alarm alarm,String active) {
        alarm.active = active;
    }

    public static String convertArrayMsgToStringMsg(final ArrayList<String> message) {
        StringBuilder str = new StringBuilder();
        for (String m : message) {
            if(str.length() == 0){
                str.append(m);
            }else {
                str.append(","+m);
            }
        }
        return str.toString();
    }

    private static String addFixAlarmMsg(final String msg) {
        String alarmMsg = "";
        String prefixMsg = "????????? ???????????? ?????? ???????????? ??????????????? ";
        String middleMsg = " ??? ???????????????? ?????? ?????????????????? ";
        String lastMsg = "???????????????";
        alarmMsg = prefixMsg+=msg;
        alarmMsg+= middleMsg;
        alarmMsg += msg;
        return alarmMsg+=lastMsg;
    }

    public static ArrayList<String> convertStringMsgToArrayMsg(String message) {
        String[] str = message.split(",");
        return new ArrayList<>(Arrays.asList(str));
    }

    public static boolean isNullArray(final ArrayList<String> array) {
        if(array == null) {
         return true;
        }
        return false;
    }


}
