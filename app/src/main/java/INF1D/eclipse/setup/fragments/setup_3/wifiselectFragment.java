package INF1D.eclipse.setup.fragments.setup_3;

import INF1D.eclipse.databinding.FragmentSetup3Binding;
import INF1D.eclipse.setup.SetupActivity;
import INF1D.eclipse.setup.adapter.MirrorSetupAdapter;
import INF1D.eclipse.setup.adapter.WifiSetupAdapter;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

public class wifiselectFragment extends Fragment {
    private FragmentSetup3Binding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSetup3Binding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.prevButton.setOnClickListener(v -> ((SetupActivity) Objects.requireNonNull(getActivity())).prevButton());

        RecyclerView recyclerView = binding.mirrorSelectContainer;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new WifiSetupAdapter(getWifiResults(), this));
      //  Objects.requireNonNull(getView()).findViewById(R.id.nextButton).setOnClickListener(v -> ((SetupActivity) Objects.requireNonNull(getActivity())).nextButton());
    }

    public List<ScanResult> getWifiResults() {
        return ((SetupActivity) requireActivity()).getFoundWiFiHotspots();
    }
}
