package com.example.fitnessapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.fitnessapp.R;

public class FragmentBMI extends Fragment {

    private EditText editTextWeight, editTextHeight;
    private Button btnCalculate,btnHome7;
    private TextView textViewResult, textViewDescription;

    public FragmentBMI() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_b_m_i, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        editTextWeight = view.findViewById(R.id.editTextWeight);
        editTextHeight = view.findViewById(R.id.editTextHeight);
        btnCalculate = view.findViewById(R.id.btnCalculate);
        textViewResult = view.findViewById(R.id.textViewResult);
        textViewDescription = view.findViewById(R.id.textViewDescription);
        btnHome7= view.findViewById(R.id.btnHome7);

        // Set button click listener
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateBMI();
            }
        });

        btnHome7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_fragmentBMI2_to_fragmentCalculators32);
            }
        });
    }

    // Method to calculate BMI
    private void calculateBMI() {
        // Get user input for weight and height
        String weightStr = editTextWeight.getText().toString().trim();
        String heightStr = editTextHeight.getText().toString().trim();

        // Check if input fields are not empty
        if (!weightStr.isEmpty() && !heightStr.isEmpty()) {
            // Convert input to floats
            float weight = Float.parseFloat(weightStr);
            float height = Float.parseFloat(heightStr) / 100; // Convert height from cm to meters

            // Calculate BMI
            float bmi = weight / (height * height);

            // Display the calculated BMI
            String bmiResult = String.format("Your BMI: %.2f", bmi);
            textViewResult.setText(bmiResult);
            textViewResult.setVisibility(View.VISIBLE);
        } else {
            // Show a message if any input field is empty
            textViewResult.setText("Please enter weight and height");
            textViewResult.setVisibility(View.VISIBLE);
        }
    }
}
