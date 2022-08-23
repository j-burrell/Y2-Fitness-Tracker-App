/*********************************************************
 *
 *                   SingleGroupController.java (Controller)
 *
 *
 **********************************************************/

package Controller.group;

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
import Model.group.Group;
import Model.group.GroupGoal;
import Model.user.User;
import home.*;
import Controller.user.*;
import Controller.sleep.*;
import Model.exercise.*;
import Controller.water.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import Controller.weight.WeightTrackerController;
import Controller.weight.WeightTrackerReportController;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class SingleGroupController {
    private Stage stage;
    private User user;
    private Group group;
    private AdminClass adminClass;
    private GroupGoal chosenGoal;
    private GroupGoal editingGoal;
    private XYChart.Series<String, Number> series1 = new XYChart.Series();


    @FXML private TableView<GroupGoal> groupGoalTable;
    @FXML private TableColumn<GroupGoal,String> goalColumn;


    @FXML private Label groupNameLabel;
    @FXML private Label adminLabel;

    @FXML public GridPane infoGrid;
    @FXML private Label goalNameLabel;
    @FXML private Label exerciseLabel;
    @FXML private Label groupGoalLabel;
    @FXML private Label startDateLabel;
    @FXML private Label endDateLabel;

    @FXML private Button newGoalButton;
    @FXML private Button progressButton;
    @FXML public Button editButton;
    @FXML public Button doneButton;
    @FXML public Button backToGoalButton;
    @FXML public Button backToGroupButton;
    @FXML public Button deleteButton;
    @FXML public AnchorPane infoPane;
    //////////////////
    @FXML private Button createButton;
    @FXML public TextField newGoalName;
    @FXML public TextField newGoal;
    @FXML public DatePicker newStartDate;
    @FXML public DatePicker newEndDate;

    @FXML public Text errorMessage;

    ///////////////////
    @FXML public ComboBox exerciseChoiceBox;
    //////////////////////////
    @FXML MenuItem fitBitAuthButton;
    @FXML MenuItem fitBitStepsButton;

    @FXML public AnchorPane progressPane;
    @FXML public ProgressBar progress;
    @FXML public Text progress_message;
    @FXML public BarChart<String,Number> progressByDay;
    @FXML public CategoryAxis xAxis;
    @FXML public NumberAxis yAxis;
    private ObservableList<GroupGoal> goalData = FXCollections.observableArrayList();

    /*
     *
     * Description: Default constructor
     * Param: void
     * Returns: void
     *
     */
    public SingleGroupController()
    {}


    /*
     *
     * Description: This method initialises the view page and all the data it needs to load
     * Param: Stage stage, User Controller.user, AdminClass adminClass
     * Returns: void
     *
     */
    public void initData(User user, Group group,Stage stage,AdminClass adminClass)
    {

        this.group=group;
        this.user=user;
        this.stage=stage;
        this.adminClass=adminClass;
        groupNameLabel.setText(group.getGroupName());
        adminLabel.setText(group.getAdmin().getName());


        goalData.addAll(group.getGroupGoal());
        groupGoalTable.setItems(goalData);

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
     * Description:Initializes the controller class. This method is automatically called after the fxml file has been loaded.
     * Param: void
     * Returns: void
     *
     */
    public void initialize()
    {
        progressByDay.getData().add(series1);
        goalColumn.setCellValueFactory(cellData->cellData.getValue().getGoalNameProperty());
        // Clear person details.
        showGroupDetails(null);
        // Listen for selection changes and show the Controller.group details when changed.
        groupGoalTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showGroupDetails(newValue));
    }


    /*
     *
     * Description: This method shows Controller.group goal details on the right panel.
     * Param:  GroupGoal g
     * Returns: void
     *
     */
    public void showGroupDetails(GroupGoal g)
    {
        if(group!=null)
        {
            exerciseLabel.setText(g.getExercise());
            goalNameLabel.setText(g.getGoalName());
            groupGoalLabel.setText(Double.toString(g.getGroupGoal()));
            startDateLabel.setText(DateUtil.format(g.getStartDate()));
            endDateLabel.setText(DateUtil.format(g.getEndDate()));
            chosenGoal=g;
        }
        else
        {
            exerciseLabel.setText("");
            goalNameLabel.setText("");
            groupGoalLabel.setText("");
            startDateLabel.setText("");
            endDateLabel.setText("");
        }
    }



    /*
     *
     * Description: This method shows the create Goal interface
     * Param: void
     * Returns: void
     *
     */
    public void newGoal() throws IOException {

        newGoalName.setText("");
        newGoal.setText("");
        newStartDate.setValue(null);
        newEndDate.setValue(null);
        newGoalName.visibleProperty().set(true);
        newGoal.visibleProperty().set(true);
        newStartDate.visibleProperty().set(true);
        newEndDate.visibleProperty().set(true);
        createButton.visibleProperty().set(true);
        backToGoalButton.visibleProperty().set(true);
        exerciseChoiceBox.getItems().addAll(adminClass.getExerciseTypes());
        exerciseChoiceBox.setVisible(true);

        backToGroupButton.visibleProperty().set(false);
        editButton.visibleProperty().set(false);
        progressButton.visibleProperty().set(false);
        newGoalButton.visibleProperty().set(false);
        deleteButton.visibleProperty().set(false);

    }


    /*
     *
     * Description: This method creates a new goal for a Controller.group.
     * Param: void
     * Returns: void
     *
     */
    public void create()
    {
        //initExerciseCB();
        GroupGoal g=new GroupGoal();
        for(GroupGoal goal:group.getGroupGoal())
        {
            if(newGoalName.getText().equals(goal.getGoalName()))
            {

                errorMessage.setText("Goal name already exists!");
                errorMessage.visibleProperty().set(true);
                return;
            }
        }

        g.setGoalName(newGoalName.getText());
        try {
            g.setGroupGoal(Double.parseDouble(newGoal.getText()));
        }catch (Exception e)
        {
            errorMessage.visibleProperty().set(true);
            errorMessage.setText("Please enter correct format of hours");

            return;
        }
        if(newStartDate.getValue().isAfter(newEndDate.getValue()))
        {
            errorMessage.setText("start date is after end date");
            errorMessage.visibleProperty().set(true);
            return;
        }


        if(newStartDate.getValue().isBefore(LocalDate.now()))
        {
            errorMessage.setText("start date is before now");
            errorMessage.visibleProperty().set(true);
            return;
        }

        if(newEndDate.getValue().isBefore(LocalDate.now()))
        {
            errorMessage.setText("end date is before now");
            errorMessage.visibleProperty().set(true);
            return;
        }
        g.setStartDate(newStartDate.getValue());
        g.setEndDate(newEndDate.getValue());
        g.setExercise((String)exerciseChoiceBox.getSelectionModel().getSelectedItem());
        goalData.add(g);
        this.group.addGoal(g);
        AdminClass.sqlExeStatement(AdminClass.insertGroupGoal(group,g));
        //adminClass.serializeSave();

        newGoalName.visibleProperty().set(false);
        newGoal.visibleProperty().set(false);
        newStartDate.visibleProperty().set(false);
        newEndDate.visibleProperty().set(false);
        createButton.visibleProperty().set(false);
        errorMessage.visibleProperty().set(false);
        exerciseChoiceBox.setVisible(false);
        backToGoalButton.setVisible(false);
        backToGroupButton.visibleProperty().set(true);
        editButton.visibleProperty().set(true);
        progressButton.visibleProperty().set(true);
        newGoalButton.visibleProperty().set(true);
        deleteButton.visibleProperty().set(true);
    }



    /*
     *
     * Description: This method shows the edit interface for a Controller.group
     * Param: void
     * Returns: void
     *
     */
    public void edit()
    {

        //initExerciseCB();
        if(chosenGoal==null)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No selection");
            alert.setHeaderText("No goal selected");
            alert.setContentText("Please select a goal in the table.");

            alert.showAndWait();
            return;
        }
        editingGoal=chosenGoal;

        newGoalName.visibleProperty().set(true);
        newGoal.visibleProperty().set(true);
        newStartDate.visibleProperty().set(true);
        newEndDate.visibleProperty().set(true);
        backToGoalButton.visibleProperty().set(true);
        exerciseChoiceBox.setVisible(true);
        exerciseChoiceBox.getItems().addAll(adminClass.getExerciseTypes());
        exerciseChoiceBox.setPromptText(chosenGoal.getExercise());
        newGoalName.setText(chosenGoal.getGoalName());
        newGoal.setText(Double.toString(chosenGoal.getGroupGoal()));
        newStartDate.setValue(chosenGoal.getStartDate());
        newEndDate.setValue(chosenGoal.getEndDate());
        doneButton.visibleProperty().set(true);

        backToGroupButton.visibleProperty().set(false);
        editButton.visibleProperty().set(false);
        progressButton.visibleProperty().set(false);
        newGoalButton.visibleProperty().set(false);
        deleteButton.visibleProperty().set(false);
    }



    /*
     *
     * Description: This method is called when the edit is done, updating Model.database when needed
     * Param: void
     * Returns: void
     *
     */
    public void doneEdit()
    {


        double goal;
        try {

            goal=Double.parseDouble(newGoal.getText());


        }catch (Exception e)
        {
            errorMessage.visibleProperty().set(true);
            errorMessage.setText("Please enter correct format of hours");
            return;
        }
        if(newStartDate.getValue().isAfter(newEndDate.getValue()))
        {
            errorMessage.setText("start date is after end date");
            errorMessage.visibleProperty().set(true);
            return;
        }

        if(newStartDate.getValue().isBefore(LocalDate.now()))
        {
            errorMessage.setText("start date is before now");
            errorMessage.visibleProperty().set(true);
            return;
        }

        if(newEndDate.getValue().isBefore(LocalDate.now()))
        {
            errorMessage.setText("end date is before now");
            errorMessage.visibleProperty().set(true);
            return;
        }
        if(!editingGoal.getGoalName().equals(newGoalName.getText())){
            for(GroupGoal g:group.getGroupGoal())
            {
                if(g.getGoalName().equals(newGoalName.getText()))
                {
                    errorMessage.setText("Goal name already exists!");
                    errorMessage.visibleProperty().set(true);
                    return;
                }
            }
        }


        //edit the goal in the table

        for(GroupGoal g:goalData)
        {

            if(g.getGoalName().equals(editingGoal.getGoalName()))
            {
                g.setGoalName(newGoalName.getText());
                g.setStartDate(newStartDate.getValue());
                g.setEndDate(newEndDate.getValue());
                g.setGroupGoal(goal);

            }
        }
        String oldName=editingGoal.getGoalName();
        //edit for the actual goal
        editingGoal.setGoalName(newGoalName.getText());
        editingGoal.setStartDate(newStartDate.getValue());
        editingGoal.setEndDate(newEndDate.getValue());
        editingGoal.setGroupGoal(goal);
        AdminClass.sqlExeStatement(AdminClass.updateGroupGoal(oldName,editingGoal));
        //adminClass.serializeSave();
        newGoalName.visibleProperty().set(false);
        newGoal.visibleProperty().set(false);
        newStartDate.visibleProperty().set(false);
        newEndDate.visibleProperty().set(false);
        doneButton.visibleProperty().set(false);
        errorMessage.visibleProperty().set(false);
        exerciseChoiceBox.setVisible(false);
        backToGroupButton.visibleProperty().set(true);
        backToGoalButton.setVisible(false);
        editButton.visibleProperty().set(true);
        progressButton.visibleProperty().set(true);
        newGoalButton.visibleProperty().set(true);
        deleteButton.visibleProperty().set(true);
        //refresh table
        goalColumn.setCellValueFactory(cellData->cellData.getValue().getGoalNameProperty());

    }


    /*
     *
     * Description: This method goes back to the goal selection.
     * Param: void
     * Returns: void
     *
     */
    public void backToGoal()
    {
        newGoalName.visibleProperty().set(false);
        newGoal.visibleProperty().set(false);
        newStartDate.visibleProperty().set(false);
        newEndDate.visibleProperty().set(false);
        createButton.visibleProperty().set(false);
        doneButton.visibleProperty().set(false);
        backToGoalButton.visibleProperty().set(false);
        errorMessage.visibleProperty().set(false);
        exerciseChoiceBox.setVisible(false);
        progress.setVisible(false);
        progress_message.setVisible(false);
        backToGroupButton.visibleProperty().set(true);
        editButton.visibleProperty().set(true);
        progressButton.visibleProperty().set(true);
        newGoalButton.visibleProperty().set(true);
        deleteButton.visibleProperty().set(true);
    }



    /*
     *
     * Description: This method loads the GroupController.
     * Param: void
     * Returns: void
     *
     */
    public void backToGroupPage() throws IOException {
        String fileLoc = "/View/groupHome.fxml";

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fileLoc));


        Parent root = (Parent) loader.load();
        GroupController ctrl = loader.getController();

        ctrl.initData(stage, user,adminClass);


        stage.getScene().setRoot(root);
    }



    /*
     *
     * Description: This method deletes a goal from a Controller.group
     * Param: void
     * Returns: void
     *
     */
    public void deleteGoal()
    {
        if(chosenGoal==null)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No selection");
            alert.setHeaderText("No goal selected");
            alert.setContentText("Please select a goal in the table.");

            alert.showAndWait();
            return;
        }

        //check if Controller.user is admin
        if(!user.equals(group.getAdmin()))
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No permission");
            alert.setHeaderText("Only admin allows to delete the goal!");
            alert.setContentText("Please contact the Controller.group admin.");

            alert.showAndWait();
            return;
        }
        //check if any goal selected
        if(chosenGoal==null)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No selection");
            alert.setHeaderText("No goal selected");
            alert.setContentText("Please select a goal in the table.");

            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("You are deleting Controller.group goal "+chosenGoal.getGoalName());
        alert.setContentText("Are you sure to delete?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){

            for(Group group:user.getGroupArray())
            {
                if(group.equals(this.group))
                {
                    group.getGroupGoal().remove(chosenGoal);
                }
            }

            AdminClass.sqlExeStatement(AdminClass.deleteGroupGoal(chosenGoal.getGoalName()));
            goalData.remove(chosenGoal);

        }
    }



    /*
     *
     * Description: This method shows the current goal progress of a Controller.group
     * Param: void
     * Returns: void
     *
     */
    public void seeProgress()
    {

        if(chosenGoal==null)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No selection");
            alert.setHeaderText("No goal selected");
            alert.setContentText("Please select a goal in the table.");

            alert.showAndWait();
            return;
        }

        double sum=0;
        //backToGoalButton.setVisible(true);



        //create chartData
        TreeMap<LocalDate,Integer> chartData= new TreeMap<>();


        //loop from start day to end day
        for(LocalDate date=chosenGoal.getStartDate();date.isBefore(chosenGoal.getEndDate());date=date.plusDays(1))
        {
            for(User u:group.getUsers())
            {
                //get exercises from exercises arrayList
                if(u.getExerciseTracker()!=null)
                    if(u.getExerciseTracker().getExerciseTracker().get(date)!=null)
                for(Exercise e:u.getExerciseTracker().getExerciseTracker().get(date))
                {
                    if(e!=null)
                    if(e.getExercise_type().equals(chosenGoal.getExercise()))
                    {

                        chartData.merge(date, (int)(e.getDuration()/60), Integer::sum);

                        sum+=e.getDuration()/60;
                    }
                }
            }
        }
        System.out.println(sum);
        //if total amount of duration greater than the goal, set progress bar to 1,else
        if(sum>=chosenGoal.getGroupGoal())
        {
            progress.setProgress(1);
            progress_message.setText("Congratulations! Your Controller.group has achieved this goal!");
        }
        else {
            progress.setProgress(sum / chosenGoal.getGroupGoal());
            progress_message.setText("Your Controller.group achieves "+sum / chosenGoal.getGroupGoal()*100+"% of this goal!");
        }

        //XYChart.Series series1=new XYChart.Series();




        chartData.forEach((k,v)->
                series1.getData().add(new XYChart.Data<String, Number>(DateUtil.format(k),v)));




        progressPane.setVisible(true);
        backToGroupButton.visibleProperty().set(false);
        editButton.visibleProperty().set(false);
        progressButton.visibleProperty().set(false);
        newGoalButton.visibleProperty().set(false);
        deleteButton.visibleProperty().set(false);
        infoGrid.setVisible(false);

    }



    /*
     *
     * Description: This method returns from the Controller.group progress page.
     * Param: void
     * Returns: void
     *
     */
    public void backFromProgress()
    {
        backToGroupButton.visibleProperty().set(true);
        editButton.visibleProperty().set(true);
        progressButton.visibleProperty().set(true);
        newGoalButton.visibleProperty().set(true);
        deleteButton.visibleProperty().set(true);
        infoGrid.setVisible(true);
        progressPane.setVisible(false);

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
