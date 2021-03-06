package com.maciejwozny.budget.model;

import android.util.Log;

import com.maciejwozny.budget.BudgetActivity;
import com.maciejwozny.budget.sql.IBudgetDatabase;
import com.maciejwozny.budget.sql.tables.Expenditure;

import java.util.Observable;

import static java.sql.Date.valueOf;

/**
 * Created by Maciej Wozny on 18.11.2017.
 * 2017 All rights reserved.
 */
public class ExpenseAdditional extends Observable {
    private static final String TAG = ExpenseAdditional.class.getSimpleName();
    private IBudgetDatabase budgetDatabase;

    public ExpenseAdditional(IBudgetDatabase budgetDatabase) {
        this.budgetDatabase = budgetDatabase;
    }

    public void addExpense(String name, double amount, String date)
    {
        Expenditure expenditure = new Expenditure(name, amount, valueOf(date));
        budgetDatabase.insertExpenditure(expenditure);
        notifyObservers();
    }

    @Override
    public void notifyObservers() {
        Log.d(TAG, "notifyObservers");
        setChanged();
        super.notifyObservers();
    }
}
