package com.qrhunter;

import org.junit.jupiter.api.Test;

/**
 * Tests the Owner class.
 */

public class TestOwner {

    private Owner owner;


    /**
     * Copying the teardown schematic from the testCollectable code. Returns the owner
     * object to a default state.
     */
    public void teardown(){
        owner = new Owner();
    }


    /**
     * Tests that the user info is properly set and can be overwritten.
     */
    @Test public void testUserInfo(){
        teardown();

        // assert that the username is initially null if nothing is assigned
        assert(owner.getUsername() == null);

        // same test on password
        assert(owner.getPassword() == null);

        // test a basic username with capitals and numbers
        owner.setUsername("owner43");
        assert(owner.getUsername().equals("owner43"));

        // test a basic password with capitals and numbers
        owner.setPassword("PassWord12");
        assert(owner.getPassword().equals("PassWord12"));

        // test overwriting on username
        owner.setUsername("gameR34");
        assert(owner.getUsername().equals("gameR34"));

        // test overwriting on password
        owner.setPassword("passWorD21");
        assert(owner.getPassword().equals("passWorD21"));
    }
}
