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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class FragmentCalendar extends Fragment {

    private TextView textViewIsYourWorkout, textViewDate;
    private List<Integer> workoutDays;

    private DatabaseReference mDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textViewIsYourWorkout = view.findViewById(R.id.textViewIsYourWorkout);
        textViewDate = view.findViewById(R.id.textViewDate);
        Button btnHome11= view.findViewById(R.id.btnHome11);

        workoutDays = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        getUserWorkoutsPerWeek();

        // Pass the current date as an argument when navigating from the previous fragment
        Bundle bundle = new Bundle();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());
        bundle.putString("selectedDate", currentDate);
        setArguments(bundle);

        // Check if arguments contain a date
        Bundle args = getArguments();
        if (args != null && args.containsKey("selectedDate")) {
            String selectedDate = args.getString("selectedDate");
            if (selectedDate != null) {
                textViewDate.setText(selectedDate);
            }
        }


        btnHome11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_fragmentCalendar2_to_fragmentHome22);
            }
        });

    }

    private void setupCalendar(View view) {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        if (workoutDays.contains(dayOfWeek)) {
            textViewIsYourWorkout.setText("It's your workout day!!");
            textViewIsYourWorkout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(v).navigate(R.id.action_fragmentHome22_to_fragmentCalendar2);
                }
            });
        }
    }

    private void getUserWorkoutsPerWeek() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            mDatabase.child("users").child(userId).child("workoutsPerWeek").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String workoutsPerWeekString = dataSnapshot.getValue(String.class);
                        if (workoutsPerWeekString != null) {
                            workoutsPerWeekString = workoutsPerWeekString.trim(); // Trim whitespace
                            int workoutsPerWeek = Integer.parseInt(workoutsPerWeekString);
                            generateWorkoutDays(workoutsPerWeek);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle database error
                }
            });
        }
    }

    private void generateWorkoutDays(int workoutsPerWeek) {
        Random random = new Random();
        for (int i = 0; i < workoutsPerWeek; i++) {
            workoutDays.add(random.nextInt(7) + 1); // Random day of the week (1-7)
        }
        setupCalendar(getView());
    }
}
