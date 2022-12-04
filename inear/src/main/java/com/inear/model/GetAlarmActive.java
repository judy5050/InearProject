package com.inear.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetAlarmActive {
    String active;

    public GetAlarmActive(String active) {
        this.active = active;
    }
}
