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
 * Created by maciej on 21.10.17.
 */
public class DailyBudgetTest {
    private static final int MONTHLY_BUDGET = 1000;
    private static final int BEGINNING_DAY = 1;
    private static final int DAYS_OF_MONTH = 30;
    private static final int DAY_OF_MONTH = 6;
    private static final Budget BUDGET = new Budget("name", BEGINNING_DAY, MONTHLY_BUDGET);
    private static final int BUDGET_ID = 123;
    private static final Date FIRST_DAY_OF_PERIOD = valueOf("2000-04-01");
    private static final Date TODAY = valueOf("2000-04-06");

    @Mock
    private IBudgetDatabase budgetDatabase;
    @Mock
    private Calendar calendar;
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private DailyBudget sut;

    @Before
    public void setup() {
        sut = new DailyBudget(budgetDatabase, calendar);
        when(calendar.getTimeInMillis()).thenReturn(FIRST_DAY_OF_PERIOD.getTime());
        when(calendar.get(Calendar.DAY_OF_MONTH)).thenReturn(DAY_OF_MONTH);
        when(calendar.getActualMaximum(Calendar.DAY_OF_MONTH)).thenReturn(DAYS_OF_MONTH);
        when(budgetDatabase.getBudgetId(BUDGET.getName())).thenReturn(BUDGET_ID);
        when(budgetDatabase.getBudget(BUDGET.getName())).thenReturn(BUDGET);
    }

    @Test
    public void shouldCorrectCalculateDailyBudgetWithoutAnyExpenditures() {
        double expectedDailyBudget = 40.00;

        when(budgetDatabase.getExpenditures(BUDGET_ID, FIRST_DAY_OF_PERIOD)).thenReturn(new ArrayList<Expenditure>());

        assertEquals(expectedDailyBudget, sut.getDailyBudget(BUDGET.getName()), 0.001);
    }


    @Test
    public void shouldCorrectCalculateDailyBudgetWithExpenditures() {
        double expectedDailyBudget = 37.00;

        when(budgetDatabase.getExpenditures(BUDGET_ID, FIRST_DAY_OF_PERIOD))
                .thenReturn(Arrays.asList(new Expenditure("", 25, TODAY),
                                          new Expenditure("", 50, TODAY)));

        assertEquals(expectedDailyBudget, sut.getDailyBudget(BUDGET.getName()), 0.001);
    }
}