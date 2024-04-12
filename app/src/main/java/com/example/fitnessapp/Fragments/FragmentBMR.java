package com.example.fitnessapp.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.fitnessapp.R;

public class FragmentBMR extends Fragment {

    Button btnHome6;
    public FragmentBMR() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_b_m_r, container, false);

        // Initialize UI components
        EditText editTextAge = view.findViewById(R.id.editTextAge);
        EditText editTextWeight = view.findViewById(R.id.editTextWeight);
        EditText editTextHeight = view.findViewById(R.id.editTextHeight);
        RadioGroup radioGroupGender = view.findViewById(R.id.radioGroupGender);
        Button btnCalculate = view.findViewById(R.id.btnCalculate);
        TextView textViewResult = view.findViewById(R.id.textViewResult);
         btnHome6=view.findViewById(R.id.btnHome6);

        // Button click listener
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform BMR calculation
                calculateBMR(editTextAge, editTextWeight, editTextHeight, radioGroupGender, textViewResult);

            }
        });

        btnHome6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_fragmentBMR_to_fragmentCalculators32);
            }
        });

        return view;
    }

    // Method to calculate BMR
    private void calculateBMR(EditText editTextAge, EditText editTextWeight, EditText editTextHeight,
                              RadioGroup radioGroupGender, TextView textViewResult) {
        // Get user input
        String ageStr = editTextAge.getText().toString().trim();
        String weightStr = editTextWeight.getText().toString().trim();
        String heightStr = editTextHeight.getText().toString().trim();
        int genderId = radioGroupGender.getCheckedRadioButtonId();

        // Check if input fields are not empty
        if (!ageStr.isEmpty() && !weightStr.isEmpty() && !heightStr.isEmpty()) {
            // Parse input to appropriate types
            int age = Integer.parseInt(ageStr);
            float weight = Float.parseFloat(weightStr);
            float height = Float.parseFloat(heightStr);

            // Calculate BMR based on user input
            double bmr;
            if (genderId == R.id.radioButtonMale) {
                // Male BMR formula: BMR = 10 * weight (kg) + 6.25 * height (cm) - 5 * age (years) + 5
                bmr = 10 * weight + 6.25 * height - 5 * age + 5;
            } else {
                // Female BMR formula: BMR = 10 * weight (kg) + 6.25 * height (cm) - 5 * age (years) - 161
                bmr = 10 * weight + 6.25 * height - 5 * age - 161;
            }

            // Set the calculated BMR to the textViewResult
            textViewResult.setText(String.format("Your Basal Metabolic Rate (BMR) is: %.2f", bmr));
            textViewResult.setVisibility(View.VISIBLE);
        } else {
            // Show a message if any input field is empty
            textViewResult.setText("Please enter age, weight, and height");
            textViewResult.setVisibility(View.VISIBLE);
        }
    }

}
