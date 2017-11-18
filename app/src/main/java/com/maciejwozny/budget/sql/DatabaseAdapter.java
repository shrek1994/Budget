package com.maciejwozny.budget.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.maciejwozny.budget.sql.tables.Budget;
import com.maciejwozny.budget.sql.tables.Expense;
import com.maciejwozny.budget.sql.tables.Period;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static com.maciejwozny.budget.sql.tables.Budget.*;
import static com.maciejwozny.budget.sql.tables.Expense.*;

/**
 * Created by maciek on 30.01.16.
 */
@Deprecated
public class DatabaseAdapter extends SQLiteOpenHelper {
    private static final int version = 20171021;

    public static final String databaseBudgetName = "PoorBudget.db";
    private String createTable =  "create table if not exists ";
    private String createBudgets =
            createTable + TABLE_BUDGET_NAME +
                    " ( " +
                    BUDGET_ID + " int primary key, " +
                    BUDGET_NAME + " varchar(50) not null, " +
                    BUDGET_BEGINNING_DAY + " int default 1, " +
                    BUDGET_MONTHLY_BUDGET + " int " +
                    ");";

    private String createExpenses =
            createTable + tableExpensesName + " ( " +
                    expenseId + " int primary key, " +
                    expenseBudgetId + " int, " +
                    expenseAmount + " int not null, " +
                    expenseDate + " date not null, " +
                    expenseName + " varchar(50), " +
                    "FOREIGN KEY ( " + expenseBudgetId + " ) " +
                    "REFERENCES " + TABLE_BUDGET_NAME + "( " + BUDGET_ID + " ) " +
                    ");";

    DatabaseAdapter(Context context){
        super(context, databaseBudgetName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createBudgets);
        db.execSQL(createExpenses);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertBudget(Budget budget) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BUDGET_NAME, budget.getName());
        values.put(BUDGET_BEGINNING_DAY, budget.getBeginningDay());
        values.put(BUDGET_MONTHLY_BUDGET, budget.getMonthlyBudget());
        database.insert(TABLE_BUDGET_NAME, null, values);
    }

    public List<Budget> getBudgets() {
        List<Budget> budgets = new ArrayList<>();

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor c = database.rawQuery("SELECT " + BUDGET_NAME + ", " + BUDGET_BEGINNING_DAY + ", " +
                BUDGET_MONTHLY_BUDGET + " FROM " + TABLE_BUDGET_NAME, null);
        if (c.moveToFirst()){
            do {
                String name = c.getString(0);
                int beginningDay = c.getInt(1);
                int monthlyBudget = c.getInt(2);

                budgets.add(new Budget(name, beginningDay, monthlyBudget));
            } while(c.moveToNext());
        }
        c.close();
        return budgets;
    }

    public void insertExpense(Expense expense) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(expenseName, expense.getName());
        values.put(expenseAmount, expense.getAmount());
        values.put(expenseDate, expense.getDate().toString());
        database.insert(tableExpensesName, null, values);
    }

    public List<Expense> getExpenses() {
        List<Expense> expenses = new ArrayList<>();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor c = database.rawQuery("SELECT " + expenseName + ", " +
                expenseAmount + ", " + expenseDate + " FROM " + tableExpensesName, null);
        if (c.moveToFirst()){
            do {
                String name = c.getString(0);
                int amount = c.getInt(1);
                Date date = new Date(c.getLong(2));

                expenses.add(new Expense(name, amount, date));
            } while(c.moveToNext());
        }
        c.close();

        return expenses;
    }
}
