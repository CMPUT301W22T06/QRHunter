package com.qrhunter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

/**
 * Dialog Fragment for the filter button in the search menu activity.
 */
public class FilterMenuFragment extends DialogFragment {

    // the fragment initialization parameters
    private static final String FILTER_PARAM1 = "High Scores (Players)";
    private static final String FILTER_PARAM2 = "Highest Score of one QR code (Collectible)";
    private static final String FILTER_PARAM3 = "Total amount of QR codes scanned (Player)";

    public FilterMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Inflate layout for this fragment
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_filter_menu,null);

        // get textViews
        TextView filterTextView1 = (TextView) view.findViewById(R.id.filter_textView_1);
        TextView filterTextView2 = (TextView) view.findViewById(R.id.filter_textView_2);
        TextView filterTextView3 = (TextView) view.findViewById(R.id.filter_textView_3);

        // set textViews
        filterTextView1.setText(FILTER_PARAM1); // high scores
        filterTextView2.setText(FILTER_PARAM2); // highest score of single QR
        filterTextView3.setText(FILTER_PARAM3); // total QRs scanned

        filterTextView1.setOnClickListener(v -> {
            SearchMenuActivity.sortByHighestScore();
            dismiss();
        });

        filterTextView2.setOnClickListener(v -> {

            dismiss();
        });

        filterTextView2.setOnClickListener(v -> {

            dismiss();
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Filter").create();

    }
}