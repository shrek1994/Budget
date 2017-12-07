package com.maciejwozny.budget.model;

import com.maciejwozny.budget.sql.IBudgetDatabase;
import com.maciejwozny.budget.sql.tables.Budget;
import com.maciejwozny.budget.sql.tables.Expenditure;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

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
        if (budget == null) return 0;
        int monthlyBudget = budget.getMonthlyBudget();
        int beginningDay = budget.getBeginningDay();
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int today = calendar.get(Calendar.DAY_OF_MONTH);
        int daysLeft = daysInMonth - today + beginningDay;
        Date firstDayOfPeriod = Utils.getFirstDayOfPeriod(calendar, beginningDay);
        int amount = Utils.getAmount(budgetDatabase, name, firstDayOfPeriod);

        return (monthlyBudget - amount) / daysLeft;
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
