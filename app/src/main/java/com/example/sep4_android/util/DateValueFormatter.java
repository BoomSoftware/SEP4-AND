package com.example.sep4_android.util;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class DateValueFormatter extends ValueFormatter {

    @Override
    public String getFormattedValue(float value) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli((long) value), ZoneId.systemDefault()).toLocalDate().toString();
    }
}
