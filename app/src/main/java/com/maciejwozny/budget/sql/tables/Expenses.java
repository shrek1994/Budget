package com.maciejwozny.budget.sql.tables;

import java.sql.Date;

/**
 * Created by maciek on 31.01.16.
 */
public class Expenses {
    public static final String tableExpensesName = "Expenses";
    public static final String expenseId = "expenseId";
    public static final String expenseBudgetId = "expenseBudgetId";
    public static final String expenseAmount = "expenseAmount";
    public static final String expenseDate = "expenseDate";
    public static final String expenseName = "expenseName";

    private int budgetId;
    private String name;
    private int amount;
    private Date date;

    public Expenses(int budgetId, String name, int amount, Date date) {
        this.budgetId = budgetId;
        this.name = name;
        this.amount = amount;
        this.date = date;
    }

    public int getBudgetId() {
        return budgetId;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Expenses)) return false;

        Expenses expenses = (Expenses) o;

        if (amount != expenses.amount) return false;
        if (budgetId != expenses.budgetId) return false;
        if (!date.equals(expenses.date)) return false;
        if (!name.equals(expenses.name)) return false;

        return true;
    }
}
