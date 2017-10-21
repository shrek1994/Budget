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
import java.util.Calendar;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by maciej on 21.10.17.
 */
public class DailyBudgetTest {
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
    }

    @Test
    public void shouldCorrectCalculateDailyBudget() {
        int monthlyBudget = 1000;
        int beginningDay = 1;
        int daysOfMonth = 30;
        int dayOfMonth = 6;
        Budget budget = new Budget("name", beginningDay, monthlyBudget);
        double expectedDailyBudget = 40.00;

        when(calendar.get(Calendar.DAY_OF_MONTH)).thenReturn(dayOfMonth);
        when(calendar.getActualMaximum(Calendar.DAY_OF_MONTH)).thenReturn(daysOfMonth);
        when(budgetDatabase.getBudget(budget.getName())).thenReturn(budget);
        when(budgetDatabase.getExpenditures()).thenReturn(new ArrayList<Expenditure>());

        assertEquals(expectedDailyBudget, sut.getDailyBudget(budget.getName()), 0.001);
    }

}