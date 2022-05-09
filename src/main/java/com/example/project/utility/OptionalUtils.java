package com.example.project.utility;

import com.example.project.exception.ApiRequestException;
import org.springframework.http.HttpStatus;

import java.util.Optional;


public class OptionalUtils {

    public static <T> void isPresent(Optional<T> option, String errMessage) {
        if (option.isPresent()) throw new ApiRequestException(errMessage, HttpStatus.BAD_REQUEST);
    }

    public static <T> void isEmpty(Optional<T> option, String errMessage) {
        if (option.isEmpty()) throw new ApiRequestException(errMessage, HttpStatus.BAD_REQUEST);
    }

}
