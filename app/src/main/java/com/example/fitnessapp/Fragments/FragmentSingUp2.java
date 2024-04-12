package com.example.fitnessapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp.R;
import com.example.fitnessapp.classes.EquipmentAdapter;
import com.example.fitnessapp.classes.UserEquipment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentSingUp2 extends Fragment {

    private RecyclerView recyclerView;
    private EquipmentAdapter adapter;
    private List<UserEquipment> equipmentList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sing_up2, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        equipmentList = new ArrayList<>();
        adapter = new EquipmentAdapter(getContext(), equipmentList);
        recyclerView.setAdapter(adapter);

        // Fetch data from Firebase
        DatabaseReference equipmentRef = FirebaseDatabase.getInstance().getReference().child("equipment");
        equipmentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                equipmentList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String equipmentImgUrl = snapshot.child("eurl").getValue(String.class);
                    String equipmentName = snapshot.child("name").getValue(String.class);
                    UserEquipment equipment = new UserEquipment(equipmentImgUrl, equipmentName);
                    equipmentList.add(equipment);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });

        // Button to finish signup
        Button btnFinish = view.findViewById(R.id.btnfinish);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEquipmentSelected() || isNonEquipmentSelected()) {
                    if (isEquipmentSelected() && isNonEquipmentSelected()) {
                        // Show toast indicating invalid selection
                        Toast.makeText(getContext(), "select at least one equipment/'Non equipment'\n(not both).", Toast.LENGTH_LONG).show();
                    } else {
                        // Proceed to the next fragment
                        saveSelectedEquipmentToFirebase();
                        Navigation.findNavController(v).navigate(R.id.action_fragmentSingUp2_to_fragmentHome12);
                    }
                } else {
                    // Prompt the user to select at least one equipment or choose "Non equipment"
                    Toast.makeText(getContext(), "select\n at least one equipment/'Non equipment'\n(not both). ", Toast.LENGTH_LONG).show();

                }
            }
        });

        return view;
    }

    // Method to check if at least one equipment is selected
    private boolean isEquipmentSelected() {
        for (UserEquipment equipment : equipmentList) {
            if (equipment.isSelected() && !equipment.getEquipmentName().equals("Non equipment")) {
                return true;
            }
        }
        return false;
    }

    // Method to check if "Non equipment" is selected alone
    private boolean isNonEquipmentSelected() {
        for (UserEquipment equipment : equipmentList) {
            if (equipment.isSelected() && equipment.getEquipmentName().equals("Non equipment")) {
                return true;
            }
        }
        return false;
    }

    // Method to save selected equipment to Firebase
    private void saveSelectedEquipmentToFirebase() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

            List<UserEquipment> selectedEquipmentList = adapter.getSelectedEquipmentList();

            userRef.child("availableEquipment").setValue(selectedEquipmentList)
                    .addOnSuccessListener(aVoid -> {
                        // Successfully saved equipment to Firebase
                    })
                    .addOnFailureListener(e -> {
                        // Failed to save equipment to Firebase
                    });
        }
    }
}
