/*********************************************************
 *
 *                   HomeController.java (Controller)
 *
 *
 **********************************************************/

package Controller.user;

import Controller.exercise.ExerciseGoalController;
import Controller.exercise.ExerciseHomeController;
import Controller.exercise.ExerciseTrackerReportController;
import Controller.FitBit.FitBitHomeController;
import Controller.FitBit.FitBitStepsController;
import Controller.reminder.AppReminderController;
import Controller.reminder.ReminderController;
import Controller.tools.ExamController;
import Controller.tools.MusicPlayerController;
import Model.user.User;
import home.*;
import Controller.group.*;
import Controller.sleep.*;
import Controller.water.*;
import Controller.weight.*;
import Controller.food.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;


import java.io.IOException;

public class HomeController {

    private User user;

    private Stage stage;

    private AdminClass adminClass;

    private String heightUnit;
    private String weightUnit;

    @FXML
    MenuBar menuBar;


    @FXML TextField user_enter_name;

    @FXML TextField user_enter_age;
    @FXML TextField user_enter_height;
    @FXML TextField user_enter_weight;
    @FXML Text user_name_null;
    @FXML Text user_gender_null;
    @FXML Text user_age_null;
    @FXML Text user_height_null;
    @FXML Text user_weight_null;

    @FXML Button editAllButton;
    @FXML Button cancelAllButton;
    @FXML Button saveAllButton;


    @FXML Text bmiCategoryText;
    @FXML
    ComboBox genderSelect;
    @FXML ComboBox heightUnitPicker;
    @FXML ComboBox weightUnitPicker;


    @FXML MenuItem fitBitAuthButton;
    @FXML MenuItem fitBitStepsButton;




    @FXML Text nameError;
    @FXML Text ageError;
    @FXML Text genderError;
    @FXML Text heightError;
    @FXML Text weightError;




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

        this.heightUnit = "cm";
        this.weightUnit = "kg";

        heightUnitPicker.getItems().add("cm");
        heightUnitPicker.getItems().add("in");
        heightUnitPicker.getSelectionModel().select(user.getHeightUnitPreference());


        weightUnitPicker.getItems().add("kg");
        weightUnitPicker.getItems().add("lb");
        weightUnitPicker.getSelectionModel().select(user.getWeightUnitPreference());
        user_enter_name.setVisible(false);

        if(user.getName() != null){
            user_name_null.setText(user.getName());
        }
        else{user_name_null.setText("No value set");}



        genderSelect.setVisible(false);


        if(user.getGender() != null){


            User.Gender gen = user.getGender();
            if(gen == User.Gender.FEMALE){
                user_gender_null.setText("FEMALE");
            }
            else{
                user_gender_null.setText("MALE");

            }


        }

        user_enter_age.setVisible(false);

        if(user.getAge() != 0){
            user_age_null.setText(String.valueOf(user.getAge()));
        }
        else{
            user_age_null.setText("No value set");
        }

        user_enter_height.setVisible(false);

        if(user.getHeight() != 0){
            user_height_null.setText(String.valueOf(user.getHeight()));
            changeHeightUnit();

        }
        else{
            user_height_null.setText("No value set");
        }

        user_enter_weight.setVisible(false);

        if(user.getWeight() != 0){
            user_weight_null.setText(String.valueOf(user.getWeight()));
        }
        else{
            user_weight_null.setText("No value set");
        }


        updateBMI();



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
     * Description: This method sets the correct displayed bmi category for the Controller.user.
     * Param: void
     * Returns: void
     *
     */
    private void updateBMI(){
        System.out.println(user.getBMIValue());
        if(user.getHeight() != 0 && user.getWeight() != 0){
            user.calculateBMI();
            bmiCategoryText.setText(user.getBmiCategory());
        }
        else{
            bmiCategoryText.setText("No value");
        }
    }



