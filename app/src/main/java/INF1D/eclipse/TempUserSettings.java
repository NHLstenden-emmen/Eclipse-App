package INF1D.eclipse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import INF1D.eclipse.common.PrefManager;
import INF1D.eclipse.databinding.ActivityMainBinding;
import INF1D.eclipse.databinding.ActivityTempUserSettingsBinding;
import INF1D.eclipse.databinding.ActivityWidgetSettingsBinding;
import INF1D.eclipse.databinding.FragmentSetupLoginBinding;

public class TempUserSettings extends AppCompatActivity {

    private FragmentSetupLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityTempUserSettingsBinding binding = ActivityTempUserSettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        PrefManager prefManager = new PrefManager(this);
        binding.userTokenTextview.setText(prefManager.getUserToken());

        binding.LogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefManager prefManager = new PrefManager(getApplicationContext());
                prefManager.setUserToken(null);
                binding.userTokenTextview.setText(prefManager.getUserToken());
            }
        });

    }

}