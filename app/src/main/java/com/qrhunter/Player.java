package com.qrhunter;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Player class for all players of the game.
 */
public class Player extends User implements Serializable {
    private ArrayList<String> claimedCollectibleIDs = new ArrayList<>();


    public Player() {}


    // constructor for the player object
    public Player(String username, String password, ArrayList<String> claimedCollectibleIDs) {
        super(username, password);
        this.claimedCollectibleIDs = claimedCollectibleIDs;
    }


    /**
     * returns an array of scannables the player has collected
     * @return array of scannable IDs
     */
    public ArrayList<String> getClaimedCollectibleIDs() {return claimedCollectibleIDs;}


    /**
     * returns the highest score that the player has gotten on a single collectible
     * @return the player's highest score on one QR code
     */
    public Long getHighestScore(){
        Long highest = 0L;
        for (String id : getClaimedCollectibleIDs()) {
            try {
                Long current = MainActivity.collectables.get(id).getScore();
                if (current > highest) highest = current;
            }
            catch (RuntimeException ignored) {}
        }
        return highest;
    }


    /**
     * returns the sum of scores that the player has in their lifetime
     * @return the player's current total score
     */
    public Long getScoreSum() {
        Long sum = 0L;
        for (String id : getClaimedCollectibleIDs()) {
            try {
                sum += MainActivity.collectables.get(id).getScore();
            }
            catch (RuntimeException ignored) {}
        }
        return sum;
    }


    /**
     * returns the total number of QR codes that the player has scanned
     * @return the number of QR codes the player has scanned
     */
    public Long getTotalCodesScanned() {return (long) claimedCollectibleIDs.size();}


    /**
     * Sets a players claimed collectables ID
     * @param claimedCollectibleIDs ArrayList<String> of a players collectibles.
     */
    public void setClaimedCollectibleIDs(ArrayList<String> claimedCollectibleIDs) {
        this.claimedCollectibleIDs = claimedCollectibleIDs;
    }
}
