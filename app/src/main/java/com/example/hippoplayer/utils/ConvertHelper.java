package com.example.hippoplayer.utils;

import java.util.concurrent.TimeUnit;

public class ConvertHelper {
    public static String convertToMinutes(int miliseconds) {
        long minutes = TimeUnit.MILLISECONDS.toMinutes(miliseconds);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(miliseconds) - minutes * 60;
        String stringSecond = "" + seconds;
        if (seconds < 10) {
            stringSecond = "0" + seconds;
        }
        String result = minutes + ":" + stringSecond;
        return result;
    }
}
