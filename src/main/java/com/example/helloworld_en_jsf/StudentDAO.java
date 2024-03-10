package com.example.helloworld_en_jsf;

import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.example.helloworld_en_jsf.DatabaseConnection.getConnection;

public class StudentDAO implements DAO {
    @Override
    public boolean addStudent(Student student) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        ServletContext servletContext = (ServletContext) externalContext.getContext();
        ServletContextEvent servletContextEvent = new ServletContextEvent(servletContext);

        boolean saved = false;
        PreparedStatement ps = null;

        try {
            Connection con = getConnection();
            if (con != null) {
                String fullName = student.getFullName();
                String sex = student.getSex();
                LocalDate birthday =  student.getBirthday();
                String email = student.getEmail(); // New parameter

                if (fullName != null && !fullName.isEmpty() && sex != null && !sex.isEmpty() && birthday != null && email != null) {
                    ps = con.prepareStatement("INSERT INTO etudiant (nom, sexe, date_de_naissance, email) VALUES (?, ?, ?, ?)");
                    ps.setString(1, fullName);
                    ps.setString(2, sex);
                    ps.setDate(3, java.sql.Date.valueOf(birthday));
                    ps.setString(4, email); // Set email in prepared statement

                    int rowsAffected = ps.executeUpdate();
                    saved = rowsAffected > 0;
                }
                ps.close();
            }else {
                System.out.println("La connexion à la base de données est nulle.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (getConnection() != null && getConnection().isClosed()) {
                    // Appeler contextDestroyed
                    ServletContextEvent contextEvent = new ServletContextEvent(servletContext);
                    DatabaseConnection databaseConnection = new DatabaseConnection();
                    databaseConnection.contextDestroyed(contextEvent);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return saved;
    }





    @Override
    public boolean searchStudentbyEmail(String email){

        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        ServletContext servletContext = (ServletContext) externalContext.getContext();
        ServletContextEvent servletContextEvent = new ServletContextEvent(servletContext);

        boolean unique = true;
        PreparedStatement ps;

        try {
            Connection con = getConnection();
            if (con != null) {
                ps = con.prepareStatement("SELECT COUNT(*) FROM etudiant WHERE email = ?");
                ps.setString(1, email);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int count = rs.getInt(1);
                    if (count > 0) {
                        unique = false;
                    }
                }
                rs.close();
                ps.close();
            } else {
                System.out.println("La connexion à la base de données est nulle.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (getConnection() != null && getConnection().isClosed()) {
                    // Appeler contextDestroyed
                    ServletContextEvent contextEvent = new ServletContextEvent(servletContext);
                    DatabaseConnection databaseConnection = new DatabaseConnection();
                    databaseConnection.contextDestroyed(contextEvent);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return unique;

    }


    @Override
    public int searchIdStudentbyEmail(String email){

        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        ServletContext servletContext = (ServletContext) externalContext.getContext();
        ServletContextEvent servletContextEvent = new ServletContextEvent(servletContext);

        int id =0;
        PreparedStatement ps;

        try {
            Connection con = getConnection();
            if (con != null) {
                ps = con.prepareStatement("SELECT id FROM etudiant WHERE email = ?");
                ps.setString(1, email);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    id = rs.getInt(1);
                }
                rs.close();
                ps.close();
            } else {
                System.out.println("La connexion à la base de données est nulle.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (getConnection() != null && getConnection().isClosed()) {
                    // Appeler contextDestroyed
                    ServletContextEvent contextEvent = new ServletContextEvent(servletContext);
                    DatabaseConnection databaseConnection = new DatabaseConnection();
                    databaseConnection.contextDestroyed(contextEvent);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return id;

    }



    @Override
    public List<Student> getAllStudents() {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        ServletContext servletContext = (ServletContext) externalContext.getContext();
        ServletContextEvent servletContextEvent = new ServletContextEvent(servletContext);

        List<Student> etudiants = new ArrayList<>();
        try {
            Connection con = getConnection();
            if (con != null) {
                PreparedStatement ps = con.prepareStatement("SELECT * FROM etudiant");
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String nom = rs.getString("nom");
                    String sexe = rs.getString("sexe");


                    LocalDate dateDeNaissance = rs.getDate("date_de_naissance").toLocalDate();


                    String email = rs.getString("email");

                    Student etudiant = new Student(id, nom, dateDeNaissance, sexe,email);
                    etudiants.add(etudiant);
                }
                rs.close();
                ps.close();
            } else {
                System.out.println("La connexion à la base de données est nulle.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (getConnection() != null && getConnection().isClosed()) {
                    // Appeler contextDestroyed
                    ServletContextEvent contextEvent = new ServletContextEvent(servletContext);
                    DatabaseConnection databaseConnection = new DatabaseConnection();
                    databaseConnection.contextDestroyed(contextEvent);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return etudiants;
    }

    @Override
    public boolean updateStudent(Student student) {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        ServletContext servletContext = (ServletContext) externalContext.getContext();
        ServletContextEvent servletContextEvent = new ServletContextEvent(servletContext);

        boolean modified = false;
        PreparedStatement ps = null;
        try {
            Connection conn = getConnection();
            if ( conn != null ) {
                ps = conn.prepareStatement("UPDATE etudiant SET nom=?, sexe=?, date_de_naissance=? ,email=? WHERE id=?");
                ps.setString(1, student.getFullName());
                ps.setString(2, student.getSex());
                LocalDate birthday = student.getBirthday();
                java.sql.Date sqlBirthday = java.sql.Date.valueOf(birthday);
                ps.setDate(3, sqlBirthday);
                ps.setString(4, student.getEmail());
                ps.setInt(5, student.getId());

                int rowsAffected = ps.executeUpdate();
                modified = rowsAffected > 0;

                ps.executeUpdate();
                ps.close();
            }else{
                System.out.println("La connexion à la base de données est nulle.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (getConnection() != null && getConnection().isClosed()) {
                    // Appeler contextDestroyed
                    ServletContextEvent contextEvent = new ServletContextEvent(servletContext);
                    DatabaseConnection databaseConnection = new DatabaseConnection();
                    databaseConnection.contextDestroyed(contextEvent);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return modified;
    }


    @Override
    public void deleteStudent(Student student) {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        ServletContext servletContext = (ServletContext) externalContext.getContext();
        ServletContextEvent servletContextEvent = new ServletContextEvent(servletContext);

        PreparedStatement stmt = null;
        try {
            Connection conn = getConnection();
            if ( conn != null ) {
                String query = "DELETE FROM etudiant WHERE id = ?";
                stmt = conn.prepareStatement(query);
                System.out.println("111111");
                System.out.println(student.getId());
                stmt.setInt(1, student.getId()); ///////
                stmt.executeUpdate();


            } else {
                System.out.println("La connexion à la base de données est nulle.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (getConnection() != null && getConnection().isClosed()) {
                    // Appeler contextDestroyed
                    ServletContextEvent contextEvent = new ServletContextEvent(servletContext);
                    DatabaseConnection databaseConnection = new DatabaseConnection();
                    databaseConnection.contextDestroyed(contextEvent);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}