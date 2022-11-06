package org.tamberg.permissions;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class BlePermissionsDialog  extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Permissions needed");
        builder.setMessage("Please accept the following permission dialogs to use this app");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Return to the activity that started this dialog
                ((BlePermissionsActivity) getActivity()).initPermissions();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Return to the activity that started this dialog
                getActivity().finish();
            }
        });

        // Create the AlertDialog object and return it
        return builder.create();
    }
}