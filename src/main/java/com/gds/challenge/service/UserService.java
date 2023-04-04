package com.gds.challenge.service;

import com.gds.challenge.entity.repository.CustomUsersRepository;
import com.gds.challenge.entity.repository.UsersRepository;
import com.gds.challenge.utils.UserSortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gds.challenge.entity.User;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final String SQL_QUERY = "SELECT * FROM users where salary between :min and :max";
    private final String OFFSET_QUERY = "offset :offset";
    private final String LIMIT_QUERY = "limit :limit";
    private final String SORT_QUERY = ":sort asc";

    @Autowired
    CustomUsersRepository customUsersRepository;

    public List<User> getUsers(float minSalary,
                               float maxSalary,
                               int offset,
                               Optional<Integer> limit,
                               Optional<UserSortType> sortType){
        logger.info("min: "+minSalary+" max: "+maxSalary+" offset: "+offset + " limit: "+limit + " sortType: "+sortType);
        return customUsersRepository.getUserResult(minSalary, maxSalary, offset, limit, sortType);

    }
}
