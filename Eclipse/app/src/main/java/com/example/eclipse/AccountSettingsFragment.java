package com.example.eclipse;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

public class AccountSettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_prefences_account, rootKey);
    }
}