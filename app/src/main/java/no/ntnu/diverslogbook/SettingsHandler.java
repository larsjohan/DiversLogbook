package no.ntnu.diverslogbook;

import android.app.Activity;
import android.content.SharedPreferences;



public class SettingsHandler implements SharedPreferences.OnSharedPreferenceChangeListener {

    private Activity activity;

    public SettingsHandler(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {


        ((MainActivity) this.activity).updateMembersFromPreferences();
    }
}