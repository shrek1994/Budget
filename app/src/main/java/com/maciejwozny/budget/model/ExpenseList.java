package com.maciejwozny.budget.model;

import com.maciejwozny.budget.sql.IBudgetDatabase;
import com.maciejwozny.budget.sql.tables.Expenditure;

import java.util.Calendar;
import java.util.List;
import java.util.Observable;

/**
 * Created by Maciej Wozny on 20.11.17.
 * 2017 All rights reserved.
 */
public class ExpenseList extends Observable {
    private IBudgetDatabase database;

    public ExpenseList(IBudgetDatabase database) {
        this.database = database;
    }

    public List<Expenditure> getExpenses(String budgetName) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        return database.getExpenditures(database.getBudgetId(budgetName),calendar.getTime());
    }

    public void removeExpense(Expenditure expenditure) {
        setChanged();
        notifyObservers();
        database.removeExpense(expenditure);
    }
}
