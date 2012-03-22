package io.iron.ironworker.client.entities;

import com.google.gson.JsonObject;
import org.joda.time.format.ISODateTimeFormat;

import java.util.Date;

public class BaseEntity {
    public static String parseString(JsonObject o, String field, String def) {
        return o.has(field) ? o.get(field).getAsString() : def;
    }

    public static String parseString(JsonObject o, String field) {
        return parseString(o, field, null);
    }

    public static int parseInt(JsonObject o, String field, int def) {
        return o.has(field) ? o.get(field).getAsInt() : def;
    }

    public static int parseInt(JsonObject o, String field) {
        return parseInt(o, field, 0);
    }

    public static Date parseDate(JsonObject o, String field, Date def) {
        return o.has(field) ? ISODateTimeFormat.dateTimeNoMillis().parseDateTime(o.get(field).getAsString()).toDate() : def;
    }

    public static Date parseDate(JsonObject o, String field) {
        return parseDate(o, field, null);
    }

    protected BaseEntity() {
    }
}
