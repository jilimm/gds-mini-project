package com.gds.challenge.entity.repository;

import com.gds.challenge.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<User, String> {

}
