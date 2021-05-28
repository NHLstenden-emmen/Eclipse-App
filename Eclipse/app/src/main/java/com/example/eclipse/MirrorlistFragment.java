package com.example.eclipse;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.eclipse.databinding.FragmentMirrorlistBinding;

public class MirrorlistFragment extends Fragment {

    private FragmentMirrorlistBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentMirrorlistBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnAddMirror.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(MirrorlistFragment.this)
                        .navigate(R.id.action_mirrorlistFragment_to_addMirrorFragment);
            }
        });

        binding.btnAccountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(MirrorlistFragment.this)
                        .navigate(R.id.action_mirrorlistFragment_to_accountSettingsFragment);
            }
        });

        binding.mirrorSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(MirrorlistFragment.this)
                        .navigate(R.id.action_mirrorlistFragment_to_settingsFragment);
            }
        });

        binding.mirrorCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(MirrorlistFragment.this)
                        .navigate(R.id.action_mirrorlistFragment_to_repositionWidgetFragment);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}