/*********************************************************
 *
 *                   Exercise.java (Model)
 *
 *
 **********************************************************/
package Model.exercise;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.*;

public class Exercise {

    private String exercise_type;
    private LocalTime start_time;
    private LocalTime end_time;
    private int duration;

    /*
     *
     * Description: Constructor method void Exercise class.
     * Param: String e_type, LocalTime s_time, LocalTime e_time
     * Returns: void
     *
     */

    public Exercise(String e_type, LocalTime s_time, LocalTime e_time){

        this.exercise_type = e_type;
        this.start_time = s_time;
        this.end_time = e_time;
        calculateDuration();
    }


    /*
     *
     * Description: method that Initialises/updates duration, called when start/end time is changed.
     * Param: void
     * Returns: void
     *
     */
    public void calculateDuration(){

        this.duration = (int)Duration.between(start_time, end_time).toMinutes();
    }


    /*
     *
     * Description: Accessor method Model.exercise type
     * Param: void
     * Returns: String exercise_type
     *
     */
    public String getExercise_type() {

        return exercise_type;
    }

    /*
     *
     * Description: Setter method for Model.exercise type
     * Param: String exercise_type
     * Returns: void
     *
     */
    public void setExercise_type(String exercise_type) {
        this.exercise_type = exercise_type;
    }

    /*
     *
     * Description: Accessor method start time
     * Param: void
     * Returns: LocalTime start_time
     *
     */
    public LocalTime getStart_time() {

        return start_time;
    }

    /*
     *
     * Description: Setter method start time
     * Param: LocalTime start_time
     * Returns: void
     *
     */
    public void setStart_time(LocalTime start_time) {
        this.start_time = start_time;
        calculateDuration();
    }

    /*
     *
     * Description: Accessor method end time
     * Param: void
     * Returns: LocalTime end_time
     *
     */
    public LocalTime getEnd_time() {
        return end_time;
    }



    /*
     *
     * Description: Setter method end time
     * Param: LocalTime end_time
     * Returns: void
     *
     */
    public void setEnd_time(LocalTime end_time) {
        this.end_time = end_time;
        calculateDuration();
    }

    /*
     *
     * Description: Accessor method duration
     * Param: void
     * Returns: int duration
     *
     */
    public int getDuration() {

        return duration;
    }

    /*
     *
     * Description: Overidden toString method
     * Param: void
     * Returns: String
     *
     */
    @Override
    public String toString() {
        return "Exercise: " + exercise_type +
                "\nStart time: " +  start_time +
                "\nEnd time: " + end_time +
                "\nDuration: " + duration + " mintues.";
    }


    public StringProperty getNameProperty(){
        StringProperty var = new SimpleStringProperty((String) exercise_type);
        return var;}



    /*
    *unit test for Exercise class
     */
    public static void main(String[] args) {

        Exercise e = new Exercise("Running", LocalTime.of(18, 30), LocalTime.of(19, 00));
        System.out.println(e);
        System.out.println(" ");

        e.setEnd_time(LocalTime.of(18, 45));
        System.out.println(e);
        System.out.println(" ");

        e.setEnd_time(LocalTime.of(19, 10));
        System.out.println(e);
        System.out.println(" ");

        System.out.println(e.getExercise_type());
        System.out.println(e.getStart_time());
        System.out.println(e.getEnd_time());
        System.out.println(e.getDuration());
    }
}
