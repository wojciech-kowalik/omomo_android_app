package pl.visualnet.omomo.ui.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.*;
import pl.visualnet.omomo.R;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onResume() {

        super.onResume();

        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); ++i) {

            Preference preference = getPreferenceScreen().getPreference(i);

            if (preference instanceof PreferenceGroup) {

                PreferenceGroup preferenceGroup = (PreferenceGroup) preference;
                for (int j = 0; j < preferenceGroup.getPreferenceCount(); ++j) {
                    updatePreference(preferenceGroup.getPreference(j));
                }

            } else {
                updatePreference(preference);
            }
        }
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updatePreference(findPreference(key));
    }

    private void updatePreference(Preference preference) {

        if (preference instanceof PreferenceScreen) {

            for (int i = 0; i < ((PreferenceScreen) preference).getPreferenceCount(); ++i) {

                if (((PreferenceScreen) preference).getPreference(i) instanceof EditTextPreference) {

                    EditTextPreference editTextPreference = (EditTextPreference) ((PreferenceScreen) preference).getPreference(i);
                    if (!editTextPreference.getText().isEmpty())
                        editTextPreference.setSummary(editTextPreference.getText());
                }

            }
        }

        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            listPreference.setSummary(listPreference.getEntry());
        }

        if (preference instanceof EditTextPreference) {
            EditTextPreference editTextPreference = (EditTextPreference) preference;

            if (!editTextPreference.getText().isEmpty())
                editTextPreference.setSummary(editTextPreference.getText());
        }

    }

}