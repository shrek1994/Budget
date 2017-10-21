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
import java.util.Arrays;
import java.util.Calendar;

import static java.sql.Date.valueOf;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by maciej on 21.10.17.
 */
public class MonthlyBudgetTest {
    private static final int MONTHLY_BUDGET = 1000;
    private static final int BEGINNING_DAY = 1;
    private static final int DAYS_OF_MONTH = 30;
    private static final int DAY_OF_MONTH = 6;
    private static final Budget BUDGET = new Budget("name", BEGINNING_DAY, MONTHLY_BUDGET);
    private static final int BUDGET_ID = 123;
    private static final Date FIRST_DAY_OF_PERIOD = valueOf("2000-04-01");
    private static final Date TODAY = valueOf("2000-04-06");

    private MonthlyBudget sut;

    @Mock private IBudgetDatabase budgetDatabase;
    @Mock private Calendar calendar;
    @Mock private Calendar firstDayOfPeriodCalendar;
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setup() {
        sut = new MonthlyBudget(budgetDatabase, calendar);
        when(calendar.get(Calendar.DAY_OF_MONTH)).thenReturn(DAY_OF_MONTH);
        when(calendar.getActualMaximum(Calendar.DAY_OF_MONTH)).thenReturn(DAYS_OF_MONTH);
        when(calendar.clone()).thenReturn(firstDayOfPeriodCalendar);
        when(calendar.getTimeInMillis()).thenReturn(TODAY.getTime());
        when(firstDayOfPeriodCalendar.getTimeInMillis()).thenReturn(FIRST_DAY_OF_PERIOD.getTime());
        when(budgetDatabase.getBudgetId(BUDGET.getName())).thenReturn(BUDGET_ID);
        when(budgetDatabase.getBudget(BUDGET.getName())).thenReturn(BUDGET);
    }

    @Test
    public void shouldCorrectReturnMonthlyBudget() {
        assertEquals(MONTHLY_BUDGET, sut.getMonthlyBugdget(), 0.001);
    }


    @Test
    public void shouldCorrectReturnMonthlySpends() {
        double amount = 50;

        when(budgetDatabase.getExpenditures(BUDGET_ID, FIRST_DAY_OF_PERIOD))
                .thenReturn(Arrays.asList(new Expenditure("", 25, TODAY),
                        new Expenditure("", (int)amount, TODAY)));

        assertEquals(amount, sut.getMonthlySpends(), 0.001);
    }


    @Test
    public void shouldCorrectReturnMonthlyRemaining() {
        when(budgetDatabase.getExpenditures(BUDGET_ID, FIRST_DAY_OF_PERIOD))
                .thenReturn(Arrays.asList(new Expenditure("", 25, TODAY),
                        new Expenditure("", 50, TODAY)));

        assertEquals(950.0, sut.getMonthlyRemaining(), 0.001);
    }

}