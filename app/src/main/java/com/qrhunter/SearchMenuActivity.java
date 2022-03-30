package com.qrhunter;

import static com.qrhunter.MainActivity.allPlayers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.ArrayList;
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
                String playerSearchString = searchInput.getText().toString();
                searchInput.setText("");
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

    @Override protected void onResume() {
        super.onResume();
        if (!scanner.isActivated()) scanner.resume();
    }


    @Override protected void onPause() {
        super.onPause();
        scanner.pause();
    }
}