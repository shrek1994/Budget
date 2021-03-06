package com.maciejwozny.budget.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.maciejwozny.budget.BuildConfig;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Calendar;
import java.util.Date;

import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static com.maciejwozny.budget.BudgetActivity.DEFAULT_BUDGET;
import static com.maciejwozny.budget.SettingsActivity.BUDGET_VALUE;
import static com.maciejwozny.budget.SettingsActivity.DAY_OF_PAYMENT;
import static java.sql.Date.valueOf;
import static org.junit.Assert.assertEquals;

/**
 * Created by Maciej Wozny on 07.12.2017.
 * 2017-2018 All rights reserved.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class,
        sdk = LOLLIPOP,
        manifest = "src/main/AndroidManifest.xml")
public class IntegrationTest {
    private Budget sut;
    private static final String FIRST_DAY_OF_NEXT_MONTH_STRING = "2000-05-01";
    private static final Date FIRST_DAY_OF_NEXT_MONTH = valueOf(FIRST_DAY_OF_NEXT_MONTH_STRING);
    private static final String FIRST_DAY_OF_PERIOD_STRING = "2000-04-10";
    private static final Date FIRST_DAY_OF_PERIOD = valueOf(FIRST_DAY_OF_PERIOD_STRING);
    private Calendar calendar = Calendar.getInstance();


    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setup() {
        Context context = RuntimeEnvironment.application;
        sut = Budget.build(context, calendar);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(BUDGET_VALUE, "1500");
        editor.putString(DAY_OF_PAYMENT, "10");
        editor.commit();
    }

    private void setToday(Date today) {
        calendar.setTime(today);
    }

    @Test
    public void shouldCorrectShowsBudgetsAfterInsertExpense() {
        setToday(FIRST_DAY_OF_PERIOD);

        assertEquals(0, sut.monthlyBudget.getMonthlySpends(), 0.001);
        assertEquals(1500, sut.monthlyBudget.getMonthlyBudget(), 0.001);
        assertEquals(1500, sut.monthlyBudget.getMonthlyRemaining(), 0.001);

        assertEquals(50, sut.dailyBudget.getDailyBudget(), 0.001);
        assertEquals(50, sut.dailyBudget.getDailyRemainingBudget(), 0.001);

        sut.expenseAdditional.addExpense("name", 50, FIRST_DAY_OF_PERIOD_STRING);

        assertEquals(50, sut.monthlyBudget.getMonthlySpends(), 0.001);
        assertEquals(1500, sut.monthlyBudget.getMonthlyBudget(), 0.001);
        assertEquals(1450, sut.monthlyBudget.getMonthlyRemaining(), 0.001);

        assertEquals(50, sut.dailyBudget.getDailyBudget(), 0.001);
        assertEquals(0, sut.dailyBudget.getDailyRemainingBudget(), 0.001);
    }


    @Test
    public void shouldCorrectShowsBudgetsInNextMonthButInTheSamePeriod() {
        sut.expenseAdditional.addExpense("name", 1000, FIRST_DAY_OF_PERIOD_STRING);

        setToday(FIRST_DAY_OF_NEXT_MONTH);

        assertEquals(1000, sut.monthlyBudget.getMonthlySpends(), 0.001);
        assertEquals(1500, sut.monthlyBudget.getMonthlyBudget(), 0.001);
        assertEquals(500, sut.monthlyBudget.getMonthlyRemaining(), 0.001);

        assertEquals(55.55, sut.dailyBudget.getDailyBudget(), 0.001);
        assertEquals(55.55, sut.dailyBudget.getDailyRemainingBudget(), 0.001);
    }

    @Test
    public void shouldCorrectShowsBudgetsWhenNumbersAreFloat() {
        setToday(FIRST_DAY_OF_PERIOD);

        sut.expenseAdditional.addExpense("name1", 94.66, FIRST_DAY_OF_PERIOD_STRING);
        sut.expenseAdditional.addExpense("name2", 33.33, FIRST_DAY_OF_PERIOD_STRING);

        assertEquals(127.99, sut.monthlyBudget.getMonthlySpends(), 0.001);
        assertEquals(1500, sut.monthlyBudget.getMonthlyBudget(), 0.001);
        assertEquals(1372.01, sut.monthlyBudget.getMonthlyRemaining(), 0.001);

        assertEquals(50, sut.dailyBudget.getDailyBudget(), 0.001);
        assertEquals(-77.99, sut.dailyBudget.getDailyRemainingBudget(), 0.001);
    }
}
