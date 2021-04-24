package com.example.sep4_android.views.mainapp;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.sep4_android.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
}