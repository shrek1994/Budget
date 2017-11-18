package com.maciejwozny.budget.sql.tables;


/**
 * Created by maciek on 31.01.16.
 */
public class Budget {
    public static final String TABLE_BUDGET_NAME = "Budgets";
    public static final String BUDGET_ID = "BUDGET_ID";
    public static final String BUDGET_NAME = "DEFAULT_BUDGET";
    public static final String BUDGET_BEGINNING_DAY = "BUDGET_BEGINNING_DAY";
    public static final String BUDGET_PERIOD = "BUDGET_PERIOD";
    public static final String BUDGET_REPEATEDLY = "BUDGET_REPEATEDLY";
    public static final String BUDGET_MONTHLY_BUDGET = "BUDGET_MONTHLY_BUDGET";


    private final String name;
    private final int beginningDay;
    private final int monthlyBudget;
    private final Period period;
    private final boolean repeatedly;

    public Budget(String name, int beginningDay, int monthlyBudget) {
        this(name, beginningDay, monthlyBudget, Period.Months, true);
    }

    public Budget(String name, int beginningDay, int monthlyBudget, Period period, boolean repeatedly) {
        this.name = name;
        this.beginningDay = beginningDay;
        this.monthlyBudget = monthlyBudget;
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

    public int getMonthlyBudget() {
        return monthlyBudget;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Budget)) return false;

        Budget budget = (Budget) o;

        if (beginningDay != budget.beginningDay) return false;
        if (repeatedly != budget.repeatedly) return false;
        if (monthlyBudget != budget.monthlyBudget) return false;
        if (!name.equals(budget.name)) return false;
        if (period != budget.period) return false;

        return true;
    }

    @Override
    public String toString() {
        return "Budget{" +
                "name='" + name + '\'' +
                ", beginningDay=" + beginningDay +
                ", monthlyBudget=" + monthlyBudget +
                ", period=" + period +
                ", repeatedly=" + repeatedly +
                '}';
    }
}
