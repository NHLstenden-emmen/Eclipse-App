package INF1D.eclipse.setup.fragments.setup_3;

import INF1D.eclipse.databinding.FragmentSetup3Binding;
import INF1D.eclipse.setup.SetupActivity;
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
import com.thanosfisherman.wifiutils.WifiUtils;

import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

public class wifiselectFragment extends Fragment {
    private FragmentSetup3Binding binding;
    private WifiSetupAdapter wifiSetupAdapter;
    private RecyclerView recyclerView;
    private Timer timer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wifiSetupAdapter = new WifiSetupAdapter(this);

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                requireActivity().runOnUiThread(() -> WifiUtils.withContext(requireActivity()).scanWifi(this::getResults).start());
            }

            private void getResults(List<ScanResult> scanResults) {
                List<ScanResult> filteredResults =  scanResults
                        .stream()
                        .filter(s -> !s.SSID.isEmpty() && !s.SSID.contains("Eclipse-") && !s.SSID.equals("eduroam"))
                        .collect(Collectors.toList());
                if(getActivity() != null) {
                    (getActivity()).runOnUiThread(() -> updateData(filteredResults));
                }
            }
        }, 0, 5000);
    }

    private void updateData(List<ScanResult> filteredResults) {
        if(getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                recyclerView.removeAllViews();
                wifiSetupAdapter.updateItems(filteredResults);
            });
        }
    }

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

        recyclerView = binding.mirrorSelectContainer;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(wifiSetupAdapter);
    }

    public void cancelSearch() {
        timer.cancel();
    }
}
