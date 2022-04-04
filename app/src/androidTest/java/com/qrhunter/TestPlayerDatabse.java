package com.qrhunter;

import org.junit.Test;

/**
 * tests the functionality of the PlayerDatabase class.
 */

public class TestPlayerDatabse {

    PlayerDatabse playerDatabse = new PlayerDatabse();
    final private String username = "testPlayerDatabse";
    final private String password = "testPlayerPassword";

    /**
     * purges the player from the database if the player already exists
     */
    public void teardown(){
        playerDatabse.deletePlayer(username);
    }


    /**
     * tests that the database can add, get, and delete players.
     */
    @Test public void testPrimaryFunctions(){
        teardown();

        // checks if the player already exists (should not)
        assert(!playerDatabse.exists(username));

        // wait for playerdatabase to complete installation first
        while (!playerDatabse.isFinishDownloading());

        // creates player
        playerDatabse.addPlayer(username,password);

        // check if the player is there
        assert(playerDatabse.exists(username));

        // gets the player
        assert(playerDatabse.getPlayer(username).getUsername().equals(username));

        // deletes the player
        playerDatabse.deletePlayer(username);

        // checks to see if the player is not there
        assert(!playerDatabse.exists(username));

    }

}
