package com.inear.inear.model;

import javax.persistence.*;

@Entity
@Table(name="test_table")
public class testModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idx;

    private String name;

    public Long getIdx() {
        return idx;
    }

    public String getName() {
        return name;
    }
}
