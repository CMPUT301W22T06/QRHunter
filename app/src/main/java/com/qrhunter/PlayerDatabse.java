package com.qrhunter;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.lang.reflect.Array;
import java.util.Objects;

/*
 * A database of all Players. Communicates to the firestore.
 */

public class PlayerDatabse {

    Player player = null;
    FirebaseFirestore database;

    // adds a player with default fields
    public void addPlayer(String playerName,String playerPassword) {
        database = FirebaseFirestore.getInstance();
        // passes in an initial array of null
        Array temp = null;
        Player player = new Player(playerPassword, temp,0,0,0);
        database.collection("Players").document(playerName).set(player);

    }

    // this is the function at problem here! everything else works!
    // returns a player object. if it does not exist in the firebase, returns a null object instead
    public Player getPlayer(String playerName) {
        database = FirebaseFirestore.getInstance();
        DocumentReference myDoc = database.collection("Players").document(playerName);


        return player;
    }


}
