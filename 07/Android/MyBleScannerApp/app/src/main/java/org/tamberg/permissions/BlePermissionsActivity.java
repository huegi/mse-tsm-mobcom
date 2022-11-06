package org.tamberg.permissions;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import org.tamberg.myblescannerapp.MainActivity;
import org.tamberg.myblescannerapp.R;

import java.util.ArrayList;
import java.util.List;


public class BlePermissionsActivity extends AppCompatActivity {
    private static final String TAG = BlePermissionsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate ble permission service");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startup_screen); // Here we can have another screen for permissions

        // wait 1 second before starting the BlePermissionsDialog
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getMissingPermissions().length == 0) {
                    startMainActivity();
                } else {
                    DialogFragment dialog = new BlePermissionsDialog();
                    dialog.show(getSupportFragmentManager(), "BlePermissionsDialog");
                }
            }
        }, 1000);
    }

    public void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // get missing permissions for all sdk versions
    public String[] getMissingPermissions() {
        List<String> permissions = new ArrayList<String>();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.BLUETOOTH_SCAN);
            }
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.BLUETOOTH_CONNECT);
            }
        } else { // if SDK less than 31 we have to ask for bluetooth and location permissions
            // nothing
        }
        return permissions.toArray(new String[permissions.size()]);
    }


    public void initPermissions() {
        Log.d(TAG, "initPermissions");
        Log.d(TAG, "missing " + getMissingPermissions().toString());

        ActivityCompat.requestPermissions(this, getMissingPermissions(), 0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 0) { // TODO: check grant result
            // if granted start main activity
            Log.d(TAG, "onRequestPermissionsResult, permissions granted");

            if (getMissingPermissions().length == 0) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else {
                Log.d(TAG, "onRequestPermissionsResult, permissions not granted");
                DialogFragment dialog = new BlePermissionDeniedDialog();
                dialog.show(getSupportFragmentManager(), "BlePermissionDeniedDialog");
            }
        }
    }
}
