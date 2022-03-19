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
import java.util.Objects;

/**
 * A database of all Players. This also facilitates communicating with the firestore.
 */

public class PlayerDatabse {

    // initially set to null in order to prevent
    Player player = null;
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    private boolean finishDownloading = false;
    private HashMap<String, Player> players = new HashMap<>();

    /**
     * @throws RuntimeException if the database cannot be accessed (Network issues)
     *
     * this function updates the local database. It only downloads those that aren't present, and
     * downloads the entirety of the player.
     *
     * When this function is first run, it will be when the local Database is constructed, and will
     * load everything into the local database.
     *
     * After that, it watches changes to the Firestore. When a change is made (even by
     * the user who made the change), this function rescans the local library and adds ONLY
     * the players that don't exist within the local databse.
     */

    private void importDatabase(){
        database.collection("Players").get().addOnCompleteListener(e -> {
            if (e.isSuccessful()){
                for (QueryDocumentSnapshot document : Objects.requireNonNull(e.getResult())){
                    Player current = new Player();

                    // put the players into a local hashmap
                    if (!players.containsKey(document.getId())){

                        Object username_object = document.get("username");
                        if (username_object != null){
                            current.setUsername((String) username_object);
                        }

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

    /**
     * constructs the databse and downloads all the data.
     */
    public PlayerDatabse(){
        // imports the databse
        importDatabase();

        // hook to update
        database.collection("Players").addSnapshotListener((queryDocumentSnapshots, error) -> importDatabase());

    }

    /**
     * adds the provided player into the firestore database (the addition updates the local database)
     * @param playerName the player's username
     * @param playerPassword the player's password
     */
    public void addPlayer(String playerName,String playerPassword) {
        // passes in an initial arraylist of null for claimedCollectibleIDs and default values
        Player player = new Player(playerName, playerPassword, null,0L,0L,0L);
        database.collection("Players").document(playerName).set(player);
    }

    /**
     * removes the provided player from the firestore database as well as the local databse.
     * @param playerName the deleted player's name
     */
    public void deletePlayer(String playerName){
        // removes player from the firebase collection
        Player selected = players.get(playerName);
        if (selected != null){
            database.collection("Players")
                    .document(playerName)
                    .delete()
                    .addOnFailureListener(e -> {throw new RuntimeException("Network Error.");});
        }
        // removes the player from the local database
        players.remove(playerName);
    }

    /**
     * communicates with the code to check if the local database is finished downloading.
     * this prevents some login errors due to firebase's async nature.
     * @return whether the databse is done downloading or not
     */
    public boolean isFinishDownloading(){
        return finishDownloading;
    }

    /**
     * specifically used for testing purposes, checks if a particular player exists within the
     * database. Please use getPlayer() in your code, as it is significantly more flexible.
     * @param playerName the username of the player
     * @return whether it is present within the database
     */
    public boolean exists(String playerName){
        return players.containsKey(playerName);
    }

    /**
     * gives a player object if the player exists within the firebase, otherwise returns null
     * @param playerName the player's username
     * @return the player
     * @return null
     */
    public Player getPlayer(String playerName) {
        if (!isFinishDownloading()){
            return null;
        }
        else if(players.containsKey(playerName)){
            return players.get(playerName);
        }
        else return null;
    }

    /**
     * adds the ID of the claimed object to the player database under the player to show that they
     * possess it
     * @param player
     * @param id
     */
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
        return players;
    }

}
