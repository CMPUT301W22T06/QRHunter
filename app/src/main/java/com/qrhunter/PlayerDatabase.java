//Barebones initial implementation for developing related activities
package com.example.qrhunter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * This class keeps track of all the players that have claimed each collectible as well as the list of all players
 */
public class PlayerDatabase {
    private ArrayList<Player> allPlayers = new ArrayList<>();
    //Unclear what type the qrCode will have, left as int for now
    private HashMap<Integer, Set<String>> claimedCodes = new HashMap<>();

    /**
     * This adds a player to the list. If the player's username already exits, then it throws IllegalArgumentException
     *
     * @throws IllegalArgumentException
     *      If the player's username exists in the list, this is thrown
     * @param newPlayer
     *     This is the player to be added
     *
     */
    public void addPlayer(Player newPlayer) {
        for(int i = 0; i<allPlayers.size(); i++) {
            if (allPlayers.get(i).getUsername().equals(newPlayer.getUsername())) {
                throw new IllegalArgumentException();
            }
        }
        allPlayers.add(newPlayer);
    }

    public void updatePlayer(Player playerToUpdate) {
        for(int i = 0; i<allPlayers.size(); i++) {
            if (allPlayers.get(i).getUsername().equals(playerToUpdate.getUsername())) {
                allPlayers.set(i, playerToUpdate);
                break;
            }
        }
    }

    public void deletePlayer(Player playerToDelete) {
        allPlayers.remove(playerToDelete);
    }

    /**
     * If the qrCode has never been claimed before, this adds in a new qrCode & username key pair
     * Else adds the username who's claimed the code to the existing list
     *
     * @param qrCode This is the qrCode being claimed
     * @param username This is the username of the player claiming the qrCode
     *
     */
    public void claimCode(int qrCode, String username) {
        if (claimedCodes.containsKey((Integer) qrCode)) {
            Set<String> players = claimedCodes.get((Integer) qrCode);
            players.add(username);
            claimedCodes.put((Integer) qrCode, players);
        }
        else {
            Set<String> players = new HashSet<String>();
            players.add(username);
            claimedCodes.put((Integer) qrCode, players);
        }
    }
}