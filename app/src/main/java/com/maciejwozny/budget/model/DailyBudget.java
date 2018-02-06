package com.maciejwozny.budget.model;

import com.maciejwozny.budget.sql.IBudgetDatabase;
import com.maciejwozny.budget.sql.tables.Budget;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Maciej Wozny on 20.10.2017.
 * 2017 All rights reserved.
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
        if (budget == null) return 0;
        int monthlyBudget = budget.getMonthlyBudget();
        int beginningDay = budget.getBeginningDay();
        int daysLeft = getDaysLeft(beginningDay);
        Date firstDayOfPeriod = Utils.getFirstDayOfPeriod(calendar, beginningDay);
        double amount = Utils.getAmount(budgetDatabase, name, firstDayOfPeriod);
        double todaySpends = Utils.getAmount(budgetDatabase, name, calendar.getTime());

        return (double)((int)((monthlyBudget - amount + todaySpends) * 100 / daysLeft)) / 100;
    }

    private int getDaysLeft(int beginningDay) {
        int daysLeft;
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int today = calendar.get(Calendar.DAY_OF_MONTH);
        if (today < beginningDay) {
            daysLeft = beginningDay - today;
        } else {
            daysLeft = daysInMonth - today + beginningDay;
        }
        return daysLeft;
    }

    public double getDailyRemainingBudget(String name) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date today = new Date(calendar.getTimeInMillis());
        return getDailyBudget(name) - Utils.getAmount(budgetDatabase, name, today);
    }
}
