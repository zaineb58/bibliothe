package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class Connexion {
    private static final String URL = "jdbc:mysql://localhost:3306/bibliothe";
    private static final String USER = "root";
    private static final String PASSWORD = "wassou2005";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;//fait une nauveaux cnx a chaque fois et sauvgarde la base
    }
}
