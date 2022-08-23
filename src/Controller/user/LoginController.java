/*********************************************************
 *
 *                   LoginController.java (Controller)
 *
 *
 **********************************************************/

package Controller.user;

import Model.database.FoodDB;
import Model.database.WeightDB;
import Model.user.User;
import home.*;

import javafx.fxml.FXML;

import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.mindrot.jbcrypt.BCrypt;


public class LoginController {


    private Stage stage;

    private AdminClass adminClass;


    @FXML public Button login_button;

    @FXML public Button signup_button;

    @FXML public TextField login_username;

    @FXML public TextField login_password;

    @FXML public Text login_error;

    public static String loginUsername;




    /*
     *
     * Description: This method gets usernames and passwords from the Model.database and attempts to log the Controller.user in
     * Param: void
     * Returns: void
     *
     */
    @FXML
    public void attempt_login() throws Exception {



        adminClass = new AdminClass();

        adminClass.populateloginAccounts();
        User passUser = null;

        String usernameIn = login_username.getText();
        String passwordIn = login_password.getText();

        boolean successfulLogin = false;

        for(int i = 0; i < adminClass.getUsers().size(); i++){
            String username = adminClass.getUsers().get(i).getUsername();
            String password = adminClass.getUsers().get(i).getPassword();
            //if (username.equals(usernameIn) && password.equals(passwordIn)){
            if (username.equals(usernameIn) && BCrypt.checkpw(passwordIn, password)){

                passUser = adminClass.getUsers().get(i);
                successfulLogin = true;
                loginUsername = username;
            }
        }

        if(successfulLogin && passUser!= null){



            adminClass.initKeyValues(passUser);

            adminClass.populateUserObject(passUser);
            adminClass.loadSleepTracker(passUser);
            adminClass.loadWaterTracker(passUser);
            adminClass.initExerciseTracker();
            adminClass.initExercise();
            adminClass.checkToken(passUser);
            WeightDB.getWeightTracker(passUser);
            FoodDB.getFoodTracker(passUser);
            adminClass.initReminder();
            passUser.getReminderTracker().getReminds();
            adminClass.initGroupInfo();
            adminClass.initExam();


            //goto home

            String fileLoc = "/View/userHome.fxml";

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fileLoc));


            Parent root = (Parent) loader.load();
            HomeController ctrl = loader.getController();


            ctrl.initData(stage,adminClass,passUser);


            stage.getScene().setRoot(root);

        }
        else{
            login_error.setText("Invalid username or password!");

        }




    }



    /*
     *
     * Description: This method loads the SignUpController.
     * Param: void
     * Returns: void
     *
     */
    @FXML
    public void attempt_signup() throws IOException {

        String urlPrefix = System.getProperty("Controller.user.dir");
        String fileLoc = "/View/signupPage.fxml";

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fileLoc));


        Parent root = (Parent) loader.load();
        SignUpController ctrl = loader.getController();


        ctrl.initData(stage,adminClass);


        stage.getScene().setRoot(root);


    }

    /*
     *
     * Description: This method initialises the view page and all the data it needs to load
     * Param: Stage stage, User Controller.user, AdminClass adminClass
     * Returns: void
     *
     */
    public void initData(Stage stage, AdminClass adminClass){
        this.stage = stage;
        this.adminClass = adminClass;


    }

/*
    /*
     *
     * Description: This method val
     * Param:
     * Returns:
     *
     *//*
    public static boolean loginValidate(String username, String password,HashMap<String,String> userAccounts ){

        for (Map.Entry<String,String> entry : userAccounts.entrySet()) {
            String key = entry.getKey();
            String pass = entry.getValue();
            if(key.equals(username) && pass.equals(password)){
                return true;
            }
        }
        return false;
    }



    *//*
     *
     * Description:
     * Param:
     * Returns:
     *
     *//*
    public static HashMap<String,String> loadLoginAccounts(String file) throws FileNotFoundException {


        Scanner sc=new Scanner(new File(file));
        sc.useDelimiter(" |,");
        ArrayList<String> words=new ArrayList<>();
        String str;
        while(sc.hasNext()) {
            str = sc.next();
            str = str.trim();
            str = str.toLowerCase();
            words.add(str);
        }

        HashMap<String,String> loginAccounts = new HashMap<>();
        for(int i = 0; i < words.size(); i=i+2){
            loginAccounts.put(words.get(i),words.get(i+1));
        }
        return loginAccounts;
    }



    *//*
     *
     * Description:
     * Param:
     * Returns:
     *
     *//*
    public static boolean usernameExists(HashMap<String,String> loginAccounts, String username){
        for (Map.Entry<String,String> entry : loginAccounts.entrySet()) {
            String key = entry.getKey();
            if(key.equals(username)){
                System.out.println("Username already exists");
                return true;
            }
        }
        return false;
    }



    *//*
     *
     * Description:
     * Param:
     * Returns:
     *
     *//*
    private void loginSuccessfullToHome() throws IOException {
        Stage stage;
        Parent loader;


        loader = FXMLLoader.load(getClass().getResource("/resources/home.fxml"));

        stage = (Stage) login_button.getScene().getWindow();



        Scene scene = new Scene(loader);
        stage.setScene(scene);
        stage.show();


    }
    */
}
