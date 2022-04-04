package com.qrhunter;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.auth.User;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.ArrayList;
import java.util.Comparator;

public class OtherUserActivity  extends AppCompatActivity {


    Player player;

    ListView scanned;
    UserScannedAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user);

        // Get the player.
        String username = getIntent().getStringExtra("username");
        player = MainActivity.allPlayers.getPlayer(username);

        scanned = findViewById(R.id.user_scanned);
        TextView user_score = findViewById(R.id.user_score);



        TextView userName = findViewById(R.id.user_text);
        if (player!=null)userName.setText(player.getUsername());

        // Setup the list of adapters
        ArrayList<Collectable> collectables = new ArrayList<>();
        for (String id : player.getClaimedCollectibleIDs()) {
            collectables.add(HomeActivity.collectables.get(id));
        }

        adapter = new UserScannedAdapter(this, R.layout.item_list_userscanned, collectables);
        scanned.setAdapter(adapter);

        user_score.setText("Total Score: " + getTotalScore());



        // Setup for what happens when a user clicks a code.
        scanned.setOnItemClickListener((parent, v, position, id) -> {
            Collectable selected = HomeActivity.collectables.get(player.getClaimedCollectibleIDs().get(position));

            // Create the popup.
            LayoutInflater layoutInflater = LayoutInflater.from(OtherUserActivity.this);
            View context_view = layoutInflater.inflate(R.layout.context_view, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(OtherUserActivity.this);

            // Add the information.
            if (selected.getPhoto() != null) selected.viewPhoto(context_view.findViewById(R.id.context_view_image));
            ((TextView)context_view.findViewById(R.id.context_view_id)).setText("Name: " + selected.getName());
            ((TextView)context_view.findViewById(R.id.context_view_score)).setText("Score: " + selected.getScore());
            ((TextView)context_view.findViewById(R.id.context_view_location)).setText("Location: " +
                    selected.getLocation().getLatitude() + " " +
                    selected.getLocation().getLongitude());

            // Create and show the dialog.
            alertDialogBuilder.setView(context_view);
            final AlertDialog dialog = alertDialogBuilder.create();
            dialog.show();

            context_view.findViewById(R.id.context_view_delete).setOnClickListener(x -> {
                if (HomeActivity.collectables.deleteCollectable(selected.getId()) != 0)
                    MainActivity.toast(getApplicationContext(), "Could not delete (It may not exist within the database)");
                MainActivity.allPlayers.removeClaimedID(player.getUsername(), selected.getId());
                refresh();
                dialog.dismiss();
            });


        });

        // Sorting button.
        findViewById(R.id.score_sort).setOnClickListener(v -> {
            player.getClaimedCollectibleIDs().sort(Comparator.naturalOrder());
            refresh();
        });


    }

    /**
     * Refreshes the list of QR Codes when a change is made.
     */
    private void refresh() {

        // Setup the adapter.
        ArrayList<Collectable> collectables = new ArrayList<>();
        for (String id : player.getClaimedCollectibleIDs()) {
            collectables.add(HomeActivity.collectables.get(id));
        }

        adapter = new UserScannedAdapter(this, R.layout.item_list_userscanned, collectables);
        scanned.setAdapter(adapter);

        // Inform everything about the change.
        adapter.notifyDataSetChanged();
        scanned.invalidateViews();
        scanned.refreshDrawableState();
    }

    private Long getTotalScore() {
        long score = 0L;
        for (String scanned : player.getClaimedCollectibleIDs()) {
            score += HomeActivity.collectables.get(scanned).getScore();
        }
        return score;
    }


}
