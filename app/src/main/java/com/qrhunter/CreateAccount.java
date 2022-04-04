package com.qrhunter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import javax.annotation.Nullable;

/**
 * Fragment that facilitates creating a new account on the MainActivity.
 */
public class CreateAccount extends DialogFragment {
    @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_create_account,container);
    }


    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        EditText username = view.findViewById(R.id.create_username);
        EditText password = view.findViewById(R.id.create_password);
        EditText confirmP = view.findViewById(R.id.confirm_password);
        Button createAcc = view.findViewById(R.id.create_account_button);
        Button cancel = view.findViewById(R.id.cancel_create);

        cancel.setOnClickListener(v -> dismiss());

        createAcc.setOnClickListener(v -> {
            String uName = username.getText().toString();
            String pWord = password.getText().toString();
            String confirmPWord = confirmP.getText().toString();

            // verify user information meets all requirements
            if (!uName.equals("") && !pWord.equals("") && confirmPWord.equals(pWord)){
                PlayerDatabse database = new PlayerDatabse();
                Player referencePlayer = database.getPlayer(uName);

                // makes sure the player does not already exist in the database
                if (referencePlayer == null){
                    database.addPlayer(uName, pWord);
                    MainActivity.toast(getActivity(), "Account Created");
                }

                // if the player does exist, toast the user to tell them the player exists
                else MainActivity.toast(getActivity(), "Player already exists!");
            }

            // toast user to tell them entered information does not meet all requirements
            else MainActivity.toast(getActivity(), "Invalid Field!");
        });
    }
}
