package com.maciejwozny.budget.model;

import android.content.Context;

import com.maciejwozny.budget.sql.IBudgetDatabase;
import com.maciejwozny.budget.sql.tables.Budget;

import java.net.ContentHandler;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Maciej Wozny on 21.10.2017.
 * 2017-2018 All rights reserved.
 */
public class MonthlyBudget {
    private Context context;
    private IBudgetDatabase budgetDatabase;
    private Calendar calendar;

    public MonthlyBudget(Context context, IBudgetDatabase budgetDatabase, Calendar calendar) {
        this.context = context;
        this.budgetDatabase = budgetDatabase;
        this.calendar = calendar;
    }

    public double getMonthlyBudget() {
        double monthlyBudget = Utils.getBudget(context).getMonthlyBudget();
        return Utils.getOnlyTwoDigitsAfterDot(monthlyBudget);
    }

    public double getMonthlySpends() {
        Budget budget = Utils.getBudget(context);
        int beginningDay = budget.getBeginningDay();
        Date firstDayOfPeriod = Utils.getFirstDayOfPeriod(calendar, beginningDay);
        double amount = Utils.getAmount(budgetDatabase, firstDayOfPeriod);
        return Utils.getOnlyTwoDigitsAfterDot(amount);
    }

    public double getMonthlyRemaining() {
        double monthlyRemaining = getMonthlyBudget() - getMonthlySpends();
        return Utils.getOnlyTwoDigitsAfterDot(monthlyRemaining);
    }
}