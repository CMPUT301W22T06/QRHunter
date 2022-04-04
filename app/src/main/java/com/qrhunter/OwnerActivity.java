package com.qrhunter;

import static com.qrhunter.MainActivity.allPlayers;
import static com.qrhunter.MainActivity.collectables;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Activity for OWNERS ONLY. Connects from MainActivity if an owner is logged in.
 * Lets Owners delete Players/Collectables.
 */
public class OwnerActivity extends AppCompatActivity {

    ListView playersList;
    ArrayAdapter<String> playerAdapter;
    ArrayList<String> playerDataList;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner);

        Button prefButton = findViewById(R.id.prefButton);

        prefButton.setOnClickListener(v -> {
            PrefMenu menu = new PrefMenu();
            menu.show(getFragmentManager(), "PrefMenu");
        });

        playersList = findViewById(R.id.collectibles_players_list);
        playerDataList = new ArrayList<>();
        updatePlayerDataList();

        TextView collectiblesPlayers = findViewById(R.id.collectibles_players);

        // set up click function for deleting from the list of players/collectables.
        playersList.setOnItemClickListener((adapterView, view, i, l) -> {
            // pop up confirmation alert dialog
            new AlertDialog.Builder(adapterView.getContext())
                    .setTitle("Confirm")
                    .setMessage("Are you sure you want to delete " + playerAdapter.getItem(i) + "?")
                    .setPositiveButton(android.R.string.yes, (dialogInterface, j) -> {
                        if (collectiblesPlayers.getText().toString().equals("Players")) {
                            allPlayers.deleteUser(playerAdapter.getItem(i));
                            updatePlayerDataList();
                        }
                        else {
                            String collectableName = playerAdapter.getItem(i);
                            String collectableID = getCollectableID(playerAdapter.getItem(i));
                            collectables.deleteCollectable(collectableID);
                            allPlayers.removeCollectable(collectableID);
                            updateCollectableDataList();
                            playerAdapter.remove(collectableName);
                        }
                    }).setNegativeButton(android.R.string.no, null)
                    .show();
        });

        // button to change list to collectables/players
        Button changeListButton = findViewById(R.id.change_list_button);
        changeListButton.setOnClickListener(view -> {
            if (changeListButton.getText().toString().equals("View Players")) {
                // change button and text of header of list
                changeListButton.setText("View Collectables");
                collectiblesPlayers.setText("Players");
                updatePlayerDataList();
            }
            else if (changeListButton.getText().toString().equals("View Collectables")) {
                // change button and text of head of list
                changeListButton.setText("View Players");
                updateCollectableDataList();
                collectiblesPlayers.setText("Collectables");
            }
        });
    }


    /**
     * Refreshes the ListAdapter to show players.
     */
    public void updatePlayerDataList() {
        // grab players list from Firestore db
        Map<String,Player> map = allPlayers.getPlayers();
        List<Player> players = new ArrayList<>(map.values());
        playerDataList = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) playerDataList.add(players.get(i).getUsername());

        // set up adapter
        playerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, playerDataList);
        playersList.setAdapter(playerAdapter);
    }


    /**
     * Refreshes the ListAdapter to show collectables.
     */
    public void updateCollectableDataList() {
        playerDataList = new ArrayList<>();
        Map<String,Collectable> map = collectables.getCollectables();
        List<Collectable> collectablesList = new ArrayList<>(map.values());
        for (int i = 0; i < collectablesList.size(); i++) playerDataList.add(collectablesList.get(i).getName());


        playerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, playerDataList);
        playersList.setAdapter(playerAdapter);
        playerAdapter.notifyDataSetChanged();
    }


    /**
     * Get a collectable id from a name, returns null if none found.
     * @param name name of the collectable
     * @return ID of the corresponding collectable
     */
    public String getCollectableID(String name) {
        Map<String,Collectable> map = collectables.getCollectables();
        List<Collectable> collectablesList = new ArrayList<>(map.values());
        for(int i = 0; i<collectablesList.size(); i++) {
            if(collectablesList.get(i).getName().equals(name)) {
                return collectablesList.get(i).getId();
            }
        }
        // if name was invalid
        return null;
    }
}