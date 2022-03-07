package com.qrhunter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.TextView;

public class SearchMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_menu);

        // search input editText
        EditText searchInput = findViewById(R.id.search_input);

        // search button
        Button searchButton = findViewById(R.id.search_button);

        // filter button
        Button filterButton = findViewById(R.id.filter_button);

        // TODO: create XML for searched item list
        ListView searchedItemList = findViewById(R.id.search_items_list);

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: set up filter to pop up on filterButton press
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
    }
}