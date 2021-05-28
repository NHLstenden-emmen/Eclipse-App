package com.example.eclipse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eclipse.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btn2.setText("Go to Homescreen");
        binding.btnMirror.setText("Go to Mirror");

        binding.btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { openHomescreen();}
        });





    }

    public void openHomescreen(){
        Intent intent = new Intent(this, Homescreen.class);
        startActivity(intent);
    }



}