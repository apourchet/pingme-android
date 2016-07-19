package com.antoinepourchet.pingme;

import android.content.Context;
import android.media.audiofx.BassBoost;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;


public class SettingsFragment extends TabbedFragment implements TextView.OnEditorActionListener {

    public static final String TAG = "SettingsFragment";

    private View rootView;
    private FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");

        rootView = inflater.inflate(R.layout.settings_fragment, container, false);
        EditText et = (EditText) rootView.findViewById(R.id.settings_host_edittext);
        et.setText(PersistentDataManager.getHost(getActivity()));
        et.setOnEditorActionListener(this);

        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);

        return rootView;
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        Snackbar.make(rootView, "Settings Saved", Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
        PersistentDataManager.setHost(getActivity(), textView.getText().toString());
        return false;
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        fab.hide();
    }

    @Override
    public void onFocus() {
        if (fab != null) {
            fab.hide();
        }
    }

    @Override
    public String getTabTitle() {
        return "SETTINGS";
    }
}
