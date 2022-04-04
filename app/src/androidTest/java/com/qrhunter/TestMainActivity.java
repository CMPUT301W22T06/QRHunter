package com.qrhunter;

import static org.junit.Assert.assertTrue;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * tests the main activity. primarily tests the login functions to see if they work.
 */

public class TestMainActivity {
    private final String TAG = "";
    private FirebaseFirestore db;
    private Solo solo;
    private PlayerDatabse playerDatabase = new PlayerDatabse();
    private final String username = "l33tgamerman43";
    private final String password = "l33tpassword43";

    private void teardown(){
        if (playerDatabase.exists(username)){
            playerDatabase.deletePlayer(username);
        }
    }


    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class,
            true, true);


    /**
     * runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }


    /**
     * checks the player can login
     */
    @Test public void checkLogin() {
        // attempts a login using false credentials
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.enterText((EditText) solo.getView(R.id.username),"totallyRealTestAccount");
        solo.enterText((EditText) solo.getView(R.id.password),"totallyRealTestPassword");
        solo.clickOnButton("Login");
        // checks if the login was successful (it should not be unless some degenerate user
        // actually claims that account with those credentials)
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        // clears the fields
        solo.clearEditText((EditText) solo.getView(R.id.username));
        solo.clearEditText((EditText) solo.getView(R.id.password));

        // logs in using the testing account and checks if the login was successful
        solo.enterText((EditText) solo.getView(R.id.username),"MyTestor");
        solo.enterText((EditText) solo.getView(R.id.password),"gudPassword");
        solo.clickOnButton("Login");
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);
    }


    /**
     * creates an account in the create account fragment
     */
    @Test public void checkCreate() {
        // clicks the create account button and checks if we move to the fragment
        solo.clickOnButton("Create Account");
        assertTrue(solo.waitForText("Register Account",1,1000));

        // leaves one field empty to check if account creation fails as expected
        solo.enterText((EditText) solo.getView(R.id.create_username), username);
        solo.enterText((EditText) solo.getView(R.id.create_password), password);
        solo.clickOnButton("Register Account");
        // the fragment closes itself if the registration is successful, so here we check if
        // it was unsuccessful
        assertTrue(solo.waitForText("Register Account",1,1000));

        // fills in the empty fields and checks if the fragment closes itself
        solo.enterText((EditText) solo.getView(R.id.confirm_password), password);
        assertTrue(solo.waitForText("Register Account",1,1000));

        teardown();
    }

}
