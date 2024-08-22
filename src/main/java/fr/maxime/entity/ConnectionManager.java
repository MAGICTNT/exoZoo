package fr.maxime.entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    private static Connection connection = null;

    private ConnectionManager() {
        try {
            String url = "jdbc:postgresql://localhost:5432/exoZoo";
            String username = "postgres";
            String password = "2203";
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'établissement de la connexion à la base de données", e);
        }
    }

    public static Connection getConnection() {
        if (connection == null) {
           new ConnectionManager();
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                throw new RuntimeException("Erreur lors de la fermeture de la connexion à la base de données", e);
            }
        }
    }
}
