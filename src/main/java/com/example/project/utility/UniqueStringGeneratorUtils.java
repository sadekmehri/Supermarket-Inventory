package com.example.project.utility;

import java.util.UUID;


public class UniqueStringGeneratorUtils {

    public static String generateRandomString() {
        return UUID.randomUUID().toString();
    }

}
