/*********************************************************
 *
 *                   WeightTrackerController.java (Controller)
 *
 *
 **********************************************************/

package Controller.tools;


import Controller.FitBit.FitBitHomeController;
import Controller.FitBit.FitBitStepsController;
import Controller.exercise.ExerciseGoalController;
import Controller.exercise.ExerciseHomeController;
import Controller.exercise.ExerciseTrackerReportController;
import Controller.food.FoodTrackerController;
import Controller.food.FoodTrackerReportController;
import Controller.group.GroupController;
import Controller.reminder.AppReminderController;
import Controller.reminder.ReminderController;
import Controller.sleep.SleepTrackerController;
import Controller.sleep.SleepTrackerReportController;
import Controller.user.HomeController;
import Controller.user.LoginController;
import Controller.water.WaterTrackerController;
import Controller.water.WaterTrackerReportController;
import Controller.weight.WeightTrackerController;
import Controller.weight.WeightTrackerReportController;
import Model.user.User;
import home.AdminClass;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class MusicPlayerController implements Initializable {

    private User user;

    private Stage stage;

    private AdminClass adminClass;

    private LocalDate currentViewDate;

    @FXML
    private MenuBar menuBar;

    @FXML
    private BorderPane container;
    //create labels
    Label lblTracks, lblPlayList, lblVolume, lblStatus;

    //create list views
    ListView<String> lvAvailableTracks, lvSelectedTracks;

    //create buttons
    Button btnAdd, btnRemove, btnRemoveAll, btnPlay, btnPause, btnStop;

    //create sliders
    Slider slrVolume, slrStatus;

    //create media and mediaPlayer
    MediaPlayer mediaPlayer;
    Media media;


    @FXML MenuItem fitBitAuthButton;
    @FXML MenuItem fitBitStepsButton;



    public void init () {

        //instantiate btnAdd
        btnAdd= new Button("Add >");

        //disable button by default
        btnAdd.setDisable(true);

        //handle event when clicking on button
        btnAdd.setOnAction(ae -> addMusicFile());

        //instantiate lblTracks
        lblTracks= new Label ("Available Tracks:");

        //instantiate lvAvailableTracks
        lvAvailableTracks= new ListView <String>();

        //event handler for selecting an item in lvAvailableTracks
        lvAvailableTracks.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            //event handler function for selection change
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                //enable btnAdd when an item is selected in lvAvailableTracks
                btnAdd.setDisable(false);
            }
        });

        //instantiate btnRemove
        btnRemove = new Button("< Remove");

        //disable button by default
        btnRemove.setDisable(true);

        //handle event when clicking on button
        btnRemove.setOnAction(ae -> removeItemFromSelectedTracks());

        //instantiate btnRemove
        btnRemoveAll = new Button("<< Remove All");

        //disable button by default
        btnRemoveAll.setDisable(true);

        //handle event when clicking on button
        btnRemoveAll.setOnAction(ae -> removeAllItemsFromSelectedTracks());

        //instantiate btnPlay
        btnPlay = new Button("Play");

        //handle event when clicking on button
        btnPlay.setOnAction(ae -> playSelectedTrack());

        //disable button by default
        btnPlay.setDisable(true);

        //instantiate btnPause
        btnPause = new Button("Pause");

        //handle event when clicking on button
        btnPause.setOnAction(ae -> pauseSelectedTrack());

        //disable button by default
        btnPause.setDisable(true);

        //instantiate btnStop
        btnStop = new Button("Stop");

        //handle event when clicking on button
        btnStop.setOnAction(ae -> stopSelectecTrack());

        //disable button by default
        btnStop.setDisable(true);

        //instantiate label
        lblPlayList= new Label ("Selected Tracks:");

        //instantiate list view
        lvSelectedTracks= new ListView <String>();

        //adding event handler for selecting an item in lvAvailableTracks
        lvSelectedTracks.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            //event handler function for selection change in selected tracks list view
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                //enable btnRemove, btnRemoveAll and btnPlay when an item is selected in lvSelectedTracks list view
                btnRemove.setDisable(false);
                btnRemoveAll.setDisable(false);
                btnPlay.setDisable(false);
            }
        });

        //manage listViews width and Height.
        lvAvailableTracks.setMinWidth(50);
        lvAvailableTracks.setMinHeight(100);
        lvSelectedTracks.setMinWidth(50);
        lvSelectedTracks.setMinHeight(100);

        //instantiate lblVolume and lblStatus
        lblVolume = new Label("Volume:");
        lblStatus = new Label("Status");

        //instantiate sliders
        slrVolume = new Slider();
        slrStatus = new Slider();

        //manage buttons minimum width
        int btnMinWidth=115;
        btnAdd.setMinWidth(btnMinWidth);
        btnRemove.setMinWidth(btnMinWidth);
        btnRemoveAll.setMinWidth(btnMinWidth);
        btnPlay.setMinWidth(btnMinWidth);
        btnPause.setMinWidth(btnMinWidth);
        btnStop.setMinWidth(btnMinWidth);

        //add .mp3 files into available tracks listView
        lvAvailableTracks.setItems(getFilesInFolder("music"));

    }//init()

    //method to list all files inside a folder
    private ObservableList<String> getFilesInFolder(String folderName){

        //ovservableList for the music files.
        ObservableList <String> musicFiles = FXCollections.observableArrayList();

        //string array to store a list of playable files.
        String [] fileList;

        //instantiate File
        File f = new File(folderName);

        //Call list() to get a directory listing.
        fileList= f.list();

        //add the array of files to the music files observable list.
        musicFiles.addAll(fileList);

        //return the observable list.
        return musicFiles;

    }//getFilesInFolder()

    private void addMusicFile(){

        //get the item from lvAvailableTracks and add them to lvSelectedTracks
        lvSelectedTracks.getItems().add(lvAvailableTracks.getSelectionModel().getSelectedItem());

        //get the index of selected item in lvAvailableTracks list view
        int selectedIdx = lvAvailableTracks.getSelectionModel().getSelectedIndex();

        //check if index is not -1 which means an item is actually selected
        if (selectedIdx != -1) {

            //remove the selected item using the selection index
            lvAvailableTracks.getItems().remove(selectedIdx);
        }

    }//addMusicFile()

    private void removeItemFromSelectedTracks(){

        //get the item from lvSelectedTracks and add them to lvAvailableTracks
        lvAvailableTracks.getItems().add(lvSelectedTracks.getSelectionModel().getSelectedItem());

        //get the index of selected item in lvSelectedTracks list view
        int selectedIdx = lvSelectedTracks.getSelectionModel().getSelectedIndex();

        //check if index is not -1 which means an item is actually selected
        if (selectedIdx != -1) {

            //remove the selected item using the selection index
            lvSelectedTracks.getItems().remove(selectedIdx);
        }

        //disable remove, remove all and play buttons if no item is left in selected tracks list view
        if (lvSelectedTracks.getItems().size() == 0){
            btnRemoveAll.setDisable(true);
            btnRemove.setDisable(true);
            btnPlay.setDisable(true);
        }

    }//removeAllItemsFromSelectedTracks()

    private void removeAllItemsFromSelectedTracks(){

        //while there is some items in selected track, remove the first item from selected track
        while(lvSelectedTracks.getItems().size() > 0){

            //add the first item to available tracks list view
            lvAvailableTracks.getItems().add(lvSelectedTracks.getItems().get(0));

            //remove the first item from selected tracks list view
            lvSelectedTracks.getItems().remove(0);
        }

        //disabling btnRemove, btnRemove and btnPlay all buttons
        btnRemove.setDisable(true);
        btnRemoveAll.setDisable(true);
        btnPlay.setDisable(true);

    }//removeAllItemsFromSelectedTracks()

    private void playSelectedTrack(){

        //get the index of selected item in lvSelectedTracks list view
        int selectedIdx = lvSelectedTracks.getSelectionModel().getSelectedIndex();

        //check if index is not -1 which means an item is actually selected
        if (selectedIdx != -1) {

            //use mediaPlayer to play music
            if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED){
                mediaPlayer.play();
            }
            else{
                String musicToPlay = lvSelectedTracks.getItems().get(selectedIdx);

                //create a Media
                String path = "./music/" + musicToPlay;
                media = new Media(new File(path).toURI().toString());
                mediaPlayer = new MediaPlayer(media);

                mediaPlayer.currentTimeProperty().addListener((InvalidationListener) ov -> {
                    Duration trackDuration = mediaPlayer.getTotalDuration();
                    Duration duration = mediaPlayer.getCurrentTime();

                    //calculate the track slider position.
                    slrStatus.setValue((duration.toSeconds()*100)/trackDuration.toSeconds());

                    //show the current track position in a label.
                    double minutes = Math.floor(duration.toMinutes());
                    double seconds = Math.floor(duration.toSeconds() % 60);

                    int m = (int) minutes;
                    int s = (int) seconds;

                    //show played time on lblStatus
                    lblStatus.setText("Status: Playing " + m + ":" + s);

                });

                //set the position of slider according to volume of the player
                slrVolume.setValue(mediaPlayer.getVolume() * 100);

                //add event handler to the event of changing slider
                slrVolume.valueProperty().addListener(new InvalidationListener(){

                    @Override
                    //set the volume of player according to value of slider
                    public void invalidated(Observable observable) {
                        mediaPlayer.setVolume(slrVolume.getValue() / 100);
                    }
                });

                //play music
                mediaPlayer.play();

            }

            //disable btnPlay
            btnPlay.setDisable(true);

            //enable btnPause and btnStop
            btnPause.setDisable(false);
            btnStop.setDisable(false);
        }

    }//playSelectedTrack()

    private void pauseSelectedTrack(){

        //pause playing if it is in play mode
        if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING){
            mediaPlayer.pause();

            //enable btnPlay button
            btnPlay.setDisable(false);

            //disable btnPause and btnStop
            btnPause.setDisable(true);
            btnStop.setDisable(true);
        }

    }//pauseSelectedTrack()

    private void stopSelectecTrack(){

        //stop playing if it is in play mode
        if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING){
            mediaPlayer.stop();

            //enable btnPlay button
            btnPlay.setDisable(false);

            //disable btnPause and btnStop
            btnPause.setDisable(true);
            btnStop.setDisable(true);
        }

    }//stopSelectecTrack()



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
        MusicPlayerController  ctrl = loader.getController();

