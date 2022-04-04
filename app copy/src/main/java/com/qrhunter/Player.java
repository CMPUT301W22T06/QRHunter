package com.qrhunter;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {

    private String username;
    private String password;
    private ArrayList<String> claimedCollectibleIDs = new ArrayList<String>();
    private Long highestScore;
    private Long scoreSum;
    private Long totalCodesScanned;
    private Bitmap QRcode;

    public Player(){
        // Default constructor
    }

    // constructor for the player object
    public Player(String username, String password, ArrayList<String> claimedCollectibleIDs,
                  Long highestScore, Long scoreSum, Long totalCodesScanned) {
        this.username = username;
        this.password = password;
        this.claimedCollectibleIDs = claimedCollectibleIDs;
        this.highestScore = highestScore;
        this.scoreSum = scoreSum;
        this.totalCodesScanned = totalCodesScanned;
    }

    // getters

    /**
     * returns the username of the player (same as object ID)
     * @return the player's username
     */

    public String getUsername() {
        return username;
    }

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

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setClaimedCollectibleIDs(ArrayList<String> claimedCollectibleIDs) {
        this.claimedCollectibleIDs = claimedCollectibleIDs;
    }

    public void setHighestScore(Long highestScore) {
        this.highestScore = highestScore;
    }

    public void setScoreSum(Long scoreSum) {
        this.scoreSum = scoreSum;
    }

    public void setTotalCodesScanned(Long totalCodesScanned) {
        this.totalCodesScanned = totalCodesScanned;
    }

    public Bitmap getQRcode() {
        return QRcode;
    }

    public void setQRcode(Bitmap QRcode) {
        this.QRcode = QRcode;
    }
}
