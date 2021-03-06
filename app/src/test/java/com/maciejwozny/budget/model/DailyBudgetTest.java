package com.maciejwozny.budget.model;

import com.maciejwozny.budget.BuildConfig;
import com.maciejwozny.budget.sql.IBudgetDatabase;
import com.maciejwozny.budget.sql.tables.Budget;
import com.maciejwozny.budget.sql.tables.Expenditure;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static com.maciejwozny.budget.BudgetActivity.DEFAULT_BUDGET;
import static java.sql.Date.valueOf;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by Maciej Wozny on 21.10.2017.
 * 2017 All rights reserved.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class,
        sdk = LOLLIPOP,
        manifest = "src/main/AndroidManifest.xml")
public class DailyBudgetTest {
    private static final int MONTHLY_BUDGET = 1000;
    private static final int BEGINNING_DAY = 5;
    private static final Budget BUDGET = new Budget("name", BEGINNING_DAY, MONTHLY_BUDGET);
    private static final int BUDGET_ID = 123;
    private static final Date FIRST_DAY_OF_PERIOD = valueOf("2000-04-05");
    private static final Date FIRST_DAY_OF_NEXT_MONTH = valueOf("2000-05-01");
    private static final Date TODAY = valueOf("2000-04-10");

    @Mock private IBudgetDatabase budgetDatabase;
    private Calendar calendar = Calendar.getInstance();
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private DailyBudget sut;

    private void setToday(java.util.Date today) {
        calendar.setTime(today);
    }

    @Before
    public void setup() {
        sut = new DailyBudget(RuntimeEnvironment.application, budgetDatabase, calendar);
        DEFAULT_BUDGET = BUDGET;
        when(budgetDatabase.getBudgetId(BUDGET.getName())).thenReturn(BUDGET_ID);
        when(budgetDatabase.getBudget(BUDGET.getName())).thenReturn(BUDGET);
    }

    @Test
    public void shouldCorrectCalculateDailyBudgetWithoutAnyExpenditures() {
        double expectedDailyBudget = 33.33;

        setToday(FIRST_DAY_OF_PERIOD);
        when(budgetDatabase.getExpenditures(FIRST_DAY_OF_PERIOD))
                .thenReturn(new ArrayList<Expenditure>());

        assertEquals(expectedDailyBudget, sut.getDailyBudget(), 0.001);
    }


    @Test
    public void shouldCorrectCalculateDailyBudgetWithExpenditures() {
        double expectedDailyBudget = 37.00;

        setToday(TODAY);
        when(budgetDatabase.getExpenditures(FIRST_DAY_OF_PERIOD))
                .thenReturn(Arrays.asList(new Expenditure("", 25, FIRST_DAY_OF_PERIOD),
                                          new Expenditure("", 50, FIRST_DAY_OF_PERIOD)));

        assertEquals(expectedDailyBudget, sut.getDailyBudget(), 0.001);
    }


    @Test
    public void shouldCorrectCalculateRemainingBudgetWithExpenditures() {
        double expectedDailyBudget = 36.00;
        double expectedRemainingBudget = 11.00;

        setToday(TODAY);
        when(budgetDatabase.getExpenditures(FIRST_DAY_OF_PERIOD))
                .thenReturn(Arrays.asList(
                        new Expenditure("", 100, FIRST_DAY_OF_PERIOD),
                        new Expenditure("", 25, TODAY)));
        when(budgetDatabase.getExpenditures(TODAY))
                .thenReturn(Arrays.asList(new Expenditure("", 25, TODAY)));

        assertEquals(expectedDailyBudget, sut.getDailyBudget(), 0.001);
        assertEquals(expectedRemainingBudget, sut.getDailyRemainingBudget(), 0.001);
    }

    @Test
    public void shouldCorrectCalculateInNextMonthButInSamePeriod() {
        setToday(FIRST_DAY_OF_NEXT_MONTH);
        when(budgetDatabase.getExpenditures(FIRST_DAY_OF_PERIOD))
                .thenReturn(Arrays.asList(
                        new Expenditure("", 400, FIRST_DAY_OF_PERIOD),
                        new Expenditure("", 400, TODAY)));
        when(budgetDatabase.getExpenditures(FIRST_DAY_OF_NEXT_MONTH))
                .thenReturn(new ArrayList<Expenditure>());

        assertEquals(50, sut.getDailyBudget(), 0.001);
        assertEquals(50, sut.getDailyRemainingBudget(), 0.001);
    }


    @Test
    public void shouldCorrectCalculateRemainingBudgetWithExpendituresWithFloatValue() {
        setToday(TODAY);

        when(budgetDatabase.getExpenditures(FIRST_DAY_OF_PERIOD))
                .thenReturn(Arrays.asList(
                        new Expenditure("", 162.75, FIRST_DAY_OF_PERIOD),
                        new Expenditure("", 10.38, TODAY)));
        when(budgetDatabase.getExpenditures(TODAY))
                .thenReturn(Arrays.asList(new Expenditure("", 10.38, TODAY)));

        assertEquals(33.49, sut.getDailyBudget(), 0.001);
        assertEquals(23.11, sut.getDailyRemainingBudget(), 0.001);
    }
}