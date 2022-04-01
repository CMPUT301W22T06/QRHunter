package com.qrhunter;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class UserActivity extends AppCompatActivity {
    private static final String TAG = "UserActivity";
    ImageView imgUser;
    RecyclerView userScore;
    Button btnGenerateUserCode;
    Button btnGeneratestatus;
    TextView scoreView;
    Button score_sort;

    Bitmap bitmap;

<<<<<<< Updated upstream
    String user_id = "1";
    String user_status = "unknown";

    Long totalScore;

    static CollectableDatabase database;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    ArrayList<String> indexList;
    ArrayList<Long> scoreList;
    ArrayList<String> locationList;
=======
    ListView scanned;
    UserScannedAdapter adapter;
    TextView user_score;
>>>>>>> Stashed changes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        initData();
        initView();
    }

    private void initData() {
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        indexList = (ArrayList<String>) getIntent().getSerializableExtra("index_list");
        scoreList = (ArrayList<Long>) getIntent().getSerializableExtra("score_list");
        locationList = (ArrayList<String>) getIntent().getSerializableExtra("location_list");

        mAdapter = new MyAdapter(getData(indexList, scoreList, locationList));
        totalScore = getTotalScore(scoreList);

    }

<<<<<<< Updated upstream
    private Long getTotalScore(ArrayList<Long> scoreList) {
        Long ret = 0L;
        for(int i = 0; i < scoreList.size(); i ++) {
            Log.d(TAG, "getTotalScore: " + i);
            ret += scoreList.get(i);
=======
        // Setup the list of adapters


        ArrayList<Collectable> collectables = new ArrayList<>();
        for (String id : player.getClaimedCollectibleIDs()) {
            collectables.add(HomeActivity.collectables.get(id));
>>>>>>> Stashed changes
        }
        return ret;
    }

<<<<<<< Updated upstream
    private void initView() {
        imgUser = findViewById(R.id.user_qr);
        userScore = findViewById(R.id.user_score);
        // TODO: set user's qr code

        btnGeneratestatus = findViewById(R.id.btn_generate_status);
        btnGenerateUserCode = findViewById(R.id.btn_generate_user);
        scoreView = findViewById(R.id.user_total_score);
        score_sort = findViewById(R.id.score_sort);

        mRecyclerView = (RecyclerView) findViewById(R.id.user_score);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


        btnGeneratestatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: handling the generate status qr code
                try {
                    Bitmap bitmap = textToImage(user_id, 300, 300);
                    imgUser.setImageBitmap(bitmap);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
=======
        adapter = new UserScannedAdapter(this,R.layout.item_list_userscanned, collectables);
        scanned.setAdapter(adapter);

        user_score.setText("Total Score: " + getTotalScore());

        // Setup for what happens when a user clicks a code.
        scanned.setOnItemClickListener((parent, v, position, id) -> {
            Collectable selected = HomeActivity.collectables.get(player.getClaimedCollectibleIDs().get(position));

            // Create the popup.
            LayoutInflater layoutInflater = LayoutInflater.from(UserActivity.this);
            View context_view = layoutInflater.inflate(R.layout.context_view, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(UserActivity.this);

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


>>>>>>> Stashed changes
        });

        btnGenerateUserCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: handling the generate user qr code
                try {
                    Bitmap bitmap = textToImage(user_id, 300, 300);
                    imgUser.setImageBitmap(bitmap);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });

        score_sort.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                scoreList.sort(Comparator.naturalOrder());
                indexList = SortList(indexList, scoreList);
                locationList = SortList(locationList, scoreList);
                mAdapter = new MyAdapter(getData(indexList, scoreList, locationList));
                mRecyclerView.setAdapter(mAdapter);
            }
        });

<<<<<<< Updated upstream
        scoreView.setText(String.valueOf(totalScore));
=======
        ArrayList<Collectable> collectables = new ArrayList<>();
        for (String id : player.getClaimedCollectibleIDs()) {
            collectables.add(HomeActivity.collectables.get(id));
        }

        // Setup the adapter.
        adapter = new UserScannedAdapter(this,R.layout.item_list_userscanned, collectables);
        scanned.setAdapter(adapter);
>>>>>>> Stashed changes

    }

<<<<<<< Updated upstream
    private ArrayList<String> SortList(ArrayList<String> currentList, ArrayList<Long> scoreList) {
        ArrayList<String> sorted_list;
        for(int i = 0; i < scoreList.size(); i ++) {
            for(int j = 0; j <scoreList.size(); j ++) {
                if (scoreList.get(j) < scoreList.get(i)) {
                    String temp = currentList.get(i);
                    currentList.set(i, currentList.get(j));
                    currentList.set(j, temp);
                }
            }
=======

    /**
     * Gets the total score of all QR's scanned by the player.
     * @return The score of the player.
     */
    private Long getTotalScore() {
        long score = 0L;
        for (String scanned : player.getClaimedCollectibleIDs()) {
            score += HomeActivity.collectables.get(scanned).getScore();
>>>>>>> Stashed changes
        }
        sorted_list = currentList;
        return sorted_list;
    }


    /**
     * grabing the user's scanned qr codes
     * @return ArrayList
     */
    private ArrayList<String> getData(ArrayList<String> indexList, ArrayList<Long> scoreList, ArrayList<String> locationList) {
        ArrayList<String> data = new ArrayList<>();
        for(int i = 0; i < indexList.size(); i++) {
            data.add("No." + i + "     " + scoreList.get(i) + "     " + locationList.get(i));
        }
        return data;
    }

    private Bitmap textToImage(String text, int width, int height) throws WriterException, NullPointerException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.DATA_MATRIX.QR_CODE,
                    width, height, null);
        } catch (IllegalArgumentException Illegalargumentexception) {
            return null;
        }

        int bitMatrixWidth = bitMatrix.getWidth();
        int bitMatrixHeight = bitMatrix.getHeight();
        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        int colorWhite = 0xFFFFFFFF;
        int colorBlack = 0xFF000000;

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;
            for (int x = 0; x < bitMatrixWidth; x++) {
                pixels[offset + x] = bitMatrix.get(x, y) ? colorBlack : colorWhite;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, width, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }
}