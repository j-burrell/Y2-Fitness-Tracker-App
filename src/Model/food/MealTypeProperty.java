package Model.food;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MealTypeProperty{
	
	private  StringProperty name;
	private IntegerProperty calorie;
	public String getName() {
		return name.get();
	}
	public MealTypeProperty(String  name, int calorie) {
		super();
		this.name = new SimpleStringProperty(name);
		this.calorie = new SimpleIntegerProperty(calorie);
	}
	public void setName(String name) {
		this.name = new SimpleStringProperty(name);
	}
	public Integer getCalorie() {
		return calorie.get();
	}
	public void setCalorie(int calorie) {
		this.calorie = new SimpleIntegerProperty(calorie);
	}
	
	

}
