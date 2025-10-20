package br.org.oficinadasmeninas.presentation.shared.utils;

import java.math.BigDecimal;

public class MoneyConverter {
    private static final int SCALE = 2; // 2 casas decimais para centavos

    public static long doubleToLong(double value) {
        return Math.round(value * Math.pow(10, SCALE));
    }

    public static double longToDouble(long cents) {
        return cents / Math.pow(10, SCALE);
    }
}
