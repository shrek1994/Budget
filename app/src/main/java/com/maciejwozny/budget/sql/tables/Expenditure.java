package com.maciejwozny.budget.sql.tables;

import java.sql.Date;

/**
 * Created by maciek on 31.01.16.
 */
public class Expenditure {
    public static final String TABLE_EXPENSES_NAME = "Expenditure";
    public static final String EXPENDITURE_ID = "expenditureId";
    public static final String EXPENDITURE_BUDGET_ID = "expenditureBudgetId";
    public static final String EXPENDITURE_AMOUNT = "expenditureAmount";
    public static final String EXPENDITURE_DATE = "expenditureDate";
    public static final String EXPENDITURE_NAME = "expenditureName";

    private int budgetId = 0;
    private String name;
    private int amount;
    private Date date;

    public Expenditure(String name, int amount, Date date) {
        this.name = name;
        this.amount = amount;
        this.date = date;
    }

    public Expenditure(int budgetId, String name, int amount, Date date) {
        this.budgetId = budgetId;
        this.name = name;
        this.amount = amount;
        this.date = date;
    }

    public void setBudgetId(int budgetId) {
        this.budgetId = budgetId;
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
        if (!(o instanceof Expenditure)) return false;

        Expenditure expenditure = (Expenditure) o;

        if (amount != expenditure.amount) return false;
        if (budgetId != expenditure.budgetId) return false;
        if (!date.equals(expenditure.date)) return false;
        if (!name.equals(expenditure.name)) return false;

        return true;
    }

    @Override
    public String toString() {
        return "Expenditure{" +
                "BUDGET_ID=" + budgetId +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                '}';
    }
}
