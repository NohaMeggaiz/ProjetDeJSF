package com.example.helloworld_en_jsf;

import jakarta.annotation.ManagedBean;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

@ManagedBean
@ViewScoped
public class StudentListBean implements Serializable {
    private List<Student> allStudents;
    private StudentDAO studentDAO;
    private boolean addStudentForm;
    private Student newStudent;
    private List<Student> editingStudents;


    @PostConstruct
    public void init() {
        allStudents = new ArrayList<>();
        studentDAO = new StudentDAO();
        addStudentForm = false;
        newStudent = new Student();
        editingStudents = new ArrayList<>();
    }




    public Student getNewStudent() {
        return newStudent;
    }
    public void setNewStudent(Student newStudent) {
        this.newStudent = newStudent;
    }




    public List<Student> getAllStudents() {
        if (allStudents.isEmpty()) {
            allStudents = studentDAO.getAllStudents();
        }
        return allStudents;
    }


    public void addStudent(){
        boolean allValidationsCorrect = true;

        if (!Validations.validateFullName(newStudent.getFullName())) {
            showMessage("Format du full name incorrect ");
            allValidationsCorrect = false;
        }

        if (!Validations.validateEmail(newStudent.getEmail())) {
            showMessage("Format de l'Email incorrect ");
            allValidationsCorrect = false;
        }

        if (!studentDAO.searchStudentbyEmail(newStudent.getEmail())) {
            showMessage("Email déjà utilisé ");
            allValidationsCorrect = false;
        }

        if (allValidationsCorrect == false) {
            return;
        }

        boolean saved = studentDAO.addStudent(newStudent);

        if (saved) {
            addStudentForm = false;
            allStudents = studentDAO.getAllStudents();
            showMessage("student added with success");
            newStudent = new Student();
        }else{
            showMessage("Echec lors de l ajout");
        }
    }

    public static void showMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, message, null));
    }


    public boolean getAddStudentForm(){
        return addStudentForm;
    }
    public boolean isShowAddStudentForm(){
        return addStudentForm;
    }
    public void showAddStudentForm(){
        addStudentForm = true;
    }


    public void deleteStudent(Student student) {
        System.out.println("2222222");
        try {
            studentDAO.deleteStudent(student);
            allStudents = studentDAO.getAllStudents();
            showMessage("student deleted with success");
        } catch (Exception e) {
            showMessage("student not deleted");
        }
    }


    public void toggleEditing(Student student) {
        student.setEditing(!student.isEditing());
    }
    public void addEditing(Student student) {
        if (!editingStudents.contains(student)) {
            editingStudents.add(student); // Active le mode édition pour la ligne
            toggleEditing(student);
        }
    }

    public void saveChanges() {
        for (Student student : editingStudents){
            boolean allValidationsCorrect = true;

            if (!Validations.validateFullName(student.getFullName())) {
                showMessage("Format du full name incorrect ");
                allValidationsCorrect = false;
            }

            if (!Validations.validateEmail(student.getEmail())) {
                showMessage("Format de l'Email incorrect ");
                allValidationsCorrect = false;
            }

            if (!studentDAO.searchStudentbyEmail(student.getEmail())) {
                if ( student.getId() != studentDAO.searchIdStudentbyEmail(student.getEmail())) {
                    showMessage("Email déjà utilisé ");
                    allValidationsCorrect = false;
                }
            }

            if (allValidationsCorrect == false) {
                return;
            }

            boolean modified = studentDAO.updateStudent(student);

            if (modified) {
                showMessage("student "+ student.getFullName() +" modified with success");
            }else{
                showMessage("Echec lors de la modification de l eleve " + student.getFullName());
            }
            toggleEditing(student);
        }
        allStudents = studentDAO.getAllStudents();
        editingStudents = new ArrayList<>();
    }
}