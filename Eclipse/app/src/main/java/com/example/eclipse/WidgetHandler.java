package com.example.eclipse;
import android.graphics.Color;
import android.media.Image;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Iterator;

public class WidgetHandler {

    ArrayList<String> widgets = getWidgetsFromDatabase();
    Homescreen homescreen;
    int selected = -1;

    public WidgetHandler(Homescreen homescreen){
        this.homescreen = homescreen;
    }

    public void click(View sender){

        if(selected == -1){
            System.out.println(getIndexByWidget(sender.getId()) + "Is now selected");
            selected = getIndexByWidget(sender.getId()) - 1;
            homescreen.getCardViewById(getWidgetByIndex(selected + 1)).setAlpha(0.2f);
        }else{
            System.out.println(getIndexByWidget(sender.getId()) + "Is now selected as 2");
            int selected2 = getIndexByWidget(sender.getId()) - 1;
            String temp = widgets.get(selected);
            widgets.set(selected, widgets.get(selected2));
            widgets.set(selected2, temp);
            loadImages();
            homescreen.getCardViewById(getWidgetByIndex(selected + 1)).setAlpha(1.0f);
            selected = -1;
        }

        System.out.println(getIndexByWidget(sender.getId()) + "Is now selected");
    }

    public void loadImages(){
        for(int i =1; i <= 28; i++){
            if(widgets.get(i-1) == ""){
                homescreen.getImageViewById(getWidgetImageByIndex(i)).setAlpha(0.0f);
            }
            else {
                homescreen.getImageViewById(getWidgetImageByIndex(i)).setAlpha(1.0f);
            }
            homescreen.getImageViewById(getWidgetImageByIndex(i)).setImageResource(getImgByWidget(widgets.get(i-1)));
        }
    }

    public ArrayList<String> getWidgetsFromDatabase(){
        ArrayList<String> widgets = new ArrayList<String>();
        widgets.add("");
        widgets.add("");
        widgets.add("");
        widgets.add("");
        widgets.add("");
        widgets.add("");
        widgets.add("");
        widgets.add("");
        widgets.add("");
        widgets.add("");
        widgets.add("");
        widgets.add("");
        widgets.add("");
        widgets.add("");
        widgets.add("");
        widgets.add("");
        widgets.add("");
        widgets.add("");
        widgets.add("");
        widgets.add("");
        widgets.add("");
        widgets.add("");
        widgets.add("");
        widgets.add("");
        widgets.add("");
        widgets.add("");
        widgets.add("");
        widgets.add("");
        return widgets;
    }

    public int getFirstEmpty(){
        int count = 0;
        for(String widget : widgets){
            if(widget == ""){
                return count;
            }
            count++;
        }
        return -1;
    }

    public boolean arrayContains(String widget){
        for(String i : widgets){
            if(i.equals(widget)){
                return true;
            }
        }
        return false;
    }

    public void removeFromArray(String widget){
        for(int i = 0; i <= 27; i++){
            if(widget.equals(widgets.get(i))){
                widgets.set(i, "");
            }
        }
    }


    public int getImgByWidget(String widget){
        switch (widget){
            case "Settings":
                return R.drawable.ic_settings;
            case "Card":
                return R.drawable.ic_card;
            case "Search":
                return R.drawable.ic_search_black_24dp;
            default:
                return R.drawable.ic_empty;
        }
    }

