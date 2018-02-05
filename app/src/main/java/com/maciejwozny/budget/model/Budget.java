package com.maciejwozny.budget.model;

import android.content.Context;

import com.maciejwozny.budget.sql.BudgetDatabase;

import java.util.Calendar;

/**
 * Created by Maciej Wozny on 18.11.2017.
 * 2017 All rights reserved.
 */
public class Budget {
    public BudgetDatabase database;
    public DailyBudget dailyBudget;
    public MonthlyBudget monthlyBudget;
    public ExpenseAdditional expenseAdditional;

    private Budget(Context context, Calendar calendar) {
        database = new BudgetDatabase(context);
        dailyBudget = new DailyBudget(database, calendar);
        monthlyBudget = new MonthlyBudget(database, calendar);
        expenseAdditional = new ExpenseAdditional(database);
    }

    public static Budget build(Context context) {
        return new Budget(context, Calendar.getInstance());
    }

    public static Budget build(Context context, Calendar calendar) {
        return new Budget(context, calendar);
    }


}
