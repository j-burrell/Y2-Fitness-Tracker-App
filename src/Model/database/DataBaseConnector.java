/*********************************************************
 *
 *                   DataBaseConnector.java
 *
 *
 **********************************************************/



package Model.database;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataBaseConnector {

    public static String url = "jdbc:postgresql://localhost:5432/postgres2";
    public static String driver = "org.postgresql.Driver";
    public static String user = "postgres";
    public static String password = "pass2";

    private Connection connection = null;
	private PreparedStatement statement = null;

	public Connection getConnection() {
        return connection;
    }


    /*
    *
    * Description: This method returns a statement based on an inputted String Query
    * Param: String sql
    * Returns: PreparedStatement statement
    *
    */
    public PreparedStatement getStatement(String sql) throws SQLException {
        statement = connection.prepareStatement(sql);
        return statement;
    }




	/*
	 *
	 * Description: This constructor method inititiates a connection with the Model.database.
	 * Param:
	 * Returns:
	 *
	 */
    public DataBaseConnector() {

        try {
            loadDataBaseDetails();

            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


	/*
	 *
	 * Description: This method closes statements and a connection with the Model.database.
	 * Param: void
	 * Returns: void
	 *
	 */
    public void close() {
        try {
            this.connection.close();
            this.statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public static void loadDataBaseDetails() throws FileNotFoundException {
        System.out.println();
        String csvFile = System.getProperty("user.dir") + "\\DataBaseDetails.csv";
        String line = "";
        String cvsSplitBy = ",";
        BufferedReader br = new BufferedReader(new FileReader(csvFile));
        try {

            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] country = line.split(cvsSplitBy);

               // System.out.println("name " + country[0] + " , value=" + country[1] + "]");
                switch (country[0]){

                    case "url":
                        url = country[1];
                        System.out.println(url);
                        break;
                    case "driver":
                        driver = country[1];
                        System.out.println(driver);

                        break;
                    case "user":
                        user = country[1];
                        System.out.println(user);

                        break;
                    case "password":
                        password = country[1];
                        System.out.println(password);

                        break;

                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*
    * unit test for class.
     */
    public static void main(String[] args) {
        String sql = "select * from meals";
        DataBaseConnector dc = new DataBaseConnector();
        try {
            ResultSet resultSet = dc.getStatement(sql).executeQuery();
            System.out.println("Id      name        calorie");
            while (resultSet.next()) {
                System.out.println(resultSet.getInt(1) + "          " + resultSet.getString(2) + "             " + resultSet.getInt(3));
            }
            dc.close();
        } catch (SQLException e) {
            dc.close();
            e.printStackTrace();
        }


    }

}
