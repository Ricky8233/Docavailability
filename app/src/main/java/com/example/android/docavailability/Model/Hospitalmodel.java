package com.example.android.docavailability.Model;

import android.widget.Switch;
import android.widget.ToggleButton;

public class Hospitalmodel {
    private String Hospital_email_id;
    private String Hospital_Name;
    private String Hospital_phone_number;
    private String Icu_beds;
    private String Hospital_image;
    private String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Hospitalmodel(String Hospital_Name, String Hospital_image, String Hospital_email_id, String Hospital_phone_number, String Icu_beds, String uid) {
        this.Hospital_email_id = Hospital_email_id;
        this.Hospital_Name = Hospital_Name;
        this.Hospital_phone_number = Hospital_phone_number;
        this.Icu_beds = Icu_beds;
        this.Hospital_image = Hospital_image;
        this.uid = uid;
    }

    public String getHospital_image() {
        return Hospital_image;
    }

    public void setHospital_image(String hospital_image) {
        Hospital_image = hospital_image;
    }

    public String getHospital_email_id() {
        return Hospital_email_id;
    }

    public void setHospital_email_id(String hospital_email_id) {
        Hospital_email_id = hospital_email_id;
    }

    public String getHospital_Name() {
        return Hospital_Name;
    }

    public void setHospital_Name(String hospital_Name) {
        Hospital_Name = hospital_Name;
    }

    public String getHospital_phone_number() {
        return Hospital_phone_number;
    }

    public void setHospital_phone_number(String hospital_phone_number) {
        Hospital_phone_number = hospital_phone_number;
    }

    public String getIcu_beds() {
        return Icu_beds;
    }

    public void setIcu_beds(String icu_beds) {
        Icu_beds = icu_beds;
    }

    public Hospitalmodel() {
    }
}
