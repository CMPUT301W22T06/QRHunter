package com.qrhunter;

import static com.qrhunter.MainActivity.allPlayers;
import static com.qrhunter.MainActivity.toast;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
    String[] sortTypes = {
            "Sorted by highest total score.",
            "Sorted by highest score on a single collectible.",
            "Sorted by total collectibles scanned."
    };

    boolean sorted = false; // to prevent the myScore button from working if the list isn't sorted yet

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_menu);

        String username = getIntent().getStringExtra("username");
        EditText searchInput = findViewById(R.id.search_input);
        Button searchButton = findViewById(R.id.search_button);
        Button filterButton = findViewById(R.id.filter_button);
        Button rankButton = findViewById(R.id.rank_button);
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
        filterButton.setOnClickListener(view -> {

            // Set the lambda using a one-line if statement.
            Collections.sort(playerDataList,
                    sortType == 0 ? (p1, p2) -> p2.getScoreSum().compareTo(p1.getScoreSum()) :
                    sortType == 1 ? (p1, p2) -> p2.getHighestScore().compareTo(p1.getHighestScore()) :
                    (p1, p2) -> p2.getTotalCodesScanned().compareTo(p1.getTotalCodesScanned())

                    );

            // Set sorted, toast the message.
            sorted = true;
            toast(getApplicationContext(), sortTypes[sortType]);
            playerAdapter = new PlayersList(this, playerDataList, sortType);

            // Increment our sortType, mod it by 3 to bind sortType to [0, 2]
            sortType++; sortType %= 3;
            playersList.setAdapter(playerAdapter);
            playerAdapter.notifyDataSetChanged();
        });

        searchButton.setOnClickListener(view -> {
            if (!TextUtils.isEmpty(searchInput.getText().toString())){
                Player p = allPlayers.getPlayer(searchInput.getText().toString());
                if (p == null) {
                    Toast.makeText(SearchMenuActivity.this, "player not found", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(SearchMenuActivity.this, UserActivity.class);
                    intent.putExtra("username", p.getUsername());
                    intent.putExtra("restricted", true);
                    startActivity(intent);
                }
            }
            else MainActivity.toast(SearchMenuActivity.this, "input username cannot be empty");
        });

        rankButton.setOnClickListener(view -> myRank(username));

        Button myCollectiblesButton = findViewById(R.id.my_collectibles_button);
        myCollectiblesButton.setOnClickListener(view -> {
            if (!TextUtils.isEmpty(username)){
                Intent intent = new Intent(SearchMenuActivity.this, MyCollectiblesList.class);
                intent.putExtra("username",username);
                startActivity(intent);
            }
            else MainActivity.toast(SearchMenuActivity.this, "please login first");
        });

        // fragment that shows on click of player in the list
        searchedItemList.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(SearchMenuActivity.this, QRViewActivity.class);
            startActivity(intent);
        });

        playersList = findViewById(R.id.search_items_list);
        playerDataList = new ArrayList<>();
        playerAdapter = new PlayersList(this, playerDataList, 0);
        playersList.setAdapter(playerAdapter);

        // grab players list from Firestore db
        Map<String,Player> map = allPlayers.getPlayers();
        List<Player> players = new ArrayList<>(map.values());
        playerDataList.addAll(players);

        // set up click function
        playersList.setOnItemClickListener((adapterView, view, i, l) -> {
            // create fragment for player collectibles
            new PlayerCollectiblesFragment(playerDataList.get(i)).show(getSupportFragmentManager(),"PLAYER_FRAGMENT");
        });
    }

    /**
     * Returns the player's current rank.
     * @param username The player's username.
     */
    void myRank(String username){
        if (!sorted) toast(getApplicationContext(),"Please sort the list first!");
        else toast(getApplicationContext(),"You are rank " + (playerDataList.indexOf(allPlayers.getPlayer(username))+1) + ".");
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