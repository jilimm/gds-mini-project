package com.gds.challenge.model.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class ErrorMessage {

    /**
     * current date time
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;

    /**
     * Error message
     */
    private String message;

    /**
     * List of error details
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ErrorDetail> details;

}
