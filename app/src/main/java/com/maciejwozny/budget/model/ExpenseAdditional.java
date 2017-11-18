package com.maciejwozny.budget.model;

import com.maciejwozny.budget.sql.IBudgetDatabase;
import com.maciejwozny.budget.sql.tables.Expenditure;

import java.util.Observable;

import static java.sql.Date.valueOf;

/**
 * Created by maciek on 18.11.17.
 */

public class ExpenseAdditional extends Observable{
    private IBudgetDatabase budgetDatabase;

    public ExpenseAdditional(IBudgetDatabase budgetDatabase) {
        this.budgetDatabase = budgetDatabase;
    }

    public void addExpense(String name, int amount, String date)
    {
        budgetDatabase.insertExpenditure(new Expenditure(name, amount, valueOf(date)));
        notifyObservers();
    }
}
