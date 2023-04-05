package com.gds.challenge.entity;

import com.opencsv.bean.CsvBindByName;
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
    @Positive
    private Float salary;


}
