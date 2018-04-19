package no.ntnu.diverslogbook.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import no.ntnu.diverslogbook.LoginActivity;
import no.ntnu.diverslogbook.R;


public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        Preference logoutButton = findPreference(getString(R.string.preferences_key_logout));

        logoutButton.setOnPreferenceClickListener((view) -> {
            Intent logout = new Intent(this.getContext(), LoginActivity.class);
            logout.putExtra("logout", true);
            logout.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(logout);

            return true;
        });
    }
}
