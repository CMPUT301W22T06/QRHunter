package com.qrhunter;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

/**
 * tests the player class.
 */

public class TestUser {

    private User user;

    /**
     * Copying the teardown schematic from the testCollectable code. Returns the User
     * object to a default state.
     */
    public void teardown(){
        user = new User();
    }

    /**
     * Tests that the user info is properly set and can be overwritten.
     */
    @Test
    public void testUserInfo(){
        teardown();

        // assert that the username is initially null if nothing is assigned
        assert(user.getUsername() == null);

        // same test on password
        assert(user.getPassword() == null);

        // test a basic username with capitals and numbers
        user.setUsername("User43");
        assert(user.getUsername().equals("User43"));

        // test a basic password with capitals and numbers
        user.setPassword("PassWord12");
        assert(user.getPassword().equals("PassWord12"));

        // test overwriting on username
        user.setUsername("gameR34");
        assert(user.getUsername().equals("gameR34"));

        // test overwriting on password
        user.setPassword("passWorD21");
        assert(user.getPassword().equals("passWorD21"));
    }


}
