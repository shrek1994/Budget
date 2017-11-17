package com.maciejwozny.budget.sql.tables;

/**
 * Created by maciek on 31.01.16.
 */
public enum Period {
    Days(0),
    Months(1),
    Years(2);

    private final int id;

    Period(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    static public Period getPeriod(int i)
    {
        switch (i) {
            case 0:
                return Days;
            case 1:
                return Months;
            case 2:
                return Years;
        }
        return Months;
    }
}
