/*********************************************************
 *
 *                   NewGroupController.java (Controller)
 *
 *
 **********************************************************/

package Controller.group;

import Controller.reminder.AppReminderController;
import Model.group.Group;
import home.AdminClass;
import Model.user.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


import java.io.IOException;

public class NewGroupController {


    private User user;
    private Stage stage;
    private AdminClass adminClass;
    @FXML public Button createButton;
    @FXML public TextField groupName;


    /*
     *
     * Description: Default constructor
     * Param: void
     * Returns: void
     *
     */
    public NewGroupController()
    {}


    /*
     *
     * Description: This method initialises the view page and all the data it needs to load
     * Param: Stage stage, User Controller.user, AdminClass adminClass
     * Returns: void
     *
     */
    public void initData(Stage stage, User user,AdminClass adminClass){
        this.user = user;
        this.stage = stage;
        this.adminClass =adminClass;

        System.out.println(user);
        System.out.println(stage);
        System.out.println(adminClass);


    }




    /*
     *
     * Description: This method creates a new Controller.group then returns back to groupController
     * Param: void
     * Returns: void
     *
     */
    @FXML
    public void create() throws IOException {
        if(groupName.getText().trim().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No input");
            alert.setHeaderText("No group name input");
            alert.setContentText("Please enter a group name.");

            alert.showAndWait();
            return;
        }
        for(Group g:adminClass.getGroups())
        {
            if(g.getGroupName().equals(groupName.getText()))
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Name exists");
                alert.setHeaderText("Group name already exists");
                alert.setContentText("Please give a different group name");

                alert.showAndWait();
                return;
            }
        }
        Group newGroup=new Group(user,groupName.getText());
        System.out.println("group Created");
        user.getGroupArray().add(newGroup);
        adminClass.addGroup(newGroup);

        //Model.database save
        AdminClass.sqlExeStatement(AdminClass.insertGroup(newGroup));
        AdminClass.sqlExeStatement(AdminClass.insertGroupUser(newGroup,user));
        //adminClass.serializeSave();

        String fileLoc = "/View/groupHome.fxml";


        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Create Group");
        alert.setContentText("Created Group successfully");
        alert.setHeaderText(null);
        alert.showAndWait();

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fileLoc));
        Parent root = (Parent) loader.load();
        GroupController groupController=loader.getController();

        groupController.initData(stage,user,adminClass);




        stage.getScene().setRoot(root);

    }


    /*
     *
     * Description: This method loads the GroupController
     * Param: void
     * Returns: void
     *
     */
    @FXML
    public void backToGroup() throws IOException {
        String fileLoc = "/View/groupHome.fxml";

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fileLoc));


        Parent root = (Parent) loader.load();
        GroupController ctrl = loader.getController();

        ctrl.initData(stage, user,adminClass);


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

}
