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

/**
 * Custom array adapter for the SearchMenuActivity to display players.
 */
public class PlayersList extends ArrayAdapter<Player> {
    private ArrayList<Player> players;
    private Context context;
    private int displayType;

    public PlayersList(SearchMenuActivity context, ArrayList<Player> players, int displayType) {
        super(context, 0, players);
        this.context = context;
        this.players = players;
        this.displayType = displayType;
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
        if (player.getScoreSum() != null){
            switch (displayType) {
                case 0:
                case 2:
                    playerScore.setText(Long.toString(player.getScoreSum())); break;
                case 1: playerScore.setText(Long.toString(player.getHighestScore())); break;
                case 3: playerScore.setText(Long.toString(player.getTotalCodesScanned())); break;
                default: throw new RuntimeException();
            }
        }

        return view;
    }
}
