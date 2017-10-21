package com.maciejwozny.budget.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.maciejwozny.budget.sql.tables.Budget;
import com.maciejwozny.budget.sql.tables.Expenditure;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static com.maciejwozny.budget.sql.tables.Budget.*;
import static com.maciejwozny.budget.sql.tables.Budget.BUDGET_NAME;
import static com.maciejwozny.budget.sql.tables.Expenditure.*;
import static com.maciejwozny.budget.sql.tables.Period.getPeriod;

/**
 * Created by maciek on 30.01.16.
 */
public class BudgetDatabase extends SQLiteOpenHelper implements IBudgetDatabase {
    private static final int version = 20171021;

    private static final String DATABASE_BUDGET_NAME = "Budget.db";
    private static final String CREATE_TABLE =  "create table if not exists ";

    private String createBudgets =
            CREATE_TABLE + TABLE_BUDGET_NAME +
                    " ( " +
                    BUDGET_ID + " int primary key, " +
                    BUDGET_NAME + " varchar(50) not null, " +
                    BUDGET_BEGINNING_DAY + " int default 1, " +
                    BUDGET_PERIOD + " int, " +
                    BUDGET_MONTHLY_BUDGET + " int, " +
                    BUDGET_REPEATEDLY + " boolean default false " +
                    ");";

    private String createExpenses =
            CREATE_TABLE + TABLE_EXPENSES_NAME + " ( " +
                    EXPENDITURE_ID + " int primary key, " +
                    EXPENDITURE_BUDGET_ID + " int, " +
                    EXPENDITURE_AMOUNT + " int not null, " +
                    EXPENDITURE_DATE + " date not null, " +
                    EXPENDITURE_NAME + " varchar(50), " +
                    "FOREIGN KEY ( " + EXPENDITURE_BUDGET_ID + " ) " +
                    "REFERENCES " + TABLE_BUDGET_NAME + "( " + BUDGET_ID + " ) " +
                    ");";

    BudgetDatabase(Context context){
        super(context, DATABASE_BUDGET_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createBudgets);
        db.execSQL(createExpenses);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void insertBudget(Budget budget) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BUDGET_NAME, budget.getName());
        values.put(BUDGET_BEGINNING_DAY, budget.getBeginningDay());
        values.put(BUDGET_PERIOD, budget.getPeriod().getId());
        values.put(BUDGET_MONTHLY_BUDGET, budget.getMonthlyBudget());
        values.put(BUDGET_REPEATEDLY, budget.isRepeatedly());
        database.insert(TABLE_BUDGET_NAME, null, values);
    }

    @Override
    public Budget getBudget(String budgetName) {
        String selection = Budget.BUDGET_NAME + " = ?";
        String[] selectionArgs = { budgetName };
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

        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(BUDGET_NAME));
            int beginningDay = cursor.getInt(cursor.getColumnIndexOrThrow(BUDGET_BEGINNING_DAY));
            boolean isRepeatedly = cursor.getInt(cursor.getColumnIndexOrThrow(BUDGET_REPEATEDLY)) > 0;
            int period = cursor.getInt(cursor.getColumnIndexOrThrow(BUDGET_PERIOD));
            int monthlyBudget = cursor.getInt(cursor.getColumnIndexOrThrow(BUDGET_MONTHLY_BUDGET));
            return new Budget(name, beginningDay, monthlyBudget, getPeriod(period), isRepeatedly);
        }
        return null;
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
        return budgets;
    }

    @Override
    public int getBudgetId(String budgetName) {
        String selection = Budget.BUDGET_NAME + " = ?";
        String[] selectionArgs = { budgetName };
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
        values.put(EXPENDITURE_AMOUNT, expenditure.getAmount());
        values.put(EXPENDITURE_DATE, expenditure.getDate().getTime());
        database.insert(TABLE_EXPENSES_NAME, null, values);
    }

    @Override
    public List<Expenditure> getExpenditures(int budgetId) {
        List<Expenditure> expenditures = new ArrayList<>();

        String selection = EXPENDITURE_BUDGET_ID + " = ?";
        String[] selectionArgs = { Integer.toString(budgetId) };
        Cursor cursor = getWritableDatabase().query(
                TABLE_EXPENSES_NAME,
                new String[]{EXPENDITURE_NAME,
                        EXPENDITURE_AMOUNT,
                        EXPENDITURE_DATE},
                selection,
                selectionArgs,
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(EXPENDITURE_NAME));
                int amount = cursor.getInt(cursor.getColumnIndexOrThrow(EXPENDITURE_AMOUNT));
                Date date = new Date(cursor.getLong(cursor.getColumnIndexOrThrow(EXPENDITURE_DATE)));
                expenditures.add(new Expenditure(budgetId, name, amount, date));
            } while (cursor.moveToNext());
        }
        return expenditures;
    }
}
