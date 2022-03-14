package com.qrhunter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class PlayersList extends ArrayAdapter<Player> {
    private ArrayList<Player> players;
    private Context context;

    public PlayersList(SearchMenuActivity context, ArrayList<Player> players) {
        super(context, 0, players);
        this.context = context;
        this.players = players;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup
            parent) {
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.player_score_content, parent,false);
        }

        Player player = players.get(position);

        // set username
        TextView playerUsername = view.findViewById(R.id.player_username);
        playerUsername.setText(player.getUsername());

        // set score
        TextView playerScore = view.findViewById(R.id.player_score);
        playerScore.setText(Long.toString(player.getScoreSum()));

        return view;
    }
}
