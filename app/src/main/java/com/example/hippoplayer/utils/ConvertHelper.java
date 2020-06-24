package com.example.hippoplayer.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.concurrent.TimeUnit;

public class ConvertHelper {
    public static String convertToMinutes(long miliseconds) {
        long minutes = (miliseconds / 1000) / 60 % 60;
        long seconds = (miliseconds / 1000) % 60;
        String stringSecond = "";
        if (seconds < 10) {
            stringSecond = "0";
        }
        stringSecond += seconds;
        String result = minutes + ":" + stringSecond;
        return result;
    }

    public static String BitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] b = byteArrayOutputStream.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
}
