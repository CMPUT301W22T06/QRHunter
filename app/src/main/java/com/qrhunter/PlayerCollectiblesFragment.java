package com.qrhunter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

/**
 * A fragment to pop up on click of a player in the SearchMenuActivity
 */
public class PlayerCollectiblesFragment extends DialogFragment {

    private Player player;


    public PlayerCollectiblesFragment(Player player) {
        this.player = player;
    }


    @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Inflate layout for this fragment
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_player_collectibles,null);

        // get list view and set it
        ListView collectiblesList = view.findViewById(R.id.collectibles_list);
        ArrayList<String> collectiblesDataList = new ArrayList<>();
        for (String id : player.getClaimedCollectibleIDs())
            collectiblesDataList.add(MainActivity.collectables.get(id).getName());

        ArrayAdapter<String> collectibleAdapter = new ArrayAdapter<>(view.getContext(), R.layout.player_content, collectiblesDataList);
        collectiblesList.setAdapter(collectibleAdapter);

        // set up on click listener
        collectiblesList.setOnItemClickListener((adapterView, view1, i, l) -> {
            Intent intent = new Intent(view1.getContext(), QRViewActivity.class);
            intent.putExtra("collectableID", player.getClaimedCollectibleIDs().get(i));
            startActivity(intent);
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle(player.getUsername() + "'s Collectibles").create();
    }
}