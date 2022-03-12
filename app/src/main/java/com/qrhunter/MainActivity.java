package com.qrhunter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    static PlayerDatabse allPlayers = new PlayerDatabse();

    /**
     * Creates a short toast, saving some code redundancy
     * @param context The context to display the toast.
     * @param message The message to toast.
     */
    static public void toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // the buttons and editTexts in the activity

        Context context = getApplicationContext();
        EditText usernameText = findViewById(R.id.username);
        EditText passwordText = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.login_button);
        Button createAccountButton = findViewById(R.id.create_account_button);

        // Request Camera Permission (Needed for the scanner)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);

        // Quick & dirty implementation of a login button, just confirming stuff is in the DB before
        // going into the home activity.
        // If the user does not exist or password doesn't match what is in the DB, displays a
        // little toast saying "user does not exist!" or "wrong password!" respectively
        loginButton.setOnClickListener(v -> {
            String username = usernameText.getText().toString();
            String password = passwordText.getText().toString();

            // if the fields are filled
            if(!username.equals("") && !password.equals("")){

                // pull the player object with the desired username from database
                // if the account exists, log them in (pass player into homeActivity)
                Player thisPlayer = allPlayers.getPlayer(username);

                // checks if the player actually exists, if not toast the user to let them know
                if (thisPlayer == null){
                    Toast.makeText(context, "User does not exist!", Toast.LENGTH_SHORT).show();
                }
                else {
                    String thisPass = thisPlayer.getPassword();

                    // if the account credentials match what is on file
                    if (thisPass.equals(password)) {
                        // logs em into the main activity
                        Toast.makeText(context, "You are now signed in as "+username,
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, HomeActivity.class);

                        // passes the player object into the intent
                        intent.putExtra("username",username);
                        startActivity(intent);
                    }
                    // otherwise toast and tell them their credentials are not correct
                    else {
                        Toast.makeText(context, "Wrong Password!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            // toast the user to let them know there are empty fields
            else{
                Toast.makeText(context, "Empty Fields!", Toast.LENGTH_SHORT).show();
            }
        });
      
        // when the create account button is pressed, creates a dialog fragment for the
        // user to register a new account into the player database.
        createAccountButton.setOnClickListener(v -> {
            createAccount createAcc = new createAccount();
            createAcc.show(getSupportFragmentManager(),"myFragment");
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int x = 0; x < permissions.length; ++x) {
            // Camera isn't a big deal, just toast them to let them know it's kinda important.
            if (permissions[x].equals(Manifest.permission.CAMERA) && requestCode == 0 && grantResults[x] != PackageManager.PERMISSION_GRANTED) {
                toast(getApplicationContext(), "The camera is needed to scan QR codes!");
            }
        }

    }
}