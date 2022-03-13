package com.qrhunter;

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

public class QRViewActivity extends AppCompatActivity {
    private final String TAG = "QRView";
    private FirebaseFirestore db;
    private ArrayList<String> commonPlayers;
    ArrayAdapter<String> commonPlayersAdapter;
    TextView commentsOrPlayers;
    ListView commentsOrPlayersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrview);

        db = FirebaseFirestore.getInstance();
        commonPlayers = new ArrayList<String>();
        commonPlayersAdapter = new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, commonPlayers);

        // list view for QR image (optional)
        ImageView qrImage = findViewById(R.id.qr_image);
        // TODO: set image
        // qrImage.setImageBitmap();

        // list header textView
        commentsOrPlayers = findViewById(R.id.comments_players);

        // comments or player listView
        commentsOrPlayersList = findViewById(R.id.comments_players_list);

        // button and edit text for commenting
        Button commentButton = findViewById(R.id.comment_button);
        EditText commentInput = findViewById(R.id.comment_input);

        Button changeListButton = findViewById(R.id.change_list_button);
        changeListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: change list elements after button click
                if(changeListButton.getText().toString().equals("View Players")) {
                    // change button and text of header of list
                    changeListButton.setText("View Comments");
                    commentsOrPlayers.setText("Players");

                    // DUMMY QRCODE (23567bd73b88b311261673dda0856cca8a88b54bc4e338bc586b4eb8f99f4fad) USED FOR NOW, ACTUAL CODE WILL BE PASSED IN INTENT
                    // show all other players who've scanned the same code
                    db.collection("Players").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    List<String> claimedCollectibleIDs = (List<String>) document.get("claimedCollectibleIDs");
                                    if(claimedCollectibleIDs != null) {
                                        if (claimedCollectibleIDs.contains("23567bd73b88b311261673dda0856cca8a88b54bc4e338bc586b4eb8f99f4fad")) {
                                            commonPlayers.add(document.getId());
                                        }
                                    }
                                }
                                commentsOrPlayersList.setAdapter(commonPlayersAdapter);
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });

                    // make comment section visible
                    commentButton.setVisibility(View.INVISIBLE);
                    commentInput.setVisibility(View.INVISIBLE);
                } else if (changeListButton.getText().toString().equals("View Comments")) {
                    // change button and text of head of list
                    changeListButton.setText("View Players");
                    commentsOrPlayers.setText("Comments");

                    // make comment section visible
                    commentButton.setVisibility(View.VISIBLE);
                    commentInput.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}