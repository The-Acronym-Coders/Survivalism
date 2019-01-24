package com.teamacronymcoders.survivalism.utils.helpers;

import com.teamacronymcoders.survivalism.Survivalism;

public class HelperMath {
    public static boolean tryPercentage(double percent) {
        return Math.random() < percent;
    }

    public static int nextIntInclusive(int min, int max) {
        return Survivalism.RANDOM.nextInt(max - min + 1) + min;
    }
}
