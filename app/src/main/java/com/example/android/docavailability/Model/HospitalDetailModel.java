package com.example.android.docavailability.Model;

import android.widget.Switch;

public class HospitalDetailModel {
    private String doctor_image;
    private String Doctor_Name;
    private String Speciality_doctor;
    private String id;

    public HospitalDetailModel(String doctor_image, String doctor_Name, String speciality_doctor, String id, int switch_availability) {
        this.doctor_image = doctor_image;
        Doctor_Name = doctor_Name;
        Speciality_doctor = speciality_doctor;
        this.id = id;
        this.switch_availability = switch_availability;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private int switch_availability;

    public HospitalDetailModel() {}


    public HospitalDetailModel(String doctor_Name, String speciality_doctor,String id) {
        this.Doctor_Name = doctor_Name;
        this.Speciality_doctor = speciality_doctor;
        this.id=id;

    }
    public String getDoctor_image() {
        return doctor_image;
    }

    public void setDoctor_image(String doctor_image) {
        this.doctor_image = doctor_image;
    }

    public String getDoctor_Name() {
        return Doctor_Name;
    }

    public void setDoctor_Name(String doctor_Name) {
        Doctor_Name = doctor_Name;
    }

    public String getSpeciality_doctor() {
        return Speciality_doctor;
    }

    public void setSpeciality_doctor(String speciality_doctor) {
        Speciality_doctor = speciality_doctor;
    }

    public int getSwitch_availability() {
        return switch_availability;
    }

    public void setSwitch_availability(int switch_availability) {
        this.switch_availability = switch_availability;
    }
}
