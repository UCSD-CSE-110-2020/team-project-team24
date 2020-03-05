package com.cse110team24.walkwalkrevolution.utils;

import android.content.SharedPreferences;

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
}
