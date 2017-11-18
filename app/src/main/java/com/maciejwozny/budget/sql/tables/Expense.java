package com.maciejwozny.budget.sql.tables;

import java.sql.Date;

/**
 * Created by maciek on 31.01.16.
 */
@Deprecated
public class Expense {
    public static final String tableExpensesName = "Expense";
    public static final String expenseId = "expenseId";
    public static final String expenseBudgetId = "expenseBudgetId";
    public static final String expenseAmount = "expenseAmount";
    public static final String expenseDate = "expenseDate";
    public static final String expenseName = "expenseName";

//    private int budgetId;
    private String name;
    private int amount;
    private Date date;

    public Expense(/*int budgetId, */String name, int amount, Date date) {
//        this.budgetId = budgetId;
        this.name = name;
        this.amount = amount;
        this.date = date;
    }

//    public int getBudgetId() {
//        return budgetId;
//    }

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
        if (!(o instanceof Expense)) return false;

        Expense expense = (Expense) o;

        if (amount != expense.amount) return false;
//        if (budgetId != expense.budgetId) return false;
//        if (!date.equals(expense.date)) return false; //TODO fix select from sql
        if (!name.equals(expense.name)) return false;

        return true;
    }

    @Override
    public String toString() {
        return "Expense{" +
//                "budgetId=" + budgetId + ", " +
                "name='" + name + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                '}';
    }
}
