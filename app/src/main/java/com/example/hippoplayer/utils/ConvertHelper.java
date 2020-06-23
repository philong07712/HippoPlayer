package com.example.hippoplayer.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

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

    public static Uri convertBitmap(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "title", null);
        return Uri.parse(path);
    }
}
