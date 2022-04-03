package com.qrhunter;

import static org.junit.Assert.assertTrue;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Tests the owner activity.
 */
public class OwnerActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());

        // navigate to the search menu activity
        solo.enterText((EditText) solo.getView(R.id.username), "testOwner");
        solo.enterText((EditText) solo.getView(R.id.password), "admin");
        solo.clickOnButton("Login");
        solo.assertCurrentActivity("Wrong Activity", OwnerActivity.class);
    }

    /**
     * Checks the swap button button.
     */
    @Test
    public void checkSwapButton() {
        //Checks the filter button
        solo.assertCurrentActivity("Wrong Activity", OwnerActivity.class);
        // make sure players are being shown
        assertTrue(solo.waitForText("Players",1,1000));

        // make sure collectables are being shown after button press
        solo.clickOnView(solo.getView("change_list_button"));
        assertTrue(solo.waitForText("Collectables", 1, 1000));
    }
}
