package com.example.eclipse;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AddWidgetFragment extends Fragment {

    public AddWidgetFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AddWidgetFragment newInstance(String param1, String param2) {
        AddWidgetFragment fragment = new AddWidgetFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


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
        if(((Homescreen)getActivity()).widgetHandler.arrayContains("Settings")){
            (((Homescreen) getActivity()).getSwitchById(R.id.SettingsSwitch)).setChecked(true);
        }



    }
}