package com.maciejwozny.budget.sql.tables;

import java.util.Date;

/**
 * Created by Maciej Wozny on 21.10.2017.
 * 2017 All rights reserved.
 */
public class Expenditure {
    public static final String TABLE_EXPENSES_NAME = "Expenditure";
    public static final String EXPENDITURE_ID = "expenditureId";
    public static final String EXPENDITURE_AMOUNT = "expenditureAmount";
    public static final String EXPENDITURE_DATE = "expenditureDate";
    public static final String EXPENDITURE_NAME = "expenditureName";

    private String name;
    private double amount;
    private Date date;

    public Expenditure(String name, double amount, Date date) {
        this.name = name;
        this.amount = amount;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
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
        if (!date.equals(expenditure.date)) return false;
        if (!name.equals(expenditure.name)) return false;

        return true;
    }

    @Override
    public String toString() {
        return "Expenditure{" +
                "name='" + name + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                '}';
    }
}