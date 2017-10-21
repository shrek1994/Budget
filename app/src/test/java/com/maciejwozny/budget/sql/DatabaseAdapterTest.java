package com.maciejwozny.budget.sql;

import android.database.sqlite.SQLiteDatabase;

import com.maciejwozny.budget.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

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

    @Before
    public void setup() {
        sut = new DatabaseAdapter(RuntimeEnvironment.application);
    }

    @Test
    public void shouldGetOpenedDatabase(){
        SQLiteDatabase database = sut.getWritableDatabase();
        assertTrue(database.isOpen());
        sut.onCreate(database);
        database.close();
    }



}