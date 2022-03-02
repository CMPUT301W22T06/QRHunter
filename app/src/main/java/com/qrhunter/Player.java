//Barebones initial implementation for developing related activities
package com.example.qrhunter;

/**
 * This class represents a player and hold their related stats
 */
public class Player {
    private String username;
    private int highestScore;
    private int totalCodesScanned;
    private int scoreSum;

    Player(String username) {
        this.username = username;
        this.highestScore = 0;
        this.totalCodesScanned = 0;
        this.scoreSum = 0;
    }

    public String getUsername() {
        return username;
    }

    //Unclear if users are able to change their username
    public void setUsername(String username) {
        this.username = username;
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
}