    /*
     *
     * Description: This method changes the users HeightUnitPreference
     * Param: void
     * Returns: void
     *
     */
    @FXML public void changeHeightUnit(){
        this.heightUnit = heightUnitPicker.getValue().toString();


        if (this.heightUnit.equals("cm")){
            //Update to cm.
            user.setHeightUnitPreference("cm");
            user_height_null.setText(Double.toString(user.getHeight()));
        }
        if(this.heightUnit.equals("in")){
            user.setHeightUnitPreference("in");

            Double inInches = user.getHeight()/2.54;
            user_height_null.setText(Double.toString(inInches));

        }

    }



    /*
     *
     * Description: This method changes the users WeightUnitPreference
     * Param: void
     * Returns: void
     *
     */
    @FXML public void changeWeightUnit(){
        this.weightUnit = weightUnitPicker.getValue().toString();
        System.out.println("changing weight preference");


        if (this.weightUnit.equals("kg")){
            //Update to cm.
            user.setWeightUnitPreference("kg");
            user_weight_null.setText(Double.toString(user.getWeight()));
        }
        if(this.weightUnit.equals("lb")){
            user.setWeightUnitPreference("lb");

            Double inLb = user.getWeight()*2.205;
            user_weight_null.setText(Double.toString(inLb));

        }

    }




    /*
     *
     * Description: This method saves and verifies Controller.user inputted name
     * Param: void
     * Returns: void
     *
     */
    public boolean saveEditName(){


        String nameIn = user_enter_name.getText();

        //If invalid data, display error and ask for re-entry.
        if(nameIn.equals("") || nameIn == null){
            //invalid entry
            nameError.setVisible(true);
            editName();
            return false;
        }
        else{
            user.setName(nameIn);
            nameError.setVisible(false);
            return true;
        }

    }




    /*
     *
     * Description: This method cancel edit name
     * Param: void
     * Returns: void
     *
     */
    @FXML public void cancelEditName(){
        nameError.setVisible(false);

        if(user.getName() != null){
            user_name_null.setText(user.getName());
        }
        else{user_name_null.setText("No value set");}

        user_name_null.setVisible(true);
        user_enter_name.setVisible(false);
        user_enter_name.setText("");



    }



    /*
     *
     * Description: This method allows the Controller.user to attempt to edit name
     * Param: void
     * Returns: void
     *
     */
    @FXML public void editName() {


        user_name_null.setVisible(false);

        if (user.getName() != null) {

            user_enter_name.setVisible(true);
            user_enter_name.setText(user.getName());

        } else {
            user_enter_name.setVisible(true);
            user_enter_name.setPromptText("Enter name.");
        }
    }



    /*
     *
     * Description: This method saves and verifies Controller.user inputted gender
     * Param: void
     * Returns: void
     *
     */
    public boolean saveEditGender(){
        User.Gender gender = (User.Gender) genderSelect.getValue();
        if(genderSelect.getValue() == null){
            genderError.setVisible(true);
            editGender();
            return false;
        }
        else{
            user.setGender(gender);
            genderError.setVisible(false);
            return true;

        }



    }




    /*
     *
     * Description: This method cancels edit of users gender
     * Param: void
     * Returns: void
     *
     */
    @FXML public void cancelEditGender(){

        genderError.setVisible(false);
        genderSelect.setVisible(false);
        user_gender_null.setVisible(true);

        if(user.getGender() != null){
            user_gender_null.setText(user.getGenderString());
        }
        else{user_gender_null.setText("No value set");}






    }



    /*
     *
     * Description: This method allows the Controller.user to edit their gender
     * Param: void
     * Returns: void
     *
     */
    @FXML public void editGender(){





        genderSelect.setVisible(true);
        genderSelect.getItems().removeAll(genderSelect.getItems());
        genderSelect.getItems().addAll(User.Gender.MALE, User.Gender.FEMALE);

        if(user.getGender()!=null){
            genderSelect.setValue(user.getGender());

        }





        user_gender_null.setVisible(false);

    }



