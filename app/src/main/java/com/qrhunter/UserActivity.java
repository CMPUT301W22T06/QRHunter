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

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.Comparator;

public class UserActivity extends AppCompatActivity {

    Player player;

    ListView scanned;
    ArrayAdapter<String> adapter;
    TextView user_score;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // Get the player.
        String username = getIntent().getStringExtra("username");
        player = MainActivity.allPlayers.getPlayer(username);

        scanned = findViewById(R.id.user_scanned);
        user_score = findViewById(R.id.user_score);

        // The Player QR Code.
        ImageView qrcode = findViewById(R.id.user_qr);
        qrcode.setImageBitmap(generatePlayerQR());

        // Setup the list of adapters
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, player.getClaimedCollectibleIDs());
        scanned.setAdapter(adapter);

        user_score.setText("Total Score: " + getTotalScore());

        // Setup for what happens when a user clicks a code.
        scanned.setOnItemClickListener((parent, v, position, id) -> {
            Collectable selected = HomeActivity.collectables.get((String)scanned.getItemAtPosition(position));

            // Create the popup.
            LayoutInflater layoutInflater = LayoutInflater.from(UserActivity.this);
            View context_view = layoutInflater.inflate(R.layout.context_view, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(UserActivity.this);

            // Add the information.
            if (selected.getPhoto() != null) selected.viewPhoto(context_view.findViewById(R.id.context_view_image));
            ((TextView)context_view.findViewById(R.id.context_view_id)).setText("ID: " + selected.getId());
            ((TextView)context_view.findViewById(R.id.context_view_score)).setText("Score: " + selected.getScore());
            ((TextView)context_view.findViewById(R.id.context_view_location)).setText("Location: " +
                    selected.getLocation().getLatitude() + " " +
                    selected.getLocation().getLongitude());

            // Create and show the dialog.
            alertDialogBuilder.setView(context_view);
            final AlertDialog dialog = alertDialogBuilder.create();
            dialog.show();

            // When we hit add picture, spawn a camera instance and get the BitMap taken.
            context_view.findViewById(R.id.context_view_delete).setOnClickListener(x -> {
                HomeActivity.collectables.deleteCollectable(selected.getId());
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
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, player.getClaimedCollectibleIDs());
        scanned.setAdapter(adapter);

        // Inform everything about the change.
        adapter.notifyDataSetChanged();
        scanned.invalidateViews();
        scanned.refreshDrawableState();
        user_score.setText("Total Score: " + getTotalScore());
    }


    /**
     * Gets the total score of all QR's scanned by the player.
     * @return The score of the player.
     */
    private Long getTotalScore() {
        long score = 0L;
        for (String scanned : player.getClaimedCollectibleIDs()) {
            score += HomeActivity.collectables.get(scanned).getScore();
        }
        return score;
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
        } catch (IllegalArgumentException | WriterException e) {
            return null;
        }

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