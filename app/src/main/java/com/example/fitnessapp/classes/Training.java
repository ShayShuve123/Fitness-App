package com.example.fitnessapp.classes;

import java.util.ArrayList;

public class Training {

    // names like in the firebase
    private  String description;//not in res view
    private  String level;
    private  String minutes;
    private  ArrayList<String> muscles;
    private  String name;
    private  ArrayList<String> necessaryEquipment;
    private  String points;
    private  String recommendedNumberOfTraining;
    private  String turl; //imageResource
    private  String tvideo; //Video Resource,not in res view





    public Training(String description, String level, String minutes, ArrayList<String> muscles, String name, ArrayList<String> necessaryEquipment, String points, String recommendedNumberOfTraining,String turl, String tvideo) {
        this.description = description;
        this.level = level;
        this.minutes = minutes;
        this.muscles = muscles;
        this.name = name;
        this.necessaryEquipment = necessaryEquipment;
        this.points = points;
        this.recommendedNumberOfTraining = recommendedNumberOfTraining;
        this.turl = turl;
        this.tvideo = tvideo;
    }

    public Training() {

    }

    public String getDescription() {
        return description;
    }

    public String getLevel() {
        return level;
    }

    public String getMinutes() {
        return minutes;
    }

    public ArrayList<String> getMuscles() {
        return muscles;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getNecessaryEquipment() {
        return necessaryEquipment;
    }

    public String getPoints() {
        return points;
    }

    public String getRecommendedNumberOfTraining() {
        return recommendedNumberOfTraining;
    }

    public String getTurl() {
        return turl;
    }

    public String getTvideo() {
        return tvideo;
    }


    //No setters,we get them all from firebase and there is no need to change

}
