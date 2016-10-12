package com.example.cdp.jueguecito.auxiliares;

import android.graphics.Color;

import java.util.concurrent.TimeUnit;

/**
 * Created by CDP on 02/09/2016.
 */
public class Utilidades {
    public static int getDarker(int color){
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.8f; // value component
        return(Color.HSVToColor(hsv));
    }

    public static String formatTiempo(long millis){
        return String.format("%02d : %02d",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        );
    }
}
