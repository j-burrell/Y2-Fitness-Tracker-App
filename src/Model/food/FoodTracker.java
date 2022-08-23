/*********************************************************
 *
 *                   FoodTracker.java (Model)
 *
 *
 **********************************************************/
package Model.food;

import home.Tracker;
import Model.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.time.LocalDate;


public class FoodTracker implements Tracker {


    private User user;
    private HashMap<LocalDate,Meal> dailyMeal;


    /*
     *
     * Description: Constructor for FoodTracker
     * Param: User Controller.user
     * Returns: void
     *
     */
    public FoodTracker(User user) {
		super();
		this.user = user;
		dailyMeal = new HashMap<>();
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
     * Description: Setter method for Controller.user
     * Param: User Controller.user
     * Returns: void
     *
     */
	public void setUser(User user) {
		this.user = user;
	}

    /*
     *
     * Description: This method keeps track of users diet by adding it to the dictionary
     * Param: LocalDate date, Meal meal
     * Returns: void
     *
     */
	public void addToDate(LocalDate date, Meal meal){

        boolean dateFound = false;

        for (HashMap.Entry<LocalDate, Meal> entry : dailyMeal.entrySet()) {
            LocalDate dateIter = entry.getKey();

            if(dateIter.equals(date)){
                entry.setValue(meal);
                dateFound = true;
            }
        }

        if (!dateFound){

        	dailyMeal.put(date, meal);

        }


    }



    /*
     *
     * Description: Accessor method for meal.
     * Param: void
     * Returns:HashMap<LocalDate, Meal>
     *
     */
    public HashMap<LocalDate, Meal> getDailyMeal() {
		return dailyMeal;
	}


    /*
     *
     * Description: This method overwrites the meal dictionary
     * Param:HashMap<LocalDate, Meal> dailyMeal
     * Returns: void
     *
     */
	public void setDailyMeal(HashMap<LocalDate, Meal> dailyMeal) {
		this.dailyMeal = dailyMeal;
	}


    /*
     *
     * Description: This method generates the information needed in the report.
     * Param: LocalDate startDate, LocalDate endDate
     * Returns:ArrayList<Integer>
     *
     */
	@Override
    public ArrayList<Integer> generateReport(LocalDate startDate, LocalDate endDate) {
        ArrayList<Integer> reportArray = new ArrayList<>();
        LocalDate date = startDate;

        while(!date.equals(endDate.plusDays(1))){
            if(dailyMeal.get(date)!=null){
                reportArray.add(dailyMeal.get(date).getTotalCalorie());
            }
            else{
                reportArray.add(0);
            }
            date = date.plusDays(1);
        }
        return reportArray;
    }

	@Override
	public void coaching() {
		
	}

 
}
