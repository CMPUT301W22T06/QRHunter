package com.qrhunter;

import static com.qrhunter.HomeActivity.collectables;
import static com.qrhunter.MainActivity.allPlayers;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QRViewActivity extends AppCompatActivity {
    private final String TAG = "QRView";
    private FirebaseFirestore db;
    private ArrayList<String> commonPlayers;
    private ArrayList<String> comments;
    ArrayAdapter<String> commonPlayersAdapter;
    ArrayAdapter<String> commentsAdapter;

    TextView commentsOrPlayers;
    ListView commentsOrPlayersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrview);

        // get collectable
        String collectableID = getIntent().getStringExtra("collectableID");
        Collectable collectable = collectables.get(collectableID);
        Log.e("TEST",collectable.getId());

        // list view for QR image (optional)
        ImageView qrImage = findViewById(R.id.qr_image);
        if(collectable.getPhoto() != null) qrImage.setImageBitmap(collectable.getPhoto());

        // list header textView
        commentsOrPlayers = findViewById(R.id.comments_players);

        // comments or player listView
        commentsOrPlayersList = findViewById(R.id.comments_players_list);

        // button and edit text for commenting
        Button commentButton = findViewById(R.id.comment_button);
        EditText commentInput = findViewById(R.id.comment_input);

        // grab players list from Firestore db
        Map<String,Player> map = allPlayers.getPlayers();
        List<Player> players = new ArrayList<>(map.values());
        Log.e("test",players.toString());
        commonPlayers = new ArrayList<String>();
        for(int i = 0; i<players.size();i++) {
            ArrayList<String> playerCollectibleIDs = players.get(i).getClaimedCollectibleIDs();
            Log.d("player:",players.get(i).getUsername());
            for(int j = 0;j<playerCollectibleIDs.size(); j++) {
                Log.d("collectable",playerCollectibleIDs.get(j));
                Log.d("compared to", collectableID);
                if (playerCollectibleIDs.get(j).equals(collectableID)) {
                    commonPlayers.add(players.get(i).getUsername());
                    break;
                }
            }
        }
        Log.d("test","teeeeeeeeeeeeest");
        // grab comments of the collectable
        db = FirebaseFirestore.getInstance();
        comments = collectable.getComments();

        // adapters
        commonPlayersAdapter = new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, commonPlayers);

        commentsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, comments);
        commentsOrPlayersList.setAdapter(commentsAdapter);


        Button changeListButton = findViewById(R.id.change_list_button);
        changeListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: change list elements after button click
                if(changeListButton.getText().toString().equals("View Players")) {
                    // change button and text of header of list
                    changeListButton.setText("View Comments");
                    commentsOrPlayers.setText("Players");

                    // change list
                    commentsOrPlayersList.setAdapter(commonPlayersAdapter);

                    // make comment section visible
                    commentButton.setVisibility(View.INVISIBLE);
                    commentInput.setVisibility(View.INVISIBLE);
                } else if (changeListButton.getText().toString().equals("View Comments")) {
                    // change button and text of head of list
                    changeListButton.setText("View Players");
                    commentsOrPlayers.setText("Comments");

                    // change list
                    commentsOrPlayersList.setAdapter(commentsAdapter);

                    // make comment section visible
                    commentButton.setVisibility(View.VISIBLE);
                    commentInput.setVisibility(View.VISIBLE);

                }
            }
        });
        // create comment
        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = commentInput.getText().toString();
                if(!comment.equals("")) {
                    collectables.addComment(collectableID,comment);
                    commentsAdapter.notifyDataSetChanged();
                    commentInput.setText("");
                } else {
                    //
                }
            }
        });
    }
}