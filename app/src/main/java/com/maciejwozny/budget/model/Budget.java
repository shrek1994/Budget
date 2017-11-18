package com.maciejwozny.budget.model;

import android.content.Context;

import com.maciejwozny.budget.sql.BudgetDatabase;

import java.util.Calendar;

/**
 * Created by maciek on 18.11.17.
 */

public class Budget {
    public BudgetDatabase database;
    public DailyBudget dailyBudget;
    public MonthlyBudget monthlyBudget;
    public ExpenseAdditional expenseAdditional;

    private Budget(Context context) {
        database = new BudgetDatabase(context);
        dailyBudget = new DailyBudget(database, Calendar.getInstance());
        monthlyBudget = new MonthlyBudget(database, Calendar.getInstance());
        expenseAdditional = new ExpenseAdditional(database);
    }

    public static Budget build(Context context) {
        return new Budget(context);
    }


}
