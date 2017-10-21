package com.maciejwozny.budget.sql.tables;


/**
 * Created by maciek on 31.01.16.
 */
public class Budget {
    public static final String tableBudgetName = "Budgets";
    public static final String budgetId = "budgetId";
    public static final String budgetName = "budgetName";
    public static final String budgetBeginningDay = "budgetBeginningDay";
    public static final String budgetPeriod = "budgetPeriod";
    public static final String budgetRepeatedly = "budgetRepeatedly";


    private final String name;
    private final int beginningDay;
    private final Period period;
    private final boolean repeatedly;

    public Budget(String name, int beginningDay) {
        this(name, beginningDay, Period.Months, true);
    }

    public Budget(String name, int beginningDay, Period period, boolean repeatedly) {
        this.name = name;
        this.beginningDay = beginningDay;
        this.period = period;
        this.repeatedly = repeatedly;
    }

    public String getName() {
        return name;
    }

    public int getBeginningDay() {
        return beginningDay;
    }

    public boolean isRepeatedly() {
        return repeatedly;
    }

    public Period getPeriod() {
        return period;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Budget)) return false;

        Budget budget = (Budget) o;

        if (beginningDay != budget.beginningDay) return false;
        if (repeatedly != budget.repeatedly) return false;
        if (!name.equals(budget.name)) return false;
        if (period != budget.period) return false;

        return true;
    }
}
