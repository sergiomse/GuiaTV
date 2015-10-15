package com.sergiomse.guiatv;

import android.util.DisplayMetrics;

/**
 * Created by turno de tarde on 30/07/2015.
 */
public class Constansts {

    public final static int DP_WIDTH_PER_HOUR = 150;

    private static int pxWidthPerHour = -1;

    public static int getPxWidthPerHour(DisplayMetrics dm) {
        if(pxWidthPerHour != -1) {
            return pxWidthPerHour;
        } else if(pxWidthPerHour == -1 && dm != null) {
            pxWidthPerHour = (int) (dm.density * DP_WIDTH_PER_HOUR);
            return pxWidthPerHour;
        } else {
            return -1;
        }
    }
}
