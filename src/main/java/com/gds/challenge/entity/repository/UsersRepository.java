package com.gds.challenge.entity.repository;

import com.gds.challenge.entity.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UsersRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {









}
