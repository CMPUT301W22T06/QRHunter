package com.qrhunter;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

/*
 * A database of all Players. Communicates to the firestore.
 */

public class PlayerDatabse {

    // initially set to null in order to prevent
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    private boolean finishDownloading = false;
    private HashMap<String, User> players = new HashMap<>();

    private void importDatabase(){
        database.collection("Players").get().addOnCompleteListener(e -> {
            if (e.isSuccessful()){
                for (QueryDocumentSnapshot document : Objects.requireNonNull(e.getResult())){

                    // put the players into a local hashmap
                    Class<? extends QueryDocumentSnapshot> test = document.getClass();
                    Log.d("TEST",test.toString());
                    if (!players.containsKey(document.getId())) {
                        // if it is a player
                        if (document.get("highestScore") != null) {
                            Player current = new Player();
                            Object username_object = document.get("username");
                            if (username_object != null) {
                                current.setUsername((String) username_object);
                            }
                            Object password_object = document.get("password");
                            if (password_object != null) {
                                current.setPassword((String) password_object);
                            }
                            Object highestScore_object = document.get("highestScore");
                            if (highestScore_object != null) {
                                current.setHighestScore((Long) highestScore_object);
                            }
                            Object scoreSum_object = document.get("scoreSum");
                            if (scoreSum_object != null) {
                                current.setScoreSum((Long) scoreSum_object);
                            }
                            Object totalCodesScanned_object = document.get("totalCodesScanned");
                            if (totalCodesScanned_object != null) {
                                current.setTotalCodesScanned((Long) totalCodesScanned_object);
                            }
                            Object claimedIDs_object = document.get("claimedCollectibleIDs");
                            if (claimedIDs_object != null) {
                                ArrayList<String> claimedIDs = (ArrayList<String>) claimedIDs_object;
                                current.setClaimedCollectibleIDs(claimedIDs);
                            }
                            players.put(document.getId(), current);
                        } else { // if it is an owner
                            Owner current = new Owner();
                            Object username_object = document.get("username");
                            if (username_object != null) {
                                current.setUsername((String) username_object);
                            }
                            Object password_object = document.get("password");
                            if (password_object != null) {
                                current.setPassword((String) password_object);
                            }
                            players.put(document.getId(), current);
                        }
                    }
                    // if
/*                    else {
                        Object claimedIDs_object = document.get("claimedCollectibleIDs");
                        if (claimedIDs_object != null){
                            ArrayList<String> claimedIDs = (ArrayList<String>) claimedIDs_object;
                            if (current.getClaimedCollectibleIDs() == null || current.getClaimedCollectibleIDs().size() != claimedIDs.size()){
                                current.setClaimedCollectibleIDs(claimedIDs);
                            }
                        }
                    }*/
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
        Player player = new Player(playerName, playerPassword, null,0L,0L,0L);
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
            return (Player) players.get(playerName);
        }
        else return null;
    }

    public void addClaimedID(String player, String id) {
        Player selected = MainActivity.allPlayers.getPlayer(player);
        if (selected != null) {
            selected.getClaimedCollectibleIDs().add(id);
            database.collection("Players")
                    .document(player)
                    .update("claimedCollectibleIDs", selected.getClaimedCollectibleIDs())
                    .addOnFailureListener(e -> {throw new RuntimeException("Network Error.");});
        }
    }

    public void removeClaimedID(String player, String id) {
        Player selected = MainActivity.allPlayers.getPlayer(player);
        if (selected != null) {
            selected.getClaimedCollectibleIDs().remove(id);
            database.collection("Players")
                    .document(player)
                    .update("claimedCollectibleIDs", selected.getClaimedCollectibleIDs())
                    .addOnFailureListener(e -> {throw new RuntimeException("Network Error.");});
        }
    }

    public HashMap<String, Player> getPlayers() {
        HashMap<String,Player> returner = new HashMap<>();
        Iterator<String> usernames = players.keySet().iterator();
        while(usernames.hasNext()) {
            String currentUsername = usernames.next();
            if(players.get(currentUsername).getClass() == Player.class) {
                returner.put(currentUsername,(Player) players.get(currentUsername));
            }
        }
        return returner;
    }

    public User getUser(String username) {
        return players.get(username);
    }

    public Boolean isPlayer(String username) {
        try {
            // try to cast to player...
            return ((Player) players.get(username)).getHighestScore() != null;
        } catch (Exception e){
            return false;
        }
    }

    public void deleteUser(String username) {
        if(players.containsKey(username)) {
            players.remove(username);
            database.collection("Players")
                    .document(username)
                    .delete()
                    .addOnFailureListener(e -> {
                        throw new RuntimeException("Network Error.");
                    });
        }
    }

}
