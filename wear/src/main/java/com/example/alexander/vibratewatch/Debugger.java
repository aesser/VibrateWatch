package com.example.alexander.vibratewatch;

/**
 * Created by Alexander on 03.11.2015.
 */
public class Debugger {
    private static boolean isEnabled(){
        return true;
    }

    public static void log(Object o){
        if (isEnabled()) {
            System.out.println(o.toString());
        }
    }
}
