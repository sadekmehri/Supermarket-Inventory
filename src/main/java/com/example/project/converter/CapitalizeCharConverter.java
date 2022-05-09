package com.example.project.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Converter
public class CapitalizeCharConverter implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String value) {
        if (value == null) return null;

        return upperCaseAllFirstCharacter(value);
    }

    @Override
    public String convertToEntityAttribute(String value) {
        return value;
    }

    private String upperCaseAllFirstCharacter(final String words) {
        return Stream.of(words.trim().replaceAll("\\s+", " ").split("\\s"))
                .filter(word -> word.length() > 0)
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                .collect(Collectors.joining(" "));
    }

}