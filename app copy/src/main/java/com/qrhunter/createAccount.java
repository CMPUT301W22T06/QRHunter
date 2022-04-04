package com.qrhunter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import javax.annotation.Nullable;

public class createAccount extends DialogFragment {

    public createAccount(){
        // blank constructor
    }

    public static createAccount newInstance(String title){
        createAccount frag = new createAccount();
        Bundle args = new Bundle();
        args.putString("Register An Account", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_create_account,container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        EditText username = view.findViewById(R.id.create_username);
        EditText password = view.findViewById(R.id.create_password);
        EditText confirmP = view.findViewById(R.id.confirm_password);
        Button createAcc = view.findViewById(R.id.create_account_button);
        Button cancel = view.findViewById(R.id.cancel_create);

        cancel.setOnClickListener(v -> {
            dismiss();
        });

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
//                    referencePlayer.setQRcode(Utils.generatePlayerQR(uName));
                    database.addPlayer(uName, pWord);
                    Toast.makeText(getActivity(), "Account Created", Toast.LENGTH_SHORT).show();
                }
                // if the player does exist, toast the user to tell them the player exists
                else {
                    Toast.makeText(getActivity(), "Player already exists!", Toast.LENGTH_SHORT).show();
                }
            }
            // toast user to tell them entered information does not meet all requirements
            else{
                Toast.makeText(getActivity(), "Invalid Field!", Toast.LENGTH_SHORT).show();
            }

        });

    }

}
