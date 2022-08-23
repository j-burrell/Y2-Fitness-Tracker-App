package home;



import Controller.user.*;

import Model.database.DataBaseConnector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        try {
            DataBaseConnector.loadDataBaseDetails();

        } catch (Exception e) {
            e.printStackTrace();
        }

        AdminClass adminClass = new AdminClass();


        if(adminClass==null){adminClass = new AdminClass();}

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "/View/loginPage.fxml"
                )
        );

        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setScene(
                new Scene(
                        (Pane) loader.load()
                )
        );
        stage.setHeight(750);
        stage.setWidth(1100);
        stage.setTitle("MyFitness");
        LoginController controller =
                loader.<LoginController>getController();
        controller.initData(stage, adminClass);

        stage.show();

    }






    public static void main(String[] args) {



        launch(args);





    }
}
