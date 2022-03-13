package com.qrhunter;

import static com.qrhunter.MainActivity.allPlayers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SearchMenuActivity extends AppCompatActivity {

    ListView playersList;
    ArrayAdapter<Player> playerAdapter;
    ArrayList<Player> playerDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_menu);

        // for commenting
        String username = getIntent().getStringExtra("username");

        // search input editText
        EditText searchInput = findViewById(R.id.search_input);

        // search button
        Button searchButton = findViewById(R.id.search_button);

        // filter button
        Button filterButton = findViewById(R.id.filter_button);

        // all players list
        ListView searchedItemList = findViewById(R.id.search_items_list);

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: set up item on click for filter menu
                new FilterMenuFragment().show(getSupportFragmentManager(),"FILTER_MENU");
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: set up searching from firebase DB
            }
        });

        // fragment that shows on click of player in the list
        searchedItemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // TODO: set up the ability to go to user profile/QR view activity on item click
                // depending on filter type, go to either the QR view activity or Player View activity
                // as of right now, it will always go to the QRView activity
                Intent intent = new Intent(SearchMenuActivity.this, QRViewActivity.class);
                startActivity(intent);
            }
        });

        playersList = findViewById(R.id.search_items_list);
        playerDataList = new ArrayList<Player>();
        // grab players list from Firestore db
        Map<String,Player> map = allPlayers.getPlayers();
        List<Player> players = new ArrayList<>(map.values());
        playerDataList.addAll(players);

        // set up adapter
        playerAdapter = new PlayersList(this, playerDataList);
        playersList.setAdapter(playerAdapter);

        // set up click function
        playersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // create fragment for player collectibles
                new PlayerCollectiblesFragment(playerDataList.get(i)).show(getSupportFragmentManager(),"PLAYER_FRAGMENT");
            }
        });

    }
}