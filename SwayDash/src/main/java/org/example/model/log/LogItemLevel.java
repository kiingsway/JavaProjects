package org.example.model.log;

import java.awt.*;

public enum LogItemLevel {
    INFO(0), WARNING(1), ERROR(2);

    private final int value;

    LogItemLevel(int value) {
        this.value = value;
    }

    public int getValue() {return value;}

    public static LogItemLevel fromInt(int value) {
        return switch (value) {
            case 0 -> INFO;
            case 1 -> WARNING;
            case 2 -> ERROR;
            default -> throw new IllegalArgumentException("Invalid TLevel value: " + value);
        };
    }

    public Color getColor() {
        return switch (this) {
            case INFO -> Color.decode("#33B5E5");
            case WARNING -> Color.decode("#FFBB33");
            case ERROR -> Color.decode("#FF4444");
        };
    }
}
