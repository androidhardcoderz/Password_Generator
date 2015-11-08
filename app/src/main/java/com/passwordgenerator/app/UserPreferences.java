package com.passwordgenerator.app;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by Scott on 11/1/2015.
 */
public class UserPreferences {

    public static final String LENGTH = "length";
    public static final String SYMBOLS = "symbols";
    public static final String NUMBERS = "numbers";
    public static final String LOWERCASE = "lowercase";
    public static final String UPPERCASE = "uppercase";
    public static final String SIMILAR = "similiar";
    public static final String AMBIGUOUS = "ambiguous";
    public static final String COPY = "copy";
    public static final String SAVE = "save";
    public static final String SHOWTIPS = "show_tips";
    public static final String SHAKE = "shake";

    public static void setItem(Context context,String string,boolean state){
        PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext()).edit().putBoolean(string,state).apply();
    }

    public static void setLengthItem(Context context,String string){
        PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext()).edit().putString(LENGTH, string).apply();
    }

    public static boolean getItem(Context context,String string){
        return PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext()).getBoolean(string,false);
    }

    public static String getLengthItem(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext()).getString(LENGTH, "16");
    }

    public static boolean getShowTips(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext()).getBoolean(SHOWTIPS, true);
    }

    public static void setShowTips(Context context,boolean state){
        PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext()).edit().putBoolean(SHOWTIPS,state).apply();
    }

    public static boolean getShake(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext()).getBoolean(SHAKE, true);
    }

    public static void setShake(Context context,boolean state){
        PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext()).edit().putBoolean(SHAKE,state).apply();
    }





}
