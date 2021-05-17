package com.example.android.docavailability;

public class Users {

    String photo,hospital_name , hospital_id,Hospital_address , phone_number,mail_id, password;

    public Users(String photo, String hospital_name, String hospital_id, String hospital_address, String phone_number, String mail_id, String password) {
        this.photo = photo;
        this.hospital_name = hospital_name;
        this.hospital_id = hospital_id;
        Hospital_address = hospital_address;
        this.phone_number = phone_number;
        this.mail_id = mail_id;
        this.password = password;
    }

    //sign up constructor
    public Users(){}
    public Users(String hospital_name, String mail_id, String password) {
        this.hospital_name = hospital_name;
        this.mail_id = mail_id;
        this.password = password;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getHospital_name() {
        return hospital_name;
    }

    public void setHospital_name(String hospital_name) {
        this.hospital_name = hospital_name;
    }

    public String getHospital_id() {
        return hospital_id;
    }

    public void setHospital_id(String hospital_id) {
        this.hospital_id = hospital_id;
    }

    public String getHospital_address() {
        return Hospital_address;
    }

    public void setHospital_address(String hospital_address) {
        Hospital_address = hospital_address;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getMail_id() {
        return mail_id;
    }

    public void setMail_id(String mail_id) {
        this.mail_id = mail_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