//
        ctrl.initData(stage,adminClass, user);


        stage.getScene().setRoot(root);

    }

    public void initData(Stage stage, AdminClass adminClass, User user) {
        this.stage = stage;
        this.adminClass = adminClass;
        this.user=user;

        if(user.getFitBitToken().equals("")){
            fitBitAuthButton.setVisible(true);
            fitBitStepsButton.setVisible(false);
        }
        else{
            fitBitAuthButton.setVisible(false);
            fitBitStepsButton.setVisible(true);

        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        init ();

//create a grid pane
        GridPane gp = new GridPane();

        //set gap
        gp.setHgap(10);
        gp.setVgap(10);

        //set margin
        gp.setPadding(new Insets(15));

        //set labels, list views, buttons and sliders
        gp.add(lblTracks, 0, 0);
        gp.add(lvAvailableTracks, 0, 1, 2, 12);

        gp.add(btnAdd, 3, 4);
        gp.add(btnRemove, 3, 5);
        gp.add(btnRemoveAll, 3, 6);
        gp.add(btnPlay, 3, 7);
        gp.add(btnPause, 3, 8);
        gp.add(btnStop, 3, 9);
        gp.add(lblVolume, 3, 10);
        gp.add(slrVolume, 3, 11);

        gp.add(lblPlayList, 4, 0);
        gp.add(lvSelectedTracks, 4, 1, 2, 12);

        gp.add(lblStatus, 4, 14);
        gp.add(slrStatus, 4, 15);


        container.setCenter(gp);
    }
}
