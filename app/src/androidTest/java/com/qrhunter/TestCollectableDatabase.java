package com.qrhunter;

import static java.lang.Thread.sleep;

import org.junit.Test;

/**
 * Testing for the CollectableDatabase. Firestore cannot initialize in Unit Testing (for whatever
 * reason), so this needs to be an instrumented test on an android device.
 */
public class TestCollectableDatabase {

    CollectableDatabase database = new CollectableDatabase();

    /**
     * Tests additions and deletions from the database.
     */
    @Test public void testAddDeleteCollectable() throws InterruptedException {

        sleep(5000);

        // Creates the collectable, adds it to the database.
        Collectable testing = new Collectable();
        testing.setId("TEST");
        database.add(testing, null);
        assert(database.exists("TEST"));

        // Deletes the collectable, verifies it's gone.
        database.deleteCollectable("TEST");
        assert(!database.exists("TEST"));
    }


    /**
     * Tests adding comments to the database on a specific ID.
     */
    @Test public void testAddingComment() throws InterruptedException {

        sleep(5000);

        // Creates the collectable.
        Collectable testing = new Collectable();
        testing.setId("TEST");

        // Adds a comment.
        database.add(testing, null);
        database.addComment("TEST", "This is a test");
        assert(database.get("TEST").getComments().contains("This is a test"));

        // Adds a second.
        database.addComment("TEST", "Another test");
        assert(database.get("TEST").getComments().contains("Another test"));


    }
}
