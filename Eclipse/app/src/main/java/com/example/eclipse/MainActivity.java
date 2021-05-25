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

        Button button_2 = findViewById(R.id.btn_2);
        Button button_Mirror = findViewById(R.id.btn_Mirror);

        button_2.setText("Naar activity 2");
        button_Mirror.setText("Naar Mirror");

        button_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity2();
            }
        });

        button_Mirror.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityMirror();
            }
        });
    }



    public void openActivity2(){
        Intent intent = new Intent(this, Activity2.class);
        startActivity(intent);
    }

    public void openActivityMirror(){
        Intent intent = new Intent(this, MirrorActivity.class);
        startActivity(intent);
    }
}