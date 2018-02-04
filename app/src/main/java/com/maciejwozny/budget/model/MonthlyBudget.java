package com.maciejwozny.budget.model;

import com.maciejwozny.budget.sql.IBudgetDatabase;
import com.maciejwozny.budget.sql.tables.Budget;

import java.util.Calendar;
import java.util.Date;

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

    public double getMonthlyBudget(String budgetName) {
        return budgetDatabase.getBudget(budgetName).getMonthlyBudget();
    }

    public double getMonthlySpends(String budgetName) {
        Budget budget = budgetDatabase.getBudget(budgetName);
        int beginningDay = budget.getBeginningDay();
        Date firstDayOfPeriod = Utils.getFirstDayOfPeriod(calendar, beginningDay);
        int amount = Utils.getAmount(budgetDatabase, budgetName, firstDayOfPeriod);
        return amount;
    }

    public double getMonthlyRemaining(String budgetName) {
        return getMonthlyBudget(budgetName) - getMonthlySpends(budgetName);
    }
}