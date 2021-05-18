package com.example.android.docavailability.Model;

import android.widget.Switch;
import android.widget.ToggleButton;

public class HospitalDetailModel {
    private String doctor_image;
    private String Doctor_Name;
    private String Speciality_doctor;
    private String id;
    private Boolean tb;

    public HospitalDetailModel( String doctor_Name, String id,String speciality_doctor,String doctor_image, Boolean tb) {
        this.doctor_image = doctor_image;
        this.Doctor_Name = doctor_Name;
        this.Speciality_doctor = speciality_doctor;
        this.id = id;
        this.tb = tb;
    }

    public HospitalDetailModel(String doctor_Name, String speciality_doctor, String id, Boolean tb) {

        this.Doctor_Name = doctor_Name;
        this.Speciality_doctor = speciality_doctor;
        this.id = id;
        this.tb = tb;
    }

    public Boolean getTb() {
        return tb;
    }

    public void setTb(Boolean tb) {
        this.tb = tb;
    }

    public HospitalDetailModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

}
