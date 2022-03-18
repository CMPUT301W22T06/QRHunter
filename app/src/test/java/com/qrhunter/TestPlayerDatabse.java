package com.qrhunter;

import org.junit.jupiter.api.Test;

/**
 * tests the functionality of the PlayerDatabase class.
 */

public class TestPlayerDatabse {

    PlayerDatabse playerDatabse = new PlayerDatabse();

    /**
     * tests that the database can add, get, and delete players.
     */

    @Test
    public void testPrimaryFunctions(){
        // wait for playerdatabase to complete installation first
        while (!playerDatabse.isFinishDownloading());

        // first check to see if the player is not there
        assert(playerDatabse.getPlayer("testPlayerDatabse") == null);

        // creates player
        playerDatabse.addPlayer("testPlayerDatabse","TEST");

        // gets the player
        assert(playerDatabse.getPlayer("testPlayerDatabse").getUsername() == "testPlayerDatabse");

        // deletes the player
        playerDatabse.deletePlayer("TEST");

        // checks to see if the player is not there
        assert(playerDatabse.getPlayer("testPlayerDatabse") == null);

    }

}
