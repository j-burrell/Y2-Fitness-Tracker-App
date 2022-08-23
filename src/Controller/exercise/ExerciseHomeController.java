/*********************************************************
 *
 *                   ExerciseHomeController.java (Controller)
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import Controller.weight.WeightTrackerController;
import Controller.weight.WeightTrackerReportController;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;


public class ExerciseHomeController {


    private Stage stage;
    private User user;
    private AdminClass adminClass;

    private String exType;
    private Exercise selectedExercise;

    @FXML Text durationLabel;

    @FXML Label exTypeVal;
    @FXML Label exDurationVal;
    @FXML Label exSTimeVal;
    @FXML Label exETimeVal;


    @FXML Button createExerciseButton;
    @FXML Button createNew;
    @FXML Button removeExButton;
    @FXML Button backButton;

    @FXML ComboBox exercisePicker;
    @FXML TextField otherExerciseEntry;

    @FXML Text coachMSG;
    @FXML TextFlow coachTextFlow;

    @FXML Text errorText;

    @FXML TextField userEnterSTimeTF;
    @FXML TextField userEnterETimeTF;
    @FXML TextField userEnterDurationTF;


    @FXML DatePicker datePicker;

    @FXML private TableView<Exercise> exerciseTable;
    @FXML private TableColumn<Exercise,String> exerciseNameColumn;
    @FXML MenuItem fitBitAuthButton;
    @FXML MenuItem fitBitStepsButton;

    private ObservableList<Exercise> exerciseData = FXCollections.observableArrayList();



    /*
     *
     * Description: This method initialises the view page and all the data it needs to load
     * Param: Stage stage, User Controller.user, AdminClass adminClass
     * Returns: void
     *
     */
    @FXML
    public void initData(Stage stage, User user,AdminClass adminClass){
        coachTextFlow.setVisible(false);
        this.stage = stage;
        this.user = user;
        this.adminClass = adminClass;

        removeExButton.setVisible(false);
        backButton.setVisible(false);
        errorText.setVisible(false);

        datePicker.setValue(LocalDate.now());


        createNew.setVisible(true);


        ArrayList<Exercise> exercisesOnDate = user.getExerciseTracker().getExerciseTracker().get(datePicker.getValue());


        if(exercisesOnDate!=null) {
            for (Exercise e : exercisesOnDate) {

                exerciseData.add(e);
            }

        }

            exerciseTable.setItems(exerciseData);

        exerciseTable =new TableView<Exercise>();


        reload();

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
     * Description: removes an Model.exercise
     * Param: void
     * Returns: void
     *
     */
    @FXML public void removeExercise(){

        System.out.println("attempting to remove");

        System.out.println(selectedExercise);
        user.getExerciseTracker().removeExercise(datePicker.getValue(),selectedExercise);
        AdminClass.sqlExeStatement(adminClass.deleteExercise(user,selectedExercise,datePicker.getValue()));
        exerciseData.remove(selectedExercise);
        reload();


    }

    /*
     *
     * Description: reloads all data in the page
     * Param: void
     * Returns: void
     *
     */
    @FXML public void reload(){
        System.out.println(checkDailyAverage());
        if(!checkDailyAverage()){
            System.out.println("outputting coach msg");
            coachTextFlow.setVisible(true);
            coachMSG.setVisible(true);
            coachMSG.setVisible(true);
            coachMSG.setText("You are below the daily advised amount of Model.exercise per day. Please attempt to do more to stay healthy!");
        }
        else{
            coachTextFlow.setVisible(false);
            coachMSG.setVisible(false);}

        //exerciseNameColumn.setCellValueFactory(cellData->cellData.getValue().getNameProperty());

        ArrayList<Exercise> exercisesOnDate = user.getExerciseTracker().getExerciseTracker().get(datePicker.getValue());

        ObservableList<Exercise> ex = FXCollections.observableArrayList();

        if(exerciseData!=null) {
            for (Exercise e : exerciseData) {
                ex.add(e);
            }
        }
        exerciseData.removeAll(ex);
       if(exercisesOnDate!=null) {
           System.out.println("how many exercises： "+exercisesOnDate.size());
            for (Exercise e : exercisesOnDate) {

                exerciseData.add(e);
                System.out.println(e);
            }
           System.out.println("size of exerciseDate:"+exerciseData.size());
            exerciseTable.setItems(exerciseData);
        }

        exerciseTable =new TableView<Exercise>();

    }

    /*
     *
     * Description: This method initialises the view page and all the data it needs to load
     * Param: Stage stage, User Controller.user, AdminClass adminClass
     * Returns: void
     *
     */
    //@FXML
    public void initialize() {



        exerciseNameColumn.setCellValueFactory(cellData->cellData.getValue().getNameProperty());

        // Clear person details.

        showGroupDetails(null);
        // Listen for selection changes and show the Controller.group details when changed.
        exerciseTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showGroupDetails(newValue));
        exerciseTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> this.selectedExercise=newValue);



    }

    /*
     *
     * Description: This method shows details about an Model.exercise
     * Param: Exercise Model.exercise
     * Returns: void
     *
     */
    private void showGroupDetails(Exercise exercise)
    {
        if(exercise!=null)
        {
            removeExButton.setVisible(true);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.UK);

            exTypeVal.setText(exercise.getExercise_type());
            exDurationVal.setText(Integer.toString(exercise.getDuration()));
            exSTimeVal.setText(formatter.format(exercise.getStart_time()));
            exETimeVal.setText(formatter.format(exercise.getEnd_time()));

        }
        else
        {
           // exTypeVal.setText("");

        }
    }

    /*
     *
     * Description: This method checks what Model.exercise type is picked
     * Param: void
     * Returns: void
     *
     */
    @FXML void checkPicked(){
        if(exercisePicker.getValue() == "Other"){
            otherExerciseEntry.setVisible(true);
        }
        else{
            otherExerciseEntry.setVisible(false);
        }
    }


    /*
     *
     * Description: This method returns to the "normal" table view
     * Param: void
     * Returns: void
     *
     */
    @FXML void backToTable(){
        removeExButton.setVisible(false);
        backButton.setVisible(false);
        exTypeVal.setVisible(true);
        exDurationVal.setVisible(true);
        exSTimeVal.setVisible(true);
        exETimeVal.setVisible(true);

        removeExButton.setVisible(true);


        durationLabel.setVisible(true);



        userEnterETimeTF.setVisible(false);
        userEnterSTimeTF.setVisible(false);
        exercisePicker.setVisible(false);
        otherExerciseEntry.setVisible(false);
        createNew.setVisible(true);
        reload();

    }


    /*
     *
     * Description: This method adds an Model.exercise
     * Param: void
     * Returns: void
     *
     */
    @FXML void addExercise(){
        backButton.setVisible(true);
        removeExButton.setVisible(false);

        createNew.setVisible(false);
        exTypeVal.setVisible(false);
        exDurationVal.setVisible(false);
        exSTimeVal.setVisible(false);
        exETimeVal.setVisible(false);

        removeExButton.setVisible(false);


        durationLabel.setVisible(false);


        userEnterETimeTF.setVisible(true);
        userEnterSTimeTF.setVisible(true);
        exercisePicker.setVisible(true);
        ArrayList<String> exs = adminClass.getExerciseTypes();
        System.out.println("size of exs: " + exs.size());
        for (String ex : exs){
            if(!exercisePicker.getItems().contains(ex)) {
                exercisePicker.getItems().add(ex);
            }
        }
        if(!exercisePicker.getItems().contains("Other")) {
            exercisePicker.getItems().add("Other");
        }

    }

    /*
     *
     * Description: This method adds an added method to the Model.database
     * Param: LocalTime sTime, LocalTime eTime, String exType
     * Returns: void
     *
     */
    private void create(LocalTime sTime, LocalTime eTime, String exType){
        Exercise ex = new Exercise(exType, sTime, eTime);
        ArrayList<Exercise> exArray = user.getExerciseTracker().getExerciseTracker().get(datePicker.getValue());
        if (!isOverlap(exArray, ex)) {
            //No overlap, allow the creation
            user.getExerciseTracker().addExercise(exType, sTime, eTime, datePicker.getValue());
            if (!adminClass.getExerciseTypes().contains(exType)) {
                adminClass.addExerciseType(exType);
            }
            adminClass.sqlExeStatement(adminClass.insertExerciseTracker(user,new Exercise(exType,sTime,eTime),datePicker.getValue()));

            userEnterSTimeTF.setText("");
            userEnterETimeTF.setText("");
            exerciseData.add(ex);
            exerciseNameColumn.setCellValueFactory(cellData->cellData.getValue().getNameProperty());
            errorText.setVisible(false);
            reload();
            backToTable();
            reload();

        }
        else {
            errorText.setVisible(true);

            errorText.setText("You already have an Model.exercise at that time.");
        }


    }



    /*
     *
     * Description: This method creates the fields that is required to create an Model.exercise.
     * Param: void
     * Returns: void
     *
     */
    @FXML
    void createExercise() {
        LocalTime sTime;
        LocalTime eTime;
        String sTimeRaw = userEnterSTimeTF.getText();
        String eTimeRaw = userEnterETimeTF.getText();

        if (checkTimeFormat(sTimeRaw, eTimeRaw)) {
            try{
                sTime = LocalTime.parse(sTimeRaw);
                eTime = LocalTime.parse(eTimeRaw);


                if (!exceriseTimes(sTime, eTime)) {
                    //Start time is not before end time
                    errorText.setVisible(true);
                    errorText.setText("End time is before start time");
                } else {
                    if (exercisePicker.getValue() == null) {
                        System.out.println("no Model.exercise selected");
                        errorText.setVisible(true);
                        errorText.setText("no Model.exercise type selected");
                    } else {

                        if (exercisePicker.getValue().toString().equals("Other")) {
                            if (otherExerciseEntry.getText().equals("") || otherExerciseEntry.getText() == null) {
                                errorText.setVisible(true);
                                errorText.setText("no Model.exercise type entered");
                            } else {
                                exType = otherExerciseEntry.getText();
                                AdminClass.sqlExeStatement(AdminClass.insertExerciseType(exType));
                                create(sTime, eTime, exType);
                            }

                        } else {
                            exType = exercisePicker.getValue().toString();
                            create(sTime, eTime, exType);
                            //AdminClass.sqlExeStatement(AdminClass.insertExerciseType(exType));
                        }
                    }


                }
        }catch(Exception e)
        {
            errorText.setVisible(true);
            errorText.setText("incorrect format");
        }
            } else{
                errorText.setVisible(true);

                errorText.setText("Time entered is incorrect");


            }



            reload();

    }
    /*


     *
     * Description: This method checks to see if the Controller.user has had their daily average of Model.exercise
     * Param: void
     * Returns: boolean
     *
     */
    private boolean checkDailyAverage(){
        ArrayList<Exercise> todaysExercise = user.getExerciseTracker().getExerciseTracker().get(datePicker.getValue());
        double count = 0;
        if(todaysExercise != null){
            for (Exercise ex : todaysExercise) {
                count = count + ex.getDuration();
            }
            System.out.println("amount done on this day " + count);
            if (count < 20) {
                return false;
            } else {
                return true;
            }
        }
        else{return false;}
    }

    /*
     *
     * Description: This method checks that inputted time is the correct format.
     * Param: String sTime, String eTime
     * Returns: boolean
     *
     */
    private boolean checkTimeFormat(String sTime, String eTime){
        if (!sTime.matches("^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$")){
            return false;
        }
        if (!eTime.matches("^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$")){
            return false;
        }
        return true;

    }


    /*
     *
     * Description: This method ensures start time is before end time
     * Param: LocalTime start, LocalTime end
     * Returns: boolean
     *
     */
    private  boolean exceriseTimes(LocalTime start, LocalTime end){

        boolean valid = false;
        if(start.isAfter(end)){

            System.out.println("Start time is after end time.");
        }
        else if(end.isBefore(start)){

            System.out.println("End time is before start.");
        }
        else{ //times are fine.

            valid = true;
        }

        return valid;
    }


    /*
     *
     * Description: This method ensures a time falls between 2 other times
     * Param: LocalTime f, LocalTime start, LocalTime end
     * Returns: boolean
     *
     */
    private boolean isBetween(LocalTime f, LocalTime start, LocalTime end) {

        boolean outcome = false;
        if (f.isAfter(start) && f.isBefore(end)) {
            outcome = true;
        }
        return outcome;
    }

    /*
     *
     * Description: This method ensures if an Model.exercise takes place between 2 other times
     * Param: ArrayList<Exercise> e_array, Exercise Model.exercise
     * Returns:boolean
     *
     */
    private boolean isOverlap(ArrayList<Exercise> e_array, Exercise exercise) {
        if(e_array == null){
            System.out.println("array is null");
        }

        boolean overlap = false;
        if(e_array != null){
            for (Exercise current : e_array) {
                if (current != null) {
                    //If the start date or end is in between another exercises start or end time.
                    if (isBetween(exercise.getStart_time(), current.getStart_time(), current.getEnd_time()) ||
                            isBetween(exercise.getEnd_time(), current.getStart_time(), current.getEnd_time())) {

                        overlap = true;
                    }
                    //Or if the start time and end times go either side of the current one.
                    else if (exercise.getStart_time().isBefore(current.getStart_time()) &&
                            exercise.getEnd_time().isAfter(current.getEnd_time())) {

                        overlap = true;
                    }
                }
            }

        }
        return overlap;
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
