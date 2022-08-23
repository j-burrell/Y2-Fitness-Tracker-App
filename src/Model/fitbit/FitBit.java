package Model.fitbit;

import Model.database.DataBaseConnector;
import org.json.JSONArray;
import org.json.JSONObject;
import Model.user.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class FitBit {

    User user;

    LocalDate date;


    public String steps;
    public  String stepsGoal;

    public String distance;
    public String distanceGoal;

    public String calories;
    public String caloriesGoal;

    public String activeMinutes;

    public String restingHeartRate;
    DataBaseConnector dataBaseConnector;

    boolean blockRequests;

public FitBit(User user, LocalDate date){
    //Initialize all variables
    this.user = user;
    this.date = date;

    steps = "No Data";
    stepsGoal = "No Data";

    distance = "No Data";
    distanceGoal = "No Data";

    calories = "No Data";
    caloriesGoal = "No Data";

    activeMinutes = "No Data";

    restingHeartRate = "No Data";


    blockRequests = false;


}

public boolean checkFitBitData(LocalDate  inputDate){

    boolean authPass = true;
    dataBaseConnector = new DataBaseConnector();
        date = inputDate;
        boolean pull = false;
        String sql = "SELECT * FROM FitBitData2 WHERE Username = \'" + user.getUsername() + "\' AND Date = \'" + date + "\';";
        try {
            ResultSet rs = dataBaseConnector.getStatement(sql).executeQuery();

            if (!rs.next()) {
                System.out.println("No valid date");
                pull = true;


            } else {


                steps = rs.getString("steps");
                stepsGoal = rs.getString("stepGoal");

                distance = rs.getString("distance");
                distanceGoal = rs.getString("distanceGoal");


                calories = rs.getString("calories");
                caloriesGoal = rs.getString("caloriesGoal");

                activeMinutes = rs.getString("activeMinutes");

                restingHeartRate = rs.getString("restingHR");


            }


                //if date isnt in db and the selected date is todays date
                //pull data from fibit api then insert into the Model.database
                if (pull) {
                    System.out.println("pulling");

                    try {


                        apiConnection();


                        try {


                            String sqlInsert = "INSERT INTO FitBitData2(Username, Date,steps,stepGoal, distance, distanceGoal, calories, caloriesGoal, activeMinutes,restingHR) " +

                                    "VALUES('" + user.getUsername() + "','" + date + "','" + steps + "','" + stepsGoal + "','"
                                    + distance + "','" + distanceGoal + "','"
                                    + calories + "','" + caloriesGoal + "','"
                                    + activeMinutes + "','" + restingHeartRate +
                                    "');";

                            dataBaseConnector.getStatement(sqlInsert).executeUpdate();


                        } catch (Exception e) {
                            e.printStackTrace();
                            System.err.println(e.getClass().getName() + ": " + e.getMessage());
                            System.exit(0);
                        }

                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        System.out.println(user.getFitBitToken());
                        if (e.getMessage().contains("401")) {
                            System.out.println("Users token is no longer authorised");
                            user.setFitBitToken("");
                            authPass = false;
                            //adminClass.updateToken(Controller.user, Controller.user.getFitBitToken());
                            // goToFitBit();
                        }
                        if (e.getMessage().contains("429")) {
                            blockRequests = true;
                            System.out.println("429 too many requests");
                        }
                    }
                }//&& stepsDate.getValue().isEqual(LocalDate.now()


                //if date is in db and the selected date is todays date
                //pull data from fibit api then update into the Model.database
                else if (!pull && date.isEqual(LocalDate.now())) {
                    try {

                        apiConnection();


                        System.out.println("updating db");
                        //update db

                        try {


                            String sqlUpdate2 = "UPDATE  FitBitData2 set " +

                                    "steps = '" + steps + "'," +
                                    "stepGoal= '" + stepsGoal + "'," +
                                    "distance ='" + distance + "'," +
                                    "distanceGoal ='" + distanceGoal + "'," +
                                    "calories ='" + calories + "'," +
                                    "caloriesGoal ='" + caloriesGoal + "'," +
                                    "activeMinutes ='" + activeMinutes + "'," +
                                    "restingHR ='" + restingHeartRate + "'" +

                                    "where Username='" + user.getUsername() + "' AND Date ='" + date + "';";


                            dataBaseConnector.getStatement(sqlUpdate2).executeUpdate();


                        } catch (Exception e) {
                            e.printStackTrace();
                            System.err.println(e.getClass().getName() + ": " + e.getMessage());
                            System.exit(0);
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        if (e.getMessage().contains("401")) {
                            System.out.println("Users token is no longer authorised");
                            user.setFitBitToken("");
                            authPass = false;

                            //adminClass.updateToken(Controller.user, Controller.user.getFitBitToken());
                            //goToFitBit();
                        }
                        if (e.getMessage().contains("429")) {
                            blockRequests = true;
                            System.out.println("429 too many requests");
                        }
                    }
                }




        } catch (SQLException e) {

            e.printStackTrace();
        }

        dataBaseConnector.close();

    return authPass;
}


public void apiConnection() throws IOException {
    System.out.println("Connecting to fitbit api");
    URL url = new URL("https://api.fitbit.com/1/user/-/activities/date/" + date + ".json");
    //System.out.println(url.toString());
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

    conn.setRequestProperty("Authorization","Bearer "+user.getFitBitToken());

    conn.setRequestProperty("Content-Type","application/json");
    conn.setRequestMethod("GET");


    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    String output;


    StringBuffer response = new StringBuffer();

    while ((output = in.readLine()) != null) {
        response.append(output);
        System.out.println(output);

        //Distance
        try {
            //Total distance travelled
            JSONObject jsonObj = new JSONObject(output);
            JSONObject jsonObjSum = (JSONObject) jsonObj.get("summary");
            JSONArray lineItems = jsonObjSum.getJSONArray("distances");
            for(int i =0; i < lineItems.length(); i++) {
                JSONObject jsonLineItem = (JSONObject) lineItems.getJSONObject(i);
                String jsonObj5 = jsonLineItem.getString("activity");
                if(jsonObj5.equals("total")) {
                    double jsonObj6 = jsonLineItem.getDouble("distance");
                    distance = String.valueOf(jsonObj6);
                }
            }

        } catch (Exception e) {
            //System.out.println("No calories");

        }

        //Distance Goal
        try {
            //Steps
            JSONObject jsonObj = new JSONObject(output);
            JSONObject jsonObjGoals = (JSONObject) jsonObj.get("goals");

            String distGoal = jsonObjGoals.get("distance").toString();
            distanceGoal = distGoal;


        } catch (Exception e) {
            //System.out.println("No distance goal");
        }




        //Steps
        try {
            //Steps
            JSONObject jsonObj = new JSONObject(output);
            JSONObject jsonObjActual = (JSONObject) jsonObj.get("summary");
            String actualSteps = jsonObjActual.get("steps").toString();
            steps = actualSteps;

        } catch (Exception e) {
            // System.out.println("No Steps");

        }


        //steps goal
        try {
            //Steps
            JSONObject jsonObj = new JSONObject(output);
            JSONObject jsonObjGoals = (JSONObject) jsonObj.get("goals");
            //System.out.println(jsonObjGoals.toString());
            String stepGoal = jsonObjGoals.get("steps").toString();
            stepsGoal = stepGoal;
            //System.out.println("step goals: " + stepGoal);

        } catch (Exception e) {
            // System.out.println("No Step goal");

        }


        //calories
        try {
            //Steps
            JSONObject jsonObj = new JSONObject(output);
            JSONObject jsonObjActual = (JSONObject) jsonObj.get("summary");

            String caloriesOut = jsonObjActual.get("caloriesOut").toString();
            calories = caloriesOut;

        } catch (Exception e) {
            // System.out.println("No calories");

        }


        //calories goal
        try {
            //Steps
            JSONObject jsonObj = new JSONObject(output);
            JSONObject jsonObjGoals = (JSONObject) jsonObj.get("goals");
            String caloriesGoal = jsonObjGoals.get("caloriesOut").toString();
            this.caloriesGoal = caloriesGoal;

        } catch (Exception e) {
            // System.out.println("No calories goal");

        }


        //Active minutes
        try {
            JSONObject jsonObj = new JSONObject(output);


            JSONObject jsonObjSum = (JSONObject) jsonObj.get("summary");
            String jsonObjfActive =jsonObjSum.get("fairlyActiveMinutes").toString();
            String jsonObjlActive =  jsonObjSum.get("lightlyActiveMinutes").toString();
            String jsonObvActive = jsonObjSum.get("veryActiveMinutes").toString();

            float totalActiveMinutes =(Float.valueOf(jsonObjlActive) + Float.valueOf(jsonObjfActive) + Float.valueOf(jsonObvActive));
            activeMinutes = String.valueOf(totalActiveMinutes);



        } catch (Exception e) {
            //System.out.println("No resting heart rate available");
        }


    }
    in.close();


    //heart rate
    url = new URL("https://api.fitbit.com/1/user/-/activities/heart/date/" + date + "/1d.json");


    conn = (HttpURLConnection) url.openConnection();

    conn.setRequestProperty("Authorization","Bearer "+user.getFitBitToken());

    conn.setRequestProperty("Content-Type","application/json");
    conn.setRequestMethod("GET");


    in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

    output ="";

    response = new StringBuffer();

    while ((output = in.readLine()) != null) {
        response.append(output);

        try {
            JSONObject jsonObj = new JSONObject(output);
            JSONArray lineItems = jsonObj.getJSONArray("activities-heart");
            for (int i = 0; i < lineItems.length(); i++) {
                JSONObject jsonLineItem = (JSONObject) lineItems.getJSONObject(i);
                String key = jsonLineItem.getString("restingHeartRate");
                restingHeartRate = key;

            }


        } catch (Exception e) {
            //System.out.println("No resting heart rate available");
        }
    }








    in.close();




}


}
