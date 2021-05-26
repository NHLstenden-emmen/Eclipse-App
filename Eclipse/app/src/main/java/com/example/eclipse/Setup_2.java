package com.example.eclipse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Setup_2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);

        Button temporaryButton = findViewById(R.id.nextButton);

        temporaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNext();
            }
        });
    }

    public void openNext() {
        Intent intent = new Intent(this, Setup_3.class);
        startActivity(intent);
    }
}