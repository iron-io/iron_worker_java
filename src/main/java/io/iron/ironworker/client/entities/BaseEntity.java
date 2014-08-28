package io.iron.ironworker.client.entities;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseEntity {
    public Date parseDate(String s) {
        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            return format.parse(s);
        } catch (ParseException ignored) {
        }

        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            return format.parse(s);
        } catch (ParseException ignored) {
        }

        return null;
    }

    public void copyFields(BaseEntity entity) throws IllegalAccessException, NoSuchFieldException {
        for (Field field : entity.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object value = field.get(entity);
            if (value != null) {
                field.setAccessible(true);
                field.set(this, value);
            }
        }
    }
}
