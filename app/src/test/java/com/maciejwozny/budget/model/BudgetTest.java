package com.maciejwozny.budget.model;

import com.maciejwozny.budget.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static com.maciejwozny.budget.BudgetActivity.DEFAULT_BUDGET;
import static org.junit.Assert.*;

/**
 * Created by Maciej Wozny on 18.11.2017.
 * 2017 All rights reserved.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class,
        sdk = LOLLIPOP,
        manifest = "src/main/AndroidManifest.xml")
public class BudgetTest {
    private Budget sut;

    @Before
    public void setup() {
        sut = Budget.build(RuntimeEnvironment.application);
    }

    @Test
    public void shouldReturnDefaultBudget() {
        assertEquals(DEFAULT_BUDGET.getMonthlyBudget(), sut.monthlyBudget.getMonthlyBudget(DEFAULT_BUDGET.getName()), 0.001);
    }
}