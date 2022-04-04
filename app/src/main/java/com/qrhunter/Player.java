package com.qrhunter;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Player class for all players of the game.
 */
public class Player extends User implements Serializable {

    private ArrayList<String> claimedCollectibleIDs = new ArrayList<String>();
    private Long highestScore = 0L;
    private Long scoreSum = 0L;
    private Long totalCodesScanned = 0L;

    public Player(){
        // Default constructor
    }

    // constructor for the player object
    public Player(String username, String password, ArrayList<String> claimedCollectibleIDs,
                  Long highestScore, Long scoreSum, Long totalCodesScanned) {
        super(username, password);
        this.claimedCollectibleIDs = claimedCollectibleIDs;
        this.highestScore = highestScore;
        this.scoreSum = scoreSum;
        this.totalCodesScanned = totalCodesScanned;
    }

    // getters

    /**
     * returns an array of scannables the player has collected
     * @return array of scannable IDs
     */
    public ArrayList<String> getClaimedCollectibleIDs() {
        return claimedCollectibleIDs;
    }

    /**
     * returns the highest score that the player has gotten on a single collectible
     * @return the player's highest score on one QR code
     */
    public Long getHighestScore(){
        return highestScore;
    }

    /**
     * returns the sum of scores that the player has in their lifetime
     * @return the player's current total score
     */
    public Long getScoreSum() {
        return scoreSum;
    }

    /**
     * returns the total number of QR codes that the player has scanned
     * @return the number of QR codes the player has scanned
     */
    public Long getTotalCodesScanned() {
        return totalCodesScanned;
    }

    /**
     * Sets a players claimed collectables ID
     * @param claimedCollectibleIDs ArrayList<String> of a players collectibles.
     */
    public void setClaimedCollectibleIDs(ArrayList<String> claimedCollectibleIDs) {
        this.claimedCollectibleIDs = claimedCollectibleIDs;
    }

    /**
     * Sets a players highest collectible score.
     * @param highestScore
     */
    public void setHighestScore(Long highestScore) {
        this.highestScore = highestScore;
    }

    /**
     * Sets a players total collectible score.
     * @param scoreSum
     */
    public void setScoreSum(Long scoreSum) {
        this.scoreSum = scoreSum;
    }

    /**
     * Sets a players total number of collectibles scanned.
     * @param totalCodesScanned
     */
    public void setTotalCodesScanned(Long totalCodesScanned) {
        this.totalCodesScanned = totalCodesScanned;
    }
}
