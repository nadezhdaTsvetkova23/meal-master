/*
package at.univie.mealmaster.generator;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import javax.swing.plaf.synth.SynthDesktopIconUI;
import java.io.FileReader;
import java.sql.*;


//Generates sample data and adds it to database
public class Generator {
    public static void main(String args[]) {
        try {
            // Loads the class "oracle.jdbc.driver.OracleDriver" into the memory
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // Connection details
            String database = "jdbc:oracle:thin:@oracle-lab.cs.univie.ac.at:1521:lab";
            String user = "a12029628";
            String pass = "dbs21";

            // Establish a connection to the database
            Connection con = DriverManager.getConnection(database, user, pass);
            Statement stmt = con.createStatement();


            //ZUTAT
            JsonReader zutatReader = new JsonReader(new FileReader("json/zutaten.json"));

            items.Zutat[] zutaten = new Gson().fromJson(zutatReader, items.Zutat[].class);
            try {
                for(items.Zutat zutat: zutaten){
                    //stmt.executeUpdate("INSERT INTO items.Zutat (name) VALUES ('" + zutat.getZutat() +"')");
                    stmt.executeUpdate("DELETE FROM items.Zutat WHERE name = ('" + zutat.getZutat() +"')");
                    System.out.println(zutat.getZutat() + " hinzugefügt.");

                    //Get id for each items.Zutat
                    ResultSet result = stmt.executeQuery("SELECT id FROM ZUTAT WHERE name = ('" + zutat.getZutat() + "')");
                    if(result.next()){
                        zutat.setId(result.getInt(1));
                        System.out.println(result.getInt(1));
                    }
                }

            } catch (Exception e) {
                System.err.println("Error while executing INSERT INTO statement: " + e.getMessage());
            }

            //Check datasets in ZUTAT
            ResultSet rsZutat = stmt.executeQuery("SELECT COUNT(*) FROM items.Zutat");
            if (rsZutat.next()) {
                int count = rsZutat.getInt(1);
                System.out.println("Number of datasets: " + count);
            }
            rsZutat.close();


            //EINHEIT
            JsonReader einheitReader = new JsonReader(new FileReader("json/einheiten.json"));

            items.Einheit[] einheiten = new Gson().fromJson(einheitReader, items.Einheit[].class);
            try {
                for(items.Einheit einheit: einheiten){
                    //stmt.executeUpdate("INSERT INTO items.Einheit (name, abkuerzung) VALUES ('" + einheit.getName() +"', '"+ einheit.getAbkuerzung()+"')");
                    stmt.executeUpdate("DELETE FROM items.Einheit WHERE name = ('" + einheit.getName() +"')");
                    System.out.println(einheit.getName() + ", "+ einheit.getAbkuerzung() + " hinzugefügt.");

                    //Get id for each items.Einheit
                    ResultSet result = stmt.executeQuery("SELECT id FROM items.Einheit WHERE name = ('" + einheit.getName() + "')");
                    if(result.next()){
                        einheit.setId(result.getInt(1));
                        System.out.println(result.getInt(1));
                    }
                }
            } catch (Exception e) {
                System.err.println("Error while executing INSERT INTO statement: " + e.getMessage());
            }

            //Check datasets in EINHEIT
            ResultSet rsEinheit = stmt.executeQuery("SELECT COUNT(*) FROM items.Einheit");
            if (rsEinheit.next()) {
                int count = rsEinheit.getInt(1);
                System.out.println("Number of datasets: " + count);
            }
            rsEinheit.close();



            //REZEPT
            JsonReader einheitReader = new JsonReader(new FileReader("json/einheiten.json"));

            items.Einheit[] einheiten = new Gson().fromJson(einheitReader, items.Einheit[].class);
            try {
                for(items.Einheit einheit: einheiten){
                    //stmt.executeUpdate("INSERT INTO items.Einheit (name, abkuerzung) VALUES ('" + einheit.getName() +"', '"+ einheit.getAbkuerzung()+"')");
                    stmt.executeUpdate("DELETE FROM items.Einheit WHERE name = ('" + einheit.getName() +"')");
                    System.out.println(einheit.getName() + ", "+ einheit.getAbkuerzung() + " hinzugefügt.");

                    //Get id for each items.Einheit
                    ResultSet result = stmt.executeQuery("SELECT id FROM items.Einheit WHERE name = ('" + einheit.getName() + "')");
                    if(result.next()){
                        einheit.setId(result.getInt(1));
                        System.out.println(result.getInt(1));
                    }
                }
            } catch (Exception e) {
                System.err.println("Error while executing INSERT INTO statement: " + e.getMessage());
            }

            //Check datasets in EINHEIT
            ResultSet rsEinheit = stmt.executeQuery("SELECT COUNT(*) FROM items.Einheit");
            if (rsEinheit.next()) {
                int count = rsEinheit.getInt(1);
                System.out.println("Number of datasets: " + count);
            }
            rsEinheit.close();





            // Clean up connections
            stmt.close();
            con.close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }
}
*/
