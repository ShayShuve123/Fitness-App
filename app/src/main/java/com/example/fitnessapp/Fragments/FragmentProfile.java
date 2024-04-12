package com.example.fitnessapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.fitnessapp.R;
import com.example.fitnessapp.classes.UserClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FragmentProfile extends Fragment {

    private TextView  textViewFirstName, textViewLastName, textViewEmail, textViewPhone, textViewHeight, textViewWeight, textViewAge, textViewGender, textViewExerciseLevel, textViewWorkoutsPerWeek;
    private Button btnHome1;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public FragmentProfile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());

        // Initialize views
        textViewFirstName = view.findViewById(R.id.textViewFirstName);
        textViewLastName = view.findViewById(R.id.textViewLastName);
        textViewEmail = view.findViewById(R.id.textViewEmail);
        textViewPhone = view.findViewById(R.id.textViewPhone);
        textViewHeight = view.findViewById(R.id.textViewHeight);
        textViewWeight = view.findViewById(R.id.textViewWeight);
        textViewAge = view.findViewById(R.id.textViewAge);
        textViewGender = view.findViewById(R.id.textViewGender);
        textViewExerciseLevel = view.findViewById(R.id.textViewExerciseLevel);
        textViewWorkoutsPerWeek = view.findViewById(R.id.textViewWorkoutsPerWeek);
        btnHome1 = view.findViewById(R.id.btnHome1);

        // Fetch user data from Firebase
        fetchUserData();

        // Set button click listener for back home profile
        btnHome1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_fragmentProfile_to_fragmentHome22);
            }
        });
    }

    private void fetchUserData() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserClass user = snapshot.getValue(UserClass.class);
                if (user != null) {
                    // Update TextViews with user data
                    textViewFirstName.setText("First Name: " + user.getFirstName());
                    textViewLastName.setText("Last Name: " + user.getLastName());
                    textViewEmail.setText("Email: " + user.getUserName());
                    textViewPhone.setText("Phone: " + user.getPhone());
                    textViewHeight.setText("Height: " + user.getHeight() + " cm");
                    textViewWeight.setText("Weight: " + user.getWeight() + " kg");
                    textViewAge.setText("Age: " + user.getAge());
                    textViewGender.setText("Gender: " + user.getGender());
                    textViewExerciseLevel.setText("Exercise Level: " + user.getExerciseLevel());
                    textViewWorkoutsPerWeek.setText("Workouts Per Week: " + user.getWorkoutsPerWeek());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });
    }
}
