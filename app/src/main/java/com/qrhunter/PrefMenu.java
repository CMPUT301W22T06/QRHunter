package com.qrhunter;
//Based off code from CodingWithMitch, https://www.youtube.com/watch?v=--dJm6z5b0s

import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A popup menu which allows owners to tap to change settings.
 */
public class PrefMenu extends DialogFragment {
    private FirebaseFirestore db;
    private static final String TAG = "PrefMenu";
    private boolean storeBigImages;

    private TextView mBigImages, mActionCancel;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // Gets the current image setting to set the option's text.
        db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Preferences").document("ImagePrefs");
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    storeBigImages = (boolean) document.get("bigImages");
                    mBigImages.setText(storeBigImages ? R.string.disable_large_images : R.string.enable_large_images);
                }
            }
            else Log.d(TAG, "Error getting image preferences", task.getException());
        });

        // Will update the setting if there are any realtime changes
        docRef.addSnapshotListener((snapshot, e) -> {
            if (e != null) {Log.w(TAG, "Listen failed.", e); return;}

            storeBigImages = (boolean) snapshot.get("bigImages");
            mBigImages.setText(storeBigImages ? R.string.disable_large_images : R.string.enable_large_images);
        });

        // Show the menu
        View view = inflater.inflate(R.layout.pref_fragment, container, false);
        mActionCancel = view.findViewById(R.id.action_cancel);
        mBigImages = view.findViewById(R.id.bigImages);

        mActionCancel.setOnClickListener(v -> getDialog().dismiss());

        // When the user taps on the setting, updates FireStore database and
        // changes the option from enable to disable & vice versa
        mBigImages.setOnClickListener(v -> {
            DocumentReference docRef1 = db.collection("Preferences").document("ImagePrefs");
            docRef1.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if (!storeBigImages) {
                            mBigImages.setText(R.string.disable_large_images);
                            docRef1.update("bigImages", true);
                        }
                        else {
                            mBigImages.setText(R.string.enable_large_images);
                            docRef1.update("bigImages", false);
                        }
                        Toast.makeText(v.getContext(), R.string.setting_saved, Toast.LENGTH_SHORT).show();
                    }
                }
                else Log.d(TAG, "Error getting document", task.getException());
            });
            getDialog().dismiss();
        });
        return view;
    }
}

















