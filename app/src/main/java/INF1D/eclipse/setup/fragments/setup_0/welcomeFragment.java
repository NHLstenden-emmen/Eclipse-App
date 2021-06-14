package INF1D.eclipse.setup.fragments.setup_0;

import INF1D.eclipse.databinding.FragmentSetup0Binding;
import INF1D.eclipse.databinding.FragmentSetup1Binding;
import INF1D.eclipse.setup.SetupActivity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.thanosfisherman.wifiutils.WifiUtils;

import java.util.Objects;

public class welcomeFragment extends Fragment {
    private FragmentSetup0Binding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSetup0Binding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.nextButton.setOnClickListener(v -> {
            WifiManager wifiManager = (WifiManager) Objects.requireNonNull(getContext()).getSystemService(Context.WIFI_SERVICE);
            if (wifiManager.isWifiEnabled()) {
                goToNextPage();
            } else {
                Toast.makeText(getContext(), "Wifi is not enabled!", Toast.LENGTH_SHORT).show();
                WifiUtils.withContext(getContext().getApplicationContext()).enableWifi(welcomeFragment.this::WiFicheckResult);
            }
        });

    }

    private void WiFicheckResult(boolean isSuccess) {
        if (isSuccess) {
            Toast.makeText(getContext(), "WIFI HAS BEEN ENABLED", Toast.LENGTH_SHORT).show();
            goToNextPage();
        }
        else {
            Toast.makeText(getContext(), "COULDN'T ENABLE WIFI", Toast.LENGTH_SHORT).show();
        }
    }
    private void goToNextPage() {
        ((SetupActivity) Objects.requireNonNull(getActivity())).nextButton();
    }
}
