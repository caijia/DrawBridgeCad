package com.caijia.drawbridgecad;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
        System.out.println(Math.sin(Math.toRadians(60)));

        double v = Math.atan2(1, 1);
        System.out.println(Math.toDegrees(v));
    }
}