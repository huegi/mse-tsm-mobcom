package org.tamberg.myblescannerapp;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

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
        populateList();
        scanResultsLive.setValue(scanResultsList);
    }

    public void populateList() {
        ScanResultModel scanResultModel = new ScanResultModel("Device 1", "00:00:00:00:00:00");

        scanResultsList.add(scanResultModel);
        scanResultsList.add(scanResultModel);
        scanResultsList.add(scanResultModel);
        scanResultsList.add(scanResultModel);
    }
}
