package com.qrhunter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays all of the collectibles the user has claimed
 */
public class MyCollectiblesList extends AppCompatActivity {
    private final String TAG = "MyCollectiblesList";
    private FirebaseFirestore db;
    private ArrayList<String> myCollectibles;
    ArrayAdapter<String> myCollectiblesAdapter;
    ListView myCollectiblesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collectibles_list);

        String username = getIntent().getStringExtra("username");

        myCollectiblesList = findViewById(R.id.my_collectibles_list);
        db = FirebaseFirestore.getInstance();
        myCollectibles = new ArrayList<String>();
        myCollectiblesAdapter = new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, myCollectibles);

        // Get list of all of the user's collectibles from firebase and displays it
        DocumentReference docRef = db.collection("Players").document(username);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List<String> temp = (List<String>) document.get("claimedCollectibleIDs");
                        if(temp != null) {
                            for (int i = 0; i < temp.size(); i++) {
                                myCollectibles.add(temp.get(i));
                            }
                        }
                        myCollectiblesList.setAdapter(myCollectiblesAdapter);
                    }
                } else {
                    Log.d(TAG, "Error getting document", task.getException());
                }
            }
        });

        // Brings the user to the QRView activity
        myCollectiblesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(view.getContext(), QRViewActivity.class);
                intent.putExtra("collectableID", myCollectibles.get(i));
                startActivity(intent);
            }
        });
    }
}