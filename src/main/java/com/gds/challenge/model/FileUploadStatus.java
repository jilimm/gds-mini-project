package com.gds.challenge.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.ToString;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@ToString
@Getter
public enum FileUploadStatus {
    SUCCESS(1),
    FAILURE(0);
    private final int success;

    FileUploadStatus(int i) {
        this.success = i;
    }
}
