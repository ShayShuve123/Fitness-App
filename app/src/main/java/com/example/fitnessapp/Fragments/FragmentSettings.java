package com.example.fitnessapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.fitnessapp.R;

public class FragmentSettings extends Fragment {

    public FragmentSettings() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Initialize button
        Button btnEditProfile = view.findViewById(R.id.btnEditProfile);
        Button btnEditEquipment = view.findViewById(R.id.btnEditEquipment);
        Button btnHome9 = view.findViewById(R.id.btnHome9);



        // Set click listener for the button to navigate to FragmentProfileEdit
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_fragmentSettings_to_fragmentProfileEdit);
            }
        });

        btnEditEquipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_fragmentSettings_to_fragmentSingUp2);
            }
        });


        btnHome9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_fragmentSettings_to_fragmentHome22);
            }
        });



        return view;
    }
}
