package com.example.eclipse;

import android.os.Bundle;
import androidx.annotation.NonNull;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.eclipse.databinding.ActivityHomescreenBinding;

public class Homescreen extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityHomescreenBinding binding;
    public WidgetHandler widgetHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomescreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_homescreen);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void cardClick(View view) {
        widgetHandler.click(view);
    }

    public ImageView getImageViewById(int id){
        return findViewById(id);
    }
    public CardView getCardViewById(int id) {return findViewById(id);}
    public Switch getSwitchById(int id) {return findViewById(id);}


    public void Settings_Switch(View view) {
        Switch SettingsSwitch = (Switch) findViewById(R.id.SettingsSwitch);
        System.out.println("Checking for switch");
        if(!SettingsSwitch.isChecked()){
            //uncheck action
            System.out.println("Disabeling Settings");
            widgetHandler.removeFromArray("Settings");
        }else{
            //check action
            if(widgetHandler.getFirstEmpty() != -1){
                widgetHandler.widgets.set(widgetHandler.getFirstEmpty(), "Settings");
            }
        }
    }


    //@Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.homescreen_bar, menu);
//        return true;
//    }

}