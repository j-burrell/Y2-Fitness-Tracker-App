/*********************************************************
 *
 *                   WeightDB.java
 *
 *
 **********************************************************/



package Model.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import Model.user.User;
import Model.weight.WeightTracker;

public class WeightDB {
	//create a DataBase Connector 
	public static DataBaseConnector dc=new DataBaseConnector();
	

	/*
	 *
	 * Description: This static method saves a WeightTracker by adding it to the Model.database.
	 * Param: WeightTracker weightTracker
	 * Returns: void
	 *
	 */
	public static void addWeightTracker(WeightTracker weightTracker) {
		   for (HashMap.Entry<LocalDate, Integer> entry :weightTracker.getTargetWeightDict().entrySet()) {
	            LocalDate dateIter = entry.getKey();
	            int targetWeight = entry.getValue();
	            int currentWeight = weightTracker.getTodayWeightDict().get(dateIter);
	            
	            
	        	String sql="insert into WeightTrackerTable (username,date,targetWeight,currentWeight) values ("+
	        	"'"+weightTracker.getUser().getUsername()+"','"+dateIter+"',"+targetWeight+","+currentWeight+")";
	        	System.out.println(sql);
	            try {
	                 dc.getStatement(sql).executeUpdate(); 
	            } catch (SQLException e) {
	              
	                e.printStackTrace();
	            }

	            
	        }
	 
	
		
	}


	/*
	 *
	 * Description: This static method retrieves a users WeightTracker from the Model.database and stores in in a User.
	 * Param: User Controller.user
	 * Returns: WeightTracker
	 *
	 */
	public static WeightTracker getWeightTracker(User user) {
		 DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-mm-yyyy");
		WeightTracker weightTracker = new WeightTracker(user);
		HashMap<LocalDate,Integer> targetWeightDict = new HashMap<LocalDate, Integer>();
		HashMap<LocalDate,Integer> currentWeightDict = new HashMap<LocalDate, Integer>();
		
	  	String sql="select * from WeightTrackerTable where username = '" +user.getUsername()+"'";
        try {
            ResultSet resultSet = dc.getStatement(sql).executeQuery();
         
            while (resultSet.next()){
            	targetWeightDict.put(resultSet.getDate(2).toLocalDate(), resultSet.getInt(3));
            	currentWeightDict.put(resultSet.getDate(2).toLocalDate(), resultSet.getInt(4));
 
            }
          
        } catch (SQLException e) {

            e.printStackTrace();
        }
		
		
		weightTracker.setTargetWeightDict(targetWeightDict);
		weightTracker.setTodayWeightDict(currentWeightDict);
		return weightTracker;
	 
		
	}
	
	/*
	*Unit test for this class
	 */
	public static void main(String[] args) {
		
		//test addWeightTracker
 
        //DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");
  
		
		//User Controller.user = new User();
		//Controller.user.setUsername("Tom");
		
//		WeightTracker weightTracker = new WeightTracker(Controller.user);
//		HashMap<LocalDate,Integer> targetWeightDict = new HashMap<LocalDate, Integer>();
//		HashMap<LocalDate,Integer> currentWeightDict = new HashMap<LocalDate, Integer>();
//		targetWeightDict.put(LocalDate.parse("2020-03-01", fmt), 60);
//		targetWeightDict.put(LocalDate.parse("2020-03-02", fmt), 60);
//		targetWeightDict.put(LocalDate.parse("2020-03-03", fmt), 60);
//		targetWeightDict.put(LocalDate.parse("2020-03-04", fmt), 60);
//		
//		currentWeightDict.put(LocalDate.parse("2020-03-01", fmt), 65);
//		currentWeightDict.put(LocalDate.parse("2020-03-02", fmt), 64);
//		currentWeightDict.put(LocalDate.parse("2020-03-03", fmt), 63);
//		currentWeightDict.put(LocalDate.parse("2020-03-04", fmt), 62);
//		
//		
//		
//		weightTracker.setTargetWeightDict(targetWeightDict);
//		weightTracker.setTodayWeightDict(currentWeightDict);
//		addWeightTracker(weightTracker);
		
		
		
		//test getWeightTracker
		
		//System.out.println(getWeightTracker(Controller.user).getTodayWeightDict().size());
		
		
	}

}
