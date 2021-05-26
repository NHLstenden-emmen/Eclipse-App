package com.example.eclipse;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.eclipse.databinding.ActivityMirrorBinding;

public class MirrorActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMirrorBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMirrorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_mirror);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        binding.fabMirrorEdit.show();
        binding.fabMirrorEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.fabMirrorEdit.hide();
                Snackbar.make(view, "Edit mirror", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                navController.navigate(R.id.action_repositionWidgetFragment_to_editWidgetFragment);


            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_mirror);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}