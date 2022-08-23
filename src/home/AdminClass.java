package home;

import Model.exam.Question;
import Model.group.Group;
import Model.group.GroupGoal;
import Model.reminder.Reminder;
import Model.user.User;
import Model.exercise.*;

import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Model.database.DataBaseConnector;


public class AdminClass  {
    ArrayList<User> users;
    ArrayList<String> exerciseTypes;
    String fileLoc;
    ArrayList<Group> groups;
    private static ArrayList<Question> questions;


    public static HashMap<Reminder,String> reminders;

    public AdminClass(){
        users = new ArrayList<>();
        exerciseTypes = new ArrayList<>();
        fileLoc = "newUsers.ser";
        groups=new ArrayList<>();
        reminders=new HashMap<>();
        questions=new ArrayList<>();
    }

    public static HashMap<Reminder, String> getReminders() {
        return reminders;
    }
    public void addExerciseType(String ex){
        exerciseTypes.add(ex);
    }

    public ArrayList<String> getExerciseTypes() {
        return exerciseTypes;
    }

    public void addUser(User user){
        users.add(user);
    }

    public ArrayList<User> getUsers(){
        return users;
    }
    public void addGroup(Group group){this.groups.add(group);}

    public ArrayList<Group> getGroups(){return groups;}
    public static ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }




    /*************************************************************
        Database operations

     *********************************************************/
    public void populateloginAccounts(){



        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection(DataBaseConnector.url,
                    		DataBaseConnector.user, DataBaseConnector.password);
            c.setAutoCommit(false);

            stmt = c.createStatement();

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT Username,Password,Name FROM Users;");
            while (rs.next()) {
                User user = new User();
                user.setUsername(rs.getString("Username"));
                user.setPassword(rs.getString("Password"));
                user.setName(rs.getString("Name"));
                this.users.add(user);

            }
            rs.close();
            stmt.close();
            c.commit();
            c.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }


    }


    public String updateUserInformation(User user) throws SQLException {
        String sqlUpdate = "UPDATE Users set " +
                "Name = '" + user.getName() + "'," +
                "Gender = '" + user.getGenderString() + "'," +
                "Age =" + user.getAge() + "," +
                "Height = " + user.getHeight() + "," +
                "Weight =" + user.getWeight() + "," +
                "HeightUnitPreference ='" + user.getHeightUnitPreference() + "'," +
                "WeightUnitPreference ='" + user.getWeightUnitPreference() + "' " +

                "where Username='" + user.getUsername() + "';";

        return sqlUpdate;
    }

    public void updateUserScore(String username,int score) throws SQLException {
        String sqlUpdate = "UPDATE Users set " +

                "bestScore ='" + score + "' " +

                "where Username='" + username + "';";
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection(DataBaseConnector.url,
                            DataBaseConnector.user, DataBaseConnector.password);
            c.setAutoCommit(false);

            stmt = c.createStatement();


            stmt.executeUpdate(sqlUpdate);

            stmt.close();
            c.commit();
            c.close();



        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened Model.database successfully");
    }


    public String addNewUsertoDB(User user){

        String sqlInsert = "INSERT INTO Users(Username, Email,Password,HeightUnitPreference,WeightUnitPreference ) " +

                "VALUES('" + user.getUsername() + "','" +user.getEmail() + "','" + user.getPassword() + "','cm','kg');";
        return sqlInsert;

    }


    public void populateUserObject(User user) throws SQLException {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection(DataBaseConnector.url,
                    		DataBaseConnector.user, DataBaseConnector.password);
            c.setAutoCommit(false);

            stmt = c.createStatement();

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Users WHERE Username = '" + user.getUsername() + "';");
            while (rs.next()) {
                user.setName(rs.getString("Name"));

                user.setAge(rs.getInt("Age"));


                String gender = rs.getString("Gender");
                if(gender != null){
                    if (gender.equals("Male")) {
                        user.setGender(User.Gender.MALE);
                    } else {
                        user.setGender(User.Gender.FEMALE);
                    }}

                user.setHeight(rs.getDouble("Height"));
                user.setWeight(rs.getDouble("Weight"));

                user.setHeightUnitPreference(rs.getString("HeightUnitPreference"));
                user.setWeightUnitPreference(rs.getString("WeightUnitPreference"));
                user.setEmail(rs.getString("email"));
            }
            rs.close();
            stmt.close();
            c.commit();
            c.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUser() throws SQLException {
        List<User> userList = new ArrayList<>();
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection(DataBaseConnector.url,
                            DataBaseConnector.user, DataBaseConnector.password);
            c.setAutoCommit(false);

            stmt = c.createStatement();

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Users");
            while (rs.next()) {
                User user = new User();
                user.setName(rs.getString("Name"));
                user.setBestScore(rs.getInt("bestScore"));
                userList.add(user);


            }
            rs.close();
            stmt.close();
            c.commit();
            c.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return userList;
    }


    public int getScoreByName(String username) throws SQLException {
        int score = 0;

        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection(DataBaseConnector.url,
                            DataBaseConnector.user, DataBaseConnector.password);
            c.setAutoCommit(false);

            stmt = c.createStatement();

            stmt = c.createStatement();


            ResultSet rs = stmt.executeQuery("select * from Users where Username='" + username + "';");
            while (rs.next()) {

                score = rs.getInt("bestScore");


            }
            rs.close();
            stmt.close();
            c.commit();
            c.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return score;
    }


    public void updateUser(User user) throws IOException {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection(DataBaseConnector.url,
                    		DataBaseConnector.user, DataBaseConnector.password);
            c.setAutoCommit(false);

            stmt = c.createStatement();



            String sqlUpdate = updateUserInformation(user);

            stmt.executeUpdate(sqlUpdate);

            stmt.close();
            c.commit();
            c.close();



        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened Model.database successfully");
    }

    public void addNewUser(User user) throws IOException {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection(DataBaseConnector.url,
                    		DataBaseConnector.user, DataBaseConnector.password);
            c.setAutoCommit(false);

            stmt = c.createStatement();



            String sqlUpdate = addNewUsertoDB(user);

            stmt.executeUpdate(sqlUpdate);

            stmt.close();
            c.commit();
            c.close();



        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened Model.database successfully");
    }


    public void updateSleepTracker(LocalDate currentDate, Double amount, User user){

        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection(DataBaseConnector.url,
                    		DataBaseConnector.user, DataBaseConnector.password);
            c.setAutoCommit(false);

            stmt = c.createStatement();

            System.out.println(currentDate);
            System.out.println(amount);

            String sqlUpdate2 = "UPDATE SleepTrackerTable set " +
                    "Date ='" + Date.valueOf(currentDate) + "'," +
                    "Value = " + amount  +
                    "where Username='" + user.getUsername() + "';";

            stmt.executeUpdate(sqlUpdate2);

            stmt.close();
            c.commit();
            c.close();



        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }

    public void insertSleepTracker(LocalDate currentDate, Double amount, User user){
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection(DataBaseConnector.url,
                    		DataBaseConnector.user, DataBaseConnector.password);
            c.setAutoCommit(false);

            stmt = c.createStatement();

            String sqlInsert = "INSERT INTO SleepTrackerTable(Username, Date,Value) " +

                    "VALUES('" + user.getUsername() + "','" +currentDate+ "'," + amount + ");";

            stmt.executeUpdate(sqlInsert);

            stmt.close();
            c.commit();
            c.close();



        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }


    }

    public void loadSleepTracker(User user){
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection(DataBaseConnector.url,
                    		DataBaseConnector.user, DataBaseConnector.password);
            c.setAutoCommit(false);

            stmt = c.createStatement();

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT Date,Value FROM SleepTrackerTable WHERE Username = '" + user.getUsername() + "';");
            while (rs.next()) {
                Date date = rs.getDate("Date");
                Double value = rs.getDouble("Value");

                user.getSleepTracker().getSleepTrackerDict().put(date.toLocalDate(),value);

            }
            rs.close();
            stmt.close();
            c.commit();
            c.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }




    }


    public void updateWaterTracker(LocalDate currentDate, int amount, User user){

        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection(DataBaseConnector.url,
                    		DataBaseConnector.user, DataBaseConnector.password);
            c.setAutoCommit(false);

            stmt = c.createStatement();

            System.out.println(currentDate);
            System.out.println(amount);

            String sqlUpdate2 = "UPDATE WaterTrackerTable set " +
                    "Date ='" + Date.valueOf(currentDate) + "'," +
                    "Value = " + amount  +
                    "where Username='" + user.getUsername() + "';";

            stmt.executeUpdate(sqlUpdate2);

            stmt.close();
            c.commit();
            c.close();



        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }

    public void insertWaterTracker(LocalDate currentDate, int amount, User user){
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection(DataBaseConnector.url,
                    		DataBaseConnector.user, DataBaseConnector.password);
            c.setAutoCommit(false);

            stmt = c.createStatement();

            String sqlInsert = "INSERT INTO WaterTrackerTable(Username, Date,Value) " +

                    "VALUES('" + user.getUsername() + "','" +currentDate+ "'," + amount + ");";

            stmt.executeUpdate(sqlInsert);

            stmt.close();
            c.commit();
            c.close();



        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }


    }

    public void loadWaterTracker(User user){
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection(DataBaseConnector.url,
                    		DataBaseConnector.user, DataBaseConnector.password);
            c.setAutoCommit(false);

            stmt = c.createStatement();

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT Date,Value FROM WaterTrackerTable WHERE Username = '" + user.getUsername() + "';");
            while (rs.next()) {
                Date date = rs.getDate("Date");
                int value =(int) rs.getDouble("Value");

                user.getWaterTracker().getWaterTrackerDict().put(date.toLocalDate(),value);

            }
            rs.close();
            stmt.close();
            c.commit();
            c.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }




    }


    /******************************************************
     SQL Group

     *****************************************************/
    public User getByUserame(String username)
    {
        System.out.println("The size of the users List: " + users.size());
        for(User u:users)
        {
            if(u.getUsername().equals(username))
                return u;
        }
        return null;
    }


    public void initGroupInfo() throws Exception {

        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection(DataBaseConnector.url,
                    		DataBaseConnector.user, DataBaseConnector.password);
            System.out.println("Opened Model.database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM Groups;" );
            while ( rs.next() ) {
                //create the Controller.group
                String  groupName = rs.getString("GroupName");

                String admin= rs.getString("Admin");
                Group group=new Group(getByUserame(admin),groupName);
                System.out.println("get by username:"+getByUserame(admin).getUsername());

                //add users to the Controller.group
                stmt = c.createStatement();
                ResultSet rs2 = stmt.executeQuery( "SELECT Username FROM GroupUsers WHERE groupName ='"+groupName+"';");
                while(rs2.next())
                {

                    String username=rs2.getString("Username");
                    group.addUser(getByUserame(username));
                }
                rs2.close();
                //add goals to the Controller.group
                ResultSet groupGoalRs=stmt.executeQuery( "SELECT * FROM GroupGoals WHERE groupName ='"+groupName+"';");
                while(groupGoalRs.next())
                {
                    String goalName=groupGoalRs.getString("GoalName");
                    String exerciseType=groupGoalRs.getString("ExerciseType");
                    Double duration =(double)groupGoalRs.getFloat("Duration");
                    LocalDate startDate=(groupGoalRs.getDate("StartDate").toLocalDate());
                    LocalDate endDate=(groupGoalRs.getDate("EndDate").toLocalDate());
                    GroupGoal groupGoal=new GroupGoal();
                    groupGoal.setGoalName(goalName);
                    groupGoal.setExercise(exerciseType);
                    groupGoal.setStartDate(startDate);
                    groupGoal.setEndDate(endDate);
                    groupGoal.setGroupGoal(duration);

                    group.addGoal(groupGoal);
                }
                groupGoalRs.close();
                //groups.add(Controller.group);
                System.out.println();

                addGroup(group);
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.out.println("here2");
        }
        System.out.println("Operation done successfully");

    }

    public static void sqlExeStatement(String sql)
    {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection(DataBaseConnector.url,
                    		DataBaseConnector.user, DataBaseConnector.password);
            c.setAutoCommit(false);
            System.out.println("Opened Model.database successfully");

            stmt = c.createStatement();
            stmt.executeUpdate(sql);

            stmt.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            System.out.println("here");
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            //System.exit(0);
        }
        System.out.println("Records created successfully");
    }



    public static String deleteGroup(Group group)
    {
        String sql="DELETE FROM Groups WHERE groupName= '"+group.getGroupName()+"'";
        return sql;
    }

    //use for creating a new Controller.group
    public static String insertGroup(Group group)
    {

        String sql = "INSERT INTO Groups (GroupName,Admin) "
                + "VALUES ('"+group.getGroupName()+"','"+group.getAdmin().getUsername()+"')";

        return sql;

    }
    //use for join a Controller.group for a Controller.user
    public static String insertGroupUser(Group group,User user)
    {

        String sql = "INSERT INTO GroupUsers (GroupName,UserName) "
                + "VALUES ('"+group.getGroupName()+"','"+ user.getUsername()+"');";

        return sql;
    }
    //use for leaving the Controller.group for a Controller.user
    public static void deleteGroupUser(Group group, User user)
    {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection(DataBaseConnector.url,
                    		DataBaseConnector.user, DataBaseConnector.password);
            c.setAutoCommit(false);
            System.out.println("Opened Model.database successfully");

            stmt = c.createStatement();
            String sql = "DELETE from GroupUsers WHERE GroupName ='"+group.getGroupName()+"'AND UserName ='"+user.getUsername()+"'";
            stmt.executeUpdate(sql);

            stmt.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println( "asdasdasd" + e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        System.out.println("Records created successfully");
    }


    //use for creating a new Controller.group goal
    public static String insertGroupGoal(Group group,GroupGoal goal)
    {
        String sql="INSERT INTO GroupGoals (GroupName,GoalName,ExerciseType,Duration,StartDate,EndDate)"
                +"VALUES ('"+group.getGroupName()+"','"+goal.getGoalName()+"','"+goal.getExercise()
                +"',"+goal.getGroupGoal()+",'"+Date.valueOf(goal.getStartDate())+"','"+Date.valueOf(goal.getEndDate())+"')";

        return sql;
    }

    //use for editing a goal
    public static String updateGroupGoal(String goalName,GroupGoal g)
    {
        String sql= "UPDATE GroupGoals set "
                +"GoalName ='"+g.getGoalName()
                +"',ExerciseType='"+g.getExercise()
                +"',Duration="+g.getGroupGoal()
                +",StartDate = '"+g.getStartDate()+"'"
                +",EndDate= '" +g.getEndDate()+"'"
                +"WHERE GoalName='"+goalName+"'";
        return sql;
    }

    //use for deleting a goal
    public static String deleteGroupGoal(String goalName)
    {
        String sql="DELETE FROM GroupGoals WHERE GoalName='"+goalName+"'";

        return sql;
    }

    public void initExerciseTracker() {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection(DataBaseConnector.url,
                    		DataBaseConnector.user, DataBaseConnector.password);
            c.setAutoCommit(false);
            System.out.println("Opened Model.database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM ExerciseTracker" );
            while ( rs.next() ) {

                String username = rs.getString("Username");
               // LocalDate exerciseDate= convertToLocalDateViaInstant(rs.getDate("ExerciseDate"));
                LocalDate exerciseDate= (rs.getDate("ExerciseDate").toLocalDate());

                String exerciseType=rs.getString("ExerciseType");
                LocalTime exerciseStartTime=rs.getTime("ExerciseStartTime").toLocalTime();
                LocalTime exerciseEndTime=rs.getTime("ExerciseEndTime").toLocalTime();
                for(User user:users)
                {
                    if(user.getUsername().equals(username))
                    {
                        Exercise e=new Exercise(exerciseType,exerciseStartTime,exerciseEndTime);
                        if(user.getExerciseTracker().getExerciseTracker().get(exerciseDate) == null){
                            user.getExerciseTracker().getExerciseTracker().put(exerciseDate,new ArrayList<Exercise>());
                        }
                        user.getExerciseTracker().getExerciseTracker().get(exerciseDate).add(e);

                    }
                }

                System.out.println();

            }
            stmt = c.createStatement();
            ResultSet rs2 = stmt.executeQuery( "SELECT * FROM ExerciseGoal" );
            while(rs2.next())
            {
                String  username = rs2.getString("Username");
                double goal=(double)rs2.getFloat("Goal");
                LocalDate goalStart= (rs2.getDate("GoalStart").toLocalDate());
                LocalDate goalEnd= (rs2.getDate("GoalEnd").toLocalDate());
                for(User user:users)
                {
                    if(user.getUsername().equals(username))
                    {
                        user.getExerciseTracker().setGoal(goalStart,goalEnd,goal);
                    }
                }

            }
            rs2.close();
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.out.println(e);

        }
        System.out.println("Operation done successfully");
    }

    public String insertExerciseTracker(User user,Exercise e,LocalDate d)
    {

        String sql="INSERT INTO ExerciseTracker (Username,ExerciseDate,ExerciseType,ExerciseStartTime,ExerciseEndTime)"
                +"VALUES ('"+user.getUsername()+"','"+Date.valueOf(d)
                +"','"+e.getExercise_type()+"','"+e.getStart_time()+"','"
                +e.getEnd_time()+"');";
        return sql;
    }

    public String deleteExercise(User user,Exercise e,LocalDate d)
    {
        String sql="DELETE FROM ExerciseTracker WHERE username='"+user.getUsername()+"' "
                +"AND ExerciseDate ='"+Date.valueOf(d)+"'AND ExerciseType = '"+e.getExercise_type()+"';";
        return  sql;
    }

    public String deleteGoal(User user)
    {

        String sql  = "DELETE FROM ExerciseGoal WHERE username = '" + user.getUsername() + "';";
        return sql;
    }


    public static String updateGoal(User user,double goal,LocalDate start, LocalDate end)
    {
        String sql="UPDATE ExerciseGoal SET Goal="+goal+"," +
                "GoalStart='"+Date.valueOf(start)+"', GoalEnd = '"+Date.valueOf(end)+"' WHERE Username='"
                +user.getUsername()+"';";
        return sql;
    }
    public static String insertGoal(User user,double goal,LocalDate start, LocalDate end)
    {
        String sql="INSERT INTO ExerciseGoal(Username,Goal,GoalStart,GoalEnd) VALUES('"+user.getUsername() + "',"+goal+"," +
                "'"+Date.valueOf(start)+"','"+Date.valueOf(end)+ "');";
        return sql;
    }

    public  void initExercise()
    {
        Connection c = null;
        Statement stmt = null;
       try {
        Class.forName("org.postgresql.Driver");
        c = DriverManager
                .getConnection(DataBaseConnector.url,
                        DataBaseConnector.user, DataBaseConnector.password);
        c.setAutoCommit(false);
        System.out.println("Opened Model.database successfully");

        stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery( "SELECT * FROM ExerciseType" );
        while ( rs.next() ) {

            String type = rs.getString("Type");
            this.exerciseTypes.add(type);

            System.out.println();

        }
        rs.close();
        stmt.close();
        c.close();
        } catch ( Exception e ) {

            System.out.println(e);

        }
        System.out.println("Operation done successfully");
    }

    public static String insertExerciseType(String type)
    {
        String sql="INSERT INTO ExerciseType (Type) VALUES ('"+type+"')";
        return sql;
    }



    //pull key values
    public void initKeyValues(User user) throws SQLException {
        System.out.println("initing key vals");
        DataBaseConnector db = new DataBaseConnector();
        String sqlSelect =

        "SELECT * FROM KeyValues;";
        ResultSet rs = db.getStatement(sqlSelect).executeQuery();
        System.out.println(rs.toString());
        double bmi_UNDERWEIGHT = 0;
        double bmi_NORMAL_lower = 0;
        double BMI_NORMAL_upper = 0;
        double BMI_OVERWEIGHT_lower = 0;
        double BMI_OVERWEIGHT_upper = 0;
        double BMI_OBESE_lower = 0;
        double BMI_OBESE_upper = 0;
        double BMI_MORBIDLY_OBESE_upper=0;

        double HEIGHT_MAX_CM = 0;
        double HEIGHT_MAX_IN = 0;
        double WEIGHT_MAX_KG = 0;
        double WEIGHT_MAX_LB = 0;

        while (rs.next()) {


            switch (rs.getString("valueName")){
                case "BMI_UNDERWEIGHT":
                    bmi_UNDERWEIGHT = rs.getDouble("value");
                    break;
                case "BMI_NORMAL_lower":
                    bmi_NORMAL_lower = rs.getDouble("value");
                    break;
                case "BMI_NORMAL_upper":
                    BMI_NORMAL_upper = rs.getDouble("value");
                    break;
                case "BMI_OVERWEIGHT_lower":
                    BMI_OVERWEIGHT_lower = rs.getDouble("value");
                    break;
                case "BMI_OVERWEIGHT_upper":
                    BMI_OVERWEIGHT_upper = rs.getDouble("value");
                    break;
                case "BMI_OBESE_lower":
                    BMI_OBESE_lower = rs.getDouble("value");
                    break;
                case "BMI_OBESE_upper":
                    BMI_OBESE_upper = rs.getDouble("value");
                    break;
                case "BMI_MORBIDLY_OBESE_upper":
                    BMI_MORBIDLY_OBESE_upper = rs.getDouble("value");
                    break;
                case "HEIGHT_MAX_CM":
                    HEIGHT_MAX_CM = rs.getDouble("value");
                    System.out.println("max cm: " + HEIGHT_MAX_CM);
                    break;
                case "HEIGHT_MAX_IN":
                    HEIGHT_MAX_IN= rs.getDouble("value");
                    System.out.println("maxin: " + HEIGHT_MAX_IN);

                    break;
                case "WEIGHT_MAX_KG":
                    WEIGHT_MAX_KG= rs.getDouble("value");
                    System.out.println("max kg: " + WEIGHT_MAX_KG);

                    break;
                case "WEIGHT_MAX_LB":
                    WEIGHT_MAX_LB = rs.getDouble("value");
                    System.out.println("max lb: " + WEIGHT_MAX_LB);
                    break;


            }


            user.setBMIRanges(bmi_UNDERWEIGHT,bmi_NORMAL_lower, BMI_NORMAL_upper, BMI_OVERWEIGHT_lower, BMI_OVERWEIGHT_upper,
                     BMI_OBESE_lower,BMI_OBESE_upper,BMI_MORBIDLY_OBESE_upper);

            user.setHeightWeightRanges(HEIGHT_MAX_CM, HEIGHT_MAX_IN, WEIGHT_MAX_KG, WEIGHT_MAX_LB);
        }
        rs.close();
        db.close();
    }



    /*****
     *
     *
     * FIT BIT
     *
     *
     */

    public void checkToken(User user){
        System.out.println("checking token");

        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection(DataBaseConnector.url,
                            DataBaseConnector.user, DataBaseConnector.password);
            c.setAutoCommit(false);

            stmt = c.createStatement();

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT token FROM FitBitUserTokens WHERE Username = \'"+ user.getUsername() + "\';");
            if(rs.next() == false){
                System.out.println("No valid token");

            }
            else {

                System.out.println("token exists, adding");
                String token = rs.getString("token");
                System.out.println(token);
                System.out.println("to Controller.user");
            user.setFitBitToken(token);

            }

            rs.close();
            stmt.close();
            c.commit();
            c.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }



    }

    public void updateToken(User user, String token){

        String sqlInsert = "UPDATE FitBitUserTokens SET token=\'"+token+"\' WHERE Username = \'" +  user.getUsername() + "\';";

        System.out.println("token savingin to db");
        System.out.println(token);
        System.out.println(sqlInsert);
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection(DataBaseConnector.url,
                            DataBaseConnector.user, DataBaseConnector.password);
            c.setAutoCommit(false);

            stmt = c.createStatement();





            stmt.executeUpdate(sqlInsert);

            stmt.close();
            c.commit();
            c.close();



        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened Model.database successfully");
    }

    public  void initReminder()
    {
        System.out.println("getting reminder");
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection(DataBaseConnector.url,
                            DataBaseConnector.user, DataBaseConnector.password);
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM Reminder" );
            while ( rs.next() ) {
                String username=rs.getString("Username");
                String event=rs.getString("Event");
                String type = rs.getString("Type");
                String remark=rs.getString("Remarks");
                LocalDate date=LocalDate.parse(rs.getString("Date"));
                LocalTime time=LocalTime.parse(rs.getString("Time"));
                Boolean email=rs.getBoolean("email");
                Reminder reminder=new Reminder(date,time,event,remark,Reminder.EventType.valueOf(type),email);
                reminders.put(reminder,username);
                System.out.println("reminder"+reminder);

            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {

            System.out.println(e);

        }
        System.out.println("Operation done successfully");
    }
    public static String addReminder(User user, Reminder r)
    {
        String sqlInsert="INSERT INTO Reminder(Username,Date,Time,Event,Remarks,Type,email)"+
                "VALUES('"+ user.getUsername()+"','"+ Date.valueOf(r.getDate()) +"','"+r.getTime()+"','"+r.getEvent()+"','"+r.getRemarks()+"','"+r.getType()+"','false');";
        return sqlInsert;
    }

    public static String deleteReminder(User user, Reminder r)
    {
        String sqlDelete="DELETE FROM Reminder WHERE username='"+user.getUsername()+"' "
                +"AND Event ='"+r.getEvent()+"'AND Date = '"+Date.valueOf(r.getDate())+"';";
        return sqlDelete;
    }
    public static String updateReminder(User user, Reminder r)
    {
        String sqlUpdate="UPDATE reminder SET email='true' where username='"+user.getUsername()+"' "
                +"AND Event ='"+r.getEvent()+"'AND Date = '"+Date.valueOf(r.getDate())+"';";;

        return sqlUpdate;
    }

    public  void initExam()
    {
        System.out.println("getting reminder");
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection(DataBaseConnector.url,
                            DataBaseConnector.user, DataBaseConnector.password);
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM quiz" );
            while ( rs.next() ) {
                String question=rs.getString("question");
                String selection1=rs.getString("selection1");
                String selection2= rs.getString("selection2");
                String selection3= rs.getString("selection3");
                String selection4=rs.getString("selection4");
                int num=rs.getInt("number");
                Question temp=new Question(question,selection1,selection2,selection3,selection4,num);
                questions.add(temp);
            }
            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {

            System.out.println(e);

        }
        System.out.println("Operation done successfully");
    }


}
