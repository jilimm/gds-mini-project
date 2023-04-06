package com.gds.challenge.model.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ErrorDetail {
    /**
     * Field name that failed validation
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String field;

    /**
     * Value that caused validation failure
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String invalidValue;

    /**
     * Validation error message - this should tell the user WHY validation failed.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;
}

