package com.example.eclipse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);

        button.setText("Naar HomeScreen");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { openHomescreen();}
        });
    }



    public void openHomescreen(){
        Intent intent = new Intent(this, Homescreen.class);
        startActivity(intent);
    }
}