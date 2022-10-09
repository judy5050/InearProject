package com.inear.inear.repository;

import com.inear.inear.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users,Long> {

    Users findBySnsId(String snsId);

}
