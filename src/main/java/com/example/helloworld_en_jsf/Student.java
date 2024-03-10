package com.example.helloworld_en_jsf;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Student {

    private int id;
    private String fullName;
    private LocalDate birthday;
    private String sex;
    private String email;
    private boolean editing = false;


    public Student(int id, String fullName, LocalDate birthday, String sex, String email) {
        this.id = id;
        this.fullName = fullName;
        this.birthday = birthday;
        this.sex = sex;
        this.email = email;
    }
    public Student() {
    }


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }



    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }



    public LocalDate getBirthday() {
        return birthday;
    }
    public void setBirthday(LocalDate birthday) {
            this.birthday = birthday;
    }



    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }



    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }



    public void setEditing(boolean editing) {
        this.editing = editing;
    }
    public boolean isEditing() {
        return editing;
    }
}
