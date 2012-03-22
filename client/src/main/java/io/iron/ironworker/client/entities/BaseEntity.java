package io.iron.ironworker.client.entities;

import org.joda.time.format.ISODateTimeFormat;

import java.util.Date;

public class BaseEntity {
    public static Date parseDate(String date) {
        return ISODateTimeFormat.dateTimeNoMillis().parseDateTime(date).toDate();
    }

    protected BaseEntity() {
    }
}
