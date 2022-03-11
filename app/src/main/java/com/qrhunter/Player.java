package com.qrhunter;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Player {

    private String password;
    private ArrayList<String> claimedCollectibleIDs;
    private int highestScore;
    private int scoreSum;
    private int totalCodesScanned;

    public Player(){
        // Default constructor required for calls to DataSnapshot.getValue(Player.class)
    }

    // constructor for the player object
    public Player(String password, ArrayList<String> claimedCollectibleIDs,
                  int highestScore, int scoreSum, int totalCodesScanned) {
        this.password = password;
        this.claimedCollectibleIDs = claimedCollectibleIDs;
        this.highestScore = highestScore;
        this.scoreSum = scoreSum;
        this.totalCodesScanned = totalCodesScanned;
    }

    // getters

    /**
     * returns the password of the player
     * @return the player's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * returns an array of scannables the player has collected
     * @return array of scannable IDs
     */
    public ArrayList<String> getClaimedCollectiveIDs() {
        return claimedCollectibleIDs;
    }

    /**
     * returns the highest score that the player has gotten on a single collectible
     * @return the player's highest score on one QR code
     */
    public int getHighestScore(){
        return highestScore;
    }

    /**
     * returns the sum of scores that the player has in their lifetime
     * @return the player's current total score
     */
    public int getScoreSum() {
        return scoreSum;
    }

    /**
     * returns the total number of QR codes that the player has scanned
     * @return the number of QR codes the player has scanned
     */
    public int getTotalCodesScanned() {
        return totalCodesScanned;
    }

}
