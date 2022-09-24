package com.inear.inear.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
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
    private LocalDateTime alarmTime;

    @Column(name = "alarmDate")
    private String alarmDate;

    @Column(name = "repeat")
    private int repeat;

    @Column(name = "term")
    private int term;

    @Column(name = "voice_type")
    private int voiceType;

    @Column(name = "name")
    private String name;

    @Column(name = "message")
    private String message;

}
