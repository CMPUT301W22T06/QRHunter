package com.qrhunter;

import static com.qrhunter.MainActivity.allPlayers;
import static com.qrhunter.MainActivity.toast;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Activity to search and view other players and their collectibles.
 */
public class SearchMenuActivity extends AppCompatActivity {

    ListView playersList;
    ArrayAdapter<Player> playerAdapter;
    ArrayList<Player> playerDataList;
    DecoratedBarcodeView scanner;

    int sortType = 0;
    boolean sorted = false; // to prevent the myScore button from working if the list isn't sorted yet

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

        // get player rank button
        Button rankButton = findViewById(R.id.rank_button);

        // all players list
        ListView searchedItemList = findViewById(R.id.search_items_list);

        // Grab the scanner within the activity.
        scanner = findViewById(R.id.search_menu_scanner);
        scanner.decodeContinuous(new BarcodeCallback() {

            // When we successfully scan a code.
            @Override public void barcodeResult(BarcodeResult result) {
               Player player = allPlayers.getPlayer(result.getText());
               if (player != null) {
                   Intent intent = new Intent(SearchMenuActivity.this, UserActivity.class);
                   intent.putExtra("username",username);
                   intent.putExtra("restricted", true);
                   startActivity(intent);
               }
            }
            @Override public void possibleResultPoints(List<ResultPoint> resultPoints) {}
        });

        // toggles between the various sorts and toasts the user with what it is sorted by
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: set up item on click for filter menu

                if (sortType==0){
                    sortByHighestScore();
                    sortType=1;
                    Toast.makeText(getApplicationContext(),
                            "Sorted by highest score on a single collectible.", Toast.LENGTH_SHORT)
                            .show();
                }
                else if (sortType==1){
                    sortByScoreSum();
                    sortType=2;
                    Toast.makeText(getApplicationContext(),
                            "Sorted by highest total score.", Toast.LENGTH_SHORT)
                            .show();
                }
                else if (sortType==2){
                    sortByScanned();
                    sortType=0;
                    Toast.makeText(getApplicationContext(),
                            "Sorted by total collectibles scanned.", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(searchInput.getText().toString())){
                    Player p = allPlayers.getPlayer(searchInput.getText().toString());
                    if (p==null){
                        Toast.makeText(SearchMenuActivity.this, "player not found", Toast.LENGTH_SHORT).show();
                    }else{
                        Intent intent = new Intent(SearchMenuActivity.this,OtherUserActivity.class);
                        intent.putExtra("username",p.getUsername());
                        startActivity(intent);
                    }
                }else{
                    Toast.makeText(SearchMenuActivity.this, "input username cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        rankButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRank(username);
            }
        });

        Button myCollectiblesButton = findViewById(R.id.my_collectibles_button);
        myCollectiblesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(username)){
                    Intent intent = new Intent(SearchMenuActivity.this, MyCollectiblesList.class);
                    intent.putExtra("username",username);
                    startActivity(intent);
                }else{
                    Toast.makeText(SearchMenuActivity.this, "please login first", Toast.LENGTH_SHORT).show();
                }
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
        // 0 for default displayType
        // 1 = highest single score
        // 2 = highest total score
        // 3 = highest collected number
        playerAdapter = new PlayersList(this, playerDataList, 0);
        playersList.setAdapter(playerAdapter);

        // toggles between the various sorts and toasts the user with what it is sorted by
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: set up item on click for filter menu

                if (sortType==0){
                    sortByHighestScore();
                    sortType=1;
                    Toast.makeText(getApplicationContext(),
                            "Sorted by highest score on a single collectible.", Toast.LENGTH_SHORT)
                            .show();
                }
                else if (sortType==1){
                    sortByScoreSum();
                    sortType=2;
                    Toast.makeText(getApplicationContext(),
                            "Sorted by highest total score.", Toast.LENGTH_SHORT)
                            .show();
                }
                else if (sortType==2){
                    sortByScanned();
                    sortType=0;
                    Toast.makeText(getApplicationContext(),
                            "Sorted by total collectibles scanned.", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        // set up click function
        playersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // create fragment for player collectibles
                new PlayerCollectiblesFragment(playerDataList.get(i)).show(getSupportFragmentManager(),"PLAYER_FRAGMENT");
            }
        });
    }

    // returns the player score
    void myRank(String username){
        if (!sorted){
            toast(getApplicationContext(),"Please sort the list first!");
        }
        else if (sorted){
            toast(getApplicationContext(),"You are rank " +
                    (playerDataList.indexOf(allPlayers.getPlayer(username))+1) + ".");
        }
    }

    public void sortByHighestScore(){
        Collections.sort(playerDataList, new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {

                return p2.getHighestScore().compareTo(p1.getHighestScore());
            }
        });
        sorted = true;
        playerAdapter = new PlayersList(this, playerDataList, 1);
        playersList.setAdapter(playerAdapter);
        playerAdapter.notifyDataSetChanged();
    }

    public void sortByScoreSum(){
        Collections.sort(playerDataList, new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                return p2.getScoreSum().compareTo(p1.getScoreSum());
            }
        });
        sorted = true;
        playerAdapter = new PlayersList(this, playerDataList, 2);
        playersList.setAdapter(playerAdapter);
        playerAdapter.notifyDataSetChanged();
    }

    public void sortByScanned(){
        Collections.sort(playerDataList, new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                return p2.getTotalCodesScanned().compareTo(p1.getTotalCodesScanned());
            }
        });
        sorted = true;
        playerAdapter = new PlayersList(this, playerDataList, 3);
        playersList.setAdapter(playerAdapter);
        playerAdapter.notifyDataSetChanged();
    }

    @Override protected void onResume() {
        super.onResume();
        if (!scanner.isActivated()) scanner.resume();
    }

    @Override protected void onPause() {
        super.onPause();
        scanner.pause();
    }
}