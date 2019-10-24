package com.goldenhour.util.phasetimecalculator;

public class Zenith {
    private double degrees;

    private Zenith(double degrees) {
        this.degrees = degrees;
    }

    double getDegrees() {
        return degrees;
    }

    public static final Zenith OFFICIAL = new Zenith(90.833333333);
}
