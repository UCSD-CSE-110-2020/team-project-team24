package com.cse110team24.walkwalkrevolution.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.Map;
import java.util.regex.Pattern;

public class Utils {
    public static boolean isValidGmail(String email) {
        return Pattern.matches("(\\W|^)[\\w.\\-]{0,25}@(gmail)\\.com(\\W|$)", email);
    }

    public static String cleanEmail(String email) {
        return email.replaceAll("@|\\.", "");
    }

    public static void saveString(SharedPreferences preferences, String key, String value) {
        preferences.edit()
                .putString(key, value)
                .apply();
    }

    public static String getString(SharedPreferences preferences, String key) {
        return preferences.getString(key, null);
    }

    public static void showToast(Context context, String msg, int len) {
        Toast.makeText(context, msg, len).show();
    }

    public static <T> T getValueOrNull(String key, Map<String, Object> data) {
        Object val = data.get(key);
        if (val != null) {
            return (T) val;
        }
        return null;
    }
}
