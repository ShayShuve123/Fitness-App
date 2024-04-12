package com.example.fitnessapp.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.Random;

public class FragmentHome2 extends Fragment {




    private TextView textViewUserName, textLevel, textPoint,textMotivationalSentence;
    private CalendarView calendarView;
    private DatabaseReference userRef;
    private ArrayList<String> motivationalSentences = new ArrayList<>();
    private Button btnBMi1,btnResultForBMR;

    Double heightD,weightD;
    int ageI;
    String heightStr,weightStr ,ageStr,genderId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        textViewUserName = view.findViewById(R.id.textView_user_name);
        textPoint = view.findViewById(R.id.textPoint);
        calendarView = view.findViewById(R.id.calendarView);
        textLevel = view.findViewById(R.id.textLevel);
        textMotivationalSentence = view.findViewById(R.id.textMotivationalSentence);
        btnBMi1=  view.findViewById(R.id.btnBMi1); //ResultForBMI
        btnResultForBMR=  view.findViewById(R.id.btnBMr1);


        // Fetch user details and current points
        fetchUserData();

        // Set calendar listener
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Handle calendar date selection
                onSelectedDayChangeHandle(view, year, month, dayOfMonth);


            }
        });

        // Set button click listeners
        view.findViewById(R.id.btnAboutUs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_fragmentHome22_to_fragmentAbout);
            }
        });

        // Set random motivational sentence
        setRandomMotivationalSentence();

        view.findViewById(R.id.btnRemainder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireView()).navigate(R.id.action_fragmentHome22_to_fragmentNotification);
            }
        });

        view.findViewById(R.id.btnProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_fragmentHome22_to_fragmentProfile);
            }
        });

        view.findViewById(R.id.button_choose_training).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Start Training button click
                Navigation.findNavController(view).navigate(R.id.action_fragmentHome22_to_fragmentTraining);
            }
        });

        view.findViewById(R.id.btnSettings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_fragmentHome22_to_fragmentSettings);
            }
        });

        view.findViewById(R.id.btnLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutConfirmationDialog();
            }
        });

        view.findViewById(R.id.btnCalculator).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_fragmentHome22_to_fragmentCalculators3);
            }
        });
    }

    private void onSelectedDayChangeHandle(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
        // Handle calendar date selection
        Bundle bundle = new Bundle();
        bundle.putInt("year", year);
        bundle.putInt("month", month);
        bundle.putInt("dayOfMonth", dayOfMonth);
        Navigation.findNavController(requireView()).navigate(R.id.action_fragmentHome22_to_fragmentCalendar2, bundle);//any press on the clender will pass us to the page we will navigate
    }


    // Method to fetch user details and current points
    private void fetchUserData() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            userRef = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String firstName = dataSnapshot.child("firstName").getValue(String.class);
                    String lastName = dataSnapshot.child("lastName").getValue(String.class);
                    String level = dataSnapshot.child("exerciseLevel").getValue(String.class);
                    String points = dataSnapshot.child("myPoints").getValue(String.class);

                    //bmi to the screen
                    weightD = dataSnapshot.child("weight").getValue(Double.class);
                    heightD = dataSnapshot.child("height").getValue(Double.class);

                    weightStr = String.valueOf(weightD);
                    heightStr = String.valueOf(heightD);

                    ageI=dataSnapshot.child("age").getValue(Integer.class);
                    ageStr=String.valueOf(ageI);

                    genderId= dataSnapshot.child("gender").getValue(String.class);




                    if (firstName != null && lastName != null) {
                        textViewUserName.setText("Welcome, " + firstName + " " + lastName);
                    }

                    if (points != null) {
                        textPoint.setText("Points: " + points);

                        // Call method to check if points need to be reset
                        checkResetPoints(Integer.parseInt(points));
                    }

                    if (level != null) {
                        textLevel.setText("Level: " + level);
                    }

                    //bmi to the screen
                    if (weightStr != null && heightStr != null) {
                        calculateBMI();
                    }

                    //bmr to the screen
                    if (weightStr != null && heightStr != null ) {
                        calculateBMR();
                    }




                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle database error
                }
            });
        }
    }

    // Method to set a random motivational sentence
    private void setRandomMotivationalSentence() {
        // Add motivational sentences to the list
        motivationalSentences.add("Believe in yourself and all that you are.");
        motivationalSentences.add("Every workout brings you one step closer to your goals.");
        motivationalSentences.add("Push yourself because no one else is going to do it for you.");
        motivationalSentences.add("Believe in your inner strength and keep moving forward");
        motivationalSentences.add("Strive for progress, not perfection.");
        motivationalSentences.add("Your body can stand almost anything. It's your mind that you have to convince.");
        motivationalSentences.add("The only bad workout is the one that didn't happen.");
        motivationalSentences.add("Be stronger than your excuses.");
        motivationalSentences.add("Success is earned, not given. Go out and earn it.");
        motivationalSentences.add("Train like a beast, look like a beauty.");
        motivationalSentences.add("The pain you feel today will be the strength you feel tomorrow.");

        // Get a random index
        Random random = new Random();
        int index = random.nextInt(motivationalSentences.size());

        // Set the random motivational sentence to the TextView below the title
        textMotivationalSentence.setText("\uD83D\uDD09Daily motivational Sentence\uD83D\uDD09\n-----------------------------------\n"+motivationalSentences.get(index));
    }

    // Method to show logout confirmation dialog
    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirm Logout");
        builder.setMessage("Are you sure you want to log out?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User clicked Yes, navigate to FragmentHome12
                Navigation.findNavController(requireView()).navigate(R.id.action_fragmentHome22_to_fragmentHome12);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User clicked No, dismiss the dialog
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    // Method to check if points need to be reset
    private void checkResetPoints(int currentPoints) {
        if (currentPoints >= 20) {
            resetPoints();
        }
    }

    // Method to reset points
// Method to reset points
    private void resetPoints() {
        // Reset points in Firebase
        userRef.child("myPoints").setValue("0")
                .addOnSuccessListener(aVoid -> {
                    // Show success message
                    Toast.makeText(requireContext(), "Points reset successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Show failure message
                    Toast.makeText(requireContext(), "Failed to reset points: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });

        // Show dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_custom_message_win, null);
        builder.setView(dialogView);

        WebView webView = dialogView.findViewById(R.id.webView);
        webView.setBackgroundColor(Color.TRANSPARENT); // Set background color to transparent
        webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null); // Enable hardware acceleration
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.loadUrl("file:///android_res/drawable/win.gif");

        TextView textViewMessage = dialogView.findViewById(R.id.textViewMessage);
        textViewMessage.setText("Congratulations!\n You have reached 500 points and are a winner!");

        AlertDialog dialog = builder.create();

        // Close the dialog after 5 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 7500);

        dialog.show();
    }


    // Method to calculate BMI
    private void calculateBMI() {
        // Get user input for weight and height
         //weightStr /heightStr

        // Check if input fields are not empty
        if (!weightStr.isEmpty() && !heightStr.isEmpty()) {
            // Convert input to floats
            float weight = Float.parseFloat(weightStr);
            float height = Float.parseFloat(heightStr) / 100; // Convert height from cm to meters

            // Calculate BMI
            float bmi = weight / (height * height);

            // Display the calculated BMI
            String bmiResult = String.format("Your BMI: %.2f", bmi);
            btnBMi1.setText(bmiResult);
            btnBMi1.setVisibility(View.VISIBLE);
        } else {
            // Show a message if any input field is empty
            btnBMi1.setText("0");
            btnBMi1.setVisibility(View.VISIBLE);
        }
    }

    // Method to calculate BMR
    private void calculateBMR() {
        // Get user input
        // Check if input fields are not empty
        if (!ageStr.isEmpty() && !weightStr.isEmpty() && !heightStr.isEmpty()) {
            // Parse input to appropriate types
            int age = Integer.parseInt(ageStr);
            float weight = Float.parseFloat(weightStr);
            float height = Float.parseFloat(heightStr);

            // Calculate BMR based on user input
            double bmr;
            if (genderId.equals("Male")) {
                // Male BMR formula: BMR = 10 * weight (kg) + 6.25 * height (cm) - 5 * age (years) + 5
                bmr = 10 * weight + 6.25 * height - 5 * age + 5;
            } else {
                // Female BMR formula: BMR = 10 * weight (kg) + 6.25 * height (cm) - 5 * age (years) - 161
                bmr = 10 * weight + 6.25 * height - 5 * age - 161;
            }

            // Set the calculated BMR to the textViewResult
            String bmrResult = String.format("Your BMR: %.2f", bmr);
            btnResultForBMR.setText(bmrResult);
            btnResultForBMR.setVisibility(View.VISIBLE);
        } else {
            // Show a message if any input field is empty
            btnResultForBMR.setText("0");
            btnResultForBMR.setVisibility(View.VISIBLE);
        }
    }












}
