package com.qrhunter;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.Test;

import java.util.ArrayList;

/**
 * Tests the Collectable Class. BitMaps cannot be testing because there's no good way to create
 * a bitmap to test against.
 */
public class TestCollectable {

    private Collectable testing;

    /**
     * Tears down the collectable, returning it to the default state.
     */
    public void teardown() {testing = new Collectable();}


    /**
     * Tests that score is correctly stored, can be overwritten, and handles negatives.
     */
    @Test
    public void testScore() {
        teardown();

        // Assert that a new collectable never returns null.
        assert(testing.getScore() != null);

        // Test a basic number
        testing.setScore(5L);
        assert(testing.getScore() == 5L);

        // Test overwriting.
        testing.setScore(Long.MAX_VALUE);
        assert(testing.getScore() == Long.MAX_VALUE);

        // Test an illegal number, both a basic, and the minimum.
        assertThrows(AssertionError.class, () -> {
            testing.setScore(Long.MIN_VALUE);
        });
        assertThrows(AssertionError.class, () -> {
            testing.setScore(-1L);
        });


    }

    /**
     * Tests that the ID can be stored, overwritten, and handles default values.
     */
    @Test
    public void testId() {
        teardown();

        // Assert that a new collectable never returns null.
        assert(testing.getId() != null);

        // Test an empty ID.
        testing.setId("");
        assert(testing.getId().equals(""));

        testing.setId("Test");
        assert(testing.getId().equals("Test"));
    }


    /**
     * Tests that the Comments can be stored, overwritten, and handles default values.
     */
    @Test
    public void testComments() {
        teardown();

        // Assert that a new collectable never returns null.
        assert(testing.getComments() != null);

        // Test an empty list.
        testing.setComments(new ArrayList<>());
        assert(testing.getComments().isEmpty());

        // Test a populated list.
        ArrayList<String> sample = new ArrayList<>();
        sample.add("This"); sample.add("Is"); sample.add("A"); sample.add("Test");
        testing.setComments(sample);
        assert(testing.getComments() == sample);
    }


    /**
     * Tests that the Location can be stored, overwritten, and handles default values.
     */
    @Test
    public void testLocation() {
        teardown();

        // Assert that a new collectable never returns null.
        assert(testing.getLocation() != null);

        // Create our values.
        Geolocation empty = new Geolocation(0.0, 0.0);
        Geolocation not = new Geolocation(12.34, 56.78);

        // Test an empty location.
        testing.setLocation(empty);
        assert(testing.getLocation() == empty);

        // Test an actual location.
        testing.setLocation(not);
        assert(testing.getLocation() == not);
    }

}
