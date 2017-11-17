package com.maciejwozny.budget.sql;

import android.database.sqlite.SQLiteDatabase;

import com.maciejwozny.budget.BuildConfig;
import com.maciejwozny.budget.sql.tables.Budget;
import com.maciejwozny.budget.sql.tables.Expense;
import com.maciejwozny.budget.sql.tables.Period;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.sql.Date;
import java.util.ArrayList;

import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static org.junit.Assert.*;

/**
 * Created by maciek on 21.10.17.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class,
        sdk = LOLLIPOP,
        manifest = "src/main/AndroidManifest.xml")
public class DatabaseAdapterTest {
    private DatabaseAdapter sut;
    private Budget budget = new Budget("bank", 10, Period.Months, true);
    private Expense expense = new Expense("dinner", 123, new Date(123456789));

    @Before
    public void setup() {
        sut = new DatabaseAdapter(RuntimeEnvironment.application);
    }

    @Test
    public void shouldGetOpenedDatabase(){
        SQLiteDatabase database = sut.getWritableDatabase();
        assertTrue(database.isOpen());
        sut.onCreate(database);
        database.close();
    }

    @Test
    public void shouldReturnInsertedBudget() {
        SQLiteDatabase database = sut.getWritableDatabase();
        sut.onCreate(database);
        sut.insertBudget(budget);

        ArrayList<Budget> expectedBudget = new ArrayList<Budget>();
        expectedBudget.add(budget);

        assertEquals(expectedBudget, sut.getBudgets());
    }

    @Test
    public void shouldReturnInsertedExpenses() {
        SQLiteDatabase database = sut.getWritableDatabase();
        sut.onCreate(database);
        sut.insertExpense(expense);

        ArrayList<Expense> expectedExpense = new ArrayList<Expense>();
        expectedExpense.add(expense);

        assertEquals(expectedExpense, sut.getExpenses());
    }


}