package com.example.ourcart.data.entities;

import androidx.annotation.NonNull;

public enum Unit {
    KG,
    LITER,
    PIECE,
    GRAM,
    MILLILITER,
    METER;

    public Unit fromString(String unitStr) {
        switch(unitStr) {
            case "кг": return KG;
            case "л": return LITER;
            case "шт": return PIECE;
            case "г": return GRAM;
            case "мл": return MILLILITER;
            case "м": return METER;
            default: return PIECE;
        }
    }

    @NonNull
    @Override
    public String toString() {
        switch (this) {
            case KG: return "кг";
            case LITER: return "л";
            case PIECE: return "шт";
            case GRAM: return "г";
            case MILLILITER: return "мл";
            case METER: return "м";
            default: return "шт";
        }
    }
}
