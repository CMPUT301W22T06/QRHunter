package com.qrhunter;

import static com.qrhunter.MainActivity.allPlayers;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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


        playersList = findViewById(R.id.collectibles_players_list);
        playerDataList = new ArrayList<String>();
        updatePlayerDataList();



        // set up click function
        playersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // pop up confirmation alert dialog
                new AlertDialog.Builder(adapterView.getContext())
                        .setTitle("Delete Player")
                        .setMessage("Are you sure you want to delete " + playerAdapter.getItem(i) + "?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int j) {
                                allPlayers.deleteUser(playerAdapter.getItem(i));
                                updatePlayerDataList();
                            }
                        }).setNegativeButton(android.R.string.no, null)
                        .show();
            }
        });
    }
    public void updatePlayerDataList() {
        playerDataList = new ArrayList<String>();
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
}