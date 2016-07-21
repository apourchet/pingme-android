package com.antoinepourchet.pingme;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;


public class AddChannelDialog extends DialogFragment {

    private Listener listener;
    private EditText hostEt;
    private EditText channelIdEt;

    public interface Listener {
        void onPositiveClick(String host, String channelId);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.add_channel_dialog, null);
        hostEt = (EditText) dialogView.findViewById(R.id.add_channel_host);
        hostEt.setText(PersistentDataManager.getHost(getContext()));

        channelIdEt = (EditText) dialogView.findViewById(R.id.add_channel_id);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(dialogView)
                .setTitle("Add Channel")
                // Add action buttons
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (listener != null) {
                            listener.onPositiveClick(hostEt.getText().toString(),
                                    channelIdEt.getText().toString());
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddChannelDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

}
