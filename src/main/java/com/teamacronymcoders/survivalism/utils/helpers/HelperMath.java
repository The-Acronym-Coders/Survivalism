package com.teamacronymcoders.survivalism.utils.helpers;

import java.math.BigDecimal;

public class HelperMath {
    public static boolean tryPercentage(double percent) {
        return Math.random() < percent;
    }

    public static BigDecimal round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd;
    }
}
