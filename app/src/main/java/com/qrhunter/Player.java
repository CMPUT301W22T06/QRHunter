package com.example.qrhunter;

import java.util.ArrayList;

/**
 * This class represents a player and hold their related stats
 */
public class Player {
    private String username;
    private int highestScore;
    private int totalCodesScanned;
    private int scoreSum;
    private ArrayList<String> claimedCollectibleIDs;

    Player(String username) {
        this.username = username;
        this.highestScore = 0;
        this.totalCodesScanned = 0;
        this.scoreSum = 0;
        this.claimedCollectibleIDs = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public int getHighestScore() {
        return highestScore;
    }

    public void setHighestScore(int highestScore) {
        this.highestScore = highestScore;
    }

    public int getTotalCodesScanned() {
        return totalCodesScanned;
    }

    public void setTotalCodesScanned(int totalCodesScanned) {
        this.totalCodesScanned = totalCodesScanned;
    }

    public int getScoreSum() {
        return scoreSum;
    }

    public void setScoreSum(int scoreSum) {
        this.scoreSum = scoreSum;
    }

    public ArrayList<String> getClaimedCollectibleIDs() {
        return claimedCollectibleIDs;
    }

    /**
     * This adds a new collectible ID to the list if the player hasn't already claimed it.
     *
     * @param newID
     *     This is the ID to be added
     * @return boolean
     *     Returns false if the player has already claimed the ID, true otherwise
     */
    public boolean addClaimedCollectibleID(String newID) {
        if(claimedCollectibleIDs.contains(newID)) {
            return false;
        }
        else {
            claimedCollectibleIDs.add(newID);
            return true;
        }
    }
}
