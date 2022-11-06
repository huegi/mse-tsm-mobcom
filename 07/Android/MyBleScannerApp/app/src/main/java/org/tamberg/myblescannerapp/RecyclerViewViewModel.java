package org.tamberg.myblescannerapp;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;

public class RecyclerViewViewModel extends ViewModel {
    private static final String TAG = RecyclerViewViewModel.class.getSimpleName();

    MutableLiveData<ArrayList<ScanResultModel>> scanResultsLive = new MutableLiveData<>();
    private ArrayList<ScanResultModel> scanResultsList = new ArrayList<>();

    public RecyclerViewViewModel() {
        init();
    }

    public MutableLiveData<ArrayList<ScanResultModel>> getUserMutableLiveData() {
        return scanResultsLive;
    }

    public void init() {
//        populateList();
        scanResultsLive.setValue(scanResultsList);
    }

    public void populateList() {
        ScanResultModel scanResultModel = new ScanResultModel("Device 1", "00:00:00:00:00:00");

        scanResultsList.add(scanResultModel);
        scanResultsList.add(scanResultModel);
        scanResultsList.add(scanResultModel);
        scanResultsList.add(scanResultModel);
    }

    public void addScanResult(ScanResultModel scanResultModel) {
        // check if devices mac address is already in the list
        boolean found = false;
        for (ScanResultModel scanResult : scanResultsList) {
            if (scanResult.getDeviceAddress().equals(scanResultModel.getDeviceAddress())) {
                found = true;
                break;
            }
        }
        if (!found) {
            scanResultsList.add(scanResultModel);
        }

        // sort the list (empty names last)
        scanResultsList.sort((o1, o2) -> {
            if (o1.getDeviceName() == null && o2.getDeviceName() == null) {
                return 0;
            } else if (o1.getDeviceName() == null) {
                return 1;
            } else if (o2.getDeviceName() == null) {
                return -1;
            } else {
                return o1.getDeviceName().compareTo(o2.getDeviceName());
            }
        });


//        // make a copy of all values with empty devicename
//        ArrayList<ScanResultModel> scanResultsListCopy = new ArrayList<>();
//        for (ScanResultModel scanResult : scanResultsList) {
//            if (scanResult.getDeviceName() == null) {
//                scanResultsListCopy.add(scanResult);
//            }
//        }
//
//        // remove all entries without name
//        scanResultsList.removeIf(scanResult -> scanResult.getDeviceName() == null);
//
//        // sort list by device name
//        scanResultsList.sort(Comparator.comparing(ScanResultModel::getDeviceName));
//
//        // add all entries without name to the end of the list
//        scanResultsList.addAll(scanResultsListCopy);


        scanResultsLive.setValue(scanResultsList);
    }
}
