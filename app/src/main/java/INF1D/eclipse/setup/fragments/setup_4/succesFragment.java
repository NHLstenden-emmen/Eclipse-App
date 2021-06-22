package INF1D.eclipse.setup.fragments.setup_4;

import INF1D.eclipse.R;
import INF1D.eclipse.databinding.FragmentSetup4Binding;
import INF1D.eclipse.setup.SetupActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Objects;

public class succesFragment extends Fragment {
    private FragmentSetup4Binding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSetup4Binding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(getView()).findViewById(R.id.nextButton).setOnClickListener(v -> ((SetupActivity) Objects.requireNonNull(getActivity())).nextButton());
    }
}
