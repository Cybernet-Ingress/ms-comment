package com.example.ms.comment.model.response;

import com.example.ms.comment.exception.ExceptionConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExceptionResponse implements ExceptionConstants {
    private String code;
    private String message;
}