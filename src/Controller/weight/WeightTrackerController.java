/*********************************************************
 *
 *                   WeightTrackerController.java (Controller)
 *
 *
 **********************************************************/

package Controller.weight;

import Controller.FitBit.FitBitHomeController;
import Controller.FitBit.FitBitStepsController;
import Controller.exercise.ExerciseGoalController;
import Controller.exercise.ExerciseHomeController;
import Controller.exercise.ExerciseTrackerReportController;
import Controller.food.FoodTrackerController;
import Controller.food.FoodTrackerReportController;
import Controller.group.GroupController;
import Controller.reminder.AppReminderController;
import Controller.reminder.ReminderController;
import Controller.tools.ExamController;
import Controller.tools.MusicPlayerController;
import home.AdminClass;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import Controller.sleep.SleepTrackerController;
import Controller.sleep.SleepTrackerReportController;
import Controller.user.HomeController;
import Controller.user.LoginController;
import Model.user.User;
import Controller.water.WaterTrackerController;
import Controller.water.WaterTrackerReportController;

import java.time.LocalDate;

import java.io.IOException;
import java.util.HashMap;

import Model.database.WeightDB;

public class WeightTrackerController {

    private User user;

    private Stage stage;

    private AdminClass adminClass;

    private LocalDate currentViewDate;
 
    @FXML
    private MenuBar menuBar;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField todayWeight;

    @FXML
    private Text currentAmount;

 

    @FXML
    private TextField targetWeight;


    @FXML Text errorAmount;
    @FXML
    TextFlow errorTextFlow;

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
        currentViewDate = LocalDate.now();


        datePicker.setValue(currentViewDate);
        errorTextFlow.setVisible(false);
        errorAmount.setVisible(false);

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
     * Description: Method that returns target Controller.weight on a certain date
     * Param:LocalDate date, HashMap<LocalDate,Integer> targetWeightDict
     * Returns: int
     *
     */
    private int getTargetWeightOnDate(LocalDate date, HashMap<LocalDate,Integer> targetWeightDict) {
        for (HashMap.Entry<LocalDate, Integer> entry : targetWeightDict.entrySet()) {
            LocalDate dateIter = entry.getKey();
            int volumeIter = entry.getValue();

            //Add the Controller.water volume to corresponding date.
            if (dateIter.equals(date)) {
                return volumeIter;
            }
        }
        return 0;
    }



    /*
     *
     * Description: method that returns the users Controller.weight on a certain date
     * Param:LocalDate date, HashMap<LocalDate,Integer> todayWeightDict
     * Returns: int
     *
     */
    private int getTodayWeightOnDate(LocalDate date, HashMap<LocalDate,Integer> todayWeightDict) {
        for (HashMap.Entry<LocalDate, Integer> entry : todayWeightDict.entrySet()) {
            LocalDate dateIter = entry.getKey();
            int volumeIter = entry.getValue();

            //Add the Controller.water volume to corresponding date.
            if (dateIter.equals(date)) {
                return volumeIter;
            }
        }
        return 0;
    }


    /*
     *
     * Description: Method that changes the focused date
     * Param: void
     * Returns: void
     *
     */
    @FXML public void changeDate(){
        System.out.println("changing date");
        currentViewDate=datePicker.getValue();
        updateWeight();

      
    }


    /*
     *
     * Description: Method that sets the current and target Controller.weight and updates the Model.database.
     * Param: void
     * Returns: void
     *
     */
    @FXML public void setValue(){

        try {
            int targetWeightCount = Integer.parseInt(targetWeight.getText());
            int todayWeightCount = Integer.parseInt(todayWeight.getText());
            System.out.println(targetWeightCount+" "+todayWeightCount);

                user.getWeightTracker().addToDate(currentViewDate, targetWeightCount,todayWeightCount);
              
                todayWeight.setText("");
                todayWeight.setPromptText("Today's Weight");
              

                WeightDB.addWeightTracker(user.getWeightTracker());
                errorTextFlow.setVisible(false);
                errorAmount.setVisible(false);
          
        }
        catch (Exception e){
            errorTextFlow.setVisible(true);
            errorAmount.setVisible(true);
        	//e.printStackTrace();
        }


    }


    /*
     *
     * Description: Method that saves the values of Controller.weight.
     * Param: void
     * Returns: void
     *
     */
    @FXML public void saveValue(){
  
                WeightDB.addWeightTracker(user.getWeightTracker());
 

    }


    /*
     *
     * Description: Method that updates the users Controller.weight.
     * Param: void
     * Returns: void
     *
     */
    public void updateWeight() {
    	
    	int targetWeight = getTargetWeightOnDate(currentViewDate, user.getWeightTracker().getTargetWeightDict());
    	int todayWeight = getTodayWeightOnDate(currentViewDate, user.getWeightTracker().getTodayWeightDict());

    	this.todayWeight.setText(todayWeight+"");
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
