/*********************************************************
 *
 *                   User.java (Model)
 *
 *
 **********************************************************/


package Model.user;


import Model.reminder.ReminderTracker;
import Model.food.FoodTracker;
import Model.group.Group;
import Model.sleep.SleepTracker;
import Model.water.WaterTracker;
import Model.weight.WeightTracker;
import Model.exercise.*;


import java.util.ArrayList;

public class User {




    //Enum creation.
    public enum Gender{MALE,FEMALE};
    private enum BMICategory{UNDERWEIGHT, NORMAL, OVERWEIGHT, OBESE,MORBIDLY_OBESE}

    //Account information.
    private String username;
    private String email;
    private String name;
    private String password;

    private String fitBitToken;

    //Personal information.
    private String heightUnitPreference;
    private String weightUnitPreference;


    private double weight;
    private double height;
    private int age;

    public int getBestScore() {
        return bestScore;
    }

    public void setBestScore(int bestScore) {
        this.bestScore = bestScore;
    }

    private int bestScore;


    private Gender gender;


    private double BMIValue;
    private BMICategory bmiCategory;

    private double BMI_UNDERWEIGHT;
    private double BMI_NORMAL_lower;
    private double BMI_NORMAL_upper;
    private double BMI_OVERWEIGHT_lower;
    private double BMI_OVERWEIGHT_upper;
    private double BMI_OBESE_lower;
    private double BMI_OBESE_upper;
    private double BMI_MORBIDLY_OBESE_upper;


    private double HEIGHT_MAX_CM;
    private double HEIGHT_MAX_IN;
    private double WEIGHT_MAX_KG;
    private double WEIGHT_MAX_LB;


    private ArrayList<Group> groupArray;


//Personal Tracker information.

    private WaterTracker waterTracker;
    private SleepTracker sleepTracker;
    private ExerciseTracker exerciseTracker;
    private WeightTracker weightTracker;
    private FoodTracker foodTracker;
    private ReminderTracker reminderTracker;



    /*
     *
     * Description: Default constructor
     * Param: void
     * Returns: void
     *
     */
    public User(){

        name = null;
        gender = null;
        age = 0;
        height = 0;
        weight = 0;
        waterTracker = new WaterTracker(this);
        sleepTracker = new SleepTracker(this);
        exerciseTracker = new ExerciseTracker(this);
        weightTracker =new WeightTracker(this);
        foodTracker =new FoodTracker(this);
        groupArray = new ArrayList<Group>();
        reminderTracker=new ReminderTracker(this);
        heightUnitPreference = "cm";
        weightUnitPreference = "kg";

        fitBitToken = "";
        HEIGHT_MAX_CM = 0;
        HEIGHT_MAX_IN = 0;
        WEIGHT_MAX_KG = 0;
        WEIGHT_MAX_LB = 0;

    }



    /*
     *
     * Description: alternative constructor
     * Param:String username, String Model.email, String password, double Controller.weight, double height, int age, String gender
     * Returns: void
     *
     */
    public User(String username, String email, String password, double weight, double height, int age, String gender){

        this.username = username;
        this.email = email;
        this.password = password;
        this.weight = weight;
        this.height = height;
        this.age = age;

        this.BMIValue = 0;

        if(gender.equals("m")){
            this.gender = Gender.MALE;
        }
        else{
            this.gender = Gender.FEMALE;
        }

        fitBitToken = "";
         HEIGHT_MAX_CM = 0;
         HEIGHT_MAX_IN = 0;
         WEIGHT_MAX_KG = 0;
         WEIGHT_MAX_LB = 0;

    }



    /*
     *
     * Description: sets bmi values
     * Param:double uw, double nL, double nU, double owL, double owU, double oL, double oU, double mo
     * Returns: void
     *
     */
    public void setHeightWeightRanges(double HEIGHT_MAX_CM, double HEIGHT_MAX_IN, double WEIGHT_MAX_KG, double WEIGHT_MAX_LB){
        this.HEIGHT_MAX_CM = HEIGHT_MAX_CM;
        this.HEIGHT_MAX_IN = HEIGHT_MAX_IN;
        this.WEIGHT_MAX_KG = WEIGHT_MAX_KG;
        this.WEIGHT_MAX_LB = WEIGHT_MAX_LB;


    }
    /*
     *
     * Description: sets bmi values
     * Param:double uw, double nL, double nU, double owL, double owU, double oL, double oU, double mo
     * Returns: void
     *
     */
    public void setBMIRanges(double uw, double nL, double nU, double owL, double owU, double oL, double oU, double mo){
        BMI_UNDERWEIGHT = uw;
        BMI_NORMAL_lower = nL;
        BMI_NORMAL_upper = nU;
        BMI_OVERWEIGHT_lower = owL;
        BMI_OVERWEIGHT_upper= owU;
        BMI_OBESE_lower = oL;
        BMI_OBESE_upper = oU;
        BMI_MORBIDLY_OBESE_upper = mo;


    }



