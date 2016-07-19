package com.antoinepourchet.pingme;


import android.support.v4.app.Fragment;

public abstract class TabbedFragment extends Fragment {
    public void onFocus() {}
    public void onUnfocus() {}
    public abstract String getTabTitle();
}
