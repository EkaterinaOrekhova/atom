package ru.example;

import static junit.framework.Assert.assertEquals;
import org.junit.Test;

public class Tests {

    @Test
    public void getWordFromFile() throws Exception {
        assertEquals("aaliis", BullsAndCows.getWordFromFile(4));
    }

    @Test
    public void checkBulls() throws Exception {
        String word = "java";
        String userWord = "lava";

        assertEquals(3, BullsAndCows.checkBullsAndCows(userWord, word)[0]);
    }

    @Test
    public void checkCows() throws Exception {
        String word = "java";
        String userWord = "lava";

        assertEquals(0, BullsAndCows.checkBullsAndCows(userWord, word)[1]);
    }
}
