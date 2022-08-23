package Controller.reminder;


import Controller.FitBit.FitBitHomeController;
import Controller.FitBit.FitBitStepsController;
import Controller.exercise.ExerciseGoalController;
import Controller.exercise.ExerciseHomeController;
import Controller.exercise.ExerciseTrackerReportController;
import Controller.food.FoodTrackerController;
import Controller.food.FoodTrackerReportController;
import Controller.group.GroupController;
import Controller.sleep.SleepTrackerController;
import Controller.sleep.SleepTrackerReportController;
import Controller.tools.ExamController;
import Controller.tools.MusicPlayerController;
import Controller.water.WaterTrackerController;
import Controller.water.WaterTrackerReportController;
import Controller.weight.WeightTrackerController;
import Controller.weight.WeightTrackerReportController;
import Model.email.Email;
import Model.reminder.Reminder;
import Model.reminder.ReminderTracker;
import home.AdminClass;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;

import Controller.user.HomeController;
import Controller.user.LoginController;
import Model.user.User;


import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


public class ReminderController {
    private Stage stage;
    private User user;
    private AdminClass adminClass;
    //use for medicine reminder
    @FXML public TableView<Reminder> reminderTable;
    @FXML public TableColumn<Reminder,String> reminderTableColumn;
    @FXML public Label eventLabel;
    @FXML public Label startTimeLabel;
    @FXML public Label remarkLabel;
    @FXML public TextField inputMedEvent;
    @FXML public TextField inputMedRemark;
    @FXML public TextField inputMedTime;

    @FXML public DatePicker currentDate;
    //panes
    @FXML public GridPane addMedPane;
    @FXML public GridPane medPane;

    @FXML public Label nextEvent;
    @FXML MenuItem fitBitAuthButton;
    @FXML
    MenuItem fitBitStepsButton;



    public ObservableList<Reminder> reminderData = FXCollections.observableArrayList();

    private Reminder selectedMedReminder;

    public ReminderController(){}
    private ArrayList<Reminder> reminderArrayList=new ArrayList<>();
    private ArrayList<Reminder> todaysReminder=new ArrayList<>();
    public void initData(User user,Stage stage,AdminClass adminClass) {
        this.user = user;
        this.stage = stage;
        this.adminClass = adminClass;


        if(user.getFitBitToken().equals("")){
            fitBitAuthButton.setVisible(true);
            fitBitStepsButton.setVisible(false);
        }
        else{
            fitBitAuthButton.setVisible(false);
            fitBitStepsButton.setVisible(true);

        }

        currentDate.setValue(LocalDate.now());

        for(Map.Entry<Reminder,String> entry: AdminClass.getReminders().entrySet())
        {
            if(entry.getValue().equals(user.getUsername()))
            {
                reminderArrayList.add(entry.getKey());
                if(entry.getKey().getDate().equals(currentDate.getValue()))
                    if(entry.getKey().getType()==Reminder.EventType.MEDICINE)
                        todaysReminder.add(entry.getKey());
            }
        }
        todaysReminder.sort(timeCompare);
        for(Reminder r:todaysReminder)
        {
            System.out.println(r.getTime().toString());
        }
        for(Reminder r:reminderArrayList)
        {
            if(r.getDate().equals(currentDate.getValue()))
            {
                if(r.getType()== Reminder.EventType.MEDICINE)
                 reminderData.add(r);


            }
        }

        reminderTable.setItems(reminderData);
        reminderTable =new TableView<Reminder>();


        fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
        fiveSecondsWonder.play();
    }

