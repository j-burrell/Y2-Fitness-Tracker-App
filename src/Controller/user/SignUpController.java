/*********************************************************
 *
 *                   SignUpController.java (Controller)
 *
 *
 **********************************************************/

package Controller.user;

import Model.user.User;
import home.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpController {


    private Stage stage;
    private AdminClass adminClass;
    private double height;
    private double width;


    @FXML public Button signup_button;

    @FXML public Button signup_back_button;

    @FXML public TextField username_signup_field;

    @FXML public TextField email_signup_field;

    @FXML public TextField password_signup_field;

    @FXML public TextField password2_signup_field;

    @FXML public Text signup_error;


    /*
     *
     * Description: This method initialises the view page and all the data it needs to load
     * Param: Stage stage, User Controller.user, AdminClass adminClass
     * Returns: void
     *
     */
    @FXML
    public void initData(Stage stage, AdminClass adminClass){
        System.out.println("signup init being called");
        if(stage == null | adminClass == null){
            System.out.println("null found 1");
        }
        this.stage = stage;
        this.adminClass = adminClass;
    }




    /*
     *
     * Description: This method checks all inputted data is valid then updates the Model.database.
     * Param: void
     * Returns: void
     *
     */
    @FXML
    public void attempt_signup() throws IOException {
        adminClass.populateloginAccounts();
        ArrayList<User> userArary = loadAccounts(adminClass);
        StringBuilder errorString = new StringBuilder();


        String username = username_signup_field.getText();
        boolean usernameBool = false;

        String email = email_signup_field.getText();
        boolean emailBool = false;

        String password = password_signup_field.getText();
        String password2 = password2_signup_field.getText();
        boolean passwordsBool = false;

        //check username does not already exist.
        boolean exists = usernameExists(userArary,username);
        if(exists){
            System.out.println("Username already exists");
            errorString.append("Username already exists\n");
        }
        else{
            System.out.println("username valid");
            usernameBool=true;
        }

        //Check Model.email is valid.
        emailBool=verifyEmail(email);
        if(!emailBool){
            System.out.println("Email is not valid.");
            errorString.append("Email is not valid.\n");

        }

        //Check passwords match.
        if(password.equals(password2)){
            passwordsBool=true;
        }
        else{
            System.out.println("passwords do not match");
            errorString.append("Passwords do not match.\n");
        }


        if(usernameBool && emailBool && passwordsBool){
            //Display account created message.


            //Save account.
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setEmail(email);

            //hash the password
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            //newUser.setPassword(password);
            newUser.setPassword(hashedPassword);

            adminClass.addUser(newUser);


            ArrayList<User> users = adminClass.getUsers();

            System.out.println("Outputting list of users after saving ser file");
            for(int i = 0; i < users.size(); i++){

                System.out.println(users.get(i));
            }


            adminClass.addNewUser(newUser);


            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Signup");
            alert.setContentText("Sign-up successful");
            alert.setHeaderText(null);
            alert.showAndWait();

            //load login page.
            backToLogin();
        }
        else{
            signup_error.setText(errorString.toString());
        }


    }




    /*
     *
     * Description: This method returns to loginController
     * Param: void
     * Returns:void
     *
     */
    public void backToLogin() throws IOException {


        String fileLoc = "/View/loginPage.fxml";


        FXMLLoader loader = new FXMLLoader(getClass().getResource(fileLoc));


        Parent root = (Parent) loader.load();
        LoginController ctrl = loader.getController();


        ctrl.initData(stage,adminClass);


        stage.getScene().setRoot(root);
    }





    /*
     *
     * Description: This method loads all usernames and passwords from the Model.database
     * Param: AdminClass adminClass
     * Returns: ArrayList<User>
     *
     */
    public static ArrayList<User> loadAccounts(AdminClass adminClass){
        ArrayList<User> returnArray = new ArrayList<>();

        for(int i = 0; i < adminClass.getUsers().size(); i++){
            returnArray.add(adminClass.getUsers().get(i));

        }

        return returnArray;

    }




    /*
     *
     * Description: Checks to see if username is present in an arrayList
     * Param: ArrayList<User> users, String username
     * Returns: boolean
     *
     */
    public static boolean usernameExists(ArrayList<User> users, String username){
        System.out.println(users.size());
        for(int i = 0; i < users.size(); i++){
            System.out.println(users.get(i).getUsername());
            if(users.get(i).getUsername().equals(username)){
                return true;
            }
        }
        return false;
    }




    /*
     *
     * Description: Checks to see if an Model.email is valid
     * Param: String Model.email
     * Returns: boolean
     *
     */
    public static boolean verifyEmail(String email){
        if(email != null){

            Pattern emailFormat = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
            Matcher matcher = emailFormat.matcher(email);
            return matcher.find();
        }
        return false;


    }

/*

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
*/


}
