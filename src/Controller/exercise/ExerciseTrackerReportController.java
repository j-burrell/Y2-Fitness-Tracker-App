/*********************************************************
 *
 *                   ExerciseTrackerReportController.java (Controller)
 *
 *
 **********************************************************/

package Controller.exercise;

import Controller.FitBit.FitBitHomeController;
import Controller.FitBit.FitBitStepsController;
import Controller.reminder.AppReminderController;
import Controller.reminder.ReminderController;
import Controller.tools.ExamController;
import Controller.tools.MusicPlayerController;
import Model.exercise.Exercise;
import Controller.food.FoodTrackerController;
import Controller.food.FoodTrackerReportController;
import Model.user.User;
import home.*;
import Controller.group.*;
import Controller.user.*;
import Controller.sleep.*;
import Controller.water.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import Controller.weight.WeightTrackerController;
import Controller.weight.WeightTrackerReportController;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class ExerciseTrackerReportController {

    private User user;

    private Stage stage;

    private AdminClass adminClass;


    @FXML
    MenuBar menuBar;

    @FXML Text dateError;
    @FXML Text dateError2;

    @FXML DatePicker startDate;
    @FXML DatePicker endDate;
    @FXML Button generateReportButton;

    @FXML NumberAxis yAxis;
    @FXML CategoryAxis xAxis;
    @FXML BarChart<Integer,String> bc;
    @FXML Button clearChartButton;
    @FXML Button goalReportButton;

    @FXML
    MenuItem fitBitAuthButton;
    @FXML MenuItem fitBitStepsButton;

    /*
     *
     * Description: This method initialises the view page and all the data it needs to load
     * Param: Stage stage, User Controller.user, AdminClass adminClass
     * Returns: void
     *
     */
    public void initData(Stage stage, User user, AdminClass adminClass){
        this.stage = stage;
        this.adminClass = adminClass;
        this.user=user;
        if(user.getFitBitToken().equals("")){
            fitBitAuthButton.setVisible(true);
            fitBitStepsButton.setVisible(false);
        }
        else{
            fitBitAuthButton.setVisible(false);
            fitBitStepsButton.setVisible(true);

        }

    }


    /*
     *
     * Description: This method is used to generate a graph based on an Model.exercise goal
     * Param: void
     * Returns: void
     *
     */
    @FXML public void getGoalReport(){
        dateError.setVisible(false);
        dateError.setVisible(false);

        if(user.getExerciseTracker().getGoalEnd() != null){
            dateError2.setVisible(false);


            LocalDate sDate = user.getExerciseTracker().getGoalStart();
            LocalDate eDate = user.getExerciseTracker().getGoalEnd();
            ArrayList<Double> reportArray = new ArrayList<>();

            LocalDate date = sDate;

            while(!date.equals(eDate.plusDays(1))){
                if(user.getExerciseTracker().getExerciseTracker().get(date)!=null){
                    ArrayList<Exercise> exArray = user.getExerciseTracker().getExerciseTracker().get(date);
                    for(Exercise ex: exArray){
                        reportArray.add((double)ex.getDuration());
                    }
                }
                else{
                    reportArray.add(0.0);
                }
                date = date.plusDays(1);
            }

            bc.setTitle("Exercise Tracker report");
            yAxis.setLabel("Duration (minutes)");
            bc.setLegendVisible(false);

            //xAxis.setLabel("Date");
            xAxis.setTickLabelRotation(90);


            XYChart.Series dataSeries1 = new XYChart.Series();


            LocalDate dateX = sDate;


            ArrayList<String> x = new ArrayList<>();
            System.out.println(reportArray.size());
            for (double amount : reportArray) {
                x.add(dateX.toString());
                dataSeries1.getData().add(new XYChart.Data(dateX.toString(), amount));
                dateX = dateX.plusDays(1);
            }
            //xAxis.setCategories(FXCollections.observableArrayList(x));
            bc.getData().add(dataSeries1);

        }
        else{
            dateError2.setVisible(true);
        }

        }

    /*
     *
     * Description: This method generates a graph from inputted dates
     * Param: void
     * Returns: void
     *
     */
    @FXML public void getReport(){
        dateError.setVisible(false);
        dateError2.setVisible(false);
        LocalDate sDate = startDate.getValue();
        LocalDate eDate = endDate.getValue();
        if(sDate != null && eDate != null) {
            if (verifyDates(sDate, eDate)) {
                dateError.setVisible(false);
                dateError2.setVisible(false);

                ArrayList<Double> report = user.getExerciseTracker().generateReport(sDate, eDate);

                bc.setTitle("Exercise Tracker Goal report");
                yAxis.setLabel("Duration (minutes)");
                bc.setLegendVisible(false);

                //xAxis.setLabel("Date");
                xAxis.setTickLabelRotation(90);


                XYChart.Series dataSeries1 = new XYChart.Series();


                LocalDate date = sDate;


                ArrayList<String> x = new ArrayList<>();
                System.out.println(report.size());
                for (double amount : report) {
                    x.add(date.toString());
                    dataSeries1.getData().add(new XYChart.Data(date.toString(), amount));
                    date = date.plusDays(1);
                }

                bc.getData().add(dataSeries1);

            } else {
                dateError.setText("Start date is not before end date");
                dateError.setVisible(true);
            }
        }
        else {
            dateError.setText("No dates selected");
            dateError.setVisible(true);}
    }


    /*
     *
     * Description: This method clears the current data in the chart
     * Param: void
     * Returns: void
     *
     */
    @FXML public void clearChart(){
        bc.getData().clear();
        bc.layout();
    }

    /*
     *
     * Description: This method verifies start date is before end date
     * Param: LocalDate sDate, LocalDate eDate
     * Returns: void
     *
     */
    private boolean verifyDates(LocalDate sDate, LocalDate eDate){
        if(eDate.isAfter(sDate)){
            return true;
        }
        return false;


    }


    /******************************************************************************
     *                            MENU BAR FXML FUNCTIONS
     *****************************************************************************/

    @FXML public void logout() throws IOException {

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


