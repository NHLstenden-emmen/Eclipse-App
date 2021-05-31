package com.example.eclipse;

<<<<<<< Updated upstream
import androidx.annotation.NonNull;
=======
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

>>>>>>> Stashed changes
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

<<<<<<< Updated upstream
=======
    private AppBarConfiguration appBarConfiguration;
    private ActivityHomescreenBinding binding;
    private WidgetHandler widgetHandler;

>>>>>>> Stashed changes
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);

        TextView resultTextVieuw = findViewById(R.id.SearchResultText);

<<<<<<< Updated upstream
        if(findMirrors()){
            resultTextVieuw.setText("Found devices");
        }else{
            resultTextVieuw.setText("Unable to find any mirror's");
        }
=======
        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_homescreen);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

//        binding.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        widgetHandler = new WidgetHandler(this);
>>>>>>> Stashed changes
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.homescreen_bar, menu);
        return true;
    }

<<<<<<< Updated upstream
    public boolean findMirrors(){
        return true;
    }
=======
    public void cardClick(View view) {
        widgetHandler.click(view);
    }

    public ImageView getImageViewById(int id){
        return findViewById(id);
    }

>>>>>>> Stashed changes


}