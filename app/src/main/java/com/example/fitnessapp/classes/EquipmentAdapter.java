package com.example.fitnessapp.classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.fitnessapp.R;
import java.util.ArrayList;
import java.util.List;

public class EquipmentAdapter extends RecyclerView.Adapter<EquipmentAdapter.ViewHolder> {

    private final Context context;
    private final List<UserEquipment> equipmentList;
    private final List<UserEquipment> selectedEquipmentList;

    public EquipmentAdapter(Context context, List<UserEquipment> equipmentList) {
        this.context = context;
        this.equipmentList = equipmentList;
        this.selectedEquipmentList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_equipment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserEquipment equipment = equipmentList.get(position);
        holder.textViewEquipmentName.setText(equipment.getEquipmentName());
        Glide.with(context)
                .load(equipment.getEquipmentImgUrl())
                .error(R.drawable.dumbbells) // Placeholder image in case of error, replaced with dumbbells
                .into(holder.imageViewEquipment);

        // Set CheckBox listener
        holder.checkBoxAdd.setOnCheckedChangeListener(null); // Reset listener to prevent unwanted triggering
        holder.checkBoxAdd.setChecked(equipment.isSelected());

        holder.checkBoxAdd.setOnCheckedChangeListener((buttonView, isChecked) -> {
            equipment.setSelected(isChecked); // Update the isSelected state of UserEquipment
            if (isChecked) {
                selectedEquipmentList.add(equipment);
            } else {
                selectedEquipmentList.remove(equipment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return equipmentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewEquipment;
        TextView textViewEquipmentName;
        CheckBox checkBoxAdd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewEquipment = itemView.findViewById(R.id.imageView);
            textViewEquipmentName = itemView.findViewById(R.id.textViewName);
            checkBoxAdd = itemView.findViewById(R.id.checkBoxAdd);
        }
    }
    
    // Method to get the selected equipment list
    public List<UserEquipment> getSelectedEquipmentList() {
        return selectedEquipmentList;
    }
}
