package com.example.eclipse;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.eclipse.databinding.FragmentAddMirrorBinding;

public class AddMirrorFragment extends Fragment {

    private FragmentAddMirrorBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentAddMirrorBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.scanningNetwork.setText(R.string.scanning_network);

        //scan network

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                networkScan();
            }
        }, 5000);

        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(AddMirrorFragment.this)
                        .navigate(R.id.action_addMirrorFragment_to_mirrorlistFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void networkScan(){
        int foundDevices = 1; // check network for mirrors
        // change it to 0 for different outcome
        if (foundDevices < 1){
            binding.foundDevices.setVisibility(View.VISIBLE);
            binding.loadingPanel.setVisibility(View.INVISIBLE);
            binding.scanningNetwork.setVisibility(View.INVISIBLE);
        } else {
            binding.loadingPanel.setVisibility(View.INVISIBLE);
            binding.mirrorCard.setVisibility(View.VISIBLE);
            binding.scanningNetwork.setVisibility(View.INVISIBLE);
            binding.foundDevices.setVisibility(View.VISIBLE);
        }
    }
}