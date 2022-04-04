package com.qrhunter;

import static org.junit.Assert.assertTrue;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class SearchMenuTest {

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
        solo.enterText((EditText) solo.getView(R.id.username), "MyTestor");
        solo.enterText((EditText) solo.getView(R.id.password), "gudPassword");
        solo.clickOnButton("Login");
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);
        solo.clickOnButton("Scoreboard");
        solo.assertCurrentActivity("Wrong Activity", SearchMenuActivity.class);
    }

    /**
     * Checks the filter button.
     */
    @Test
    public void checkFilter() {
        //Checks the filter button
        solo.assertCurrentActivity("Wrong Activity", SearchMenuActivity.class);
        solo.clickOnView(solo.getView("filter_button"));
        solo.waitForText("Sorted by", 1, 2000);
    }

    /**
     * checks the get rank button
     */
    @Test
    public void checkRank() {
        solo.assertCurrentActivity("Wrong Activity", SearchMenuActivity.class);
        solo.clickOnView(solo.getView("rank_button"));
        solo.waitForText("Please",1,1000);
        solo.clickOnView(solo.getView("filter_button"));
        solo.clickOnView(solo.getView("rank_button"));
        solo.waitForText("You are",1,1000);
    }


    /**
     * Checks the search button.
     */
    @Test
    public void checkSearchButton() {
        //Checks the search button
        solo.assertCurrentActivity("Wrong Activity", SearchMenuActivity.class);

        // enter player to search
        solo.enterText((EditText) solo.getView(R.id.search_input), "MyTestor");
        solo.clickOnView(solo.getView("search_button"));
        assertTrue(solo.waitForText("MyTestor", 1, 1000));
        // make sure search field is empty after searching
        assertTrue(((EditText) solo.getView(R.id.search_input)).getText().toString().equals(""));
        // can add more later to check if searching functionality actually works
    }

}
