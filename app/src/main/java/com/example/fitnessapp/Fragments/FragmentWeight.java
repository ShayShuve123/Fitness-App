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

public class FragmentWeight extends Fragment {

    private EditText editTextWeight, editTextHeight;
    private Button btnCalculate,btnHome4;
    private TextView textViewResult;

    public FragmentWeight() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weight, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        editTextWeight = view.findViewById(R.id.editTextWeight);
        editTextHeight = view.findViewById(R.id.editTextHeight);
        btnCalculate = view.findViewById(R.id.btnCalculate);
        textViewResult = view.findViewById(R.id.textViewResult);
        btnHome4=view.findViewById(R.id.btnHome4);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateWeight();

            }

        });

        btnHome4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_fragmentWeight_to_fragmentCalculators32);
            }
        });



    }

    // Method to calculate ideal weight
    private void calculateWeight() {
        // Get user input for weight and height
        String weightStr = editTextWeight.getText().toString().trim();
        String heightStr = editTextHeight.getText().toString().trim();

        // Check if input fields are not empty
        if (!weightStr.isEmpty() && !heightStr.isEmpty()) {
            // Convert input to floats
            float weight = Float.parseFloat(weightStr);
            float height = Float.parseFloat(heightStr);

            // Calculate ideal weight
            // You can implement your own calculation logic here

            // Display the calculated ideal weight
            float idealWeight = calculateIdealWeight(weight, height);
            textViewResult.setText(String.format("Your ideal weight is: %.2f kg", idealWeight));
            textViewResult.setVisibility(View.VISIBLE);
        } else {
            // Show a message if any input field is empty
            textViewResult.setText("Please enter weight and height");
            textViewResult.setVisibility(View.VISIBLE);


        }
    }

    // Method to calculate ideal weight (example implementation)
    private float calculateIdealWeight(float weight, float height) {
        // Example calculation: Ideal weight = height - 100
        return height - 100;
    }
}
