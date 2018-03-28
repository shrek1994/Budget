package com.maciejwozny.budget.model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Maciej Wozny on 28.03.2018.
 * 2018 All rights reserved.
 */
public class UtilsTest {
    @Test
    public void shouldGetOnlyTwoDigitsAfterDotCorrectly() throws Exception {
        assertEquals(1000.11, Utils.getOnlyTwoDigitsAfterDot(1000.1111111111), 0.00001);
    }

}