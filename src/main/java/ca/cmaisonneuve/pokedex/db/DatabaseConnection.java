package ca.cmaisonneuve.pokedex.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:postgresql://localhost:5432/pokedex_db";
    private static final String UTILISATEUR = "postgres";
    private static final String MOT_DE_PASSE = "Hugo";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, UTILISATEUR, MOT_DE_PASSE);
    }
}