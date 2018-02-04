package com.maciejwozny.budget.model;

import com.maciejwozny.budget.sql.IBudgetDatabase;
import com.maciejwozny.budget.sql.tables.Expenditure;

import java.util.Date;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Maciej Wozny on 07.12.17.
 * 2017 All rights reserved.
 */

class Utils {
    static int getAmount(IBudgetDatabase budgetDatabase, String name, Date fromDate) {
        int amount = 0;
        int budgetId = budgetDatabase.getBudgetId(name);
        List<Expenditure> expenditures = budgetDatabase.getExpenditures(budgetId, fromDate);
        for (Expenditure expenditure: expenditures)
            amount += expenditure.getAmount();
        return amount;
    }

    static Date getFirstDayOfPeriod(Calendar calendar, int beginningDay) {
        calendar = (Calendar) calendar.clone();
        if (calendar.get(Calendar.DAY_OF_MONTH) < beginningDay) {
            calendar.add(Calendar.MONTH, -1);
        }
        calendar.set(Calendar.DAY_OF_MONTH, beginningDay);
        Date firstDay = new Date(calendar.getTimeInMillis());
        return firstDay;
    }
}
