package com.maciejwozny.budget.sql;

import com.maciejwozny.budget.sql.tables.Budget;
import com.maciejwozny.budget.sql.tables.Expenditure;

import java.util.Date;
import java.util.List;

/**
 * Created by Maciej Wozny on 21.10.2017.
 * 2017 All rights reserved.
 */
public interface IBudgetDatabase {
    //TODO remove inserting budget - it is in settings
    void insertBudget(Budget budget);
    Budget getBudget(String budgetName);
    List<Budget> getBudgets();
    int getBudgetId(String budgetName);

    void insertExpenditure(Expenditure expenditure);
    void removeExpense(Expenditure expenditure);
    List<Expenditure> getExpenditures(Date fromDate);
}
