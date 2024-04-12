package com.example.fitnessapp.Fragments;

import android.os.Bundle;
import android.os.Handler;
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

public class FragmentHeart extends Fragment {

    private EditText editTextAge, editTextRestingHR;
    private Button btnCalculate,btnHome5;
    private TextView textViewResult, textViewDescription;

    public FragmentHeart() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_heart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        editTextAge = view.findViewById(R.id.editTextAge);
        editTextRestingHR = view.findViewById(R.id.editTextRestingHR);
        btnCalculate = view.findViewById(R.id.btnCalculate);
        textViewResult = view.findViewById(R.id.textViewResult);
        textViewDescription = view.findViewById(R.id.textViewDescription);
        btnHome5=view.findViewById(R.id.btnHome5);

        // Set button click listener
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateHeartHealth();
            }
        });

        // Set button click listener for saving profile
        btnHome5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_fragmentHeart2_to_fragmentCalculators32);
            }
        });

    }

    // Method to calculate heart health
    private void calculateHeartHealth() {
        // Get user input for age and resting heart rate
        String ageStr = editTextAge.getText().toString().trim();
        String restingHRStr = editTextRestingHR.getText().toString().trim();

        // Check if input fields are not empty
        if (!ageStr.isEmpty() && !restingHRStr.isEmpty()) {
            // Convert input to integers
            int age = Integer.parseInt(ageStr);
            int restingHR = Integer.parseInt(restingHRStr);

            // Perform heart health calculation (replace with your calculation logic)
            // Here, we're just displaying the age and resting heart rate as the result
            String result = String.format("Age: %d, Resting HR: %d", age, restingHR);
            textViewResult.setText(result);
            textViewResult.setVisibility(View.VISIBLE);
        } else {
            // Show a message if any input field is empty
            textViewResult.setText("Please enter age and resting heart rate");
            textViewResult.setVisibility(View.VISIBLE);
        }
    }
}
