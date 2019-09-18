package com.haze.core.jpa.hibernate;

import com.haze.common.json.HazeJsonUtils;

import javax.persistence.AttributeConverter;

public class JpaJsonConverter implements AttributeConverter<Object, String> {

    @Override
    public String convertToDatabaseColumn(Object attribute) {
        return HazeJsonUtils.writeToString(attribute);
    }

    @Override
    public Object convertToEntityAttribute(String value) {
        return HazeJsonUtils.readFromString(value, Object.class);
    }
}
