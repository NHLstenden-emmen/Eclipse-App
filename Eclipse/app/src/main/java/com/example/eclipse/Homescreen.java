package com.example.eclipse;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

public class Homescreen extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;

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

    public void openSettings(){

    }

}