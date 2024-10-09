package dev.coms4156.project.teamproject;

import jakarta.annotation.PreDestroy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App implements CommandLineRunner {
    /**
     * The main launcher for the service all it does is make a call to the overridden run method.
     *
     * @param args A {@code String[]} of any potential runtime arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    /**
     * This contains all the setup logic, it will mainly be focused on loading up and creating an
     * instance of the database based off a saved file or will create a fresh database if the file
     * is not present.
     *
     * @param args A {@code String[]} of any potential runtime args
     */
    @Override
    public void run(String[] args) {
        // TODO: Convert this to a test.
        databaseExperiment();
        System.out.println("Start up");
    }

    // TODO: Convert this to a test.
    private static void databaseExperiment() {
        String url = "jdbc:postgresql://34.86.228.87/team_project_database";
        Properties props = new Properties();
        props.setProperty("user", "postgres");
        props.setProperty("password", "ase_aces");

        try {
            Connection conn = DriverManager.getConnection(url, props);

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM employee WHERE salary > 500");
            while (rs.next()) {
                System.out.print("Column 1 returned ");
                System.out.println(rs.getString(1));
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
