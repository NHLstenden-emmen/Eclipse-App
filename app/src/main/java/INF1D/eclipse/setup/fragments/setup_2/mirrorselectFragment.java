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

import java.util.*;
import java.util.stream.Collectors;

public class mirrorselectFragment extends Fragment {
    private FragmentSetup2Binding binding;
    private MirrorSetupAdapter mirrorSetupAdapter;
    private RecyclerView recyclerView;
    private Timer timer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mirrorSetupAdapter = new MirrorSetupAdapter(this);

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(getActivity() != null) {
                    getActivity().runOnUiThread(() -> WifiUtils.withContext(getActivity()).scanWifi(this::getResults).start());
                }
            }

            private void getResults(List<ScanResult> scanResults) {
                List<ScanResult> filteredResults =  scanResults
                        .stream()
                        .filter(s -> !s.SSID.isEmpty() && s.SSID.startsWith("Eclipse-"))
                        .collect(Collectors.toList());
                if(getActivity() != null) {
                    (getActivity()).runOnUiThread(() -> updateData(filteredResults));
                }
            }
        }, 0, 5000);
    }

    private void updateData(List<ScanResult> scanResults) {
        requireActivity().runOnUiThread(() -> {
            recyclerView.removeAllViews();
            mirrorSetupAdapter.updateItems(scanResults);
        });
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
        recyclerView = binding.mirrorSelectContainer;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mirrorSetupAdapter);

        binding.prevButton.setOnClickListener(v -> ((SetupActivity) requireActivity()).prevButton());
    }

    public void cancelSearch() {
        timer.cancel();
    }
}
