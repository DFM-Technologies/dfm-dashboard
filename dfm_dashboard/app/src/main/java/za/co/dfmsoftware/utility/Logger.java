package za.co.dfmsoftware.utility;

import android.util.Log;

public class Logger {
    public static void d(String tag, String msg) {
        Log.d(tag, msg); }

    public static void w(String tag, String msg) {
        Log.w(tag, msg);
    }

    public static void e(String tag, String msg) {
        Log.e(tag, msg);
    }

    public static void e(String tag, String msg, Throwable throwable){
        Log.e(tag, msg, throwable);
    }

    public static void logMessages(Throwable throwable){
        //TODO Fabric initialisation
        //todo bot nav change background
        //todo bug - not moving from dashboard to profile
        //todo add delete account prompt
    }
}
