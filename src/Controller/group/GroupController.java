/*********************************************************
 *
 *                   GroupController.java (Controller)
 *
 *
 **********************************************************/


package Controller.group;

import Controller.FitBit.FitBitHomeController;
import Controller.FitBit.FitBitStepsController;
import Controller.exercise.ExerciseGoalController;
import Controller.exercise.ExerciseHomeController;
import Controller.exercise.ExerciseTrackerReportController;
import Controller.reminder.AppReminderController;
import Controller.reminder.ReminderController;
import Controller.tools.ExamController;
import Controller.tools.MusicPlayerController;
import Model.email.Email;
import Controller.food.FoodTrackerController;
import Controller.food.FoodTrackerReportController;
import Model.group.Group;
import Model.user.User;
import home.*;
import Controller.user.*;
import Controller.sleep.*;
import Controller.water.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javafx.scene.control.Label;
import Controller.weight.WeightTrackerController;
import Controller.weight.WeightTrackerReportController;

import javax.swing.*;
import java.io.IOException;
import java.util.Optional;

public class GroupController  {
    @FXML public TableView<Group> groupTable;
    @FXML public TableColumn<Group,String> groupNameColumn;

    @FXML private Label groupNameLabel;
    @FXML private Label adminLabel;

    @FXML MenuItem fitBitAuthButton;
    @FXML MenuItem fitBitStepsButton;

    public ObservableList<Group> groupData = FXCollections.observableArrayList();
    private Stage stage;
    private User user;
    private AdminClass adminClass;
    private Group selectedGroup;

    /*
     *
     * Description: Default constructor
     * Param: void
     * Returns: void
     *
     */
    public GroupController()
    {}




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
        this.adminClass=adminClass;

        for(Group g:adminClass.getGroups())
        {
            if(g.getUsers().contains(user))
                groupData.add(g);
            System.out.println("here is a group");
        }
        groupTable.setItems(groupData);

