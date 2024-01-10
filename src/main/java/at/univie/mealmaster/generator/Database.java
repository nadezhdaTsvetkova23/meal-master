package at.univie.mealmaster.generator;

import java.sql.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 *Connects to the Database and handles all Queries and all Database related stuff.
 */
public class Database {

    // Connection details
    private final String database = "jdbc:mariadb://localhost:3306/mealmaster";
    private final String user = "mealmaster_user";
    private final String pass = "d4bc1e78e053c6c2d0419bd742b41adc";

    private Connection con;
    private Statement stmt;

    public Database(){
        connectionSetup();
    }

    public boolean connectionSetup(){
        try{
            // Establish a connection to the database
            this.con = DriverManager.getConnection(database, user, pass);
            this.stmt = con.createStatement();
            return true;

        }catch(Exception e){
            System.err.println(e);
            return false;
        }
    }

    public void close(){
        try{
            stmt.close();
            con.close();
        }catch(Exception e){
            System.err.println(e);
        }
    }

    public boolean addQuery(String query){
        try {
            stmt.executeQuery(query);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    public boolean addQueries(List<String> queries){
        AtomicBoolean exception = new AtomicBoolean(false);

        queries.forEach((query)-> {
            if(!addQuery(query)){
                exception.set(true);
            }
        });
        return !exception.get();
    }

    public int checkDatasets(String table){
        try {
            ResultSet rsZutat = stmt.executeQuery("SELECT COUNT(*) FROM " + table);
            if (rsZutat.next()) {
                return rsZutat.getInt(1);
            }
        }catch(Exception e){
            System.err.println(e);
        }
        return 0;
    }
}
