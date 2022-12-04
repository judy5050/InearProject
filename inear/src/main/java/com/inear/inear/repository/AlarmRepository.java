package com.inear.inear.repository;

import com.inear.inear.model.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AlarmRepository extends JpaRepository<Alarm,Long> {
    @Query("select a from Alarm  a where a.userID.userId= :userId order by  a.alarmTime asc ")
    public List<Alarm> findAlarmsBy(@Param("userId") Long userId);
}
