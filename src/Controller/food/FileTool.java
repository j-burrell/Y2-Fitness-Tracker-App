/*********************************************************
 *
 *                   FileTool.java (Database assist)
 *
 *
 **********************************************************/

package Controller.food;

import java.sql.ResultSet;
import java.sql.SQLException;

import Model.database.DataBaseConnector;
import Model.food.MealType;
import Model.food.MealTypeProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FileTool {
 
	//create a DataBase Connector 
	public static DataBaseConnector dc=new DataBaseConnector();

    /*
     *
     * Description: This static method saves a meal by updating it into the Model.database
     * Param: MealType mealType
     * Returns: void
     *
     */
	public static void addMeal(MealType mealType) {
		String foodName = mealType.getName();
		int calorie = mealType.getCalorie();
		String sql="insert into meals (name,calorie) values ('"+foodName+"',"+calorie+")";
        try {
             dc.getStatement(sql).executeUpdate(); 
        } catch (SQLException e) {
          
            e.printStackTrace();
        }
		
	}

    /*
     *
     * Description: This method gets all meals from the Model.database
     * Param: void
     * Returns: ObservableList<MealTypeProperty>
     *
     */
    public static ObservableList<MealTypeProperty> getMeals(){
    	
    	ObservableList<MealTypeProperty> tempdata = FXCollections.observableArrayList(); 
    	String sql="select * from meals";
        try {
            ResultSet resultSet = dc.getStatement(sql).executeQuery();
         
            while (resultSet.next()){
//            	System.out.println(resultSet.getString(2)+" "+resultSet.getInt(3));
             	  tempdata.add(new MealTypeProperty(resultSet.getString(2),resultSet.getInt(3))); 
            }
          
        } catch (SQLException e) {
         
            e.printStackTrace();
        }
   
    	
    	return tempdata;
    	
    	
    }
    
    public static void main(String[] args) {
//    	MealType mt = new MealType("Fruits", 60);
//    	addMeal(mt);
    	
//    	System.out.println( getMeals().get(0).getName());
    }

}
