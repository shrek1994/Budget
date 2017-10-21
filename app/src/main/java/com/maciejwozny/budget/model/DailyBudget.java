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
        int monthlyBudget = budget.getMonthlyBudget();
        int beginningDay = budget.getBeginningDay();
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int today = calendar.get(Calendar.DAY_OF_MONTH);
        int daysLeft = daysInMonth - today + beginningDay;
        Date firstDayOfPeriod = getFirstDayOfPeriod();
        int amount = getAmount(name, firstDayOfPeriod);

        return (monthlyBudget - amount) / daysLeft;
    }

    public double getDailyRemainingBudget(String name) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date today = new Date(calendar.getTimeInMillis());
        return getDailyBudget(name) - getAmount(name, today);
    }

    private int getAmount(String name, Date fromDate) {
        int amount = 0;
        int budgetId = budgetDatabase.getBudgetId(name);
        List<Expenditure> expenditures = budgetDatabase.getExpenditures(budgetId, fromDate);
        for (Expenditure expenditure: expenditures)
            amount += expenditure.getAmount();
        return amount;
    }

    private Date getFirstDayOfPeriod() {
        Calendar calendar = (Calendar) this.calendar.clone();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date firstDay = new Date(calendar.getTimeInMillis());
        return firstDay;
    }
}
