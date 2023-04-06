package com.gds.challenge.entity;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvNumber;
import com.opencsv.bean.validators.MustMatchRegexExpression;
import com.opencsv.bean.validators.PreAssignmentValidator;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Entity
@Table(name = "users")
@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @CsvBindByName(column = "NAME",required = true)
    @PreAssignmentValidator(validator = MustMatchRegexExpression.class, paramString = "^[a-zA-Z]+[ a-zA-Z]+$")
    @NotBlank
    @NotNull
    private String name;

    @CsvBindByName(column = "SALARY", required = true)
    @CsvNumber("#.00")
    @Min(0)
    private float salary;


}
