// https://tamberg.mit-license.org/

package org.tamberg.myblescannerapp;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.core.app.ActivityCompat;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS = 0;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final long SCAN_PERIOD_MS = 10000;

    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothLeScanner bluetoothLeScanner;
    private Handler handler = new Handler(Looper.getMainLooper());

    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            Log.d(TAG, "onScanResult, result = " + result.getDevice().getAddress());
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.d(TAG, "onScanFailed, errorCode = " + errorCode);
        }
    };

    private void scan() {
        assert (handler != null);
        assert (bluetoothLeScanner != null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "stop scan");
                bluetoothLeScanner.stopScan(scanCallback);
            }
        }, SCAN_PERIOD_MS);
        Log.d(TAG, "start scan");
        bluetoothLeScanner.startScan(scanCallback);
    }

    // ^hue stuff
    private String[] getMissingPermissionsSdk31AndGreater() {
        List<String> permissions = new ArrayList<String>();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.BLUETOOTH_SCAN);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.BLUETOOTH_CONNECT);
        }

        return permissions.toArray(new String[permissions.size()]);
    }

    private void initBlePermissionSdk31AndGreater() {
        if (getMissingPermissionsSdk31AndGreater().length > 0) {
            ActivityCompat.requestPermissions(this, getMissingPermissionsSdk31AndGreater(), REQUEST_PERMISSIONS);
        } else {
            startBleServices();
        }

//        if (getMissingPermissionsSdk31AndGreater().length == 0) {
//            Toast.makeText(this, "Bluetooth scan permission granted", Toast.LENGTH_SHORT).show();
//        }
    }

    private void initPermissions() {
        // if SDK greater than 31, then we need to ask for permissions
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            initBlePermissionSdk31AndGreater();
        } else {
            // init with location permission
            Toast.makeText(this, "System currently not supported", Toast.LENGTH_SHORT).show();
        }
    }
    // end of ^hue stuff

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");

        // Or <uses-feature android:name="android.hardware.bluetooth_le" android:required="true"/>
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "BLE not supported", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "BLE not supported");
            finish();
        }
        Log.d(TAG, "BLE available");

        initPermissions(); // asks the user for permissions
    }

    private void startBleServices() {
        Log.d(TAG, "startBleServices");

        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();


        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            Log.d(TAG, "BLE enabled");

            startBluetoothScan();
        } else {
            Log.d(TAG, "BLE not enabled");
            startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), REQUEST_ENABLE_BT);
        }
    }

    private void startBluetoothScan() {
        Log.d(TAG, "startBluetoothScan");

        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
        scan();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS) { // TODO: check grant result
            Log.d(TAG, "onRequestPermissionsResult " + requestCode);

            startBleServices();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            Log.d(TAG, "onActivityResult " + requestCode + " " + resultCode);
            startBluetoothScan();
        }
    }
}
