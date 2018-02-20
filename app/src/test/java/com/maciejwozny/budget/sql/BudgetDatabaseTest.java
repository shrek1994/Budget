package com.maciejwozny.budget.sql;

import android.database.sqlite.SQLiteDatabase;

import com.maciejwozny.budget.BuildConfig;
import com.maciejwozny.budget.sql.tables.Budget;
import com.maciejwozny.budget.sql.tables.Expenditure;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static com.maciejwozny.budget.BudgetActivity.DEFAULT_BUDGET;
import static org.junit.Assert.*;

/**
 * Created by Maciej Wozny on 21.10.2017.
 * 2017-2018 All rights reserved.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class,
        sdk = LOLLIPOP,
        manifest = "src/main/AndroidManifest.xml")
public class BudgetDatabaseTest {
    private final Budget BUDGET = new Budget("name", 1, 1000);
    private final Expenditure EXPENSE =
            new Expenditure("name", 50.75, Date.valueOf("2000-04-01"));
    private final Expenditure OLDEST_EXPENSE =
            new Expenditure("old", 1.01, Date.valueOf("2000-03-25"));
    private final Expenditure NEWEST_EXPENSE =
            new Expenditure("new", 9.99, Date.valueOf("2000-04-05"));

    private BudgetDatabase sut;
    private SQLiteDatabase database;

    @Before
    public void setup() {
        sut = new BudgetDatabase(RuntimeEnvironment.application);
        database = sut.getWritableDatabase();
    }

    @After
    public void teardown() {
        database.close();
    }

    @Test
    public void shouldGetOpenedDatabase(){
        assertTrue(database.isOpen());
    }

    @Test
    public void shouldCorrectInsertBudgetIntoDatabase() {
        List<Budget> expectedBudgets = new ArrayList<>(Arrays.asList(DEFAULT_BUDGET, BUDGET));

        sut.insertBudget(DEFAULT_BUDGET);
        sut.insertBudget(BUDGET);

        assertEquals(expectedBudgets, sut.getBudgets());
    }

    @Test
    public void shouldCorrectReturnBudgetUsingName() {
        sut.insertBudget(BUDGET);

        assertEquals(BUDGET, sut.getBudget(BUDGET.getName()));
    }

    @Test
    public void shouldCorrectReturnBudgetId() {
        int expectedBudgetId = 0;

        sut.insertBudget(BUDGET);

        assertEquals(expectedBudgetId, sut.getBudgetId(BUDGET.getName()));
    }

    @Test
    public void shouldCorrectInsertExpensesIntoDatabase() {
        List<Expenditure> expectedExpenditures = new ArrayList<>(Collections.singletonList(EXPENSE));

        sut.insertExpenditure(EXPENSE);

        assertEquals(expectedExpenditures, sut.getExpenditures(Date.valueOf("2000-04-01")));
    }


    @Test
    public void shouldNotGetExpensesBeforeDate() {
        sut.insertExpenditure(EXPENSE);

        assertEquals(new ArrayList<Expenditure>(),
                sut.getExpenditures(Date.valueOf("2000-04-05")));
    }

    @Test
    public void shouldGetExpensesFromTheSameDay() {
        List<Expenditure> expectedExpenditures = new ArrayList<>(Collections.singletonList(EXPENSE));

        sut.insertExpenditure(EXPENSE);

        assertEquals(expectedExpenditures, sut.getExpenditures(Date.valueOf("2000-04-01")));
    }

    @Test
    public void shouldRemoveExpensesCorrectly() {
        sut.insertExpenditure(EXPENSE);
        sut.removeExpense(EXPENSE);

        assertEquals(new ArrayList<Expenditure>(),
                sut.getExpenditures(Date.valueOf("2000-04-01")));
    }

    @Test
    public void shouldUpdateFrom20171021VersionTo20180206Ver_shouldMultiplyByHundred() {
        sut.insertExpenditure(EXPENSE);
        sut.onUpgrade(sut.getWritableDatabase(), 20171021, 20180206);

        assertEquals(new ArrayList<Expenditure>(
                Collections.singletonList(new Expenditure("name", 5075,
                                                            Date.valueOf("2000-04-01")))),
                sut.getExpenditures(Date.valueOf("2000-04-01")));
    }


    @Test
    public void shouldReturnSortedExpendituresFromNewestToOldest() {
        sut.insertExpenditure(EXPENSE);
        sut.insertExpenditure(OLDEST_EXPENSE);
        sut.insertExpenditure(NEWEST_EXPENSE);

        assertEquals(new ArrayList<>(Arrays.asList(NEWEST_EXPENSE, EXPENSE, OLDEST_EXPENSE)),
                sut.getExpenditures(Date.valueOf("2000-01-01")));
    }
}