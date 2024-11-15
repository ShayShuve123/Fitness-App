package com.example.fitnessapp.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp.R;
import com.example.fitnessapp.classes.Training;
import com.example.fitnessapp.classes.TrainingAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentTraining extends Fragment {

    private RecyclerView recyclerView;
    private TrainingAdapter adapter;
    private List<Training> trainingList;
    private DatabaseReference userRef;
    
    String userLevel,level;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_training, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fetchUserLevel();
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        trainingList = new ArrayList<>();
        adapter = new TrainingAdapter(getContext(), trainingList);

        recyclerView.setAdapter(adapter);


        // Fetch data from Firebase
        DatabaseReference trainingRef = FirebaseDatabase.getInstance().getReference().child("training");
        trainingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                trainingList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String description = snapshot.child("description").getValue(String.class);
                    level = snapshot.child("level").getValue(String.class);//level traning


                    String minutes = snapshot.child("minutes").getValue(String.class);

                    ArrayList<String> muscles = new ArrayList<>();
                    for (DataSnapshot muscleSnapshot : snapshot.child("muscles").getChildren()) {
                        muscles.add(muscleSnapshot.getValue(String.class));
                    }

                    String name = snapshot.child("name").getValue(String.class);
                    ArrayList<String> necessaryEquipment = new ArrayList<>();
                    for (DataSnapshot equipmentSnapshot : snapshot.child("necessaryEquipment").getChildren()) {
                        necessaryEquipment.add(equipmentSnapshot.getValue(String.class));
                    }

                    String points = snapshot.child("points").getValue(String.class);
                    String recommendedNumberOfTraining = snapshot.child("recommended number of training").getValue(String.class);
                    String turl = snapshot.child("turl").getValue(String.class);
                    String tvideo = snapshot.child("tvideo").getValue(String.class);

                    // Logging the values of level and userLevel for debugging
                    Log.d("FirebaseData", "level: " + level);
                    Log.d("FirebaseData", "userLevel: " + userLevel);


                    if (level.trim().equals(userLevel.trim())) {
                        Training training = new Training(description, level, minutes, muscles, name, necessaryEquipment, points, recommendedNumberOfTraining, turl, tvideo);
                        trainingList.add(training);
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to fetch training data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Set click listener for the button
        Button btnHome2 = view.findViewById(R.id.btnHome2);
        btnHome2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_fragmentTraining_to_fragmentHome22);
            }
        });
    }

    private void fetchUserLevel() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            userRef = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userLevel = dataSnapshot.child("exerciseLevel").getValue(String.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Throwable databaseError = null;
                    Toast.makeText(getContext(), "Failed to fetch training data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
