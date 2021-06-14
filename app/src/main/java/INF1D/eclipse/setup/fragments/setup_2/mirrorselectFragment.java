package INF1D.eclipse.setup.fragments.setup_2;

import INF1D.eclipse.R;
import INF1D.eclipse.databinding.FragmentSetup2Binding;
import INF1D.eclipse.setup.SetupActivity;
import INF1D.eclipse.setup.adapter.WifiAdapter;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.thanosfisherman.wifiutils.WifiUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class mirrorselectFragment extends Fragment {
    private RecyclerView recyclerView;
    private FragmentSetup2Binding binding;
    public static boolean hasSelected;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSetup2Binding.inflate(inflater, container, false);
        hasSelected = false;
        return binding.getRoot();
    }

    private void getScanResults(@NonNull final List<ScanResult> scanResults) {
        List<ScanResult> filteredResults =  scanResults
                .stream()
                .filter(s -> !s.SSID.isEmpty() && s.SSID.startsWith("Eclipse-"))
                .collect(Collectors.toList());
        recyclerView.setAdapter(new WifiAdapter(filteredResults, getContext()));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        WifiUtils.withContext(Objects.requireNonNull(getContext()).getApplicationContext()).scanWifi(this::getScanResults).start();

        recyclerView = binding.mirrorSelectContainer;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Objects.requireNonNull(getView()).findViewById(R.id.nextButton).setOnClickListener(v -> {
            ((SetupActivity) Objects.requireNonNull(getActivity())).nextButton();
        });
    }

    public static void setSelected() {
        mirrorselectFragment.hasSelected = true;
    }

}
