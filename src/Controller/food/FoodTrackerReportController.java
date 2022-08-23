/*********************************************************
 *
 *                   FoodTrackerReportController.java (Controller)
 *
 *
 **********************************************************/

package Controller.food;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import Controller.FitBit.FitBitHomeController;
import Controller.FitBit.FitBitStepsController;
import Controller.reminder.AppReminderController;
import Controller.reminder.ReminderController;
import Controller.tools.ExamController;
import Controller.tools.MusicPlayerController;
import Model.database.FoodDB;
import Controller.exercise.ExerciseGoalController;
import Controller.exercise.ExerciseHomeController;
import Controller.exercise.ExerciseTrackerReportController;
import Model.food.Meal;
import Model.food.MealTypeProperty;
import Controller.group.GroupController;
import home.AdminClass;
import Controller.weight.WeightTrackerController;
import Controller.weight.WeightTrackerReportController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import Controller.sleep.SleepTrackerController;
import Controller.sleep.SleepTrackerReportController;
import Controller.user.HomeController;
import Controller.user.LoginController;
import Model.user.User;
import Controller.water.WaterTrackerController;
import Controller.water.WaterTrackerReportController;

public class FoodTrackerReportController {

    private User user;

    private Stage stage;

    private AdminClass adminClass;
    private LocalDate currentViewDate;
    

    @FXML
    private MenuBar menuBar;

    @FXML
    private DatePicker startDate;

    @FXML
    private DatePicker endDate;

    @FXML
    private Button generateReportButton;

    @FXML NumberAxis yAxis;
    @FXML CategoryAxis xAxis;
    @FXML LineChart<Integer,String> lc;
 

    @FXML
    private Text dateError;

    @FXML
    private BarChart<String, Integer> bc;

    @FXML
    private CategoryAxis xAxis1;

    @FXML
    private NumberAxis yAxis1;

    @FXML
    private DatePicker singleDay;

    @FXML
    MenuItem fitBitAuthButton;
    @FXML MenuItem fitBitStepsButton;

    /*
     *
     * Description: This method gets called when the date changes and adds FoodTracker information
     * Param: void
     * Returns: void
     *
     */
    @FXML public void changeDate(){
    	   bc.getData().clear();
           bc.layout();
        currentViewDate=singleDay.getValue();
        System.out.println("new:"+currentViewDate.toString());
        
        Meal tempMeal =FoodDB.getFoodTracker(user).getDailyMeal().get(currentViewDate);
        List<String> foodNameList = new ArrayList<>();
       for (MealTypeProperty meal: FileTool.getMeals()) {
    	   foodNameList.add(meal.getName());
		
	};

       if(tempMeal!=null){
            HashMap<String,Integer> foodCount = tempMeal.getFoodCount(foodNameList);
            bc.setTitle("FoodTracker report");
            //xAxis1.setLabel("Type");

            yAxis1.setLabel("Count");
            xAxis1.setTickLabelRotation(90);


            XYChart.Series dataSeries1 = new XYChart.Series();
            dataSeries1.setName("Count");

            for (HashMap.Entry<String, Integer> entry : foodCount.entrySet()) {
            if(entry.getValue()!=0){
                dataSeries1.getData().add(new XYChart.Data(entry.getKey(),entry.getValue()));
            }
            }

            bc.getData().addAll(dataSeries1);

        }else {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("No Data");
            alert.setContentText("No food Data on this date");

            alert.showAndWait();

        }

    }


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
    }




    /*
     *
     * Description: This method generates a graph from inputted dates
     * Param: void
     * Returns: void
     *
     */
    @FXML public void getReport(){
    	   lc.getData().clear();
           lc.layout();
        LocalDate sDate = startDate.getValue();
        LocalDate eDate = endDate.getValue();

        if(sDate == null || eDate == null){
            dateError.setText("Dates not selected");
            dateError.setVisible(true);
        }else {

            if (verifyDates(sDate, eDate)) {
                dateError.setVisible(false);
                ArrayList<Integer> report = FoodDB.getFoodTracker(user).generateReport(sDate, eDate);

                lc.setTitle("FoodTracker report");
                lc.setLegendVisible(false);

                //xAxis.setLabel("Date");

                yAxis.setLabel("Calorie");
                xAxis.setTickLabelRotation(90);


                XYChart.Series dataSeries1 = new XYChart.Series();
                dataSeries1.setName("Calorie");


                LocalDate date = sDate;

                System.out.println(report.size());
                for (int amount : report) {

                    if (amount > 0) {
                        dataSeries1.getData().add(new XYChart.Data(date.toString(), amount));
                    }
                    date = date.plusDays(1);
                }
                //xAxis.setCategories(FXCollections.observableArrayList(x));
                lc.getData().addAll(dataSeries1);

            }
            else{
                dateError.setText("Start Date is not before End Date");
                dateError.setVisible(true);
            }
        }
       
    }


    /*
     *
     * Description: This method clears the current data in the chart
     * Param: void
     * Returns: void
     *
     */
    @FXML
    void clearChart() {
        lc.getData().clear();

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


	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
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
