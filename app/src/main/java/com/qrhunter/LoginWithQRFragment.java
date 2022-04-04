package com.qrhunter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.List;

/**
 * Fragment for logging in with the QR Code on the UserActivity.
 */
public class LoginWithQRFragment extends DialogFragment {
    DecoratedBarcodeView scanner;

    @Override public void onResume() {
        super.onResume();
        new Thread(() -> {
            while (true) {
                if (!scanner.isActivated()) {
                    requireActivity().runOnUiThread(() -> scanner.resume());
                    break;
                }
            }
        }).start();
    }

    @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Inflate layout for this fragment
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_login_qr, null);

        // Setup the scanner
        scanner = view.findViewById(R.id.scanner);
        scanner.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {

                String username = result.getText();
                Player thisPlayer = MainActivity.allPlayers.getPlayer(username);
                scanner.pause();
                if (thisPlayer == null) {
                    Toast.makeText(getContext(), "try again", Toast.LENGTH_SHORT).show();
                    scanner.resume();
                }
                else {
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    // passes the player object into the intent
                    intent.putExtra("username",username);
                    startActivity(intent);
                }
            }
            @Override public void possibleResultPoints(List<ResultPoint> resultPoints) {}
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder.setView(view).create();
    }
}
