package com.haze.core.hibernate;

import com.haze.common.util.HazeJsonUtils;

import javax.persistence.AttributeConverter;

/**
 * 将字符串映射为数据库Json字段转换器
 */
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
