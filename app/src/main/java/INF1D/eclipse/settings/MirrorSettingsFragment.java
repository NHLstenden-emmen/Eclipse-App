package INF1D.eclipse.settings;

import INF1D.eclipse.R;
import INF1D.eclipse.databinding.ActivityMainBinding;
import INF1D.eclipse.discovery.Mirror;
import INF1D.eclipse.settings.widgethandler.WidgetHandlerActivity;
import INF1D.eclipse.settings.widgethandler.WidgetHandlerFragment;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import java.util.Objects;

public class MirrorSettingsFragment extends PreferenceFragmentCompat {

    private Mirror selectedMirror;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectedMirror = (Mirror) getArguments().getSerializable("selectedMirror");
            findPreference("serialnmbr").setTitle(selectedMirror.serialNo);
            findPreference("ip").setTitle(selectedMirror.IP.getHostAddress());
        }
    }


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        findPreference("placement").setOnPreferenceClickListener(preference -> {
            Intent intent = new Intent(getActivity(), WidgetHandlerActivity.class);
            intent.putExtra("selectedMirror", selectedMirror);
            startActivity(intent);
            return true;
        });
    }
}
