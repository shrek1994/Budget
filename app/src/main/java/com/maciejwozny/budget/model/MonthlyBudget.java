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
public class MonthlyBudget {
    private IBudgetDatabase budgetDatabase;
    private Calendar calendar;

    public MonthlyBudget(IBudgetDatabase budgetDatabase, Calendar calendar) {
        this.budgetDatabase = budgetDatabase;
        this.calendar = calendar;
    }

    double getMonthlyBudget(String budgetName) {
        return budgetDatabase.getBudget(budgetName).getMonthlyBudget();
    }

    double getMonthlySpends(String budgetName) {
        Budget budget = budgetDatabase.getBudget(budgetName);
        int beginningDay = budget.getBeginningDay();
        Date firstDayOfPeriod = getFirstDayOfPeriod(beginningDay);
        int amount = getAmount(budgetName, firstDayOfPeriod);
        return amount;
    }

    double getMonthlyRemaining(String budgetName) {
        return getMonthlyBudget(budgetName) - getMonthlySpends(budgetName);
    }


    private int getAmount(String name, Date fromDate) {
        int amount = 0;
        int budgetId = budgetDatabase.getBudgetId(name);
        List<Expenditure> expenditures = budgetDatabase.getExpenditures(budgetId, fromDate);
        for (Expenditure expenditure: expenditures)
            amount += expenditure.getAmount();
        return amount;
    }

    private Date getFirstDayOfPeriod(int beginningDay) {
        Calendar calendar = (Calendar) this.calendar.clone();
        calendar.set(Calendar.DAY_OF_MONTH, beginningDay);
        Date firstDay = new Date(calendar.getTimeInMillis());
        return firstDay;
    }

}