    @FXML
    private void initialize() {
        reminderTableColumn.setCellValueFactory(cellData->cellData.getValue().getNameProperty());

        // Clear reminder details.
        showReminderDetails(null);
        // Listen for selection changes and show the reminder details when changed.
        reminderTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showReminderDetails(newValue));

    }


    private void showReminderDetails(Reminder reminder)
    {
        if(reminder!=null){

        if(reminder.getType()== Reminder.EventType.MEDICINE) {
            eventLabel.setText(reminder.getEvent());
            startTimeLabel.setText(String.valueOf(reminder.getTime()));
            remarkLabel.setText(reminder.getRemarks());
            selectedMedReminder=reminder;
            System.out.println("selected event:"+selectedMedReminder.getEvent());
        }
        }
        else
        {
            eventLabel.setText("");
            startTimeLabel.setText("");
            remarkLabel.setText("");

        }

    }


    @FXML void reload()
    {

        ObservableList<Reminder> ex = FXCollections.observableArrayList();
        if(reminderData!=null) {
            ex.addAll(reminderData);
        }
        ObservableList<Reminder> app = FXCollections.observableArrayList();


        assert reminderData != null;
        reminderData.removeAll(ex);


        for(Reminder r:reminderArrayList)
        {
            if(r.getDate().equals(currentDate.getValue()))
            {

                if(r.getType()== Reminder.EventType.MEDICINE)
                    reminderData.add(r);

            }
        }

        reminderTable.setItems(reminderData);
        reminderTableColumn.setCellValueFactory(cellData->cellData.getValue().getNameProperty());

    }

    @FXML
    public void goAddMedicine()
    {
        medPane.setVisible(false);
        addMedPane.setVisible(true);
        inputMedTime.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    }

    @FXML void addMedicine()
    {
        if(inputMedEvent.getText().trim().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No input");
            alert.setHeaderText("No event name input");
            alert.setContentText("Please enter a event name.");
            alert.showAndWait();
            return;
        }
        String event=inputMedEvent.getText();

        String remarks=inputMedRemark.getText();
        if(inputMedTime.getText().trim().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No input");
            alert.setHeaderText("No start time input");
            alert.setContentText("Please enter start time.");
            alert.showAndWait();
            return;
        }

        try {
            LocalTime  time=LocalTime.parse(inputMedTime.getText());
            Reminder newReminder= new Reminder(currentDate.getValue(),time,event,remarks, Reminder.EventType.MEDICINE);
            user.getReminderTracker().addReminder(newReminder);
            reminderData.add(newReminder);
            AdminClass.reminders.put(newReminder,user.getUsername());
            todaysReminder.add(newReminder);
            //sql execution
            AdminClass.sqlExeStatement(AdminClass.addReminder(user,newReminder));
            medPane.setVisible(true);
            addMedPane.setVisible(false);
            reminderTableColumn.setCellValueFactory(cellData->cellData.getValue().getNameProperty());
        } catch (DateTimeParseException e) {
            System.out.println("invalid input time");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Invalid time input");
            alert.setHeaderText("Please enter time in the format of 00:00:00.");
            alert.showAndWait();
            return;
        }
    }
    @FXML
    public void removeMedicine()
    {
        if(selectedMedReminder==null)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No reminder selected.");
            alert.setHeaderText("Please select a reminder to delete.");
            alert.showAndWait();
            return;
        }
        Reminder removed=selectedMedReminder;
        System.out.println("removing event "+removed.getEvent());
        AdminClass.reminders.remove(removed,user.getUsername());
        user.getReminderTracker().getReminders().remove(removed);
        reminderData.remove(removed);
        todaysReminder.remove(removed);
        //sql execution
        AdminClass.sqlExeStatement(AdminClass.deleteReminder(user,removed));
        reminderTableColumn.setCellValueFactory(cellData->cellData.getValue().getNameProperty());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Removed!");
        alert.setHeaderText("Event "+removed.getEvent()+" removed.");
        alert.showAndWait();
    }

    @FXML void back()
    {
        medPane.setVisible(true);
        addMedPane.setVisible(false);
    }

    Timeline fiveSecondsWonder = new Timeline(new KeyFrame(Duration.seconds(5), new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
           System.out.println("this is called every 5 seconds on UI thread");
            boolean haveEvent=false;
            todaysReminder.sort(timeCompare);
            for(Reminder r:todaysReminder)
            {
                if(r.getTime().compareTo(LocalTime.now())>0)
                {
                    nextEvent.setText(r.getEvent());
                    haveEvent=true;
                    break;
                }
            }
            if(!haveEvent)
            {
                nextEvent.setText("No event today");
            }

            for(Reminder r:user.getReminderTracker().getReminders())
            {
                if(r.getType()==Reminder.EventType.APPOINTMENT)
                if(!r.getEmailSent())
                {
                    if(r.getDate().isAfter(LocalDate.now()))
                    {
                        long noOfDaysBetween = ChronoUnit.DAYS.between(LocalDate.now(), r.getDate());
                        if(noOfDaysBetween==5)
                        {
                            Email.sendEmail(user.getEmail(),"Your medical appointment reminder:"+r.getEvent(),
                                    "Your medical appointment \""+r.getEvent()+"\"will be taken in 5 days on"
                                            +r.getDate().toString()+"\nremark:" +r.getRemarks());
                            r.setEmailSent(true);

                            AdminClass.sqlExeStatement(AdminClass.updateReminder(user,r));
                        }
                    }
                }


            }
        }
    }));
    public static Comparator<Reminder> timeCompare= Comparator.comparing(Reminder::getTime);

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


        ctrl.initData(user,stage,adminClass);

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
