package com.maciejwozny.budget.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.maciejwozny.budget.sql.tables.Budget;
import com.maciejwozny.budget.sql.tables.Period;

import java.util.ArrayList;
import java.util.List;

import static com.maciejwozny.budget.sql.tables.Budget.*;
import static com.maciejwozny.budget.sql.tables.Expenses.*;
import static com.maciejwozny.budget.sql.tables.Period.getPeriod;

/**
 * Created by maciek on 30.01.16.
 */
public class DatabaseAdapter extends SQLiteOpenHelper {
    private static final int version = 20171021;

    private static final String databaseBudgetName = "PoorBudget.db";
    private final String createTable =  "create table if not exists ";

    private String createBudgets =
            createTable + tableBudgetName +
                    " ( " +
                    budgetId + " int primary key, " +
                    budgetName + " varchar(50) not null, " +
                    budgetBeginningDay + " int default 1, " +
                    budgetPeriod + " int, " +
                    budgetRepeatedly + " boolean default false " +
                    ");";

    private String createExpenses =
            createTable + tableExpensesName + " ( " +
                    expenseId + " int primary key, " +
                    expenseBudgetId + " int, " +
                    expenseAmount + " int not null, " +
                    expenseDate + " date not null, " +
                    expenseName + " varchar(50), " +
                    "FOREIGN KEY ( " + expenseBudgetId + " ) " +
                    "REFERENCES " + tableBudgetName + "( " + budgetId + " ) " +
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
        values.put(budgetName, budget.getName());
        values.put(budgetBeginningDay, budget.getBeginningDay());
        values.put(budgetPeriod, budget.getPeriod().getId());
        values.put(budgetRepeatedly, budget.isRepeatedly());
        database.insert(tableBudgetName, null, values);
    }

    public List<Budget> getBudgets() {
        List<Budget> budgets = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(
                tableBudgetName,
                new String[]{budgetName,
                            budgetBeginningDay,
                            budgetRepeatedly,
                            budgetPeriod},
                null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(budgetName));
                int beginningDay = cursor.getInt(cursor.getColumnIndexOrThrow(budgetBeginningDay));
                boolean isRepeatedly = cursor.getInt(cursor.getColumnIndexOrThrow(budgetRepeatedly)) > 0;
                int period = cursor.getInt(cursor.getColumnIndexOrThrow(budgetPeriod));
                budgets.add(new Budget(name, beginningDay, getPeriod(period), isRepeatedly));
            } while (cursor.moveToNext());
        }
        return budgets;
    }
}
