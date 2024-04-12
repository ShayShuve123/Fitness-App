package com.example.fitnessapp.classes;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fitnessapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;



public class TrainingAdapter extends RecyclerView.Adapter<TrainingAdapter.ViewHolder> {

    private Context context;
    private List<Training> trainingList;

    public TrainingAdapter(Context context, List<Training> trainingList) {
        this.context = context;
        this.trainingList = trainingList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_training, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Training training = trainingList.get(position);


        //NEED TO  PASS FROM HER THE URL VIDEO TO FRAGMENT VIDEO
        holder.btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pointsForTraining = Integer.parseInt(training.getPoints());//Points of the current training
                addPointsToCurrentUser(pointsForTraining);

                // Retrieve the video URL associated with the selected training
                String videoUrl = training.getTvideo();
                String description = training.getDescription();
                int minutes = Integer.parseInt(training.getMinutes()); // Minutes of the current training

              // Bundle to pass both video URL and description as arguments
                Bundle bundle = new Bundle();
                bundle.putString("videoUrl", videoUrl);
                bundle.putString("description", description);
                bundle.putInt("minutes", minutes);

                // Navigate to FragmentVideo and pass the bundle as an argument
                Navigation.findNavController(v).navigate(R.id.action_fragmentTraining_to_fragmentVideo, bundle);



            }
        });

        holder.trainingName.setText(training.getName());
        holder.minutes.setText("⏱️ MIN: " + training.getMinutes());
        holder.points.setText("\uD83D\uDCB0points for training: " + training.getPoints());
        holder.textViewEquipment.setText(TextUtils.join(", ", training.getNecessaryEquipment()));
        holder.textViewMuscles.setText(TextUtils.join(", ", training.getMuscles()));
        holder.textViewRecommendedTimes.setText(training.getRecommendedNumberOfTraining());
        holder.textViewLevel.setText(training.getLevel());



        Glide.with(context)
                .load(training.getTurl())
                .placeholder(R.drawable.dumbbells)
                .error(R.drawable.dumbbells)
                .into(holder.imageViewTraining);
    }

    @Override
    public int getItemCount() {
        return trainingList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView trainingName, minutes, points, textViewEquipment, textViewMuscles, textViewRecommendedTimes,textViewLevel;
        ImageView imageViewTraining;
        Button btnGetStarted;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            trainingName = itemView.findViewById(R.id.training_name);
            minutes = itemView.findViewById(R.id.minutes);
            points = itemView.findViewById(R.id.points);
            textViewEquipment = itemView.findViewById(R.id.textView_equipment);
            textViewMuscles = itemView.findViewById(R.id.textView_muscles);
            textViewRecommendedTimes = itemView.findViewById(R.id.textView_recommended_times);
            imageViewTraining = itemView.findViewById(R.id.imageView_training);
            btnGetStarted = itemView.findViewById(R.id.btnGetStarted);
            textViewLevel= itemView.findViewById(R.id.textViewLevel);
        }
    }

    private void addPointsToCurrentUser(int pointsForTraining) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        if (user != null) {
            String userId = user.getUid();
            DatabaseReference userRef = mDatabase.child("users").child(userId);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        UserClass userClass = dataSnapshot.getValue(UserClass.class);
                        if (userClass != null) {
                            int currentPoints = Integer.parseInt(userClass.getMyPoints());
                            int newPoints = currentPoints + pointsForTraining;
                            userClass.setMyPoints(String.valueOf(newPoints));

                            userRef.setValue(userClass)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(context, "Points added successfully!", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(context, "Failed to add points: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }
                    }
                }




                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(context, "Failed to fetch user data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
