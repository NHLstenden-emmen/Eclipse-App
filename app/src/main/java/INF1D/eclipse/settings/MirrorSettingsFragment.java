package INF1D.eclipse.settings;

import INF1D.eclipse.R;
import INF1D.eclipse.common.Mirror;
import INF1D.eclipse.settings.widgethandler.WidgetHandlerActivity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.HashMap;

public class MirrorSettingsFragment extends PreferenceFragmentCompat {

    private Mirror selectedMirror;
    private HashMap<Integer, String> userSettingsAPI = new HashMap<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectedMirror = (Mirror) getArguments().getSerializable("selectedMirror");
            userSettingsAPI = (HashMap<Integer, String>) getArguments().getSerializable("userSettings");
        }
    }


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        Preference pref = findPreference("placement");
        if(pref != null) {
            pref.setOnPreferenceClickListener(preference -> {
                Intent intent = new Intent(getActivity(), WidgetHandlerActivity.class);
                intent.putExtra("selectedMirror", selectedMirror);
                intent.putExtra("userSettings", userSettingsAPI);
                startActivity(intent);
                return true;
            });
        }
    }
}
