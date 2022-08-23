/*********************************************************
 *
 *                   GroupGoal.java (Model)
 *
 *
 **********************************************************/

package Model.group;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

public class GroupGoal {


    private Group group;
    double groupGoal;
    private String exercise;
    LocalDate startDate;
    LocalDate endDate;
    String goalName;


    /*
     *
     * Description: Default constructor for GroupGoal Controller
     * Param: void
     * Returns: void
     *
     */
    public GroupGoal()
    {}


    /*
     *
     * Description: This method returns the Controller.group
     * Param: void
     * Returns: Group
     *
     */
    public Group getGroup() {
        return group;
    }

    /*
     *
     * Description: This method returns the endDate
     * Param: void
     * Returns: LocalDate
     *
     */
    public LocalDate getEndDate() {
        return endDate;
    }


    /*
     *
     * Description: This method returns the start date
     * Param: void
     * Returns: LocalDate
     *
     */
    public LocalDate getStartDate() {
        return startDate;
    }


    /*
     *
     * Description: This method sets the start Date
     * Param: LocalDate startDate
     * Returns: void
     *
     */
    public void setStartDate(LocalDate startDate)
    {
        this.startDate=startDate;
    }


    /*
     *
     * Description: This method sets the end Date
     * Param: LocalDate endDate
     * Returns: void
     *
     */
    public void setEndDate(LocalDate endDate)
    {
        this.endDate=endDate;
    }


    /*
     *
     * Description: This method sets the Exercise type
     * Param: String e
     * Returns: void
     *
     */
    public void setExercise(String e){this.exercise=e;}


    /*
     *
     * Description: This method returns the Model.exercise type
     * Param: void
     * Returns: String
     *
     */
    public String getExercise(){return exercise;}


    /*
     *
     * Description: This method returns the value of the Controller.group goal
     * Param: void
     * Returns: double
     *
     */
    public double getGroupGoal()
    {
        return groupGoal;
    }


    /*
     *
     * Description: This method sets the Controller.group goal
     * Param: double goal
     * Returns: void
     *
     */
    public void setGroupGoal(double goal)
    {
        groupGoal=goal;
    }


    /*
     *
     * Description: This method returns the goal name
     * Param: void
     * Returns: String
     *
     */
    public String getGoalName()
    {
        return goalName;
    }


    /*
     *
     * Description: This method sets the goal na,e
     * Param: String name
     * Returns: void
     *
     */
    public void setGoalName(String name)
    {
        goalName=name;
    }


    /*
     *
     * Description: This method returns the goal name
     * Param: void
     * Returns: StringProperty
     *
     */
    public StringProperty getGoalNameProperty(){return new SimpleStringProperty(goalName);}

}
