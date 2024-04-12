package com.example.fitnessapp.classes;

public class UserEquipment {

    private String equipmentImgUrl; // Changed variable name to match Firebase field
    private String equipmentName;

    private boolean isSelected; // Added field to track selection//for non equipment


    public UserEquipment() {
        // Default constructor required for Firebase
    }

    public UserEquipment(String equipmentImgUrl, String equipmentName) {
        this.equipmentImgUrl = equipmentImgUrl;
        this.equipmentName = equipmentName;
    }

    public String getEquipmentImgUrl() {
        return equipmentImgUrl;
    }


    public String getEquipmentName() {
        return equipmentName;
    }


    //for non equipment
    public boolean isSelected() {
        return isSelected;
    }
    //for non equipment
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

}


