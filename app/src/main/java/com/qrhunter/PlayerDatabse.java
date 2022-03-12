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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/*
 * A database of all Players. Communicates to the firestore.
 */

public class PlayerDatabse {

    // initially set to null in order to prevent
    Player player = null;
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    private boolean finishDownloading = false;
    private HashMap<String, Player> players = new HashMap<>();

    private void importDatabase(){
        database.collection("Players").get().addOnCompleteListener(e -> {
            if (e.isSuccessful()){
                for (QueryDocumentSnapshot document : Objects.requireNonNull(e.getResult())){
                    Player current = new Player();

                    // put the players into a local hashmap
                    if (!players.containsKey(document.getId())){
                        Object password_object = document.get("password");
                        if (password_object != null){
                            current.setPassword((String) password_object);
                        }
                        Object highestScore_object = document.get("highestScore");
                        if (highestScore_object != null){
                            current.setHighestScore((Long) highestScore_object);
                        }
                        Object scoreSum_object = document.get("scoreSum");
                        if (scoreSum_object != null){
                            current.setScoreSum((Long) scoreSum_object);
                        }
                        Object totalCodesScanned_object = document.get("totalCodesScanned");
                        if (totalCodesScanned_object != null){
                            current.setTotalCodesScanned((Long) totalCodesScanned_object);
                        }
                        Object claimedIDs_object = document.get("claimedCollectibleIDs");
                        if (claimedIDs_object != null) {
                            ArrayList<String> claimedIDs = (ArrayList<String>) claimedIDs_object;
                            current.setClaimedCollectibleIDs(claimedIDs);
                        }
                        players.put(document.getId(), current);
                    }
                    // if
                    else {
                        Object claimedIDs_object = document.get("claimedCollectibleIDs");
                        if (claimedIDs_object != null){
                            ArrayList<String> claimedIDs = (ArrayList<String>) claimedIDs_object;
                            if (current.getClaimedCollectibleIDs() == null || current.getClaimedCollectibleIDs().size() != claimedIDs.size()){
                                current.setClaimedCollectibleIDs(claimedIDs);
                            }
                        }
                    }

                }
            }
            else{
                throw new RuntimeException("Network Error:" + e.getException());
            }
            finishDownloading = true;
        });

    }

    public PlayerDatabse(){
        // imports the databse
        importDatabase();

        // hook to update
        database.collection("Players").addSnapshotListener((queryDocumentSnapshots, error) -> importDatabase());

    }

    // adds a player with default fields
    public void addPlayer(String playerName,String playerPassword) {
        // passes in an initial arraylist of null for claimedCollectibleIDs and default values
        Player player = new Player(playerPassword, null,0L,0L,0L);
        database.collection("Players").document(playerName).set(player);
    }

    public boolean isFinishDownloading(){
        return finishDownloading;
    }

    // this is the function at problem here! everything else works!
    // returns a player object. if it does not exist in the firebase, returns a null object instead
    public Player getPlayer(String playerName) {
        if (!isFinishDownloading()){
            return null;
        }
        else if(players.containsKey(playerName)){
            return players.get(playerName);
        }
        else return null;
    }

    /*
    public void getPlayer(String playerName, MainActivity callback){
        database.collection("Player").document(playerName).get().addOnCompleteListener(e -> {
            callback.recievePlayer();
        });
    }
    */

}
