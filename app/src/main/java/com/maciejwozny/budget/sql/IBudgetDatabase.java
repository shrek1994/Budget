package com.maciejwozny.budget.sql;

import com.maciejwozny.budget.sql.tables.Budget;
import com.maciejwozny.budget.sql.tables.Expenditure;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by maciej on 21.10.17.
 */

public interface IBudgetDatabase {
    void insertBudget(Budget budget);
    Budget getBudget(String budgetName);
    List<Budget> getBudgets();
    int getBudgetId(String budgetName);

    void insertExpenditure(Expenditure expenditure);
    void removeExpense(Expenditure expenditure);
    List<Expenditure> getExpenditures(int budgetId, Date fromDate);
}
