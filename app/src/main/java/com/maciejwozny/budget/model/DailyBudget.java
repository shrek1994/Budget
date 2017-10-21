package com.maciejwozny.budget.model;

import com.maciejwozny.budget.sql.IBudgetDatabase;
import com.maciejwozny.budget.sql.tables.Budget;

import java.sql.Date;
import java.util.Calendar;

/**
 * Created by maciej on 21.10.17.
 */

public class DailyBudget {
    private IBudgetDatabase budgetDatabase;
    private Calendar calendar;

    public DailyBudget(IBudgetDatabase budgetDatabase, Calendar calendar) {
        this.budgetDatabase = budgetDatabase;
        this.calendar = calendar;
    }

    public double getDailyBudget(String name) {
        Budget budget = budgetDatabase.getBudget(name);
        int monthlyBudget = budget.getMonthlyBudget();
        int beginningDay = budget.getBeginningDay();
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int today = calendar.get(Calendar.DAY_OF_MONTH);
        int daysLeft = daysInMonth - today + beginningDay;

        int budgetId = budgetDatabase.getBudgetId(name);
        
        return monthlyBudget / daysLeft;
    }

    public double getDailyRemainingBudget() {
        return 0;
    }
}
