package com.qrhunter;
import java.util.ArrayList;

// temporary placeholder player class

public class Player {
    private ArrayList<String> claimedCollectibleIDs;
    private String username;

    public Player(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<String> getClaimedCollectibleIDs() {
        return claimedCollectibleIDs;
    }

    public void setClaimedCollectibleIDs(ArrayList<String> claimedCollectibleIDs) {
        this.claimedCollectibleIDs = claimedCollectibleIDs;
    }
}