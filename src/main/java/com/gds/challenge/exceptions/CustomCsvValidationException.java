package com.gds.challenge.exceptions;

import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import lombok.Builder;

import java.io.Serial;

@Builder
public class CustomCsvValidationException extends CsvValidationException {
    @Serial
    private static final long serialVersionUID = 7073912321167257823L;

    public CustomCsvValidationException() {    }

    public CustomCsvValidationException(String message) {
        super(message);
    }

}
