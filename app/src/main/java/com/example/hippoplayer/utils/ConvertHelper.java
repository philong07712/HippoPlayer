package com.example.hippoplayer.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;

import com.bumptech.glide.Glide;

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

    public static Uri getImageUri(Context context, Bitmap bitmap, String title) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, title, null);
        if (path == null) {
            return null;
        }
        return Uri.parse(path);
    }

    public static Drawable copy(Drawable drawable) {
        Bitmap bitmap = null;
        if (drawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        }
        if (bitmap != null) {
            bitmap = bitmap.copy(bitmap.getConfig(), bitmap.isMutable());
            return new BitmapDrawable(bitmap);
        }
        return null;
    }
}
