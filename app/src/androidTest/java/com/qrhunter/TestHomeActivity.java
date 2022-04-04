package com.qrhunter;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class) public class TestHomeActivity {
    private Solo solo;

    @Rule
    public ActivityTestRule<HomeActivity> rule = new ActivityTestRule<>(HomeActivity.class, true, true);

    @Before public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }


    /**
     * Tests the Scoreboard button.
     */
    @Test public void testScoreboardButton() {
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);
        solo.clickOnButton("Scoreboard");
        solo.assertCurrentActivity("Could not switch to Scoreboard", SearchMenuActivity.class);
    }


    @Test public void testQRMapButton() {
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);
        solo.clickOnButton("QR Map");
        solo.assertCurrentActivity("Could not switch to QR Map", QRMapActivity.class);

    }


    @After public void tearDown() {
        solo.finishOpenedActivities();
    }

}