    public int getWidgetImageByIndex(int i){
        switch (i){
            case 1:
                return R.id.h1_v1_img;
            case 2:
                return R.id.h1_v2_img;
            case 3:
                return R.id.h1_v3_img;
            case 4:
                return R.id.h1_v4_img;
            case 5:
                return R.id.h1_v5_img;
            case 6:
                return R.id.h1_v6_img;
            case 7:
                return R.id.h1_v7_img;
            case 8:
                return R.id.h2_v1_img;
            case 9:
                return R.id.h2_v2_img;
            case 10:
                return R.id.h2_v3_img;
            case 11:
                return R.id.h2_v4_img;
            case 12:
                return R.id.h2_v5_img;
            case 13:
                return R.id.h2_v6_img;
            case 14:
                return R.id.h2_v7_img;
            case 15:
                return R.id.h3_v1_img;
            case 16:
                return R.id.h3_v2_img;
            case 17:
                return R.id.h3_v3_img;
            case 18:
                return R.id.h3_v4_img;
            case 19:
                return R.id.h3_v5_img;
            case 20:
                return R.id.h3_v6_img;
            case 21:
                return R.id.h3_v7_img;
            case 22:
                return R.id.h4_v1_img;
            case 23:
                return R.id.h4_v2_img;
            case 24:
                return R.id.h4_v3_img;
            case 25:
                return R.id.h4_v4_img;
            case 26:
                return R.id.h4_v5_img;
            case 27:
                return R.id.h4_v6_img;
            case 28:
                return R.id.h4_v7_img;
            default:
                return R.id.h1_v1_img;
        }
    }

    public int getWidgetByIndex(int i){
        switch (i){
            case 1:
                return R.id.h1_v1;
            case 2:
                return R.id.h1_v2;
            case 3:
                return R.id.h1_v3;
            case 4:
                return R.id.h1_v4;
            case 5:
                return R.id.h1_v5;
            case 6:
                return R.id.h1_v6;
            case 7:
                return R.id.h1_v7;
            case 8:
                return R.id.h2_v1;
            case 9:
                return R.id.h2_v2;
            case 10:
                return R.id.h2_v3;
            case 11:
                return R.id.h2_v4;
            case 12:
                return R.id.h2_v5;
            case 13:
                return R.id.h2_v6;
            case 14:
                return R.id.h2_v7;
            case 15:
                return R.id.h3_v1;
            case 16:
                return R.id.h3_v2;
            case 17:
                return R.id.h3_v3;
            case 18:
                return R.id.h3_v4;
            case 19:
                return R.id.h3_v5;
            case 20:
                return R.id.h3_v6;
            case 21:
                return R.id.h3_v7;
            case 22:
                return R.id.h4_v1;
            case 23:
                return R.id.h4_v2;
            case 24:
                return R.id.h4_v3;
            case 25:
                return R.id.h4_v4;
            case 26:
                return R.id.h4_v5;
            case 27:
                return R.id.h4_v6;
            case 28:
                return R.id.h4_v7;
            default:
                return R.id.h1_v1;
        }
    }

    public int getIndexByWidget(int i){
        switch (i){
            case R.id.h1_v1:
                return 1;
            case R.id.h1_v2:
                return 2;
            case R.id.h1_v3:
                return 3;
            case R.id.h1_v4:
                return 4;
            case R.id.h1_v5:
                return 5;
            case R.id.h1_v6:
                return 6;
            case R.id.h1_v7:
                return 7;
            case R.id.h2_v1:
                return 8;
            case R.id.h2_v2:
                return 9;
            case R.id.h2_v3:
                return 10;
            case R.id.h2_v4:
                return 11;
            case R.id.h2_v5:
                return 12;
            case R.id.h2_v6:
                return 13;
            case R.id.h2_v7:
                return 14;
            case R.id.h3_v1:
                return 15;
            case R.id.h3_v2:
                return 16;
            case R.id.h3_v3:
                return 17;
            case R.id.h3_v4:
                return 18;
            case R.id.h3_v5:
                return 19;
            case R.id.h3_v6:
                return 20;
            case R.id.h3_v7:
                return 21;
            case R.id.h4_v1:
                return 22;
            case R.id.h4_v2:
                return 23;
            case R.id.h4_v3:
                return 24;
            case R.id.h4_v4:
                return 25;
            case R.id.h4_v5:
                return 26;
            case R.id.h4_v6:
                return 27;
            case R.id.h4_v7:
                return 28;
            default:
                return 1;
        }
    }
}
