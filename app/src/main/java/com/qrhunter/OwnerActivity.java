package com.qrhunter;

import static com.qrhunter.HomeActivity.collectables;
import static com.qrhunter.MainActivity.allPlayers;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OwnerActivity extends AppCompatActivity {
    ListView playersList;
    ArrayAdapter<String> playerAdapter;
    ArrayList<String> playerDataList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner);

        // need to create the database
        collectables.exists("");

        playersList = findViewById(R.id.collectibles_players_list);
        playerDataList = new ArrayList<String>();
        updatePlayerDataList();

        TextView collectiblesPlayers = findViewById(R.id.collectibles_players);


        // set up click function
        playersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // pop up confirmation alert dialog
                new AlertDialog.Builder(adapterView.getContext())
                        .setTitle("Confirm")
                        .setMessage("Are you sure you want to delete " + playerAdapter.getItem(i) + "?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int j) {
                                if(collectiblesPlayers.getText().toString().equals("Players")) {
                                    allPlayers.deleteUser(playerAdapter.getItem(i));
                                    updatePlayerDataList();
                                } else {
                                    collectables.deleteCollectable(getCollectableID(playerAdapter.getItem(i)));
                                    updateCollectableDataList();
                                }
                            }
                        }).setNegativeButton(android.R.string.no, null)
                        .show();
            }
        });

        Button changeListButton = findViewById(R.id.change_list_button);
        changeListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(changeListButton.getText().toString().equals("View Players")) {
                    // change button and text of header of list
                    changeListButton.setText("View Collectables");
                    collectiblesPlayers.setText("Players");
                    updatePlayerDataList();
                } else if (changeListButton.getText().toString().equals("View Collectables")) {
                    // change button and text of head of list
                    changeListButton.setText("View Players");
                    updateCollectableDataList();
                    collectiblesPlayers.setText("Collectables");

                }
            }
        });
    }

    public void updatePlayerDataList() {
        // grab players list from Firestore db
        Map<String,Player> map = allPlayers.getPlayers();
        List<Player> players = new ArrayList<>(map.values());
        playerDataList = new ArrayList<String>();
        for(int i = 0; i<players.size();i++) {
            playerDataList.add(players.get(i).getUsername());
        }
        // set up adapter
        playerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, playerDataList);
        playersList.setAdapter(playerAdapter);
    }

    public void updateCollectableDataList() {
        playerDataList = new ArrayList<String>();
        Map<String,Collectable> map = collectables.getCollectables();
        List<Collectable> collectablesList = new ArrayList<>(map.values());
        for(int i = 0; i<collectablesList.size(); i++) {
            // TODO: change to collectable name (waiting on merge)
            //playerDataList.add(collectablesList.get(i).getName())
            playerDataList.add(collectablesList.get(i).toString());
        }
        playerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, playerDataList);
        playersList.setAdapter(playerAdapter);
    }

    public String getCollectableID(String name) {
        Map<String,Collectable> map = collectables.getCollectables();
        List<Collectable> collectablesList = new ArrayList<>(map.values());
        for(int i = 0; i<collectablesList.size(); i++) {
            // TODO: CHANGE TO COLLECTABLE NAME (waiting on merge)
            //if(collectablesList.get(i).getName().equals(name)) {
            if(true) {
                return collectablesList.get(i).getId();
            }
        }
        // if name was invalid
        return null;
    }
}