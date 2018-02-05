package com.maciejwozny.budget.model;

import com.maciejwozny.budget.sql.IBudgetDatabase;
import com.maciejwozny.budget.sql.tables.Budget;
import com.maciejwozny.budget.sql.tables.Expenditure;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import static java.sql.Date.valueOf;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by Maciej Wozny on 21.10.2017.
 * 2017 All rights reserved.
 */
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
        sut = new DailyBudget(budgetDatabase, calendar);
        when(budgetDatabase.getBudgetId(BUDGET.getName())).thenReturn(BUDGET_ID);
        when(budgetDatabase.getBudget(BUDGET.getName())).thenReturn(BUDGET);
    }

    @Test
    public void shouldCorrectCalculateDailyBudgetWithoutAnyExpenditures() {
        double expectedDailyBudget = 33.00;

        setToday(FIRST_DAY_OF_PERIOD);
        when(budgetDatabase.getExpenditures(BUDGET_ID, FIRST_DAY_OF_PERIOD))
                .thenReturn(new ArrayList<Expenditure>());

        assertEquals(expectedDailyBudget, sut.getDailyBudget(BUDGET.getName()), 0.001);
    }


    @Test
    public void shouldCorrectCalculateDailyBudgetWithExpenditures() {
        double expectedDailyBudget = 37.00;

        setToday(TODAY);
        when(budgetDatabase.getExpenditures(BUDGET_ID, FIRST_DAY_OF_PERIOD))
                .thenReturn(Arrays.asList(new Expenditure("", 25, FIRST_DAY_OF_PERIOD),
                                          new Expenditure("", 50, FIRST_DAY_OF_PERIOD)));

        assertEquals(expectedDailyBudget, sut.getDailyBudget(BUDGET.getName()), 0.001);
    }


    @Test
    public void shouldCorrectCalculateRemainingBudgetWithExpenditures() {
        double expectedDailyBudget = 36.00;
        double expectedRemainingBudget = 11.00;

        setToday(TODAY);
        when(budgetDatabase.getExpenditures(BUDGET_ID, FIRST_DAY_OF_PERIOD))
                .thenReturn(Arrays.asList(
                        new Expenditure("", 100, FIRST_DAY_OF_PERIOD),
                        new Expenditure("", 25, TODAY)));
        when(budgetDatabase.getExpenditures(BUDGET_ID, TODAY))
                .thenReturn(Arrays.asList(new Expenditure("", 25, TODAY)));

        assertEquals(expectedDailyBudget, sut.getDailyBudget(BUDGET.getName()), 0.001);
        assertEquals(expectedRemainingBudget, sut.getDailyRemainingBudget(BUDGET.getName()), 0.001);
    }

    @Test
    public void shouldCorrectCalculateInNextMonthButInSamePeriod() {
        setToday(FIRST_DAY_OF_NEXT_MONTH);
        when(budgetDatabase.getExpenditures(BUDGET_ID, FIRST_DAY_OF_PERIOD))
                .thenReturn(Arrays.asList(
                        new Expenditure("", 400, FIRST_DAY_OF_PERIOD),
                        new Expenditure("", 400, TODAY)));
        when(budgetDatabase.getExpenditures(BUDGET_ID, FIRST_DAY_OF_NEXT_MONTH))
                .thenReturn(new ArrayList<Expenditure>());

        assertEquals(50, sut.getDailyBudget(BUDGET.getName()), 0.001);
        assertEquals(50, sut.getDailyRemainingBudget(BUDGET.getName()), 0.001);
    }
}