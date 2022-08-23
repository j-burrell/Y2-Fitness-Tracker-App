/*********************************************************
 *
 *                   Meal.java (Model)
 *
 *
 **********************************************************/

package Model.food;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class Meal {
	private List<MealType> breakfast = new ArrayList<>();
    private List<MealType> lunch = new ArrayList<>();
    private List<MealType> dinner = new ArrayList<>();
    private List<MealType> snack = new ArrayList<>();




	/*
	 *
	 * Description: Accessor method for breakfast
	 * Param: void
	 * Returns: List<MealType>
	 *
	 */
    public List<MealType> getBreakfast() {
		return breakfast;
	}

	/*
	 *
	 * Description: Setter method for Breakfast
	 * Param:List<MealType> breakfast
	 * Returns: void
	 *
	 */
	public void setBreakfast(List<MealType> breakfast) {
		this.breakfast = breakfast;
	}

	/*
	 *
	 * Description: Accessor method for Lunch
	 * Param: void
	 * Returns:List<MealType>
	 *
	 */
	public List<MealType> getLunch() {
		return lunch;
	}

	/*
	 *
	 * Description: Setter method for lunch
	 * Param: List<MealType> lunch
	 * Returns: void
	 *
	 */
	public void setLunch(List<MealType> lunch) {
		this.lunch = lunch;
	}

	/*
	 *
	 * Description: Accessor method for Dinner
	 * Param: void
	 * Returns: List<MealType>
	 *
	 */
	public List<MealType> getDinner() {
		return dinner;
	}

	/*
	 *
	 * Description: Setter method for Dinner
	 * Param: List<MealType> dinner
	 * Returns: void
	 *
	 */
	public void setDinner(List<MealType> dinner) {
		this.dinner = dinner;
	}

	/*
	 *
	 * Description: Accessor method for snack
	 * Param: void
	 * Returns: List<MealType>
	 *
	 */
	public List<MealType> getSnack() {
		return snack;
	}

	/*
	 *
	 * Description: Setter method for snack
	 * Param: List<MealType> snack
	 * Returns: void
	 *
	 */
	public void setSnack(List<MealType> snack) {
		this.snack = snack;
	}

	/*
	 *
	 * Description: Accessor method for total colaories
	 * Param: void
	 * Returns: int
	 *
	 */
	public int getTotalCalorie() {
    int  totalCalorie = 0;
    	for (MealType mealType:breakfast) {    		
    		totalCalorie+=mealType.getCalorie();
		}
    	for (MealType mealType:lunch) {    		
    		totalCalorie+=mealType.getCalorie();
		}
    	for (MealType mealType:dinner) {    		
    		totalCalorie+=mealType.getCalorie();
		}
    	for (MealType mealType:snack) {    		
    		totalCalorie+=mealType.getCalorie();
		}
		return totalCalorie;
    	
    }

	/*
	 *
	 * Description: Accessor method for foodCount
	 * Param: List<String> foodNameList
	 * Returns: HashMap<String,Integer>
	 *
	 */
    public HashMap<String,Integer> getFoodCount(List<String> foodNameList){
    	HashMap<String,Integer> foodCount = new HashMap<String, Integer>();
    	
    	for (String food :foodNameList) {
    		foodCount.put(food, 0);			
		}
    	
    	for (MealType mealType:breakfast) {   
    		for (Entry<String, Integer> entry:foodCount.entrySet()) {
    			String foodName = entry.getKey();
    			if(foodName.equals(mealType.getName())){
					int value = entry.getValue();
					entry.setValue(value+1);
				}
			}
    		 
		}
      	for (MealType mealType:lunch) {   
    		for (Entry<String, Integer> entry:foodCount.entrySet()) {
    			String foodName = entry.getKey();
				if(foodName.equals(mealType.getName())){
					int value = entry.getValue();
					entry.setValue(value+1);
				}
			}
    		 
		}
      	for (MealType mealType:dinner) {   
    		for (Entry<String, Integer> entry:foodCount.entrySet()) {
    			String foodName = entry.getKey();
				if(foodName.equals(mealType.getName())){
					int value = entry.getValue();
					entry.setValue(value+1);
				}
			}
    		 
		}
      	for (MealType mealType:snack) {   
    		for (Entry<String, Integer> entry:foodCount.entrySet()) {
    			String foodName = entry.getKey();
				if(foodName.equals(mealType.getName())){
					int value = entry.getValue();
					entry.setValue(value+1);
				}
			}
    		 
		}
    	
    	List<String> removeZeroItems = new ArrayList<>();
    	for (Entry<String, Integer> entry:foodCount.entrySet()) {
			String foodName = entry.getKey();
			int value = entry.getValue();
			if(value==0) {
				removeZeroItems.add(foodName);
			}  
		}
    	for(String name:removeZeroItems){
			foodCount.remove(name);
		}
    	return foodCount;
    }

}
