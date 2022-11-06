package org.tamberg.myblescannerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    Context context;
    ArrayList<ScanResultModel> scanResultsList;


    public RecyclerViewAdapter(Context context, ArrayList<ScanResultModel> scanResultsList) {
        this.context = context;
        this.scanResultsList = scanResultsList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating the layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_main_recycler_view_row, parent, false);
        return new RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.MyViewHolder holder, int position) {
        // assigning values to the created rows

        holder.deviceName.setText(scanResultsList.get(position).getDeviceName());
        holder.deviceAddress.setText(scanResultsList.get(position).getDeviceAddress());
    }

    public void setScanResults(ArrayList<ScanResultModel> scanResultsList) {
        this.scanResultsList = scanResultsList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return scanResultsList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        // mapping the layout fields to variables
        TextView deviceName, deviceAddress;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            deviceName = itemView.findViewById(R.id.textViewName);
            deviceAddress = itemView.findViewById(R.id.textViewMac);
        }
    }
}
