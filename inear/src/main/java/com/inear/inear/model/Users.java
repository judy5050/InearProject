package com.inear.inear.model;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name="users")
@Getter
public class Users {

    public Users() {
    }

    public Users(String snsId) {
        this.snsId = snsId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "sns_id")
    private String snsId;


}
