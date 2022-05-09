package com.example.project.utility;

import com.example.project.exception.ApiRequestException;
import org.springframework.http.HttpStatus;

import java.util.List;


public class ListUtils {

    public static <T> void isListEmpty(List<T> lst) {
        boolean isEmpty = lst.isEmpty();
        if (isEmpty) throw new ApiRequestException("No data found", HttpStatus.NOT_FOUND);
    }

}