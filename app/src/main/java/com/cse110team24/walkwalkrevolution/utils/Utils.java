package com.cse110team24.walkwalkrevolution.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

import static com.google.common.base.Ascii.toUpperCase;

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

    public static boolean checkNotNull(Object ref) {
        return ref != null;
    }


    public static String getInitials(String name, int maxInitials) {
        String[] nameArraySeparatedBySpace = name.split(" ");
        String initialsToReturn = "";
        if (maxInitials < 0) {
            maxInitials = nameArraySeparatedBySpace.length;
        }
        for(int i = 0; i < nameArraySeparatedBySpace.length && i < maxInitials; i++) {
            initialsToReturn += toUpperCase(nameArraySeparatedBySpace[i]).charAt(0);
        }
        return initialsToReturn;
    }

    public static int generateRandomARGBColor(int seed) {
        Random rnd = new Random(seed);
        return Color.argb(255, rnd.nextInt(192), rnd.nextInt(192), rnd.nextInt(192));
    }

    public static boolean fileExists(String filename, Context dirContext) {
        if (checkNotNull(filename)) {
            return new File(dirContext.getFilesDir(), filename).exists();
        }
        return false;
    }
}
