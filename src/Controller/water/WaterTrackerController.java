/*********************************************************
 *
 *                   WaterTrackerController.java (Controller)
 *
 *
 **********************************************************/

package Controller.water;

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
import Controller.sleep.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import Controller.weight.WeightTrackerController;
import Controller.weight.WeightTrackerReportController;

import java.time.LocalDate;

import java.io.IOException;
import java.util.HashMap;

public class WaterTrackerController {

    private User user;

    private Stage stage;

    private AdminClass adminClass;

    private LocalDate currentViewDate;


    @FXML
    MenuBar menuBar;

    @FXML DatePicker datePicker;

    @FXML Text coachingMSG;
    @FXML TextField amountEntry;
    @FXML Button amountButton;
    @FXML TextFlow coachingMSGF;
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
        updateCoaching();
        updateCurrentAmount();
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
     * Description: Method that updates users intake amount message based on coaching
     * Param: void
     * Returns: void
     *
     */
    private void updateCoaching(){
        String msg;
        int currDrank = getAmountOnDate(currentViewDate,user.getWaterTracker().getWaterTrackerDict());
        if(currDrank < user.getWaterTracker().getAdviceAmount()) {
            msg = "You have currently drank " + currDrank + "ml out of " + user.getWaterTracker().getAdviceAmount()
                    + "ml. you need to drink " + (user.getWaterTracker().getAdviceAmount() - currDrank) + "ml to drink the daily adviced amount.";
        }
        else{
            msg = "You have drank enough to meet our advised amount today!";
        }
        coachingMSG.setText(msg);
    }




    /*
     *
     * Description: Method that updates current volume
     * Param: void
     * Returns: void
     *
     */
    private void updateCurrentAmount(){
        int currDrank = getAmountOnDate(currentViewDate,user.getWaterTracker().getWaterTrackerDict());
       // currentAmount.setText("You have currently drank " + String.valueOf(currDrank) + "ml.");
    }



    /*
     *
     * Description: Method that changes the focus date and loads any needed information
     * Param: void
     * Returns: void
     *
     */
    @FXML public void changeDate(){
        System.out.println("changing date");
        currentViewDate=datePicker.getValue();

        updateCurrentAmount();
        updateCoaching();
    }




    /*
     *
     * Description: Method that adds an amount of volume to the focused date
     * Param: void
     * Returns: void
     *
     */
    @FXML public void add(){

        try {
            int amountToAdd = Integer.parseInt(amountEntry.getText());

            if(amountToAdd >0 && amountToAdd< 20000) {


                if (user.getWaterTracker().getWaterTrackerDict().containsKey(currentViewDate)) {

                    errorTextFlow.setVisible(false);
                    errorAmount.setVisible(false);
                    user.getWaterTracker().addToDate(currentViewDate, amountToAdd);
                    amountEntry.setText("");
                    amountEntry.setPromptText("Enter amount in ml");
                    updateCoaching();
                    updateCurrentAmount();
                    adminClass.updateWaterTracker(currentViewDate,user.getWaterTracker().getWaterTrackerDict().get(currentViewDate),user);

                    // adminClass.serializeSave();
                }
                else{
                    errorTextFlow.setVisible(false);
                    errorAmount.setVisible(false);
                    user.getWaterTracker().addToDate(currentViewDate, amountToAdd);
                    amountEntry.setText("");
                    amountEntry.setPromptText("Enter amount in ml");
                    updateCoaching();
                    updateCurrentAmount();
                    adminClass.insertWaterTracker(currentViewDate,user.getWaterTracker().getWaterTrackerDict().get(currentViewDate),user);

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
     * Description: Accessor method that returns the volume drank on a certain date
     * Param: LocalDate date, HashMap<LocalDate,Integer> waterTrackerDict
     * Returns: int
     *
     */
    private int getAmountOnDate(LocalDate date, HashMap<LocalDate,Integer> waterTrackerDict) {
        for (HashMap.Entry<LocalDate, Integer> entry : waterTrackerDict.entrySet()) {
            LocalDate dateIter = entry.getKey();
            int volumeIter = entry.getValue();

            //Add the Controller.water volume to corresponding date.
            if (dateIter.equals(date)) {
                return volumeIter;
            }
        }
        return 0;
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
