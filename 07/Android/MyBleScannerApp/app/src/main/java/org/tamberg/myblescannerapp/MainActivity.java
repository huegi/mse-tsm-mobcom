// https://tamberg.mit-license.org/

package org.tamberg.myblescannerapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.lifecycle.ViewModelProviders;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import org.tamberg.permissions.BlePermissionsActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final long SCAN_PERIOD_MS = 3000;

    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothLeScanner bluetoothLeScanner;
    private Handler handler = new Handler(Looper.getMainLooper());

    ActivityResultLauncher<Intent> enableBluetoothLauncher;

    RecyclerViewViewModel viewModel;
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");

        createEnableBluetoothLauncher();
        registerButtonScanListener();
        setupRecyclerView();

        startBle();
    }

    private void createEnableBluetoothLauncher() {
        enableBluetoothLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Log.d(TAG, "User enabled Bluetooth");
                        startScan();
                    } else {
                        Log.d(TAG, "User did not enable Bluetooth");
                        Toast.makeText(this, "Bluetooth not enabled", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void registerButtonScanListener() {
        Button buttonScan = findViewById(R.id.buttonStartScan);
        buttonScan.setOnClickListener(v -> {
            Log.d(TAG, "buttonScan clicked");
            startScan();
        });
    }

    // View Stuff
    private void setupRecyclerView() {
        Log.d(TAG, "setupRecyclerView");

        recyclerView = findViewById(R.id.scanView);
        ArrayList<ScanResultModel> scanResultsList = new ArrayList<>();
        scanResultsList.add(new ScanResultModel("Device Name", "Device Address"));
        scanResultsList.add(new ScanResultModel("Device Name2", "Device Address"));
        scanResultsList.add(new ScanResultModel("Device Name3", "Device Address"));
        scanResultsList.add(new ScanResultModel("Device Name4", "Device Address"));
        scanResultsList.add(new ScanResultModel("Device Name5", "Device Address"));
        recyclerViewAdapter = new RecyclerViewAdapter(this, scanResultsList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        viewModel = ViewModelProviders.of(this).get(RecyclerViewViewModel.class);
//        viewModel.getUserMutableLiveData().observe(this, scanResultsObserver);
    }

//    Observer<ArrayList<ScanResultModel>> scanResultsObserver = new Observer<ArrayList<ScanResultModel>>() {
//        @Override
//        public void onChanged(ArrayList<ScanResultModel> scanResults) {
//            Log.d(TAG, "scanResults changed");
//            Log.d(TAG, "scanResults: " + scanResults.get(0).getDeviceAddress());
//            recyclerViewAdapter.setScanResults(scanResults);
//        }
//    };

    // BLE Stuff
    @SuppressLint("MissingPermission")
    private void startBle() {
        Log.d(TAG, "startBle");

        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
    }

    private void startScan() {
        Log.d(TAG, "startBluetoothScan");

        // check if bluetooth is enabled
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            enableBluetoothLauncher.launch(enableBtIntent);
        } else {
            if (bluetoothLeScanner == null) {
                bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
            }
            scan();
        }
    }

    @SuppressLint("MissingPermission")
    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            Log.d(TAG, "onScanResult, result = " + result.getDevice().getAddress() + ", " + result.getDevice().getName());
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.d(TAG, "onScanFailed, errorCode = " + errorCode);
        }
    };

    @SuppressLint("MissingPermission")
    private void scan() {
        assert (handler != null);
        assert (bluetoothLeScanner != null);

        handler.postDelayed(() -> {
            Log.d(TAG, "stop scan");
            bluetoothLeScanner.stopScan(scanCallback);
        }, SCAN_PERIOD_MS);

        Log.d(TAG, "start scan");
        bluetoothLeScanner.startScan(scanCallback);
    }
}
