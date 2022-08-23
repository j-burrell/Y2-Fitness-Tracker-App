/**************************************************

			FoodDB.Java


 *************************************************/

package Model.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Model.food.FoodTracker;
import Model.food.Meal;
import Model.food.MealType;
import Model.user.User;

public class FoodDB {
	//create a DataBase Connector 
	public static DataBaseConnector dc=new DataBaseConnector();


	/*
	 *
	 * Description: This static method saves a users foodTracker by updating the Model.database
	 * Param: FoodTracker foodTracker
	 * Returns: void
	 *
	 */
	public static void addFoodTracker(FoodTracker foodTracker) {
		   for (HashMap.Entry<LocalDate, Meal> entry :foodTracker.getDailyMeal().entrySet()) {
	            LocalDate dateIter = entry.getKey();
	            Meal meal = entry.getValue();
	            
	            //save breakfast
	            for(MealType mt:meal.getBreakfast()) { 
	            	String sql="insert into FoodTrackerTable (username,date,mealtype,foodname,calorie) values ("+
	        	        	"'"+foodTracker.getUser().getUsername()+"','"+dateIter+"',' breakfast','"+mt.getName()+"'"+","+mt.getCalorie()+")";
	        	        	System.out.println(sql);
	        	            try {
	        	                 dc.getStatement(sql).executeUpdate(); 
	        	            } catch (SQLException e) {
	        	              
	        	                e.printStackTrace();
	        	            } 
	            }
	            
	            //save lunch
	            for(MealType mt:meal.getLunch()) { 
	            	String sql="insert into FoodTrackerTable (username,date,mealtype,foodname,calorie) values ("+
	        	        	"'"+foodTracker.getUser().getUsername()+"','"+dateIter+"',' lunch','"+mt.getName()+"'"+","+mt.getCalorie()+")";
	        	        	System.out.println(sql);

	        	            try {
	        	                 dc.getStatement(sql).executeUpdate(); 
	        	            } catch (SQLException e) {
	        	              
	        	                e.printStackTrace();
	        	            } 
	            }
	            //save dinner
	            for(MealType mt:meal.getDinner()) { 
	            	String sql="insert into FoodTrackerTable (username,date,mealtype,foodname,calorie) values ("+
	        	        	"'"+foodTracker.getUser().getUsername()+"','"+dateIter+"',' dinner','"+mt.getName()+"'"+","+mt.getCalorie()+")";
	        	        	System.out.println(sql);

	        	            try {
	        	                 dc.getStatement(sql).executeUpdate(); 
	        	            } catch (SQLException e) {
	        	              
	        	                e.printStackTrace();
	        	            } 
	            }
	            //save snack
	            for(MealType mt:meal.getSnack()) { 
	            	String sql="insert into FoodTrackerTable (username,date,mealtype,foodname,calorie) values ("+
	        	        	"'"+foodTracker.getUser().getUsername()+"','"+dateIter+"',' snack','"+mt.getName()+"'"+","+mt.getCalorie()+")";
	        	        	System.out.println(sql);
	        	            try {
	        	                 dc.getStatement(sql).executeUpdate(); 
	        	            } catch (SQLException e) {
	        	              
	        	                e.printStackTrace();
	        	            } 
	            }
	          
	             
	            
	        }
	 
	
		
	}



	/*
	 *
	 * Description: This static method retrieves a users foodTracker from the Model.database and stores in in a User.
	 * Param: User Controller.user
	 * Returns: FoodTracker
	 *
	 */
	public static FoodTracker getFoodTracker(User user) {
		 DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-YYYY");
		 FoodTracker foodTracker = new FoodTracker(user);

		
	  	String sql="select * from FoodTrackerTable where username = '" +user.getUsername()+"'";
        try {
            ResultSet resultSet = dc.getStatement(sql).executeQuery();
        	HashMap<LocalDate,Meal> dailyMeal = new HashMap<LocalDate, Meal>();
            while (resultSet.next()){
          
    		 if(dailyMeal.get(resultSet.getDate(2).toLocalDate())==null) {
    			 List<MealType> breakfast = new ArrayList<>();
       		     List<MealType> lunch = new ArrayList<>();
       		     List<MealType> dinner = new ArrayList<>();
       		     List<MealType> snack = new ArrayList<>();
       		     Meal meal = new Meal();
       		     meal.setBreakfast(breakfast);
       		     meal.setLunch(lunch);
       		     meal.setDinner(dinner);
       		     meal.setSnack(snack);
       		     
       		     switch (resultSet.getString(3).hashCode()) {
				case 404078011:breakfast.add(new MealType(resultSet.getString(4),resultSet.getInt(5)));					
					break;
				case 1019467530:lunch.add(new MealType(resultSet.getString(4),resultSet.getInt(5)));					
				break;
				case 1298617490:dinner.add(new MealType(resultSet.getString(4),resultSet.getInt(5)));					
				break;
				case 1025711150:snack.add(new MealType(resultSet.getString(4),resultSet.getInt(5)));					
				break;

				default:
					break;
				}
       		     
       		 
       		     
       		    dailyMeal.put(resultSet.getDate(2).toLocalDate(), meal);
    			 
    		 }else {
    			 System.out.println(resultSet.getString(3)+" "+resultSet.getString(4)+" "+resultSet.getInt(5));
    		 
    		     switch (resultSet.getString(3).hashCode()) {
 				case 404078011:dailyMeal.get(resultSet.getDate(2).toLocalDate()).getBreakfast().add(new MealType(resultSet.getString(4),resultSet.getInt(5)));					
 				
 				break;
 				case 1019467530:dailyMeal.get(resultSet.getDate(2).toLocalDate()).getLunch().add(new MealType(resultSet.getString(4),resultSet.getInt(5)));									
 				break;
 				case 1298617490:dailyMeal.get(resultSet.getDate(2).toLocalDate()).getDinner().add(new MealType(resultSet.getString(4),resultSet.getInt(5)));								
 				break;
 				case 1025711150:dailyMeal.get(resultSet.getDate(2).toLocalDate()).getSnack().add(new MealType(resultSet.getString(4),resultSet.getInt(5)));									
 				break;

 				default:
 					break;
 				}
        		     
    		 }
   		    
   		    foodTracker.setDailyMeal(dailyMeal);
          
 
            }
          
        } catch (SQLException e) {
         
            e.printStackTrace();
        }
		
 
		return foodTracker;
	 
		
	}
	

	/*
	*Unit test for class
	 */
	public static void main(String[] args) {


        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");


		User user = new User();
		user.setUsername("Tom");

		FoodTracker foodTracker = new FoodTracker(user);


		System.out.println(getFoodTracker(user).getDailyMeal().get(LocalDate.parse("2020-03-01", fmt)).getTotalCalorie());


	}

}