    /*
     *
     * Description: This method saves and verifies Controller.user inputted age
     * Param:
     * Returns:
     *
     */
    public boolean saveEditAge(){
        String ageIn = user_enter_age.getText();
        boolean valid = false;
        try {
            int ageConversion = Integer.parseInt(ageIn);
            valid = true;
        }
        catch (NumberFormatException e) {
            valid = false;
        }
        if(valid && Integer.parseInt(ageIn) >0 && Integer.parseInt(ageIn) < 150){
            user.setAge(Integer.parseInt(ageIn));
            ageError.setVisible(false);
            return true;

        }
        else{
            ageError.setVisible(true);
            editAge();
            return false;
        }


    }



    /*
     *
     * Description: This method cancels any edit of age
     * Param:
     * Returns:
     *
     */
    @FXML public void cancelEditAge(){
        ageError.setVisible(false);


        if(user.getAge() != 0){
            user_age_null.setText((String.valueOf(user.getAge())));
        }
        else{user_age_null.setText("No value set");}

        user_age_null.setVisible(true);
        user_enter_age.setVisible(false);


    }



    /*
     *
     * Description: This method allows the Controller.user to attempt to edit their age
     * Param:
     * Returns:
     *
     */
    @FXML public void editAge(){




        user_age_null.setVisible(false);
        if(user.getAge() != 0){

            user_enter_age.setVisible(true);
            user_enter_age.setText(Integer.toString(user.getAge()));

        }
        else{
            user_enter_age.setVisible(true);
            user_enter_age.setPromptText("Enter age.");
        }
    }



    /*
     *
     * Description: This method saves and verifies Controller.user inputted height
     * Param:
     * Returns:
     *
     */
    public boolean saveEditHeight(){
        String heightIn =user_enter_height.getText();
        boolean valid = false;
        try {
            double ageConversion = Double.parseDouble(heightIn);
            valid = true;
        }
        catch (NumberFormatException e) {
            valid = false;
        }
        if(valid &&  Double.parseDouble(heightIn) >0 &&  Double.parseDouble(heightIn) < user.getHEIGHT_MAX_CM() && user.getHeightUnitPreference().equals("cm")){//250
            user.setHeight( Double.parseDouble(heightIn));
            heightError.setVisible(false);
            return true;

        }
        else if(valid &&  Double.parseDouble(heightIn) >0 &&  Double.parseDouble(heightIn) < user.getHEIGHT_MAX_IN() && user.getHeightUnitPreference().equals("in")){//96
            user.setHeight( Double.parseDouble(heightIn)*2.54);
            heightError.setVisible(false);
            return true;

        }
        else{
            heightError.setVisible(true);
            editHeight();
            return false;
        }

    }



    /*
     *
     * Description: This method cancels edit height
     * Param: void
     * Returns: void
     *
     */
    @FXML public void cancelEditHeight(){

        heightError.setVisible(false);

        if(user.getHeight() != 0 && user.getHeightUnitPreference().equals("cm")){
            user_height_null.setText((String.valueOf(user.getHeight())));
        }
        else if(user.getHeight() != 0 && user.getHeightUnitPreference().equals("in")){
            user_height_null.setText((String.valueOf(user.getHeight()/2.54)));
        }
        else{user_height_null.setText("No value set");}

        user_height_null.setVisible(true);
        user_enter_height.setVisible(false);


        updateBMI();
    }


    /*
     *
     * Description: This method allows the Controller.user to attempt to edit their height
     * Param: void
     * Returns: void
     *
     */
    @FXML public void editHeight(){




        user_height_null.setVisible(false);




        if(user.getHeight() != 0){

            user_enter_height.setVisible(true);
            //Display the correct format
            if (this.heightUnit.equals("cm")){
                //Update to cm.
                user_enter_height.setText(Double.toString(user.getHeight()));
            }
            if(this.heightUnit.equals("in")){
                Double inInches = user.getHeight()/2.54;
                user_enter_height.setText(Double.toString(inInches));

            }
            //user_enter_height.setText(Double.toString(Controller.user.getHeight()));

        }
        else{
            user_enter_height.setVisible(true);
            user_enter_height.setPromptText("Enter height.");
        }
    }