        groupTable =new TableView<Group>();

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
     * Description: This method initialises the left side listView of groups
     * Param: void
     * Returns: void
     *
     */
    @FXML
    private void initialize() {
        groupNameColumn.setCellValueFactory(cellData->cellData.getValue().getNameProperty());

        // Clear Controller.group details.
        showGroupDetails(null);
        // Listen for selection changes and show the Controller.group details when changed.
        groupTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showGroupDetails(newValue));
    }




    /*
     *
     * Description: This method sets and shows the table of Controller.group details on the write
     * Param: void
     * Returns: void
     *
     */
    private void showGroupDetails(Group group)
    {
        if(group!=null)
        {
            groupNameLabel.setText(group.getGroupName());
            System.out.println("Group admin:" +group.getAdmin().getName());
            adminLabel.setText(group.getAdmin().getName());
            selectedGroup=group;
        }
        else
        {
            groupNameLabel.setText("");
            adminLabel.setText("");
        }
    }



    /*
     *
     * Description: This method gos to SingleGroup
     * Param: void
     * Returns: void
     *
     */
    @FXML
    public void goGroupDetail() throws IOException {



        if(selectedGroup==null)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No selection");
            alert.setHeaderText("No group selected");
            alert.setContentText("Please select a group in the table.");

            alert.showAndWait();
            return;
        }

        String fileLoc = "/View/singleGroup.fxml";

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fileLoc));


        Parent root = (Parent) loader.load();
        SingleGroupController ctrl = loader.getController();


        ctrl.initData(user,selectedGroup,stage,adminClass);


        stage.getScene().setRoot(root);


    }


    /*
     *
     * Description: This method loads the NewGroupController
     * Param: void
     * Returns: void
     *
     */
    @FXML
    public void createNewGroup() throws IOException {


        String fileLoc = "/View/newGroup.fxml";

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fileLoc));


        Parent root = (Parent) loader.load();
        NewGroupController ctrl = loader.getController();


        ctrl.initData(stage,user,adminClass);


        stage.getScene().setRoot(root);
    }


    /*
     *
     * Description: This method removes the Controller.user from the selected Controller.group
     * Param: void
     * Returns: void
     *
     */
    @FXML
    public void leaveGroup()
    {

        if(selectedGroup==null)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No selection");
            alert.setHeaderText("No group selected");
            alert.setContentText("Please select a group in the table.");

            alert.showAndWait();
            return;
        }
        //if there is only one Controller.user in the Controller.group, delete the Controller.group
        if(selectedGroup.getUsers().size()==1)
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("No more users in the group");
            alert.setHeaderText("You are deleting group: "+this.selectedGroup.getGroupName());
            alert.setContentText("Are you sure to delete?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK)
            {
                user.getGroupArray().remove(selectedGroup);
                AdminClass.sqlExeStatement(AdminClass.deleteGroup(selectedGroup));
                adminClass.getGroups().remove(selectedGroup);
                groupData.remove(selectedGroup);
                return;
            }
            else
            {
                return;
            }
        }


        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("You are leaving group: "+this.selectedGroup.getGroupName());
        alert.setContentText("Are you sure to leave?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            AdminClass.deleteGroupUser(selectedGroup,user);
            user.getGroupArray().remove(selectedGroup);
            for(Group group:adminClass.getGroups())
            {
                if(group.getGroupName().equals(selectedGroup.getGroupName()))
                {
                    group.removeUser(user);
                }
            }
            groupData.remove(selectedGroup);
        }
        else
        {
            return;
        }

        groupNameColumn.setCellValueFactory(cellData->cellData.getValue().getNameProperty());
    }



    /*
     *
     * Description: This method lets the Controller.user join the selected Controller.group
     * Param: void
     * Returns: void
     *
     */
    @FXML public void join()
    {
        boolean joined=false;
        String groupName = JOptionPane.showInputDialog("Please enter group name:");
        System.out.println("groupName:"+ groupName);
        for(Group group:adminClass.getGroups())
        {

            if(group.getGroupName().equals(groupName)) {
                //avoid adding Controller.user to the same Controller.group
                for (User u : group.getUsers()) {
                    if (u.equals(user)) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Error!");
                        alert.setHeaderText("You are already in the group " + group.getGroupName());

                        alert.showAndWait();
                        return;
                    }
                }

                group.addUser(user);
                user.getGroupArray().add(group);

                joined=true;
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Joined!");
                alert.setHeaderText("You have joined group "+group.getGroupName());


                alert.showAndWait();

                AdminClass.sqlExeStatement(AdminClass.insertGroupUser(group,user));

                groupData.add(group);

                //adminClass.serializeSave();
            }
        }



        if(!joined)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Not joined");
            alert.setHeaderText("Cant find group!");
            alert.setContentText("Group "+groupName+"does not exist;");

            alert.showAndWait();
        }

        groupNameColumn.setCellValueFactory(cellData->cellData.getValue().getNameProperty());


    }


    /*
     *
     * Description: This method sends an Model.email invite so that another Controller.user can join a Controller.group
     * Param: void
     * Returns: void
     *
     */
    @FXML public void invite()
    {
        if(selectedGroup==null)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No selection");
            alert.setHeaderText("No group selected");
            alert.setContentText("Please select a group in the table.");

            alert.showAndWait();
            return;
        }

        String email=JOptionPane.showInputDialog("Please enter friend's email:");
        if(SignUpController.verifyEmail(email)){
        String subject=user.getName()+"send you an invitation to join exercise group!";
        String msg="Hi,\n" +
                user.getName()+"invites you to join exercise group"+selectedGroup.getGroupName()
                +" admin of"+ selectedGroup.getAdmin().getName()+"\n" +
                "Please enter the group name \""+selectedGroup.getGroupName()+"\" in MyFitness to join!\n" +
                "King regards,\n" +
                "MyFitness.";
        Email.sendEmail(email,subject,msg);
        JOptionPane.showMessageDialog(null, "Invitation sent");
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Email format not correct");

            alert.setContentText("Please enter correct Model.email.");

            alert.showAndWait();
            return;
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
