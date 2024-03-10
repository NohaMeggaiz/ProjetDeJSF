package com.example.helloworld_en_jsf;

import java.util.List;

public interface DAO {
    // Create
    boolean addStudent(Student student);

    // Read
    List<Student> getAllStudents();
    boolean searchStudentbyEmail(String email);
    int searchIdStudentbyEmail(String email);


    //UPDATE
    boolean updateStudent(Student student);


    // Delete
    void deleteStudent(Student student);
}