    /*
     *
     * Description: This method saves and verifies Controller.user inputted Controller.weight
     * Param:
     * Returns:
     *
     */
    public boolean saveEditWeight(){
        String weightIn =user_enter_weight.getText();
        boolean valid = false;
        try {
            double ageConversion = Double.parseDouble(weightIn);
            valid = true;
        }
        catch (NumberFormatException e) {
            valid = false;
        }

        System.out.println(weightIn);
        System.out.println(valid);
        System.out.println(user.getWeightUnitPreference());
        if(valid &&  Double.parseDouble(weightIn) >0 &&  Double.parseDouble(weightIn) < user.getWEIGHT_MAX_KG() && user.getWeightUnitPreference().equals("kg")){//100
            user.setWeight( Double.parseDouble(weightIn));
            weightError.setVisible(false);
            return true;

        }
        else if(valid &&  Double.parseDouble(weightIn) >0 &&  Double.parseDouble(weightIn) < user.getWEIGHT_MAX_LB() && user.getWeightUnitPreference().equals("lb")){//300
            user.setWeight( Double.parseDouble(weightIn)/2.205);
            weightError.setVisible(false);
            return true;

        }
        else{
            weightError.setVisible(true);
            editWeight();
            return false;
        }




    }



    /*
     *
     * Description: This method cancels edit Controller.weight
     * Param: void
     * Returns:void
     *
     */
    @FXML public void cancelEditWeight(){
        weightError.setVisible(false);

        if(user.getWeight() != 0 && user.getWeightUnitPreference().equals("kg")){
            user_weight_null.setText((String.valueOf(user.getWeight())));
        }
        else if(user.getWeight() != 0 && user.getWeightUnitPreference().equals("lb")){
            user_weight_null.setText((String.valueOf(user.getWeight()*2.205)));
        }
        else{user_weight_null.setText("No value set");}



        user_weight_null.setVisible(true);
        user_enter_weight.setVisible(false);


        updateBMI();
    }


    /*
     *
     * Description: This method allows the Controller.user to attempt to edit their Controller.weight
     * Param:
     * Returns:
     *
     */
    @FXML public void editWeight(){

        user_weight_null.setVisible(false);
        System.out.println(user.getWeightUnitPreference());
        System.out.println(user.getWeight());



        if(user.getWeight() != 0){
            user_enter_weight.setVisible(true);

            if(user.getWeightUnitPreference().equals("kg")) {
                user_enter_weight.setText(Double.toString(user.getWeight()));
            }
            if(user.getWeightUnitPreference().equals("lb")){
                System.out.println("here");
                user_enter_weight.setText(Double.toString(user.getWeight()*2.205));

            }

        }
        else{
            user_enter_weight.setVisible(true);
            user_enter_weight.setPromptText("Enter weight.");
        }
    }


    /*
     *
     * Description: This method calls all the edit methods
     * Param: void
     * Returns: void
     *
     */
    @FXML void editAll(){
        editAllButton.setVisible(false);
        cancelAllButton.setVisible(true);
        saveAllButton.setVisible(true);
        System.out.println("editing information");

        editName();
        editGender();
        editAge();
        editHeight();
        editWeight();


    }


    /*
     *
     * Description: This method calls all cancel methods
     * Param: void
     * Returns: void
     *
     */
    @FXML void cancelAll(){
        editAllButton.setVisible(true);
        cancelAllButton.setVisible(false);
        saveAllButton.setVisible(false);


        cancelEditName();
        cancelEditGender();
        cancelEditAge();
        cancelEditHeight();
        cancelEditWeight();

    }


    /*
     *
     * Description: This method calls all save methods
     * Param: void
     * Returns: void
     *
     */
    @FXML void saveAll() throws IOException {
        if(
                saveEditName() &
                        saveEditHeight() &
                        saveEditGender() &
                        saveEditAge() &
                        saveEditWeight()){
            adminClass.updateUser(user);


            cancelAll();


        }

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
