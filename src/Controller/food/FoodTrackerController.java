/*********************************************************
 *
 *                   FoodTrackerController.java (Controller)
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
import Model.food.MealType;
import Model.food.MealTypeProperty;
import Controller.group.GroupController;
import Model.user.User;
import home.AdminClass;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import Controller.weight.WeightTrackerReportController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import Controller.sleep.SleepTrackerController;
import Controller.sleep.SleepTrackerReportController;
import Controller.user.HomeController;
import Controller.user.LoginController;
import Controller.water.WaterTrackerController;
import Controller.water.WaterTrackerReportController;
import Controller.weight.WeightTrackerController;

public class FoodTrackerController implements Initializable{

    private User user;

    private Stage stage;

    private AdminClass adminClass;

    private LocalDate currentViewDate;


    @FXML
    private TextField name;

    @FXML
    private TextField calorie;

    @FXML
    private Button addMealType;

    @FXML
    MenuBar menuBar;

    @FXML DatePicker datePicker;
    @FXML
    private TableView<MealTypeProperty> tableView;
    @FXML
    private ComboBox<String> lunchCombo;

    @FXML
    private ComboBox<String> dinnerCombo;

    @FXML
    private ComboBox<String> snackCombo;

    @FXML
    private TextField lunchCount;

    @FXML
    private TextField dinnerCount;

    @FXML
    private TextField snackCount;
    @FXML
    private ComboBox<String> breakfastCombo;

    @FXML
    private TextField breakfastCount;
    @FXML
    private Button setMeal;

    @FXML
    private TextArea mealListTxt;
    @FXML
    private Label amount;


    @FXML
    Text errorAmount;
    @FXML
    GridPane errorTextFlow;

    @FXML
    MenuItem fitBitAuthButton;
    @FXML MenuItem fitBitStepsButton;


    StringProperty mealList = new SimpleStringProperty("");
    StringProperty calorieAmount = new SimpleStringProperty("");
    Meal meal;

	ObservableList<MealTypeProperty> data ;
	ObservableList<MealTypeProperty> tempdata = FXCollections.observableArrayList();
	ObservableList<String> options =  FXCollections.observableArrayList();
	private List<MealType> breakfast = new ArrayList<>();
    private List<MealType> lunch = new ArrayList<>();
    private List<MealType> dinner = new ArrayList<>();
    private List<MealType> snack = new ArrayList<>();
    HashMap<String,Integer> foodCalorie;
    StringConverter<Object> sc = new StringConverter<Object>() {

        @Override
        public String toString(Object t) {

            return t == null ? null : t.toString();

        }



        @Override

        public Object fromString(String string) {

            return string;

        }

    };


    /*
     *
     * Description: this method is called to update data items and information on the screen.
     * Param: void
     * Returns: void
     *
     */
    public void update()  {
        breakfast.clear();
        lunch.clear();
        dinner.clear();
        snack.clear();

    	mealList.set("");
    	options.clear();
    	data= FileTool.getMeals();
    	foodCalorie = new HashMap<>();
    	for (MealTypeProperty mealType:data) {
    		foodCalorie.put(mealType.getName(), mealType.getCalorie());

		}

        tableView.setItems(data);
        for(MealTypeProperty m:data) {
        	options.add(m.getName());

        }

        breakfastCombo.setItems(options);


        lunchCombo.setItems(options);



        dinnerCombo.setItems(options);



        snackCombo.setItems(options);

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
        currentViewDate = LocalDate.now();
        datePicker.setValue(currentViewDate);

        errorTextFlow.setVisible(false);
        errorAmount.setVisible(false);

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
     * Description: This method gets called when the date changes and adds FoodTracker information
     * Param: void
     * Returns: void
     *
     */
    @FXML public void changeDate(){

        currentViewDate=datePicker.getValue();
        breakfast = new ArrayList<>();
        lunch = new ArrayList<>();
        dinner = new ArrayList<>();
        snack = new ArrayList<>();
        meal = new Meal();

        meal.setBreakfast(breakfast);
        meal.setLunch(lunch);
        meal.setDinner(dinner);
        meal.setSnack(snack);


        Meal tempMeal =user.getFoodTracker().getDailyMeal().get(currentViewDate);
        if(tempMeal!=null) {
        	 calorieAmount.set(tempMeal.getTotalCalorie()+"");
        }else {
        	calorieAmount.set("00");
        }
        breakfastCombo.setValue(null);
        lunchCombo.setValue(null);
        dinnerCombo.setValue(null);
        snackCombo.setValue(null);

        mealList.set("");
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





    @Override
	public void initialize(URL location, ResourceBundle resources) {
        meal = new Meal();
        meal.setBreakfast(breakfast);
        meal.setLunch(lunch);
        meal.setDinner(dinner);
        meal.setSnack(snack);

		mealListTxt.textProperty().bind(mealList);
		amount.textProperty().bind(calorieAmount);


		//Set recipe of the day
		setMeal.setOnAction(e->{


		                user.getFoodTracker().addToDate(currentViewDate, meal);
		                FoodDB.addFoodTracker(user.getFoodTracker());

//		                adminClass.serializeSave();
		});



	     TableColumn nameCol = new TableColumn();

	     nameCol.setText("Name");

	     nameCol.setCellValueFactory(new PropertyValueFactory("name"));

	     nameCol.setCellFactory(TextFieldTableCell.forTableColumn(sc));





	     TableColumn calorieCol = new TableColumn();

	     calorieCol.setText("Calorie");

	     calorieCol.setCellValueFactory(new PropertyValueFactory("calorie"));

	     calorieCol.setCellFactory(TextFieldTableCell.forTableColumn(sc));


	     tableView.getColumns().addAll(nameCol ,calorieCol);

	     update();



             addMealType.setOnAction(e -> {

                 if(!name.getText() .equals("") && !calorie.getText().trim().equals("") ) {
                    try {
                        int x = Integer.parseInt(calorie.getText());
                        errorTextFlow.setVisible(false);
                        errorAmount.setVisible(false);
                        FileTool.addMeal(new MealType(name.getText(), Integer.parseInt(calorie.getText())));
                        update();
                    }
                    catch (Exception ex){
                        errorAmount.setText("Calorie value not valid");
                        errorAmount.setVisible(true);
                        errorTextFlow.setVisible(true);
                    }
                 }
                 else{
                     errorAmount.setText("Not all information entered");
                     errorAmount.setVisible(true);
                     errorTextFlow.setVisible(true);

                 }

             });
             breakfastCombo.valueProperty().addListener(new ChangeListener<String>() {

                 @Override
                 public void changed(ObservableValue ov, String v1, String v2) {

                     int count = Integer.parseInt(breakfastCount.getText());

                     if (v2 != null) {
                         for (int i = 0; i < count; i++) {
                             breakfast.add(new MealType(v2, foodCalorie.get(v2)));
                         }
                         mealList.set(mealList.get() + "breakfast: " + count +" "+ v2 + "\n");
                         System.out.println(meal.getTotalCalorie());
                         calorieAmount.set(meal.getTotalCalorie() + "");
                     }


                 }
             });
             lunchCombo.valueProperty().addListener(new ChangeListener<String>() {
                 @Override
                 public void changed(ObservableValue ov, String v1, String v2) {
                     int count = Integer.parseInt(lunchCount.getText());
                     if (v2 != null) {
                         for (int i = 0; i < count; i++) {
                             lunch.add(new MealType(v2, foodCalorie.get(v2)));
                         }
                         System.out.println(meal.getTotalCalorie());
                         mealList.set(mealList.get() + "lunch: " + count  +" "+ v2 + "\n");
                         calorieAmount.set(meal.getTotalCalorie() + "");
                     }
                 }
             });
             dinnerCombo.valueProperty().addListener(new ChangeListener<String>() {
                 @Override
                 public void changed(ObservableValue ov, String v1, String v2) {
                     int count = Integer.parseInt(dinnerCount.getText());
                     if (v2 != null) {
                         for (int i = 0; i < count; i++) {
                             dinner.add(new MealType(v2, foodCalorie.get(v2)));
                         }
                         System.out.println(meal.getTotalCalorie());
                         mealList.set(mealList.get() + "dinner: " + count +" " + v2 + "\n");
                         calorieAmount.set(meal.getTotalCalorie() + "");
                     }
                 }
             });
             snackCombo.valueProperty().addListener(new ChangeListener<String>() {
                 @Override
                 public void changed(ObservableValue ov, String v1, String v2) {
                     int count = Integer.parseInt(snackCount.getText());
                     if (v2 != null) {
                         for (int i = 0; i < count; i++) {
                             snack.add(new MealType(v2, foodCalorie.get(v2)));
                         }
                         System.out.println(meal.getTotalCalorie());
                         mealList.set(mealList.get() + "snack: " + count +" " + v2 + "\n");
                         calorieAmount.set(meal.getTotalCalorie() + "");
                     }
                 }
             });



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
