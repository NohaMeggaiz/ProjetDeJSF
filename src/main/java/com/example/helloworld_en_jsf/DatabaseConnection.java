/* NOTE
ce code initialise la connexion a la base de donnes avant
d executer les requetes du DAO
il contient l intialisation qu'on a défini dans web.xml >> contextInitalized
et la methode >> contextDestroyed pour fermer la base de données lorsqu'on finit
 */

package com.example.helloworld_en_jsf;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.ServletException;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnection implements ServletContextListener {
    private static Connection connection;


    public void contextInitialized(ServletContextEvent sce) {
        String pilote = sce.getServletContext().getInitParameter("jdbc.Driver");
        String basedonnees = sce.getServletContext().getInitParameter("localisation");

        try{
            Class.forName(pilote);
            connection = DriverManager.getConnection(basedonnees,"avnadmin","AVNS_QLsH-HDQspiiUVQxlJp");

        }catch (ClassNotFoundException e){
            log(Level.SEVERE,"Driver BD non trouve",e);
            throw new RuntimeException("Driver BD non trouve",e);

        }
        catch(SQLException e){
            log(Level.SEVERE,"Erreur lors de la connexion à la base de données", e);
            throw new RuntimeException("Erreur lors de la connexion à la base de données",e);
        }

    }

    public static Connection getConnection() {
        return connection;
    }


    public void contextDestroyed(ServletContextEvent sce) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            log(Level.SEVERE, "Erreur lors de la fermeture de la connexion à la base de données", e);
            throw new RuntimeException("Erreur lors de la fermeture de la connexion à la base de données", e);
        }
    }


    // cette methode pour ajouter des notes dans le log file utilisée dans contextInitialized
    private void log(Level level, String message, Throwable throwable) {
        Logger.getLogger(DatabaseConnection.class.getName()).log(level, message, throwable);
    }
}
