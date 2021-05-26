package com.example.eclipse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Homescreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);

        TextView resultTextVieuw = findViewById(R.id.SearchResultText);

        if(findMirrors()){
            resultTextVieuw.setText("Found devices");
        }else{
            resultTextVieuw.setText("Unable to find any mirror's");
        }

        Button temporaryButton = findViewById(R.id.addMirror);

        temporaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMirror();
            }
        });
    }

    public void openMirror(){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.homescreen_bar, menu);
        return true;
    }

    public boolean findMirrors(){
        return true;
    }


}