package com.example.fitnessapp.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.fitnessapp.R;
import com.example.fitnessapp.classes.UserClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class FragmentProfileEdit extends Fragment {

    private EditText editTextFirstName, editTextLastName, editTextPhone, editTextHeight, editTextWeight, editTextAge;
    private Button btnSaveProfile;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private RadioGroup radioGroupExerciseLevel;


    public FragmentProfileEdit() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());

        // Initialize views
        editTextFirstName = view.findViewById(R.id.editTextFirstName);
        editTextLastName = view.findViewById(R.id.editTextLastName);
        editTextPhone = view.findViewById(R.id.editTextPhone);
        editTextHeight = view.findViewById(R.id.editTextHeight);
        editTextWeight = view.findViewById(R.id.editTextWeight);
        editTextAge = view.findViewById(R.id.editTextAge);
        btnSaveProfile = view.findViewById(R.id.btnSaveProfile);

        radioGroupExerciseLevel = view.findViewById(R.id.radioGroupExerciseLevel);

        // Fetch user data from Firebase and populate EditText fields
        fetchUserData();

        // Set button click listener for saving profile
        btnSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Navigation.findNavController(view).navigate(R.id.action_fragmentProfileEdit_to_fragmentHome22);
                    }
                }, 1000);
            }
        });
    }

    private void fetchUserData() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            mDatabase.get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult().exists()) {
                    UserClass userClass = task.getResult().getValue(UserClass.class);
                    if (userClass != null) {
                        editTextFirstName.setText(userClass.getFirstName());
                        editTextLastName.setText(userClass.getLastName());
                        editTextPhone.setText(userClass.getPhone());
                        editTextHeight.setText(String.valueOf(userClass.getHeight()));
                        editTextWeight.setText(String.valueOf(userClass.getWeight()));
                        editTextAge.setText(String.valueOf(userClass.getAge()));

                        // Check the appropriate radio button based on the user's exercise level
                        switch (userClass.getExerciseLevel()) {
                            case "Beginner":
                                radioGroupExerciseLevel.check(R.id.radioBtnBeginner);
                                break;
                            case "Intermediate":
                                radioGroupExerciseLevel.check(R.id.radioBtnIntermediate);
                                break;
                            case "Experienced":
                                radioGroupExerciseLevel.check(R.id.radioBtnExperienced);
                                break;
                            default:
                                // Handle the case when exercise level is not recognized
                                break;
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "Failed to fetch user data.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @SuppressLint("NonConstantResourceId")
    private void saveProfile() {
        String firstName = editTextFirstName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String height = editTextHeight.getText().toString().trim();
        String weight = editTextWeight.getText().toString().trim();
        String age = editTextAge.getText().toString().trim();

        int selectedRadioButtonId = radioGroupExerciseLevel.getCheckedRadioButtonId();
        String exerciseLevel = "";

        if (selectedRadioButtonId == R.id.radioBtnBeginner) {
            exerciseLevel = "Beginner";
        } else if (selectedRadioButtonId == R.id.radioBtnIntermediate) {
            exerciseLevel = "Intermediate";
        } else if (selectedRadioButtonId == R.id.radioBtnExperienced) {
            exerciseLevel = "Experienced";
        } else {
            // Handle the case when no RadioButton is selected
            Toast.makeText(getActivity(), "Please select an exercise level", Toast.LENGTH_SHORT).show();
            return; // Exit the method if no exercise level is selected
        }

        // Check if any required field is empty
        if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(phone)
                || TextUtils.isEmpty(height) || TextUtils.isEmpty(weight) || TextUtils.isEmpty(age)) {
            Toast.makeText(getActivity(), "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the phone number has 10 digits
        if (phone.length() != 10) {
            Toast.makeText(getActivity(), "Phone number must be 10 digits", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            DatabaseReference userRef = mDatabase;
            
            userRef.child("firstName").setValue(firstName);
            userRef.child("lastName").setValue(lastName);
            userRef.child("phone").setValue(phone);
            userRef.child("height").setValue(Double.parseDouble(height));
            userRef.child("weight").setValue(Double.parseDouble(weight));
            userRef.child("age").setValue(Integer.parseInt(age));
            userRef.child("exerciseLevel").setValue(exerciseLevel); // Update exercise level

            Toast.makeText(getActivity(), "✅Profile updated successfully✅", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "User not found", Toast.LENGTH_SHORT).show();
        }
    }

}
