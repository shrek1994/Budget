package com.maciejwozny.budget.model;

import com.maciejwozny.budget.BudgetActivity;
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
        Expenditure expenditure = new Expenditure(name, amount, valueOf(date));
        expenditure.setBudgetId(budgetDatabase.getBudgetId(BudgetActivity.DEFAULT_BUDGET.getName()));
        budgetDatabase.insertExpenditure(expenditure);
        notifyObservers();
    }
}
