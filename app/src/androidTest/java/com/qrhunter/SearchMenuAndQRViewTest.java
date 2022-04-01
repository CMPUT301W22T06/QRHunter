package com.qrhunter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.v1.WriteResult;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for MainActivity. All the UI tests are written here. Robotium test framework is
 used
 */
public class SearchMenuAndQRViewTest {
    private final String TAG = "SearchMenuTest";
    private FirebaseFirestore db;
    private Solo solo;
    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    /**
     * Checks that a player can view his collectibles
     */
    @Test
    public void checkMyCollectibles(){
        // Logs in using the intent testing account
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.enterText((EditText) solo.getView(R.id.username), "MyTestor");
        solo.enterText((EditText) solo.getView(R.id.password), "gudPassword");
        solo.clickOnButton("Login");

        // Goes to the Search Menu
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);
        solo.clickOnButton("Scoreboard");

        //Checks the player's collectibles
        solo.assertCurrentActivity("Wrong Activity", SearchMenuActivity.class);
        solo.clickOnView(solo.getView("my_collectibles_button"));
        solo.assertCurrentActivity("Wrong Activity", MyCollectiblesList.class);
        assertTrue(solo.waitForText("test1", 1, 1000));
    }

    /**
     * Checks that a player can add a comment on a collectible and that it persists between runs
     */
    @Test
    public void checkAddComment(){
        // Logs in using the intent testing account
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.enterText((EditText) solo.getView(R.id.username), "MrTester");
        solo.enterText((EditText) solo.getView(R.id.password), "gudPassword");
        solo.clickOnButton("Login");

        // Goes to the Search Menu
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);
        solo.clickOnButton("Scoreboard");

        //Selects a player's collectible
        solo.assertCurrentActivity("Wrong Activity", SearchMenuActivity.class);
        solo.clickOnText("McTesty");
        solo.clickOnText("test1");

        //Adds a comment to the collectible
        solo.assertCurrentActivity("Wrong Activity", QRViewActivity.class);
        solo.enterText((EditText) solo.getView(R.id.comment_input), "Good pic");
        solo.clickOnButton("Comment");
        assertTrue(solo.waitForText("Good pic", 1, 1000));

        //Make sure the comment was added to firebase, then delete it
        db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Scanned").document("ea5eaab938554a8fe3fdd286ceb588dfbe63749fbcb41760dcd95a5958d5fa01");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List<String> allComments = (List<String>) document.get("Comments");
                        if(allComments != null) {
                            assertTrue(allComments.contains("Good pic"));
                            allComments.remove("Good pic");
                            docRef.update("Comments", allComments);
                        }
                    }
                } else {
                    Log.d(TAG, "Error getting document", task.getException());
                }
            }
        });
    }

    /**
     * Checks that other players that have claimed a collectible are shown
     */
    @Test
    public void checkCommonPlayers(){
        // Logs in using the intent testing account
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.enterText((EditText) solo.getView(R.id.username), "MrTester");
        solo.enterText((EditText) solo.getView(R.id.password), "gudPassword");
        solo.clickOnButton("Login");

        // Goes to the Search Menu
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);
        solo.clickOnButton("Scoreboard");

        // Selects a player's collectible
        solo.assertCurrentActivity("Wrong Activity", SearchMenuActivity.class);
        solo.clickOnText("McTesty");
        solo.clickOnText("test1");

        // Checks that the two test players who have this collectible are on the list
        solo.assertCurrentActivity("Wrong Activity", QRViewActivity.class);
        solo.clickOnView(solo.getView("change_list_button"));
        assertTrue(solo.waitForText("MyTestor", 1, 1000));
        assertTrue(solo.waitForText("McTesty", 1, 1000));

        // Checks if the buttons swap properly
        assertTrue(((TextView)solo.getView("change_list_button")).getText().toString().equals("View Comments"));
        assertFalse(solo.getView(R.id.comment_button).isShown());
        assertFalse(solo.getView(R.id.comment_input).isShown());
    }
}
