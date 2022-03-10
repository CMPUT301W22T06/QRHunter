package com.qrhunter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.Test;

public class TestScoringSystem {
    @Test
    public void testHashQRCorrectness() {
        assertEquals("8227ad036b504e39fe29393ce170908be2b1ea636554488fa86de5d9d6cd2c32", ScoringSystem.hashQR("BFG5DGW54"));
        assertEquals("181bd5538d3c853f6a2ef40974045d03c626a71e297bac6d761e3c098d0e0b07", ScoringSystem.hashQR("CMPUT301"));
        assertEquals("ffb9420769e5f216bd7368503baa50706f83546818a888192adc744593e7dadf",
                ScoringSystem.hashQR("https://github.com/CMPUT301W22T06/QRHunter"));
        assertEquals("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855", ScoringSystem.hashQR(""));

    }

    @Test
    public void testHashQRWithBadInput() {
        //null input
        assertThrows(IllegalArgumentException.class, () -> {
            ScoringSystem.hashQR(null);
        });
    }

    @Test
    public void testScoreCorrectness() {
        assertEquals(111, ScoringSystem.score("696ce4dbd7bb57cbfe58b64f530f428b74999cb37e2ee60980490cd9552de3a6"));
        assertEquals(49, ScoringSystem.score("777"));
        assertEquals(8000, ScoringSystem.score("0000"));
        assertEquals(0, ScoringSystem.score("123456789"));
        assertEquals(0, ScoringSystem.score("a"));
        assertEquals(6, ScoringSystem.score("112233"));
        assertEquals(36, ScoringSystem.score("00abcdef444"));


        //max score is set to long maximum
        assertEquals(9223372036854775807L, ScoringSystem.score("00000000000000000000000000000000000"));
    }

    @Test
    public void testScoreWithNullInput() {
        assertThrows(IllegalArgumentException.class, () -> {
            ScoringSystem.score(null);
        });
    }

    @Test
    public void testScoreWithEmptyString() {
        assertThrows(IllegalArgumentException.class, () -> {
            ScoringSystem.score("");
        });
    }

    @Test
    public void testScoreWithUnhashedString() {
        //string that has not been hashed
        assertThrows(IllegalArgumentException.class, () -> {
            ScoringSystem.score("QWERTY");
        });
    }
}
