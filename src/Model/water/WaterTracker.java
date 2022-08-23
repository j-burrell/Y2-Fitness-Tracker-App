/*********************************************************
 *
 *                   WaterTracker.java (Model)
 *
 *
 **********************************************************/

package Model.water;

import Model.user.User;
import home.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.time.LocalDate;


public class WaterTracker implements Tracker{


    private User user;
    private HashMap<LocalDate,Integer> waterTrackerDict;
    private int adviceAmount;

    /*
     *
     * Description: Constructor for WaterTracker
     * Param: User Controller.user
     * Returns: void
     *
     */
    public WaterTracker(User user){
        this.user = user;
        waterTrackerDict = new HashMap<>();
        adviceAmount = 2000;
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
     * Description: Method that adds a volume to a certain date
     * Param: LocalDate date, int volume
     * Returns: void
     *
     */
    public void addToDate(LocalDate date, int volume){

        boolean dateFound = false;

        for (HashMap.Entry<LocalDate, Integer> entry : waterTrackerDict.entrySet()) {
            LocalDate dateIter = entry.getKey();
            int volumeIter = entry.getValue();

            //Add the Controller.water volume to corresponding date.
            if(dateIter.equals(date)){
                entry.setValue(volumeIter+volume);
                dateFound = true;
            }
        }

        if (!dateFound){

            waterTrackerDict.put(date, volume);

        }


    }


    /*
     *
     * Description: Accessor method for waterTrackerDict
     * Param: void
     * Returns: HashMap<LocalDate,Integer>
     *
     */
    public HashMap<LocalDate,Integer> getWaterTrackerDict(){return waterTrackerDict;}


    /*
     *
     * Description: Setter method for WaterTrackerDict
     * Param: HashMap<LocalDate,Integer> waterTrackerHashMap
     * Returns: void
     *
     */
    public void setWaterTrackerDict(HashMap<LocalDate,Integer> waterTrackerHashMap){this.waterTrackerDict=waterTrackerHashMap;}



    /*
     *
     * Description: Interface implementation for generating a report for graphs
     * Param: LocalDate startDate, LocalDate endDate
     * Returns: ArrayList<Integer>
     *
     */
    @Override
    public ArrayList<Integer> generateReport(LocalDate startDate, LocalDate endDate) {
        ArrayList<Integer> reportArray = new ArrayList<>();
        LocalDate date = startDate;

        while(!date.equals(endDate.plusDays(1))){
            if(waterTrackerDict.get(date)!=null){
                reportArray.add(waterTrackerDict.get(date));
            }
            else{
                reportArray.add(0);
            }
            date = date.plusDays(1);
        }
        return reportArray;
    }




    /*
     *
     * Description: Interface Implementation for coaching
     * Param: void
     * Returns: void
     *
     */
    @Override
    public void coaching() {
        LocalDate todaysDate = LocalDate.now();
        if (waterTrackerDict.get(todaysDate) < adviceAmount){
            System.out.println("You're drinking less than reccomended");
        }

    }



}
