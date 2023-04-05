package com.gds.challenge.entity;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvNumber;
import com.opencsv.bean.validators.MustMatchRegexExpression;
import com.opencsv.bean.validators.PreAssignmentValidator;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class User {

    @Id
    @CsvBindByName(column = "NAME",required = true)
    private String name;

    @CsvBindByName(column = "SALARY",required = true)
    @CsvNumber("#.00")
    private Float salary;


}
