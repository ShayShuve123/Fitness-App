package com.example.fitnessapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.fitnessapp.R;

public class FragmentCalculators extends Fragment implements View.OnClickListener {

    public FragmentCalculators() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calculators, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize buttons
        Button btnBMI = view.findViewById(R.id.btnBMI);
        Button btnHeart = view.findViewById(R.id.btnHeart);
        Button btnWeight = view.findViewById(R.id.btnWeight);
        Button btnBMR = view.findViewById(R.id.btnBMR);
        Button btnHome = view.findViewById(R.id.btnHome);

        // Set click listeners
        btnBMI.setOnClickListener(this);
        btnHeart.setOnClickListener(this);
        btnWeight.setOnClickListener(this);
        btnBMR.setOnClickListener(this);
        btnHome.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // Handle button clicks
        if (v.getId() == R.id.btnBMI) {
            // Handle BMI Calculator button click
            Navigation.findNavController(requireView()).navigate(R.id.action_fragmentCalculators3_to_fragmentBMI2);
        } else if (v.getId() == R.id.btnHeart) {
            // Handle Heart Calculator button click
            Navigation.findNavController(requireView()).navigate(R.id.action_fragmentCalculators3_to_fragmentHeart2);
        } else if (v.getId() == R.id.btnWeight) {
            // Handle Weight Calculator button click
            Navigation.findNavController(requireView()).navigate(R.id.action_fragmentCalculators3_to_fragmentWeight);
        } else if (v.getId() == R.id.btnBMR) {
            // Handle BMR/TDEE Calculator button click
            Navigation.findNavController(requireView()).navigate(R.id.action_fragmentCalculators3_to_fragmentBMR);
        } else if (v.getId() == R.id.btnHome) {
            // Handle Home button click
            Navigation.findNavController(requireView()).navigate(R.id.action_fragmentCalculators3_to_fragmentHome22);
        }
    }
}
