package com.arman.util;

import org.jetbrains.annotations.Contract;

public class ExtraMath {

    private ExtraMath() {

    }

    public static double clamp(double value, double minimum, double maximum) {
        return Math.min(maximum, Math.max(value, minimum));
    }

    @Contract(pure = true)
    public static double lerp(double a, double b, double p) {
        return a + p * (b - a);
    }

}
