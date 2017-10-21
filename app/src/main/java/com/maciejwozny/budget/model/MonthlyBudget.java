package com.maciejwozny.budget.model;

import com.maciejwozny.budget.sql.IBudgetDatabase;

import java.util.Calendar;

/**
 * Created by maciej on 21.10.17.
 */

public class MonthlyBudget {
    private IBudgetDatabase budgetDatabase;
    private Calendar calendar;

    public MonthlyBudget(IBudgetDatabase budgetDatabase, Calendar calendar) {
        this.budgetDatabase = budgetDatabase;
        this.calendar = calendar;
    }

    double getMonthlyBugdget() {
        return 0;
    }

    double getMonthlySpends() {
        return 0;
    }

    double getMonthlyRemaining() {
        return 0;
    }

}