    /*
     *
     * Description: This method calculates the users BMI based on their height and Controller.weight
     * Param: void
     * Returns: double
     *
     */
    public double calculateBMI(){
        BMIValue = weight/((height/100)*(height/100));
        System.out.println("Controller.weight: " + weight);
        System.out.println("height: " + height);
        System.out.println("bmvalue : " + BMIValue);
        System.out.println(BMI_UNDERWEIGHT);
        if( BMIValue < BMI_UNDERWEIGHT){
            bmiCategory = BMICategory.UNDERWEIGHT;
        }
        else if( BMIValue >= BMI_NORMAL_lower && BMIValue <= BMI_NORMAL_upper){
            bmiCategory = BMICategory.NORMAL;


        }
        else if( BMIValue >= BMI_OVERWEIGHT_lower && BMIValue <= BMI_OVERWEIGHT_upper){
            bmiCategory = BMICategory.OVERWEIGHT;


        }
        else if( BMIValue >= BMI_OBESE_lower && BMIValue <= BMI_OBESE_upper){
            bmiCategory = BMICategory.OBESE;


        }
        else if(BMIValue >= BMI_MORBIDLY_OBESE_upper){
            bmiCategory = BMICategory.MORBIDLY_OBESE;


        }
        return BMIValue;
    }

    //Accessor methods
    public String getUsername(){return username;}
    public String getName(){return name;}
    public String getEmail(){return email;}
    public String getPassword(){return password;}

    public double getWeight(){return weight;}
    public double getHeight(){return height;}
    public double getBMIValue(){return BMIValue;}

    public int getAge(){return age;}

    public Gender getGender(){return gender;}
    public String getGenderString(){
        if (gender == Gender.FEMALE){
            return "FEMALE";
        }
        if(gender == Gender.MALE){
            return "MALE";
        }
        else{return null;}
    }

    public String getBmiCategory(){return bmiCategory.toString();}


    public WaterTracker getWaterTracker(){return waterTracker;}
    public SleepTracker getSleepTracker(){return sleepTracker;}

    public ExerciseTracker getExerciseTracker(){return exerciseTracker;}



    public WeightTracker getWeightTracker(){return weightTracker;}
    public FoodTracker getFoodTracker(){return foodTracker;}


    public ArrayList<Group> getGroupArray(){return groupArray;}

    public String getHeightUnitPreference() {
        return heightUnitPreference;
    }

    public String getWeightUnitPreference() {
        return weightUnitPreference;
    }

    public String getFitBitToken(){ return fitBitToken;}

    public double getHEIGHT_MAX_CM() {
        return HEIGHT_MAX_CM;
    }

    public double getHEIGHT_MAX_IN() {
        return HEIGHT_MAX_IN;
    }

    public double getWEIGHT_MAX_KG() {
        return WEIGHT_MAX_KG;
    }

    public double getWEIGHT_MAX_LB() {
        return WEIGHT_MAX_LB;
    }



    //Setter methods
    public void setUsername(String username){this.username=username;}
    public void setName(String name){this.name=name;}
    public void setEmail(String email){this.email = email;}
    public void setPassword(String password){this.password = password;}

    public void setWeight(double weight){this.weight = weight;}
    public void setHeight(double height){this.height = height;}
    public void setBMIValue(double bmiValue){this.BMIValue = BMIValue;}

    public void setAge(int age){this.age = age;}

    public void setGender(Gender gender){this.gender = gender;}

    public void setWeightUnitPreference(String weightUnitPreference) {
        this.weightUnitPreference = weightUnitPreference;
    }

    public void setHeightUnitPreference(String heightUnitPreference) {
        this.heightUnitPreference = heightUnitPreference;
    }

    public void setFitBitToken(String fitBitToken){
        this.fitBitToken = fitBitToken;
    }

    public ReminderTracker getReminderTracker() {
        return reminderTracker;
    }

    public void setReminderTracker(ReminderTracker reminderTracker) {
        this.reminderTracker = reminderTracker;
    }

}
