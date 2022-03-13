package com.qrhunter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A fragment to pop up on click of a player in the SearchMenuActivity
 */
public class PlayerCollectiblesFragment extends DialogFragment {

    private Player player;


    public PlayerCollectiblesFragment(Player player) {
        this.player = player;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Inflate layout for this fragment
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_player_collectibles,null);


        // get list view and set it
        ListView collectiblesList = view.findViewById(R.id.collectibles_list);
        ArrayList<String> collectiblesDataList = player.getClaimedCollectibleIDs();
        ArrayAdapter<String> collectibleAdapter = new ArrayAdapter<String>(view.getContext(), R.layout.player_content,collectiblesDataList);
        collectiblesList.setAdapter(collectibleAdapter);

        // set up on click listener
        collectiblesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(view.getContext(), QRViewActivity.class);
                startActivity(intent);
            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle(player.getUsername() + "'s Collectibles").create();
    }
}