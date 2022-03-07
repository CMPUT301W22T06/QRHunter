package com.qrhunter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class QRViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrview);

        // list view for QR image (optional)
        ImageView qrImage = findViewById(R.id.qr_image);
        // TODO: set image
        // qrImage.setImageBitmap();

        // list header textView
        TextView commentsOrPlayers = findViewById(R.id.comments_players);

        // comments or player listView
        ListView commentsOrPlayersList = findViewById(R.id.comments_players_list);

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