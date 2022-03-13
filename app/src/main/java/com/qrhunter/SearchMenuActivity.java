package com.qrhunter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class SearchMenuActivity extends AppCompatActivity {

    ListView playersList;
    ArrayAdapter<Player> playerAdapter;
    ArrayList<Player> playerDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_menu);

        String username = getIntent().getStringExtra("username");

        // search input editText
        EditText searchInput = findViewById(R.id.search_input);

        // search button
        Button searchButton = findViewById(R.id.search_button);

        // filter button
        Button filterButton = findViewById(R.id.filter_button);

        // TODO: create XML for searched item list
        ListView searchedItemList = findViewById(R.id.search_items_list);

        Button QRViewButton = findViewById(R.id.QR_view_button);
        QRViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchMenuActivity.this, QRViewActivity.class);
                startActivity(intent);
            }
        });

        Button myCollectiblesButton = findViewById(R.id.my_collectibles_button);
        myCollectiblesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchMenuActivity.this, MyCollectiblesList.class);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });

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

        // temp testing list of players
        /*Player []testPlayersList = new Player[20];
        for(int i = 0; i<20;i++) {
            testPlayersList[i] = new Player("Test Player " + Integer.toString(i));
            String []testCollectiblesIDList = new String[20];
            for(int j = 0; j < 20; j++) {
                testCollectiblesIDList[j] = "TestCollectible"+Integer.toString(j);
            }
            testPlayersList[i].setClaimedCollectibleIDs(new ArrayList<String>(Arrays.asList(testCollectiblesIDList)));
        }
        playersList = findViewById(R.id.search_items_list);
        playerDataList = new ArrayList<Player>();
        // put temp list into list
        playerDataList.addAll(Arrays.asList(testPlayersList));

        // set up adapter
        playerAdapter = new ArrayAdapter<Player>(this,  R.layout.player_content, playerDataList);
        playersList.setAdapter(playerAdapter);

        // set up click function
        playersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // create fragment for player collectibles
                new PlayerCollectiblesFragment(testPlayersList[i]).show(getSupportFragmentManager(),"PLAYER_FRAGMENT");
            }
        });*/

    }
}