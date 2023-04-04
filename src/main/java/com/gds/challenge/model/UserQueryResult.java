package com.gds.challenge.model;

import com.gds.challenge.entity.User;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class UserQueryResult {

    private List<User> results;
}
