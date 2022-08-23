/*********************************************************
 *
 *                   ExerciseGoalController.java (Controller)
 *
 *
 **********************************************************/

package Controller.exercise;

import Controller.FitBit.FitBitHomeController;
import Controller.FitBit.FitBitStepsController;
import Controller.food.FoodTrackerController;
import Controller.food.FoodTrackerReportController;
import Controller.reminder.AppReminderController;
import Controller.reminder.ReminderController;
import Controller.tools.ExamController;
import Controller.tools.MusicPlayerController;
import Model.user.User;
import home.*;
import Controller.group.*;
import javafx.scene.layout.GridPane;
import Controller.user.*;
import Controller.sleep.*;
import Controller.water.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import Controller.weight.WeightTrackerController;
import Controller.weight.WeightTrackerReportController;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;


public class ExerciseGoalController {


    private Stage stage;
    private User user;
    private AdminClass adminClass;
    private double goalValue;


    @FXML Text errorText;

    @FXML DatePicker startDatePicker;
    @FXML DatePicker endDatePicker;

    @FXML Button setGoalButton;
    @FXML Button removeGoalButton;

    @FXML TextField goalAmountTF;

    @FXML Text currentGoalProgressLabel;

    @FXML ProgressBar progressBar;

    @FXML Text goalStartDateText;
    @FXML Text goalEndDateText;

    @FXML GridPane goalInfo;
    @FXML GridPane errorPane;
    @FXML MenuItem fitBitAuthButton;
    @FXML MenuItem fitBitStepsButton;
    /*
     *
     * Description: This method initialises the view page and all the data it needs to load
     * Param: Stage stage, User Controller.user, AdminClass adminClass
     * Returns: void
     *
     */
    @FXML
    public void initData(Stage stage, User user,AdminClass adminClass){

        this.stage = stage;
        this.user = user;
        this.adminClass = adminClass;
        goalInfo.setVisible(false);
        if(user.getExerciseTracker().getGoalEnd() != null) {
            goalInfo.setVisible(true);
            goalStartDateText.setVisible(true);
            goalStartDateText.setText("Start Date: " +user.getExerciseTracker().getGoalStart().toString());

            goalEndDateText.setVisible(true);
            goalEndDateText.setText("End Date: " + user.getExerciseTracker().getGoalEnd().toString());


            System.out.println("goal percentage: " + user.getExerciseTracker().goalPercentage());
            progressBar.setProgress(user.getExerciseTracker().goalPercentage());
            currentGoalProgressLabel.setText("Current goal: " + user.getExerciseTracker().goalProgress() + " / "  +user.getExerciseTracker().getGoalValue());
        }
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
     * Description: removes the current goal
     * Param: void
     * Returns: void
     *
     */
    @FXML public void removeGoal(){
        goalStartDateText.setVisible(false);
        goalEndDateText.setVisible(false);
        goalInfo.setVisible(false);
        user.getExerciseTracker().removeGoal();
        adminClass.sqlExeStatement(adminClass.deleteGoal(user));
        progressBar.setProgress(0);
        currentGoalProgressLabel.setText("Current Goal: No current goal");
        //adminClass.serializeSave();
    }

    /*
     *
     * Description: Sets the current goal
     * Param: void
     * Returns: void
     *
     */
    @FXML public void setGoal(){
        LocalDate sDate = startDatePicker.getValue();
        LocalDate eDate = endDatePicker.getValue();
        String userEnterGoal = goalAmountTF.getText();
        boolean validInput = false;

        try{
            double userEnterGoalDouble = Double.parseDouble(userEnterGoal);
            System.out.println("amound of minutes: " + (sDate.until(eDate).getDays()*1440));
           validInput = true;

        }
        catch (Exception e){
            errorPane.setVisible(true);
            errorText.setVisible(true);
            errorText.setText("Invalid goal amount inputted");
        }

        if(verifyDates(sDate,eDate) && verifyGoalAmount(userEnterGoal,startDatePicker.getValue(),endDatePicker.getValue()) && validInput){
            //all inputs are valid. set the goal
            boolean setG = user.getExerciseTracker().setGoal(sDate,eDate,Double.parseDouble(userEnterGoal));
            if(setG){
                currentGoalProgressLabel.setText("Current goal: " + user.getExerciseTracker().goalProgress() + " / "  +user.getExerciseTracker().getGoalValue());
                goalInfo.setVisible(true);

                goalStartDateText.setVisible(true);
                goalStartDateText.setText("Start Date: " +user.getExerciseTracker().getGoalStart().toString());

                goalEndDateText.setVisible(true);
                goalEndDateText.setText("End Date: " + user.getExerciseTracker().getGoalEnd().toString());

                errorText.setText("");
                errorText.setVisible(false);
                errorPane.setVisible(false);
                AdminClass.sqlExeStatement(AdminClass.insertGoal(user,Double.parseDouble(userEnterGoal),sDate,eDate));
                progressBar.setProgress(user.getExerciseTracker().goalPercentage());
            }
            else{
                errorPane.setVisible(true);
                errorText.setVisible(true);
                errorText.setText("A goal is already present.");
            }

        }

    }

    /*
     *
     * Description: Verify goal is valid.
     * Param: String goalAmount, LocalDate sDate, LocalDate eDate
     * Returns: boolean
     *
     */
    private boolean verifyGoalAmount(String goalAmount, LocalDate sDate, LocalDate eDate){
        try{
            double numberVal = Double.valueOf(goalAmount);
            if(numberVal >0 && (numberVal < Duration.between(sDate.atStartOfDay(),eDate.atStartOfDay()).toMinutes())){
                return true;

            }
            else{
                errorPane.setVisible(true);
                errorText.setVisible(true);
                errorText.setText("Invalid goal amount");
                return false;
            }
        }
        catch (Exception e){
            System.out.println(e);
            System.out.println(Double.parseDouble(goalAmount));
            errorPane.setVisible(true);
            errorText.setVisible(true);
            errorText.setText("goal inputted is not a valid number");
            return false;
        }
    }

    /*
     *
     * Description: This method verifies that end date is after start date
     * Param: LocalDate sDate, LocalDate eDate
     * Returns: boolean
     *
     */
    private boolean verifyDates(LocalDate sDate, LocalDate eDate) {
        if (eDate.isAfter(sDate)) {
            return true;
        }
        errorPane.setVisible(true);
        errorText.setVisible(true);
        errorText.setText("End date is not after start date");
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


