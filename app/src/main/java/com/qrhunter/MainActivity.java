package com.qrhunter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * The MainActivity facilitates logging into the game, or creating an account if the user doesn't
 * have one, and passing that information to the HomeActivity
 */
public class MainActivity extends AppCompatActivity {

    // Store the database's here, so they can be initialized as early as possible.
    static PlayerDatabse allPlayers = new PlayerDatabse();
    static CollectableDatabase collectables = new CollectableDatabase();


    /**
     * Creates a short toast, saving some code redundancy
     * @param context The context to display the toast.
     * @param message The message to toast.
     */
    static public void toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // the buttons and editTexts in the activity
        Context context = getApplicationContext();
        EditText usernameText = findViewById(R.id.username);
        EditText passwordText = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.login_button);
        Button createAccountButton = findViewById(R.id.create_account_button);
        Button loginWithoutAccountButton = findViewById(R.id.login_without_account);
        Button loginWithQR = findViewById(R.id.login_with_QR);

        // Request Camera Permission (Needed for the scanner)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);

        loginWithoutAccountButton.setOnClickListener( v ->{
            MainActivity.toast(context, "You are now signed in without account");
            Intent intent = new Intent(this, HomeActivity.class);

            // passes the player object into the intent
            intent.putExtra("username","");
            startActivity(intent);
        });

        loginWithQR.setOnClickListener( v -> new LoginWithQRFragment().show(getSupportFragmentManager(),"LOGIN_WITH_QR"));

        loginButton.setOnClickListener(v -> {
            String username = usernameText.getText().toString();
            String password = passwordText.getText().toString();

            // if the fields are filled
            if(!username.equals("") && !password.equals("")){

                // pull the player object with the desired username from database
                // if the account exists, log them in (pass player into homeActivity)
                User thisPlayer = allPlayers.getUser(username);

                // checks if the player actually exists, if not toast the user to let them know
                if (thisPlayer == null) MainActivity.toast(context, "User does not exist!");

                else {
                    String thisPass = thisPlayer.getPassword();

                    // if the account credentials match what is on file
                    if (thisPass.equals(password)) {

                        // logs em into the main activity
                        MainActivity.toast(context, "You are now signed in as " + username);

                        Intent intent;
                        // if player is logging in...
                        if (allPlayers.isPlayer(username)) {
                            intent = new Intent(this, HomeActivity.class);
                            // passes the player object into the intent
                            intent.putExtra("username",username);
                        }

                        // if owner is logging in...
                        else intent = new Intent(this, OwnerActivity.class);

                        startActivity(intent);
                    }

                    // otherwise toast and tell them their credentials are not correct
                    else MainActivity.toast(context, "Wrong Password!");
                }
            }

            // toast the user to let them know there are empty fields
            else MainActivity.toast(context, "Empty Fields!");
        });
      
        // when the create account button is pressed, creates a dialog fragment for the
        // user to register a new account into the player database.
        createAccountButton.setOnClickListener(v -> {
            CreateAccount createAcc = new CreateAccount();
            createAcc.show(getSupportFragmentManager(),"myFragment");
        });
    }


    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int x = 0; x < permissions.length; ++x) {
            // Camera isn't a big deal, just toast them to let them know it's kinda important.
            if (permissions[x].equals(Manifest.permission.CAMERA) && requestCode == 0 && grantResults[x] != PackageManager.PERMISSION_GRANTED) {
                toast(getApplicationContext(), "The camera is needed to scan QR codes!");
            }
        }

    }
}