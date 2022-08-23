/*********************************************************
 *
 *                   MealType.java (Model)
 *
 *
 **********************************************************/

package Model.food;

import java.io.Serializable;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MealType{
	private  String name;
	private int calorie;
	/*
	 *
	 * Description: Constructor method for MealType
	 * Param: String name, int calorie
	 * Returns: void
	 *
	 */
	public MealType(String name, int calorie) {
		super();
		this.name = name;
		this.calorie = calorie;
	}


	/*
	 *
	 * Description: Accessor method for name
	 * Param: void
	 * Returns: String name
	 *
	 */
	public String getName() {
		return name;
	}

	/*
	 *
	 * Description: Setter method for name
	 * Param: String name
	 * Returns: void
	 *
	 */
	public void setName(String name) {
		this.name = name;
	}

	/*
	 *
	 * Description: Accessor method for calorie
	 * Param: void
	 * Returns: int
	 *
	 */
	public int getCalorie() {
		return calorie;
	}

	/*
	 *
	 * Description: Setter method for calorie
	 * Param: int calorie
	 * Returns: void
	 *
	 */
	public void setCalorie(int calorie) {
		this.calorie = calorie;
	}
	 
	

}
