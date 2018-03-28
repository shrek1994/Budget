package com.maciejwozny.budget.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.maciejwozny.budget.SettingsActivity;
import com.maciejwozny.budget.sql.IBudgetDatabase;
import com.maciejwozny.budget.sql.tables.Expenditure;
import com.maciejwozny.budget.sql.tables.Budget;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.maciejwozny.budget.BudgetActivity.DEFAULT_BUDGET;

/**
 * Created by Maciej Wozny on 07.12.17.
 * 2017-2018 All rights reserved.
 */
class Utils {
    static double getAmount(IBudgetDatabase budgetDatabase, Date fromDate) {
        double amount = 0;
        List<Expenditure> expenditures = budgetDatabase.getExpenditures(fromDate);
        for (Expenditure expenditure: expenditures)
            amount += expenditure.getAmount();
        return amount;
    }

    static Date getFirstDayOfPeriod(Calendar calendar, int beginningDay) {
        Calendar firstDayCalendar = (Calendar) calendar.clone();
        if (firstDayCalendar.get(Calendar.DAY_OF_MONTH) < beginningDay) {
            firstDayCalendar.add(Calendar.MONTH, -1);
        }
        firstDayCalendar.set(Calendar.DAY_OF_MONTH, beginningDay);
        Date firstDay = new Date(firstDayCalendar.getTimeInMillis());
        return firstDay;
    }

    static public Budget getBudget(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        int beginningDay = Integer.parseInt(sharedPref.getString(SettingsActivity.DAY_OF_PAYMENT,
                Integer.toString(DEFAULT_BUDGET.getBeginningDay())));
        int budgetValue = Integer.parseInt(sharedPref.getString(SettingsActivity.BUDGET_VALUE,
                Integer.toString(DEFAULT_BUDGET.getMonthlyBudget())));
        return new Budget("default budget", beginningDay, budgetValue);
    }

    static public double getOnlyTwoDigitsAfterDot(double number) {
        DecimalFormat decimalFormat = (DecimalFormat)DecimalFormat.getInstance(Locale.UK).clone();
        decimalFormat.applyPattern("0.00");
        String formatResult = decimalFormat.format(number);
        return Double.parseDouble(formatResult);
    }
}
