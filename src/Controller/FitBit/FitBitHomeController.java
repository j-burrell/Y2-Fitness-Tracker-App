package Controller.FitBit;

import Controller.reminder.AppReminderController;
import Controller.reminder.ReminderController;
import Controller.tools.ExamController;
import Controller.tools.MusicPlayerController;
import Model.database.DataBaseConnector;
import Controller.exercise.ExerciseGoalController;
import Controller.exercise.ExerciseHomeController;
import Controller.exercise.ExerciseTrackerReportController;
import Controller.food.FoodTrackerController;
import Controller.food.FoodTrackerReportController;
import Controller.group.GroupController;
import home.AdminClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import Controller.sleep.SleepTrackerController;
import Controller.sleep.SleepTrackerReportController;
import Controller.user.HomeController;
import Controller.user.LoginController;
import Model.user.User;
import Controller.water.WaterTrackerController;
import Controller.water.WaterTrackerReportController;
import Controller.weight.WeightTrackerController;
import Controller.weight.WeightTrackerReportController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FitBitHomeController {
    static WebView webView;
    private User user;

    private Stage stage;

    private AdminClass adminClass;

    private LocalDate currentViewDate;

    @FXML
    DatePicker stepsDate;
    @FXML
    Label fitBitUserSteps;

    @FXML
    Button GetDataButton;
    @FXML MenuItem fitBitAuthButton;
    @FXML MenuItem fitBitStepsButton;
    /*
     *
     * Description: This method initialises the view page and all the data it needs to load
     * Param: Stage stage, User Controller.user, AdminClass adminClass
     * Returns: void
     *
     */
    public void initData(Stage stage, AdminClass adminClass, User user){
        this.stage = stage;
        this.adminClass = adminClass;
        this.user=user;
        //launchBrowser();
        if(user.getFitBitToken().equals(""))
        {
            fitBitAuthButton.setVisible(true);
            fitBitStepsButton.setVisible(false);

        }
        else
        {
            fitBitAuthButton.setVisible(false);
            fitBitStepsButton.setVisible(true);
        }

        checkToken();



    }

    @FXML
    public void GetStepData() throws IOException {
        System.out.println(user.getFitBitToken());
        URL url = new URL("https://api.fitbit.com/1/user/-/activities/steps/date/" + stepsDate.getValue() + "/1d.json");
        System.out.println(url.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestProperty("Authorization","Bearer "+user.getFitBitToken());

        conn.setRequestProperty("Content-Type","application/json");
        conn.setRequestMethod("GET");


        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String output;

        StringBuffer response = new StringBuffer();
        while ((output = in.readLine()) != null) {
            response.append(output);
        }

        in.close();
        // printing result from response
        System.out.println("Response:-" + response.toString());


    }



    @FXML
    public void auth(){
        checkToken();
    }
    public void start(Stage primaryStage) {
        primaryStage.setTitle("FitBit Auth");

        webView = new WebView();

        webView.getEngine().locationProperty().addListener((observable, oldValue, newValue) -> {
            String location = (String)newValue;
            //pulls data
            if(location.contains("#access_token=")) {
                System.out.println(location);
                // Sending get request
                try {

                    //Extract access token

                    Pattern p = Pattern.compile("(?<=#access_token=).+?(?=&)");
                    Matcher m = p.matcher(location);
                    if (m.find()) {
                        System.out.println("token extracted below");
                        user.setFitBitToken(m.group());
                        System.out.println(m.group());


                        Connection c = null;
                        Statement stmt = null;
                        try {
                            Class.forName("org.postgresql.Driver");
                            c = DriverManager
                                    .getConnection(DataBaseConnector.url,
                                            DataBaseConnector.user, DataBaseConnector.password);
                            c.setAutoCommit(false);

                            stmt = c.createStatement();

                            stmt = c.createStatement();
                            ResultSet rs = stmt.executeQuery("SELECT token FROM FitBitUserTokens WHERE Username = \'"+ user.getUsername() + "\';");
                            if(rs.next() == false ){
                                saveToken();


                            }
                            else if(rs.getString("token").equals("")){
                                adminClass.updateToken(user,user.getFitBitToken());


                            }
                            else{

                                adminClass.updateToken(user,user.getFitBitToken());

                            }

                            rs.close();
                            stmt.close();
                            c.commit();
                            c.close();
                            goToFitBitSteps();


                        } catch (ClassNotFoundException | SQLException e) {
                            e.printStackTrace();
                        }
                    }




                    primaryStage.close();
                }
                catch (Exception e) {
                    System.out.println("x");
                }

            }
            else if(location.contains("https://www.facebook.com/dialog/oauth?response_type=token%")){
                System.out.println(location);
                System.out.println("facebook FOUND");
                primaryStage.close();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Authentication");
                alert.setHeaderText("FitBit Support");
                alert.setContentText("FitBit no longer supports facebook login");

                alert.showAndWait();
            }
            else if(location.contains("https://accounts.google.com/signin/oauth?response_type=token")){
                System.out.println(location);
                System.out.println("google FOUND");


                primaryStage.close();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Authentication");
                alert.setHeaderText("FitBit Support");
                alert.setContentText("FitBit no longer supports google login");

                alert.showAndWait();
            }

        });








        webView.getEngine().load("https://www.fitbit.com/oauth2/authorize?response_type=token&client_id=22BLZJ&redirect_uri=https%3A%2F%2Flocalhost.com%2F&scope=activity%20heartrate%20location%20nutrition%20profile%20settings%20sleep%20social%20weight&expires_in=604800");




        VBox vBox = new VBox(webView);
        Scene scene = new Scene(vBox, 960, 600);

        primaryStage.setScene(scene);
        primaryStage.show();


    }


    private void saveToken(){

        String sqlInsert = "INSERT INTO FitBitUserTokens(Username, token )" +

                "VALUES('" + user.getUsername() + "' , '" + user.getFitBitToken() + " '"  +");";


        System.out.println(sqlInsert);
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection(DataBaseConnector.url,
                            DataBaseConnector.user, DataBaseConnector.password);
            c.setAutoCommit(false);

            stmt = c.createStatement();





            stmt.executeUpdate(sqlInsert);

            stmt.close();
            c.commit();
            c.close();



        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened Model.database successfully");
    }


    private void checkToken(){
        System.out.println("checking token");

            Connection c = null;
            Statement stmt = null;
            try {
                Class.forName("org.postgresql.Driver");
                c = DriverManager
                        .getConnection(DataBaseConnector.url,
                                DataBaseConnector.user, DataBaseConnector.password);
                c.setAutoCommit(false);

                stmt = c.createStatement();

                stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT token FROM FitBitUserTokens WHERE Username = \'"+ user.getUsername() + "\';");
                if(rs.next() == false ){
                    System.out.println("No valid token");
                    launchBrowser();
                }
                else if(rs.getString("token").equals("")){
                    launchBrowser();
                }
                else{

                        System.out.println("token exists, adding");
                        String token = rs.getString("token");
                        System.out.println(token);
                        System.out.println("to Controller.user");
                        user.setFitBitToken(token);

                }

                rs.close();
                stmt.close();
                c.commit();
                c.close();

            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }



    }

    public void launchBrowser(){
        System.setProperty("sun.net.http.allowRestrictedHeaders","true");

        com.sun.javafx.webkit.WebConsoleListener.setDefaultListener((webView, message, lineNumber, sourceId) -> System.out.println("Console: [" + sourceId + ":" + lineNumber + "] " + message));

        Stage stage1 = new Stage();
        start(stage1);

    }

    /******************************************************************************
     *                            MENU BAR FXML FUNCTIONS
     *****************************************************************************/

    @FXML
    public void logout() throws IOException {

        String fileLoc = "/View/loginPage.fxml";

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fileLoc));


        Parent root = (Parent) loader.load();
        LoginController ctrl = loader.getController();


        ctrl.initData(stage,adminClass);


        stage.getScene().setRoot(root);

    }
    @FXML public void goToWaterTracker() throws IOException {


        String fileLoc = "/View/waterTrackerPage.fxml";

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fileLoc));


        Parent root = (Parent) loader.load();
        WaterTrackerController ctrl = loader.getController();


        ctrl.initData(stage,adminClass, user);


        stage.getScene().setRoot(root);

    }
    @FXML public void goToWaterTrackerReport() throws IOException {
        String fileLoc = "/View/waterTrackerReport.fxml";

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fileLoc));


        Parent root = (Parent) loader.load();
        WaterTrackerReportController ctrl = loader.getController();


        ctrl.initData(stage, adminClass, user);


        stage.getScene().setRoot(root);
    }
    @FXML public void goToHome() throws IOException {
        String fileLoc = "/View/userHome.fxml";

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fileLoc));


        Parent root = (Parent) loader.load();
        HomeController ctrl = loader.getController();


        ctrl.initData(stage,adminClass, user);


        stage.getScene().setRoot(root);

    }
    @FXML public void goToSleepTracker() throws IOException {
        String fileLoc = "/View/sleepTrackerPage.fxml";
        System.out.println(user);

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fileLoc));


        Parent root = (Parent) loader.load();
        SleepTrackerController ctrl = loader.getController();


        ctrl.initData(stage,adminClass, user);


        stage.getScene().setRoot(root);

    }
    @FXML public void goToSleepTrackerReport() throws IOException {
        String fileLoc = "/View/sleepTrackerReport.fxml";

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fileLoc));


        Parent root = (Parent) loader.load();
        SleepTrackerReportController ctrl = loader.getController();


        ctrl.initData(stage,adminClass, user);


        stage.getScene().setRoot(root);

    }
    @FXML public void goToGroup() throws IOException {

        String fileLoc = "/View/groupHome.fxml";

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fileLoc));


        Parent root = (Parent) loader.load();
        GroupController ctrl = loader.getController();


        ctrl.initData(stage,user,adminClass);


        stage.getScene().setRoot(root);

    }
    @FXML public void goToExercise() throws IOException {

        String fileLoc = "/View/exerciseHome.fxml";

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fileLoc));


        Parent root = (Parent) loader.load();
        ExerciseHomeController ctrl = loader.getController();


        ctrl.initData(stage, user,adminClass);


        stage.getScene().setRoot(root);

    }
    @FXML public void goToExerciseGoal() throws IOException {

        String fileLoc = "/View/exerciseGoalPage.fxml";

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fileLoc));


        Parent root = (Parent) loader.load();
        ExerciseGoalController ctrl = loader.getController();


        ctrl.initData(stage, user,adminClass);


        stage.getScene().setRoot(root);

    }
    @FXML public void goToExerciseReport() throws IOException {

        String fileLoc = "/View/exerciseTrackerReport.fxml";

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fileLoc));


        Parent root = (Parent) loader.load();
        ExerciseTrackerReportController ctrl = loader.getController();


        ctrl.initData(stage, user,adminClass);


        stage.getScene().setRoot(root);

    }

    @FXML public void goToFoodTracker() throws IOException {
        String fileLoc = "/View/foodTrackerPage.fxml";
        System.out.println(user);

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fileLoc));


        Parent root = (Parent) loader.load();
        FoodTrackerController ctrl = loader.getController();


        ctrl.initData(stage,adminClass, user);


        stage.getScene().setRoot(root);

    }
    @FXML public void goToFoodTrackerReport() throws IOException {
        String fileLoc = "/View/foodTrackerReport.fxml";

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fileLoc));


        Parent root = (Parent) loader.load();
        FoodTrackerReportController ctrl = loader.getController();


        ctrl.initData(stage,adminClass, user);


        stage.getScene().setRoot(root);

    }
    @FXML public void goToWeightTracker() throws IOException {
        String fileLoc = "/View/weightTrackerPage.fxml";
        System.out.println(user);

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fileLoc));


        Parent root = (Parent) loader.load();
        WeightTrackerController ctrl = loader.getController();


        ctrl.initData(stage,adminClass, user);


        stage.getScene().setRoot(root);

    }
    @FXML public void goToWeightTrackerReport() throws IOException {
        String fileLoc = "/View/weightTrackerReport.fxml";

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fileLoc));


        Parent root = (Parent) loader.load();
        WeightTrackerReportController ctrl = loader.getController();


        ctrl.initData(stage,adminClass,user);

        stage.getScene().setRoot(root);

    }
    @FXML public void goToFitBit() throws IOException {
        String fileLoc = "/View/FitBitHomePage.fxml";

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fileLoc));


        Parent root = (Parent) loader.load();
        FitBitHomeController ctrl = loader.getController();


        ctrl.initData(stage,adminClass,user);

        stage.getScene().setRoot(root);

    }

    @FXML public void goToFitBitSteps() throws IOException {
        String fileLoc = "/View/FitBitStepsPage.fxml";

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fileLoc));


        Parent root = (Parent) loader.load();
        FitBitStepsController ctrl = loader.getController();


        ctrl.initData(stage,adminClass,user);

        stage.getScene().setRoot(root);

    }

    @FXML public void goToReminder() throws IOException {
        String fileLoc = "/View/reminder.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fileLoc));
        Parent root = (Parent) loader.load();
        ReminderController ctrl = loader.getController();
        ctrl.initData(user, stage, adminClass);
        stage.getScene().setRoot(root);
    }
    @FXML public void goToAppReminder() throws IOException {
        String fileLoc = "/View/appReminder.fxml";

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fileLoc));


        Parent root = (Parent) loader.load();
        AppReminderController ctrl = loader.getController();


        ctrl.initData(user,stage,adminClass);

        stage.getScene().setRoot(root);

    }
    @FXML public void goToExam() throws IOException {


        String fileLoc = "/View/exam.fxml";

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fileLoc));


        Parent root = (Parent) loader.load();
        ExamController ctrl = loader.getController();


        ctrl.initData(stage,adminClass, user);


        stage.getScene().setRoot(root);

    }
    @FXML public void goToMusic() throws IOException {


        String fileLoc = "/View/music.fxml";

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fileLoc));


        Parent root = (Parent) loader.load();
        MusicPlayerController ctrl = loader.getController();

//
        ctrl.initData(stage,adminClass, user);


        stage.getScene().setRoot(root);

    }
}
