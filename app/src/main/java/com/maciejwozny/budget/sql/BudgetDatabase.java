package com.maciejwozny.budget.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.maciejwozny.budget.BudgetActivity;
import com.maciejwozny.budget.sql.tables.Budget;
import com.maciejwozny.budget.sql.tables.Expenditure;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import static com.maciejwozny.budget.sql.tables.Budget.*;
import static com.maciejwozny.budget.sql.tables.Budget.BUDGET_NAME;
import static com.maciejwozny.budget.sql.tables.Expenditure.*;
import static com.maciejwozny.budget.sql.tables.Period.getPeriod;

/**
 * Created by Maciej Wozny on 21.10.2017.
 * 2017-2018 All rights reserved.
 */
public class BudgetDatabase extends SQLiteOpenHelper implements IBudgetDatabase {
    private static final String TAG = BudgetDatabase.class.getSimpleName();
    private static final int version = 20180206;

    private static final String DATABASE_BUDGET_NAME = "Budget.db";
    private static final String CREATE_TABLE = "create table if not exists ";
    private static final int AMOUNT_MULTIPLIER = 100;

    private static final String createBudgets =
            CREATE_TABLE + TABLE_BUDGET_NAME +
                    " ( " +
                    BUDGET_ID + " int primary key, " +
                    BUDGET_NAME + " varchar(50) not null, " +
                    BUDGET_BEGINNING_DAY + " int default 1, " +
                    BUDGET_PERIOD + " int, " +
                    BUDGET_MONTHLY_BUDGET + " int, " +
                    BUDGET_REPEATEDLY + " boolean default false " +
                    ");";

    private static final String createExpenses =
            CREATE_TABLE + TABLE_EXPENSES_NAME + " ( " +
                    EXPENDITURE_ID + " int primary key, " +
                    EXPENDITURE_BUDGET_ID + " int, " +
                    EXPENDITURE_AMOUNT + " int not null, " +
                    EXPENDITURE_DATE + " date not null, " +
                    EXPENDITURE_NAME + " varchar(50), " +
                    "FOREIGN KEY ( " + EXPENDITURE_BUDGET_ID + " ) " +
                    "REFERENCES " + TABLE_BUDGET_NAME + "( " + BUDGET_ID + " ) " +
                    ");";

