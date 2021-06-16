package INF1D.eclipse.setup.fragments.setup_2;

import INF1D.eclipse.databinding.FragmentSetup2Binding;
import INF1D.eclipse.setup.SetupActivity;
import INF1D.eclipse.setup.adapter.MirrorSetupAdapter;
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
import com.thanosfisherman.wifiutils.WifiUtils;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class mirrorselectFragment extends Fragment {
    private FragmentSetup2Binding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void getScanResults(List<ScanResult> scanResults) {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSetup2Binding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        ((SetupActivity) requireActivity()).nextButton();
        RecyclerView recyclerView = binding.mirrorSelectContainer;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new MirrorSetupAdapter(getMirrorResults(), this));

        binding.prevButton.setOnClickListener(v -> ((SetupActivity) requireActivity()).prevButton());
    }

    public List<ScanResult> getMirrorResults() {
        return ((SetupActivity) requireActivity()).getFoundMirrorHotspots();
    }
}
