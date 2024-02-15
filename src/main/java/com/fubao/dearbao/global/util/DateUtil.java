package com.fubao.dearbao.global.util;

import org.springframework.stereotype.Component;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateUtil {
    public String toResponseTimeFormat(LocalTime localTime){
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return timeFormatter.format(localTime);
    }
}
