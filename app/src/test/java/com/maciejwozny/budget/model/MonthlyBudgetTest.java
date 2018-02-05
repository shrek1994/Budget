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
    private static final int BEGINNING_DAY = 10;
    private static final Budget BUDGET = new Budget("name", BEGINNING_DAY, MONTHLY_BUDGET);
    private static final int BUDGET_ID = 123;
    private static final Date FIRST_DAY_OF_PERIOD = valueOf("2000-04-10");
    private static final Date MIDDLE_OF_THE_PERIOD = valueOf("2000-04-25");
    private static final Date NEXT_MONTH_SAME_PERIOD = valueOf("2000-05-03");
    private static final Date TODAY = valueOf("2000-04-16");

    private MonthlyBudget sut;

    @Mock private IBudgetDatabase budgetDatabase;
    @Mock private Calendar calendar;
    @Mock private Calendar firstDayOfPeriodCalendar;
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private void setToday(java.util.Date today) {
        calendar.setTime(today);
        when(calendar.clone()).thenReturn(firstDayOfPeriodCalendar);
        when(firstDayOfPeriodCalendar.getTimeInMillis()).thenReturn(FIRST_DAY_OF_PERIOD.getTime());
    }

    @Before
    public void setup() {
        sut = new MonthlyBudget(budgetDatabase, calendar);
        setToday(TODAY);
        when(budgetDatabase.getBudgetId(BUDGET.getName())).thenReturn(BUDGET_ID);
        when(budgetDatabase.getBudget(BUDGET.getName())).thenReturn(BUDGET);
    }

    @Test
    public void shouldCorrectReturnMonthlyBudget() {
        assertEquals(MONTHLY_BUDGET, sut.getMonthlyBudget(BUDGET.getName()), 0.001);
    }


    @Test
    public void shouldCorrectReturnMonthlySpends() {
        double amount = 50;

        when(budgetDatabase.getExpenditures(BUDGET_ID, FIRST_DAY_OF_PERIOD))
                .thenReturn(Arrays.asList(new Expenditure("name", (int)amount, MIDDLE_OF_THE_PERIOD)));

        assertEquals(amount, sut.getMonthlySpends(BUDGET.getName()), 0.001);
    }


    @Test
    public void shouldCorrectReturnMonthlyRemaining() {
        when(budgetDatabase.getExpenditures(BUDGET_ID, FIRST_DAY_OF_PERIOD))
                .thenReturn(Arrays.asList(new Expenditure("name", 50, MIDDLE_OF_THE_PERIOD)));

        assertEquals(950.0, sut.getMonthlyRemaining(BUDGET.getName()), 0.001);
    }

    //TODO fix its
    @Test
    public void shouldCorrectCalculateMonthlySpendsInNextMonthButInTheSamePeriod() {
        setToday(NEXT_MONTH_SAME_PERIOD);

        when(budgetDatabase.getExpenditures(BUDGET_ID, FIRST_DAY_OF_PERIOD))
                .thenReturn(Arrays.asList(new Expenditure("name", 50, MIDDLE_OF_THE_PERIOD),
                        new Expenditure("name", 50, NEXT_MONTH_SAME_PERIOD)));

        assertEquals(100.0, sut.getMonthlySpends(BUDGET.getName()), 0.001);
    }

}