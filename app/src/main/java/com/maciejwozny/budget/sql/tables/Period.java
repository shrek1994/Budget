package com.maciejwozny.budget.sql.tables;

/**
 * Created by maciek on 31.01.16.
 */
public enum Period {
    Days(0),
    Months(1),
    Years(2);

    private final int id;

    private Period(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
