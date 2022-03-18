package com.qrhunter;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

/**
 * tests the player class.
 */

public class TestPlayer {

    private Player gamer;

    /**
     * Copying the teardown schematic from the testCollectable code. Returns the player
     * object to a default state.
     */
    public void teardown(){
        gamer = new Player();
    }

    /**
     * Tests that the user info is properly set and can be overwritten.
     */
    @Test
    public void testUserInfo(){
        teardown();

        // assert that the username is initially null if nothing is assigned
        assert(gamer.getUsername() == null);

        // same test on password
        assert(gamer.getPassword() == null);

        // test a basic username with capitals and numbers
        gamer.setUsername("Gamer43");
        assert(gamer.getUsername().equals("Gamer43"));

        // test a basic password with capitals and numbers
        gamer.setPassword("PassWord12");
        assert(gamer.getPassword().equals("PassWord12"));

        // test overwriting on username
        gamer.setUsername("gameR34");
        assert(gamer.getUsername().equals("gameR34"));

        // test overwriting on password
        gamer.setPassword("passWorD21");
        assert(gamer.getPassword().equals("passWorD21"));

    }

    /**
     * Tests that the user scores are properly set to the default values and can be overwritten
     */
    @Test
    public void testScores(){
        teardown();

        // test the default scores
        assert(gamer.getScoreSum() == 0L);
        assert(gamer.getHighestScore() == 0L);
        assert(gamer.getTotalCodesScanned() == 0L);

        // test overwriting of all the values
        gamer.setScoreSum(2L);
        assert(gamer.getScoreSum() == 2L);

        gamer.setHighestScore(3L);
        assert(gamer.getHighestScore() == 3L);

        gamer.setTotalCodesScanned(4L);
        assert(gamer.getTotalCodesScanned() == 4L);
    }

    /**
     * Tests the user collectibles arraylist is properly initialized and can be overwritten
     */
    @Test
    public void testCollectedIDs(){
        teardown();

        // create a test arrayList
        ArrayList<String> test = new ArrayList<>();
        test.add("gaming");

        // check that it is initially empty
        assert(gamer.getClaimedCollectibleIDs().isEmpty());

        // check the overwriting
        gamer.setClaimedCollectibleIDs(test);
        assert(!gamer.getClaimedCollectibleIDs().isEmpty());
    }


}
