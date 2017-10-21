package com.maciejwozny.budget.sql;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.maciejwozny.budget.BuildConfig;
import com.maciejwozny.budget.sql.tables.Budget;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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
    private SQLiteDatabase database;

    @Before
    public void setup() {
        sut = new DatabaseAdapter(RuntimeEnvironment.application);
        database = sut.getWritableDatabase();
//        sut.onCreate(database);
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
        Budget budget = new Budget("name", 1);
        List<Budget> expectedBudgets = new ArrayList<>(Arrays.asList(budget));

        sut.insertBudget(budget);

        assertEquals(expectedBudgets, sut.getBudgets());
    }



}