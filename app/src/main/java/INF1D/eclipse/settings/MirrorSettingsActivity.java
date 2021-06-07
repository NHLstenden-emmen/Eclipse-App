package INF1D.eclipse.settings;

import INF1D.eclipse.R;
import INF1D.eclipse.discovery.Mirror;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;

public class MirrorSettingsActivity extends FragmentActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null){
            Mirror selectedMirror = (Mirror) getIntent().getSerializableExtra("selectedMirror");
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, newInstance(selectedMirror)).commit();
        }
    }

    public MirrorSettingsFragment newInstance(Mirror selected) {
        MirrorSettingsFragment fragment = new MirrorSettingsFragment();
        Bundle args = new Bundle();
        args.putSerializable("selectedMirror", selected);
        fragment.setArguments(args);

        return fragment;
    }
}
