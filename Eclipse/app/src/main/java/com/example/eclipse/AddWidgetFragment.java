package com.example.eclipse;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;

import java.util.ArrayList;

public class AddWidgetFragment extends Fragment {

    public Homescreen homescreen;

    public AddWidgetFragment() {
        // Required empty public constructor
    }
    public static AddWidgetFragment newInstance(String param1, String param2) {
        AddWidgetFragment fragment = new AddWidgetFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homescreen = ((Homescreen)getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_widget, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
     /*   if(((Homescreen)getActivity()).widgetHandler.arrayContains("Settings")){
            (((Homescreen) getActivity()).getSwitchById(R.id.SettingsSwitch)).setChecked(true);
        } */

        ArrayList<View> views = getALlViews();
        for(View i : views){
            Switch sw = (Switch) i;
           if(homescreen.widgetHandler.arrayContains(sw.getText().toString())){
                homescreen.getSwitchById(i.getId()).setChecked(true);
           }
        }
    }

    public ArrayList<View> getALlViews(){
        ArrayList<View> views = new ArrayList<View>();
        views.add(homescreen.findViewById(R.id.SettingsSwitch));
        views.add(homescreen.findViewById(R.id.cardSWitch));
        views.add(homescreen.findViewById(R.id.searchSwitch));
        return views;
    }
}