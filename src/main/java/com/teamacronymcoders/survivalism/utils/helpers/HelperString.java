package com.teamacronymcoders.survivalism.utils.helpers;

public class HelperString {
    public static String cleanBarrelStateString(String input) {
        String stateStr = input.toLowerCase();
        String s1 = stateStr.substring(0, 1).toUpperCase();
        String s2 = s1 + stateStr.substring(1);
        return s2;
    }

    public static String cleanCommandString(String input) {
        input.replace("<", "");
        input.replace(":", "");
        input.replace(">", "");
        return input;
    }

    public static String getDurationString(int seconds) {

        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;
        return twoDigitString(hours) + ":" + twoDigitString(minutes) + ":" + twoDigitString(seconds);
    }

    private static String twoDigitString(int number) {

        if (number == 0) {
            return "00";
        }

        if (number / 10 == 0) {
            return "0" + number;
        }

        return String.valueOf(number);
    }
}
