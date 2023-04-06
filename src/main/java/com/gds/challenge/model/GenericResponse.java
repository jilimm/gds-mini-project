package com.gds.challenge.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gds.challenge.model.error.ErrorMessage;
import lombok.*;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenericResponse {

    private int success;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ErrorMessage error;


}