    public BudgetDatabase(Context context) {
        super(context, DATABASE_BUDGET_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createDatabase(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 20171021) {
            db.execSQL("UPDATE " + TABLE_EXPENSES_NAME + " SET " + EXPENDITURE_AMOUNT + " = " +
                EXPENDITURE_AMOUNT + " * " + Integer.toString(AMOUNT_MULTIPLIER));
        }
        else {
            dropDownDatabase(db);
            onCreate(db);
        }
    }

    private void dropDownDatabase(SQLiteDatabase db) {
        db.delete(TABLE_BUDGET_NAME, null, null);
        db.delete(TABLE_EXPENSES_NAME, null, null);
    }

    private void createDatabase(SQLiteDatabase db) {
        db.execSQL(createBudgets);
        db.execSQL(createExpenses);

        insertBudget(BudgetActivity.DEFAULT_BUDGET, db);
    }

    private void insertBudget(Budget budget, SQLiteDatabase database) {
        ContentValues values = new ContentValues();
        values.put(BUDGET_NAME, budget.getName());
        values.put(BUDGET_BEGINNING_DAY, budget.getBeginningDay());
        values.put(BUDGET_PERIOD, budget.getPeriod().getId());
        values.put(BUDGET_MONTHLY_BUDGET, budget.getMonthlyBudget());
        values.put(BUDGET_REPEATEDLY, budget.isRepeatedly());
        database.insert(TABLE_BUDGET_NAME, null, values);
        Log.d(TAG, "Inserted: " + budget.toString());
    }

    @Override
    public void insertBudget(Budget budget) {
        SQLiteDatabase database = this.getWritableDatabase();
        insertBudget(budget, database);
    }

    @Override
    public Budget getBudget(String budgetName) {
        Log.d(TAG, "Looking for: " + budgetName);
        String selection = Budget.BUDGET_NAME + " = ?";
        String[] selectionArgs = {budgetName};
        Cursor cursor = getWritableDatabase().query(
                TABLE_BUDGET_NAME,
                new String[]{BUDGET_NAME,
                        BUDGET_BEGINNING_DAY,
                        BUDGET_REPEATEDLY,
                        BUDGET_PERIOD,
                        BUDGET_MONTHLY_BUDGET},
                selection,
                selectionArgs,
                null, null, null);
        Budget budget;
        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(BUDGET_NAME));
            int beginningDay = cursor.getInt(cursor.getColumnIndexOrThrow(BUDGET_BEGINNING_DAY));
            boolean isRepeatedly = cursor.getInt(cursor.getColumnIndexOrThrow(BUDGET_REPEATEDLY)) > 0;
            int period = cursor.getInt(cursor.getColumnIndexOrThrow(BUDGET_PERIOD));
            int monthlyBudget = cursor.getInt(cursor.getColumnIndexOrThrow(BUDGET_MONTHLY_BUDGET));
            budget = new Budget(name, beginningDay, monthlyBudget, getPeriod(period), isRepeatedly);
            Log.d(TAG, "Found: " + budget.toString());
            return budget;
        }
        budget = new Budget("", 0, 0);
        Log.i(TAG, "Not found: \'" + budgetName + "\', returning: " + budget.toString());
        return budget;
    }

    @Override
    public List<Budget> getBudgets() {
        List<Budget> budgets = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(
                TABLE_BUDGET_NAME,
                new String[]{BUDGET_NAME,
                        BUDGET_BEGINNING_DAY,
                        BUDGET_REPEATEDLY,
                        BUDGET_PERIOD,
                        BUDGET_MONTHLY_BUDGET},
                null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(BUDGET_NAME));
                int beginningDay = cursor.getInt(cursor.getColumnIndexOrThrow(BUDGET_BEGINNING_DAY));
                boolean isRepeatedly = cursor.getInt(cursor.getColumnIndexOrThrow(BUDGET_REPEATEDLY)) > 0;
                int period = cursor.getInt(cursor.getColumnIndexOrThrow(BUDGET_PERIOD));
                int monthlyBudget = cursor.getInt(cursor.getColumnIndexOrThrow(BUDGET_MONTHLY_BUDGET));
                budgets.add(new Budget(name, beginningDay, monthlyBudget, getPeriod(period), isRepeatedly));
            } while (cursor.moveToNext());
        }
        Log.d(TAG, "All budgets: " + budgets);
        return budgets;
    }

    @Override
    public int getBudgetId(String budgetName) {
        if (budgetName == null) {
            return 0;
        }
        String selection = Budget.BUDGET_NAME + " = ?";
        String[] selectionArgs = {budgetName};
        Cursor cursor = getWritableDatabase().query(
                TABLE_BUDGET_NAME,
                new String[]{BUDGET_ID},
                selection,
                selectionArgs,
                null, null, null);
        if (cursor.moveToFirst()) {
            return cursor.getInt(cursor.getColumnIndexOrThrow(BUDGET_ID));
        }
        return -1;
    }

    @Override
    public void insertExpenditure(Expenditure expenditure) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EXPENDITURE_BUDGET_ID, expenditure.getBudgetId());
        values.put(EXPENDITURE_NAME, expenditure.getName());
        int amount = (int) (expenditure.getAmount() * AMOUNT_MULTIPLIER);
        values.put(EXPENDITURE_AMOUNT, amount);
        values.put(EXPENDITURE_DATE, expenditure.getDate().getTime());
        database.insert(TABLE_EXPENSES_NAME, null, values);
        Log.d(TAG, "Inserted: " + expenditure.toString());
    }

    @Override
    public void removeExpense(Expenditure expenditure) {
        SQLiteDatabase database = this.getWritableDatabase();
        String whereClause = EXPENDITURE_NAME + "=? and " + EXPENDITURE_AMOUNT + "=? and "
                + EXPENDITURE_DATE + "=?";
        int amount = (int) (expenditure.getAmount() * AMOUNT_MULTIPLIER);
        String[] whereArgs = new String[]{expenditure.getName(),
                Integer.toString(amount),
                Long.toString(expenditure.getDate().getTime())};
        database.delete(TABLE_EXPENSES_NAME, whereClause, whereArgs);
    }

    @Override
    public List<Expenditure> getExpenditures(int budgetId, Date fromDate) {
        List<Expenditure> expenditures = new ArrayList<>();

        String selection = EXPENDITURE_BUDGET_ID + " = ? and " + EXPENDITURE_DATE + " >= ?";
        String[] selectionArgs = {Integer.toString(budgetId), Long.toString(fromDate.getTime())};
        Cursor cursor = getWritableDatabase().query(
                TABLE_EXPENSES_NAME,
                new String[]{EXPENDITURE_NAME,
                        EXPENDITURE_AMOUNT,
                        EXPENDITURE_DATE},
                selection,
                selectionArgs,
                null, null, EXPENDITURE_DATE + " DESC");

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(EXPENDITURE_NAME));
                double amount = (double) cursor.getInt(cursor.getColumnIndexOrThrow(EXPENDITURE_AMOUNT))
                        / AMOUNT_MULTIPLIER;
                Date date = new Date(cursor.getLong(cursor.getColumnIndexOrThrow(EXPENDITURE_DATE)));
                expenditures.add(new Expenditure(budgetId, name, amount, date));
            } while (cursor.moveToNext());
        }
        Log.d(TAG, "All expenditures: " + expenditures);
        return expenditures;
    }

}