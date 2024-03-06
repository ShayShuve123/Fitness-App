package com.example.fitnessapp.classes;

public class UserClass {
    private String firstName;
    private String lastName;
    private String userName;
    //private String password;
    private String phone;
    private double height;
    private double weight;
    private int age;
    private String gender;

    // Constructors
    public UserClass() {
        // Default constructor required for Firebase
    }

    public UserClass(String firstName, String lastName, String userName, String phone,
                     double height, double weight, int age, String gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        //this.password = password;
        this.phone = phone;
        this.height = height;
        this.weight = weight;
        this.age = age;
        this.gender = gender;
    }

    // Getters and setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
