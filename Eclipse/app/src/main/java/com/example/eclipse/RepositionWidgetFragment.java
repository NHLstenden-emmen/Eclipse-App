package com.example.eclipse;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.eclipse.databinding.FragmentRepositionWidgetBinding;

public class RepositionWidgetFragment extends Fragment {

    private FragmentRepositionWidgetBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentRepositionWidgetBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.btnMirrorSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(RepositionWidgetFragment.this)
                        //.navigate(R.id.action_repositionWidgetFragment_to_editWidgetFragment);
                        .navigate(R.id.action_repositionWidgetFragment_to_settingsFragment);

            }
        });

        binding.btnEditWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(RepositionWidgetFragment.this)
                        //.navigate(R.id.action_repositionWidgetFragment_to_editWidgetFragment);
                        .navigate(R.id.action_repositionWidgetFragment_to_editWidgetFragment);

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}