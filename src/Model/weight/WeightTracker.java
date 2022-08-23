/*********************************************************
 *
 *                   WeightTracker.java (Model)
 *
 *
 **********************************************************/

package Model.weight;

import home.Tracker;
import Model.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.time.LocalDate;


public class WeightTracker implements Tracker{


    private User user;
    private HashMap<LocalDate,Integer> targetWeightDict;
    private HashMap<LocalDate,Integer> todayWeightDict;


    /*
     *
     * Description: Construct for WeightTracker
     * Param: User Controller.user
     * Returns: void
     *
     */
    public WeightTracker( User user){
        this.user = user;
        targetWeightDict = new HashMap<>();
        todayWeightDict = new HashMap<>();

    }



    /*
     *
     * Description: Accessor method for Controller.user
     * Param: void
     * Returns: User
     *
     */
    public User getUser() {
		return user;
	}



    /*
     *
     * Description: Setter method for User
     * Param: User Controller.user
     * Returns: void
     *
     */
	public void setUser(User user) {
		this.user = user;
	}


    /*
     *
     * Description: Method that adds target Controller.weight and todays Controller.weight to tracker
     * Param: LocalDate date, int targetWeight,int todayWeight
     * Returns: void
     *
     */
	public void addToDate(LocalDate date, int targetWeight,int todayWeight){

        boolean dateFound = false;

        for (HashMap.Entry<LocalDate, Integer> entry : targetWeightDict.entrySet()) {
            LocalDate dateIter = entry.getKey();
            int volumeIter = entry.getValue();

            //Add the Controller.water volume to corresponding date.
            if(dateIter.equals(date)){
                entry.setValue(targetWeight);
            }
        }

        if (!dateFound){

        	targetWeightDict.put(date, targetWeight);

        }
        
        for (HashMap.Entry<LocalDate, Integer> entry : todayWeightDict.entrySet()) {
            LocalDate dateIter = entry.getKey();
      

            //Add the Controller.water volume to corresponding date.
            if(dateIter.equals(date)){
                entry.setValue(todayWeight);
                dateFound = true;
            }
        }

        if (!dateFound){

        	todayWeightDict.put(date, todayWeight);
        }


    }


    /*
     *
     * Description: AccessorMethod for targetWeightDict
     * Param: void
     * Returns: HashMap<LocalDate,Integer>
     *
     */
    public HashMap<LocalDate, Integer> getTargetWeightDict() {
		return targetWeightDict;
	}



    /*
     *
     * Description: Setter method for targetWeightDict
     * Param: HashMap<LocalDate, Integer> targetWeightDict
     * Returns: void
     *
     */
	public void setTargetWeightDict(HashMap<LocalDate, Integer> targetWeightDict) {
		this.targetWeightDict = targetWeightDict;
	}



    /*
     *
     * Description: Accessor method for todaysWeightDict
     * Param: void
     * Returns:HashMap<LocalDate, Integer>
     *
     */
	public HashMap<LocalDate, Integer> getTodayWeightDict() {
		return todayWeightDict;
	}



    /*
     *
     * Description: Setter method for todaysWeightDict
     * Param:HashMap<LocalDate, Integer> todayWeightDict
     * Returns: void
     *
     */
	public void setTodayWeightDict(HashMap<LocalDate, Integer> todayWeightDict) {
		this.todayWeightDict = todayWeightDict;
	}



    /*
     *
     * Description:Interface implementation for generating report information
     * Param:LocalDate startDate, LocalDate endDate
     * Returns:ArrayList<Integer>
     *
     */
	@Override
    public ArrayList<Integer> generateReport(LocalDate startDate, LocalDate endDate) {
        ArrayList<Integer> reportArray = new ArrayList<>();
        LocalDate date = startDate;

        while(!date.equals(endDate.plusDays(1))){
            if(todayWeightDict.get(date)!=null){
                reportArray.add(todayWeightDict.get(date));
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
     * Description: Accessor method for target Controller.weight
     * Param:LocalDate startDate, LocalDate endDate
     * Returns:ArrayList<Integer>
     *
     */
    public ArrayList<Integer> getTarget(LocalDate startDate, LocalDate endDate) {
        ArrayList<Integer> reportArray = new ArrayList<>();
        LocalDate date = startDate;

        while(!date.equals(endDate.plusDays(1))){
            if(targetWeightDict.get(date)!=null){
                reportArray.add(targetWeightDict.get(date));
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
     * Description: Interface implementation of coaching
     * Param: void
     * Returns:void
     *
     */
	@Override
	public void coaching() {
		// TODO Auto-generated method stub
		
	}

 
}
