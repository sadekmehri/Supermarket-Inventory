package com.example.project.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class PriceConverter implements AttributeConverter<Float, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Float attribute) {
        return (int) (attribute * 100);
    }

    @Override
    public Float convertToEntityAttribute(Integer dbData) {
        return Math.round(dbData * 100.0f) / 10000.0f;
    }

}
