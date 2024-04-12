package com.example.fitnessapp.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.fitnessapp.R;
import com.example.fitnessapp.classes.UserClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FragmentSignUp extends Fragment {

    private View view;
    private EditText editTextFirstName, editTextLastName, editTextEmail, editTextPassword,
            editTextPhone, editTextHeight, editTextWeight, editTextAge;
    private RadioGroup radioGroupGender,radioGroupExerciseLevel, radioGroupWorkoutsPerWeek;

    private Button buttonSignUp;
    private FirebaseAuth mAuth;

    int selectedGenderId,selectedExerciseLevel,selectedWorkoutsPerWeek;

    private String userId;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        mAuth = FirebaseAuth.getInstance();

        editTextFirstName = view.findViewById(R.id.editTextFirstName);
        editTextLastName = view.findViewById(R.id.editTextLastName);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        editTextPhone = view.findViewById(R.id.editTextPhone);
        editTextHeight = view.findViewById(R.id.editTextHeight);
        editTextWeight = view.findViewById(R.id.editTextWeight);
        editTextAge = view.findViewById(R.id.editTextAge);
        radioGroupGender = view.findViewById(R.id.radioGroupGender);
        radioGroupExerciseLevel = view.findViewById(R.id.radioGroupExerciseLevel);
        radioGroupWorkoutsPerWeek = view.findViewById(R.id.radioGroupWorkoutsPerWeek);
        buttonSignUp = view.findViewById(R.id.btnSignUp);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reg();
            }
        });

        return view;
    }

    public void reg() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Validate user input
        if (!validateInput()) {
            return; // Return if input is not valid
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            //assert user != null;
                            userId = user.getUid();// Use the UID directly
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId);
                            createUser(databaseReference, userId);
                            Toast.makeText(getContext(), "Register Succeeded.", Toast.LENGTH_SHORT).show();
                            // If registration is successful, navigate to another fragment
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Navigation.findNavController(view).navigate(R.id.action_fragmentSignUp_to_fragmentSingUp2);
                                }
                            }, 1000);
                        } else {
                            Toast.makeText(getContext(), "Register failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean validateInput() {
        String firstName = editTextFirstName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim().toLowerCase();
        String password = editTextPassword.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String heightStr = editTextHeight.getText().toString().trim();
        String weightStr = editTextWeight.getText().toString().trim();
        String ageStr = editTextAge.getText().toString().trim();

        // Perform validation checks
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() ||
                phone.isEmpty() || heightStr.isEmpty() || weightStr.isEmpty() || ageStr.isEmpty()) {
            Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (phone.length() != 10) {
            Toast.makeText(getActivity(), "Phone number must be 10 digits", Toast.LENGTH_SHORT).show();
            return false;
        }

        selectedGenderId = radioGroupGender.getCheckedRadioButtonId();
        if (selectedGenderId == -1) {
            Toast.makeText(getActivity(), "Please select a gender", Toast.LENGTH_SHORT).show();
            return false;
        }

        selectedExerciseLevel = radioGroupExerciseLevel.getCheckedRadioButtonId();
        if (selectedExerciseLevel == -1) {
            Toast.makeText(getActivity(), "Please select your exercise level", Toast.LENGTH_SHORT).show();
            return false;
        }

        selectedWorkoutsPerWeek = radioGroupWorkoutsPerWeek.getCheckedRadioButtonId();
        if (selectedWorkoutsPerWeek == -1) {
            Toast.makeText(getActivity(), "Please select number of workouts", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Additional validation checks can be added here

        return true; // Input is valid
    }

    private void createUser(DatabaseReference databaseReference, String userId) {
        //confer that not need to dealer aging
        String firstName = editTextFirstName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim().toLowerCase();
        String phone = editTextPhone.getText().toString().trim();
        String heightStr = editTextHeight.getText().toString().trim();
        String weightStr = editTextWeight.getText().toString().trim();
        String ageStr = editTextAge.getText().toString().trim();

        double height = Double.parseDouble(heightStr);
        double weight = Double.parseDouble(weightStr);
        int age = Integer.parseInt(ageStr);

        RadioButton selectedGenderRadioButton = view.findViewById(selectedGenderId);
        String gender = selectedGenderRadioButton.getText().toString();

        RadioButton selectedExerciseLevelRadioButton = view.findViewById(selectedExerciseLevel);
        String exerciseLevel = selectedExerciseLevelRadioButton.getText().toString();

        RadioButton selectedWorkoutsPerWeekRadioButton = view.findViewById(selectedWorkoutsPerWeek);
        String workoutsPerWeek = selectedWorkoutsPerWeekRadioButton.getText().toString();

        String myPoints="0";

        UserClass user = new UserClass(firstName, lastName, email, phone, height, weight, age, gender,exerciseLevel,workoutsPerWeek,myPoints);
        //check what this do
        if (userId != null) {
            databaseReference.setValue(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //Toast.makeText(getActivity(), "User created successfully", Toast.LENGTH_SHORT).show();//just for us
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Failed to create user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(getActivity(), "Failed to create user", Toast.LENGTH_SHORT).show();
        }
    }
}





