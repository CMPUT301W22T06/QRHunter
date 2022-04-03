package com.qrhunter;

import static android.content.ContentValues.TAG;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

public class ImagePrefTest {
    private final String TAG = "ImagePrefTest";
    private FirebaseFirestore db;
    private boolean storeBigImages;
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
     * Checks that an owner can enable or disable storing of large images
     */
    @Test
    public void checkChangeImagePref(){
        // Logs in using the intent testing account
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.enterText((EditText) solo.getView(R.id.username), "testOwner");
        solo.enterText((EditText) solo.getView(R.id.password), "admin");
        solo.clickOnButton("Login");

        // Opens the preference menu in the Owner's Menu
        solo.assertCurrentActivity("Wrong Activity", OwnerActivity.class);
        solo.clickOnView(solo.getView("prefButton"));

        //Changes the setting and ensures it matches the Firestore database
        if(solo.waitForText("Enable large image uploads", 1, 1000)) {
            solo.clickOnText("Enable large image uploads");
            //Make sure the change is reflected firebase
            db = FirebaseFirestore.getInstance();
            db.collection("Preferences").document("ImagePrefs").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        storeBigImages = (boolean) document.getBoolean("bigImages");
                    }
                }
            });
            assertFalse(storeBigImages);
        }
        else if(solo.waitForText("Disable large image uploads", 1, 1000)) {
            solo.clickOnText("Disable large image uploads");
            //Make sure the change is reflected firebase
            db = FirebaseFirestore.getInstance();
            db.collection("Preferences").document("ImagePrefs").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        storeBigImages = (boolean) document.getBoolean("bigImages");
                    }
                }
            });
            assertFalse(storeBigImages);
        }
    }
}
