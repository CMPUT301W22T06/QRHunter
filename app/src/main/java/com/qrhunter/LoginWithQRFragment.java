package com.qrhunter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.List;

public class LoginWithQRFragment extends DialogFragment {

    boolean building = false;

    DecoratedBarcodeView scanner;


    public LoginWithQRFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (!scanner.isActivated()) {

                        Log.e("GETRESULT", "is active");

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                scanner.resume();
                            }
                        });
                        break;
                    }else{
                        Log.e("GETRESULT", "not active");
                    }
                }
            }
        }).start();


    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Inflate layout for this fragment
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_login_qr, null);


        // get textViews
        scanner = view.findViewById(R.id.scanner);
        scanner.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {

                String username = result.getText();
                Player thisPlayer = MainActivity.allPlayers.getPlayer(username);
                scanner.pause();
                if (thisPlayer==null){
                    Toast.makeText(getContext(), "try again", Toast.LENGTH_SHORT).show();
                    scanner.resume();
                }else{
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    // passes the player object into the intent
                    intent.putExtra("username",username);
                    startActivity(intent);
                }
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {

            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .create();
    }
}
