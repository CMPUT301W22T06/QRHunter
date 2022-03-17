package com.qrhunter;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * Displays all of the collectibles the user has claimed
 */
public class MyCollectiblesList extends AppCompatActivity {

    private ArrayList<String> myCollectibles;

    ArrayAdapter<String> myCollectiblesAdapter;
    ListView myCollectiblesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collectibles_list);

        String username = getIntent().getStringExtra("username");

        myCollectiblesList = findViewById(R.id.my_collectibles_list);
        myCollectibles = new ArrayList<>();

        myCollectiblesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, myCollectibles);


        Player current = MainActivity.allPlayers.getPlayer(username);
        for (String id : current.getClaimedCollectibleIDs()) {
            myCollectibles.add(HomeActivity.collectables.get(id).getName());
        }
        myCollectiblesList.setAdapter(myCollectiblesAdapter);

        // Brings the user to the QRView activity
        myCollectiblesList.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(view.getContext(), QRViewActivity.class);
            intent.putExtra("collectableID", current.getClaimedCollectibleIDs().get(i));
            startActivity(intent);
        });
    }
}