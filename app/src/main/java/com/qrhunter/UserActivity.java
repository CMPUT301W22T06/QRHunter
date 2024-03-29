package com.qrhunter;

import static com.qrhunter.MainActivity.allPlayers;
import static com.qrhunter.MainActivity.collectables;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/**
 * The UserActivity shows the player's current QR Codes that they've scanned, and their login
 * code. The restricted field passed to the activity determines whether the login code is shown
 * and deletions are enabled.
 */
public class UserActivity extends AppCompatActivity {

    Player player;

    ListView scanned;
    UserScannedAdapter adapter;
    TextView user_score;

    ArrayList<Collectable> player_collectables = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.N) @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // Get the player.
        String username = getIntent().getStringExtra("username");
        player = allPlayers.getPlayer(username);

        boolean restricted = getIntent().getBooleanExtra("restricted", true);

        scanned = findViewById(R.id.user_scanned);
        user_score = findViewById(R.id.user_score);

        if (restricted) {
            TextView name = findViewById(R.id.user_text);
            name.setText(player.getUsername());
        }
        else {
            ImageView qrcode = findViewById(R.id.user_qr);
            qrcode.setImageBitmap(generatePlayerQR());
        }

        // Setup collectables.
        for (String id : player.getClaimedCollectibleIDs())
            player_collectables.add(collectables.get(id));

        // Setup the adapter.
        adapter = new UserScannedAdapter(this,R.layout.item_list_userscanned, player_collectables);
        scanned.setAdapter(adapter);

        user_score.setText("Total Score: " + player.getScoreSum());

        // Setup for what happens when a user clicks a code.
        scanned.setOnItemClickListener((parent, v, position, id) -> {
            Collectable selected = collectables.get(player.getClaimedCollectibleIDs().get(position));

            // Boilerplate to generalize the creation of the view.
            int view, v_id, v_score, v_location, v_image;
            if (restricted) {
                view = R.layout.context_restricted;
                v_id = R.id.restricted_view_id;
                v_score = R.id.restricted_view_score; 
                v_location = R.id.restricted_view_location;
                v_image = R.id.restricted_view_image;
            }
            else {
                view = R.layout.context_view;
                v_id = R.id.context_view_id;
                v_score = R.id.context_view_score;
                v_location = R.id.context_view_location;
                v_image = R.id.context_view_image;
            }
            
            // Create the popup.
            LayoutInflater layoutInflater = LayoutInflater.from(UserActivity.this);
            View context_view = layoutInflater.inflate(view, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(UserActivity.this);

            // Add the information
            if (selected.getPhoto() != null) selected.viewPhoto(context_view.findViewById(v_image));
            ((TextView)context_view.findViewById(v_id)).setText("Name: " + selected.getName());
            ((TextView)context_view.findViewById(v_score)).setText("Score: " + selected.getScore());
            ((TextView)context_view.findViewById(v_location)).setText("Location: " +
                    selected.getLocation().getLatitude() + " " +
                    selected.getLocation().getLongitude());

            // Create and show the dialog.
            alertDialogBuilder.setView(context_view);
            final AlertDialog dialog = alertDialogBuilder.create();
            dialog.show();

            if (!restricted) {
                context_view.findViewById(R.id.context_view_delete).setOnClickListener(x -> {
                    allPlayers.removeClaimedID(player.getUsername(), selected.getId());
                    refresh();
                    dialog.dismiss();
                });
            }
        });

        // Sorting button.
        findViewById(R.id.score_sort).setOnClickListener(v -> {
            Collections.sort(player_collectables, Comparator.comparing(Collectable::getScore));
            refresh();
        });
    }


    /**
     * Refreshes the list of QR Codes when a change is made.
     */
    private void refresh() {

        // Setup the adapter.
        adapter = new UserScannedAdapter(this,R.layout.item_list_userscanned, player_collectables);
        scanned.setAdapter(adapter);

        // Inform everything about the change.
        adapter.notifyDataSetChanged();
        scanned.invalidateViews();
        scanned.refreshDrawableState();
        user_score.setText("Total Score: " + player.getScoreSum());
    }


    /**
     * Generates a QR Code from the player's username.
     * @return The QR Bitmap.
     */
    private Bitmap generatePlayerQR()  {

        // Creates the matrix.
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(player.getUsername(), BarcodeFormat.QR_CODE,
                    300, 300, null);
        }
        catch (IllegalArgumentException | WriterException e) {return null;}

        int bitMatrixWidth = bitMatrix.getWidth();
        int bitMatrixHeight = bitMatrix.getHeight();
        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        int colorWhite = 0xFFFFFFFF;
        int colorBlack = 0xFF000000;

        // Fills
        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;
            for (int x = 0; x < bitMatrixWidth; x++) {
                pixels[offset + x] = bitMatrix.get(x, y) ? colorBlack : colorWhite;
            }
        }

        // Creates the Bitmap and returns.
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);
        bitmap.setPixels(pixels, 0, 300, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }
}