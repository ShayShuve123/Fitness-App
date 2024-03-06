package com.example.fitnessapp.Fragments;

import android.os.Bundle;
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
    private RadioGroup radioGroupGender;
    private Button buttonSignUp;
    private FirebaseAuth mAuth;

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

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userId = user.getUid();
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId);
                            createUser(databaseReference);
                            Toast.makeText(getContext(), "Register success.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Register failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void createUser(DatabaseReference databaseReference) {
        String firstName = editTextFirstName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim().toLowerCase();
        String password = editTextPassword.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String heightStr = editTextHeight.getText().toString().trim();
        String weightStr = editTextWeight.getText().toString().trim();
        String ageStr = editTextAge.getText().toString().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() ||
                phone.isEmpty() || heightStr.isEmpty() || weightStr.isEmpty() || ageStr.isEmpty()) {
            Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (phone.length() != 10) {
            Toast.makeText(getActivity(), "Phone number must be 10 digits", Toast.LENGTH_SHORT).show();
            return;
        }

        double height = Double.parseDouble(heightStr);
        double weight = Double.parseDouble(weightStr);
        int age = Integer.parseInt(ageStr);

        int selectedGenderId = radioGroupGender.getCheckedRadioButtonId();
        if (selectedGenderId == -1) {
            Toast.makeText(getActivity(), "Please select a gender", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selectedGenderRadioButton = view.findViewById(selectedGenderId);
        String gender = selectedGenderRadioButton.getText().toString();

        String userId = databaseReference.push().getKey();
        UserClass user = new UserClass(firstName, lastName, email, phone, height, weight, age, gender);

        if (userId != null) {
            databaseReference.child(userId).setValue(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "User created successfully", Toast.LENGTH_SHORT).show();
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
