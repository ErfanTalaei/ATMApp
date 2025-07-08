package org.bihe.finalproject.utils;

import androidx.annotation.NonNull;

public class Validator {

    public static boolean isPasswordValid(@NonNull String s) {
        String regex = "[a-zA-Z0-9]{3,25}$";
        return s.matches(regex);
    }

    public static boolean isAgeValid(int a) {
        return a >= 18;
    }

    public static boolean isPhoneValid(@NonNull String s) {
        String regex = "[0-9]{11}$";
        return s.matches(regex);
    }

    public static boolean isAccountNumValid(@NonNull String s) {
        String regex = "[0-9]{9}$";
        return s.matches(regex);
    }

    public static boolean isCardNumValid(@NonNull String s) {
        String regex = "[0-9]{16}$";
        return s.matches(regex);
    }

    public static boolean isCvv2Valid(@NonNull String s) {
        String regex = "[0-9]{3,4}$";
        return s.matches(regex);
    }
    public static boolean isBalanceOrAmountValid(float f) {
        return f >= 0;
    }
}
