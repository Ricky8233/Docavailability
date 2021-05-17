package com.example.android.docavailability;

import java.util.List;

public class Doctors
{
    String doctor_photo,doctor_name , doctor_id,doctor_speciality;
    private List<String> members;

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public Doctors()
    {

    }
    public Doctors(String doctor_name, String doctor_id, String doctor_speciality) {
        this.doctor_name = doctor_name;
        this.doctor_id = doctor_id;
        this.doctor_speciality = doctor_speciality;
    }
    public String getDoctor_photo() {
        return doctor_photo;
    }

    public void setDoctor_photo(String doctor_photo) {
        this.doctor_photo = doctor_photo;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getDoctor_speciality() {
        return doctor_speciality;
    }

    public void setDoctor_speciality(String doctor_speciality) {
        this.doctor_speciality = doctor_speciality;
    }
}
