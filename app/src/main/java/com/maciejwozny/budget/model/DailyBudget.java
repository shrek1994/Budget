package com.maciejwozny.budget.model;

import android.content.Context;

import com.maciejwozny.budget.sql.IBudgetDatabase;
import com.maciejwozny.budget.sql.tables.Budget;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Maciej Wozny on 20.10.2017.
 * 2017-2018 All rights reserved.
 */
public class DailyBudget {
    private Context context;
    private IBudgetDatabase budgetDatabase;
    private Calendar calendar;

    public DailyBudget(Context context, IBudgetDatabase budgetDatabase, Calendar calendar) {
        this.context = context;
        this.budgetDatabase = budgetDatabase;
        this.calendar = calendar;
    }

    public double getDailyBudget() {

        Budget budget = Utils.getBudget(context);
        if (budget == null) return 0;
        int monthlyBudget = budget.getMonthlyBudget();
        int beginningDay = budget.getBeginningDay();
        int daysLeft = getDaysLeft(beginningDay);
        Date firstDayOfPeriod = Utils.getFirstDayOfPeriod(calendar, beginningDay);
        double amount = Utils.getAmount(budgetDatabase, firstDayOfPeriod);
        double todaySpends = Utils.getAmount(budgetDatabase, calendar.getTime());
        double dailyBudget = (double)((int)((monthlyBudget - amount + todaySpends) * 100 / daysLeft)) / 100;
        return Utils.getOnlyTwoDigitsAfterDot(dailyBudget);
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

    public double getDailyRemainingBudget() {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date today = new Date(calendar.getTimeInMillis());
        double dailyRemainingBudget = getDailyBudget() - Utils.getAmount(budgetDatabase, today);
        return Utils.getOnlyTwoDigitsAfterDot(dailyRemainingBudget);
    }
}
