/*********************************************************
 *
 *                   SleepTracker.java (Model)
 *
 *
 **********************************************************/

package Model.sleep;


import Model.user.User;
import home.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.time.LocalDate;


public class SleepTracker implements Tracker {


    private User user;
    private HashMap<LocalDate,Double> sleepTrackerDict;
    private int adviceAmount;

    /*
     *
     * Description: Default Constructor for SleepTracker
     * Param: User Controller.user
     * Returns: void
     *
     */
    public SleepTracker(User user){
        this.user = user;
        sleepTrackerDict = new HashMap<>();
        adviceAmount = 7;
    }


    /*
     *
     * Description: Accessor method for advice amount
     * Param: void
     * Returns: int
     *
     */
    public int getAdviceAmount(){
        return adviceAmount;
    }


    /*
     *
     * Description: Setter method for advice amount
     * Param: int adviceAmount
     * Returns: void
     *
     */
    public void setAdviceAmount(int adviceAmount){
        this.adviceAmount = adviceAmount;

    }


    /*
     *
     * Description: Method that adds a Controller.sleep amount to a certain date
     * Param: LocalDate date, double time
     * Returns: void
     *
     */
    public void addToDate(LocalDate date, double time){

        boolean dateFound = false;

        for (HashMap.Entry<LocalDate, Double> entry : sleepTrackerDict.entrySet()) {
            LocalDate dateIter = entry.getKey();
            double timeIter = entry.getValue();

            //Add the Controller.water volume to corresponding date.
            if(dateIter.equals(date)){
                entry.setValue(timeIter+time);
                dateFound = true;
            }
        }

        if (!dateFound){

            sleepTrackerDict.put(date, time);

        }


    }


    /*
     *
     * Description: Accessor method for sleepTrackerDict
     * Param: void
     * Returns: HashMap<LocalDate,Double>
     *
     */
    public HashMap<LocalDate,Double> getSleepTrackerDict(){return sleepTrackerDict;}


    /*
     *
     * Description: Setter method for sleepTrackerDict
     * Param: HashMap<LocalDate,Double> sleepTrackerHashMap
     * Returns: void
     *
     */
    public void setSleepTrackerDict(HashMap<LocalDate,Double> sleepTrackerHashMap){this.sleepTrackerDict=sleepTrackerHashMap;}



    /*
     *
     * Description: Interface implementation to produce results for a report for a graph
     * Param: LocalDate startDate, LocalDate endDate
     * Returns: ArrayList<Double>
     *
     */
    @Override
    public ArrayList<Double> generateReport(LocalDate startDate, LocalDate endDate) {
        ArrayList<Double> reportArray = new ArrayList<>();
        LocalDate date = startDate;

        while(!date.equals(endDate.plusDays(1))){
            if(sleepTrackerDict.get(date)!=null){
                reportArray.add(sleepTrackerDict.get(date));
            }
            else{
                reportArray.add(0.0);
            }
            date = date.plusDays(1);
        }
        return reportArray;
    }




    /*
     *
     * Description: Interface implementation to advice Controller.user how much to Controller.sleep
     * Param: void
     * Returns: void
     *
     */
    @Override
    public void coaching() {
        LocalDate todaysDate = LocalDate.now();
        if (sleepTrackerDict.get(todaysDate) < adviceAmount){
            System.out.println("You're sleeping less than recomended.");
        }

    }


}
