/*********************************************************
 *
 *                   SleepTrackerController.java (Controller)
 *
 *
 **********************************************************/

package Controller.sleep;

import Controller.FitBit.FitBitHomeController;
import Controller.FitBit.FitBitStepsController;
import Controller.exercise.ExerciseGoalController;
import Controller.exercise.ExerciseHomeController;
import Controller.exercise.ExerciseTrackerReportController;
import Controller.food.FoodTrackerController;
import Controller.food.FoodTrackerReportController;
import Controller.reminder.AppReminderController;
import Controller.reminder.ReminderController;
import Controller.tools.ExamController;
import Controller.tools.MusicPlayerController;
import Model.user.User;
import home.*;
import Controller.group.*;
import Controller.user.*;
import Controller.water.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import Controller.weight.WeightTrackerController;
import Controller.weight.WeightTrackerReportController;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;

public class SleepTrackerController {

    private User user;

    private Stage stage;

    private AdminClass adminClass;

    private LocalDate currentViewDate;
    private int currentAmountDate;

    @FXML
    MenuBar menuBar;

    @FXML DatePicker datePicker;
    @FXML Text currentAmount;
    @FXML Text coachingMSG;
    @FXML TextField amountEntry;
    @FXML Button amountButton;
    @FXML TextFlow coachingMSGF;
    @FXML Text errorAmount;
    @FXML
    TextFlow errorTextFlow;
    @FXML MenuItem fitBitAuthButton;
    @FXML
    MenuItem fitBitStepsButton;


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
        updateCoaching();
        errorTextFlow.setVisible(false);

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
     * Description: This method updates the coaching method for Controller.sleep that is displayed to the Controller.user.
     * Param: void
     * Returns: void
     *
     */
    private void updateCoaching(){
        String msg;

        System.out.println(user.getSleepTracker().getSleepTrackerDict());
        double currSlept = getAmountOnDate(currentViewDate,user.getSleepTracker().getSleepTrackerDict());
        if(currSlept < user.getSleepTracker().getAdviceAmount()) {
            msg = "You slept " + currSlept + " hours out of " + user.getSleepTracker().getAdviceAmount()
                    + " hours. You need to sleep " + (user.getSleepTracker().getAdviceAmount() - currSlept) + " hours to sleep the daily advised amount.";
        }
        else{
            msg = "You have slept enough to meet our advised amount today!";
        }
        coachingMSG.setText(msg);
    }



    /*
     *
     * Description: This method updates the current amount slept of the Controller.user on the selected date.
     * Param: void
     * Returns: void
     *
     */
    private void updateCurrentAmount(){
        double currSlept = getAmountOnDate(currentViewDate,user.getSleepTracker().getSleepTrackerDict());
        //currentAmount.setText("You have slept " + currSlept + " hours today.");
    }



    /*
     *
     * Description: This method changes the current focused date.
     * Param:void
     * Returns: void
     *
     */
    @FXML public void changeDate(){

        currentViewDate=datePicker.getValue();

        updateCurrentAmount();
        updateCoaching();
    }


    /*
     *
     * Description: This method adds Controller.sleep to the Controller.user at the correct date as well as updating the Model.database
     * Param: void
     * Returns: void
     *
     */
    @FXML public void add(){

        try {

            double amountToAdd = Integer.parseInt(amountEntry.getText());
            if(amountToAdd >0 && amountToAdd < 20){



                if(user.getSleepTracker().getSleepTrackerDict().containsKey(currentViewDate)){
                    errorTextFlow.setVisible(false);
                    errorAmount.setVisible(false);
                    user.getSleepTracker().addToDate(currentViewDate, amountToAdd);
                    amountEntry.setText("");
                    amountEntry.setPromptText("Enter amount in hours");
                    updateCoaching();
                    updateCurrentAmount();
                    System.out.println("date exists");
                adminClass.updateSleepTracker(currentViewDate,user.getSleepTracker().getSleepTrackerDict().get(currentViewDate),user);
                }
                else{
                    errorTextFlow.setVisible(false);
                    errorAmount.setVisible(false);
                    user.getSleepTracker().addToDate(currentViewDate, amountToAdd);
                    amountEntry.setText("");
                    amountEntry.setPromptText("Enter amount in hours");
                    updateCoaching();
                    updateCurrentAmount();
                    System.out.println("date does not exist");
                    adminClass.insertSleepTracker(currentViewDate,user.getSleepTracker().getSleepTrackerDict().get(currentViewDate),user);
                }

                 }
            else{
                errorTextFlow.setVisible(true);
                errorAmount.setVisible(true);
            }
        }
        catch (Exception e){
            errorTextFlow.setVisible(true);
            errorAmount.setVisible(true);
        }


    }



    /*
     *
     * Description: This method returns the amount slept on a certain date.
     * Param: LocalDate date, HashMap<LocalDate,Double> sleepTrackerDict
     * Returns: double
     *
     */
    private double getAmountOnDate(LocalDate date, HashMap<LocalDate,Double> sleepTrackerDict) {
        for (HashMap.Entry<LocalDate, Double> entry : sleepTrackerDict.entrySet()) {
            LocalDate dateIter = entry.getKey();
            double sleepIter = entry.getValue();

            //Add the Controller.water volume to corresponding date.
            if (dateIter.equals(date)) {
                return sleepIter;
            }
        }
        return 0;
    }


    /*
     *
     * Description: This method returns the sleepTrackerDictionary of the Controller.user
     * Param: void
     * Returns:HashMap<LocalDate, Double>
     *
     */
    public HashMap<LocalDate, Double> getSleepTrackerDict(){return user.getSleepTracker().getSleepTrackerDict();}
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
