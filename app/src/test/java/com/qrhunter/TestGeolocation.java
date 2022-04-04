package com.qrhunter;


import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.Test;

/**
 * Tests the Geolocation class.
 *
 * @author Kyle Kernick, Zack Rodgers
 */
public class TestGeolocation {

    private Geolocation test;

    /**
     * Tests that the Geolocation won't accept null as constructor arguments.
     *
     * @throws AssertionError if the Geolocation can be constructed with a null value.
     */
    @Test public void testConstructorNull() {

        // Tests constructing the latitude with null.
        assertThrows(IllegalArgumentException.class, () -> test = new Geolocation(0.0, null));

        // Test constructing the longitude with null.
        assertThrows(IllegalArgumentException.class, () -> test = new Geolocation(null, 0.0));

        // Test constructing both with null.
        assertThrows(IllegalArgumentException.class, () -> test = new Geolocation(null, null));
    }


    /**
     * Tests that the Geolocation won't accept null as a setter argument.
     *
     * @throws AssertionError if the Geolocation accepts the null as a setter argument.
     */
    @Test public void testSetNull() {

        // Construct a Geolocation.
        test = new Geolocation(0.0, 0.0);

        // Tests latitude.
        assertThrows(IllegalArgumentException.class, () -> test.setLatitude(null));

        // Tests longitude.
        assertThrows(IllegalArgumentException.class, () -> test.setLongitude(null));
    }


    /**
     * Tests that the Geolocation can successfully return its latitude through its getter method.
     *
     * @throws AssertionError if the returned value does not match what is expected.
     */
    @Test public void testGetLatitude() {

        // Tests a random number.
        test = new Geolocation(0.0, 4.56);
        assert(test.getLatitude() == 4.56);

        // Tests the maximum value.
        test = new Geolocation(0.0, Double.MAX_VALUE);
        assert(test.getLatitude() == Double.MAX_VALUE);


        // Test the minimum number.
        test = new Geolocation(0.0, Double.MIN_VALUE);
        assert(test.getLatitude() == Double.MIN_VALUE);
    }


    /**
     * Tests that the Geolocation can successfully return its longitude through the getter method.
     *
     * @throws AssertionError if the returned value does not match what is expected.
     */
    @Test public void testGetLongitude() {

        // Test a random number.
        test = new Geolocation(1.23, 0.00);
        assert(test.getLongitude() == 1.23);

        // Test the minimum value.
        test = new Geolocation(Double.MAX_VALUE, 0.00);
        assert(test.getLongitude() == Double.MAX_VALUE);

        // Test the maximum value.
        test = new Geolocation(Double.MIN_VALUE, 0.00);
        assert(test.getLongitude() == Double.MIN_VALUE);
    }
}
