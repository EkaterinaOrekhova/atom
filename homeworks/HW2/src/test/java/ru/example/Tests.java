package ru.example;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static junit.framework.Assert.assertEquals;
import org.junit.Test;

public class Tests {

    @Test
    public void max0() throws Exception {
        assertEquals("aahed", BullsAndCows.getWordFromFile(0));
    }
}
