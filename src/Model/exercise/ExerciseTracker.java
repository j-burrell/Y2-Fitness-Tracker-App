package Model.exercise;

import Model.user.User;


import java.util.*;
import java.time.*;

public class ExerciseTracker {

    private User user;
    private double goal;
    private LocalDate goalend;
    private LocalDate goalStart;
    private HashMap<LocalDate, ArrayList<Exercise>> exerciseTracker;
    private ArrayList<String> exerciseTypeList;

    public ExerciseTracker(User user) {

        this.user = user;
        this.goal = 0;
        this.goalStart = null;
        this.goalend = null;
        this.exerciseTracker = new HashMap<LocalDate, ArrayList<Exercise>>();
        this.exerciseTypeList = new ArrayList<>();
    }


    public ArrayList<Double> generateReport(LocalDate sDate, LocalDate eDate){
        ArrayList<Double> reportArray = new ArrayList<>();

        LocalDate date = sDate;

        while(!date.equals(eDate.plusDays(1))){
            if(exerciseTracker.get(date)!=null){
                ArrayList<Exercise> exArray = exerciseTracker.get(date);
                for(Exercise ex: exArray){
                    reportArray.add((double)ex.getDuration());
                }
            }
            else{
                reportArray.add(0.0);
            }
            date = date.plusDays(1);
        }
        return reportArray;


    }

    public HashMap<LocalDate, ArrayList<Exercise>> getExerciseTracker() {
        return exerciseTracker;
    }

    //Set current goal.
    public boolean setGoal(LocalDate sDate, LocalDate eDate, double duration){

        //If there is already a goal.
        if(this.goal != 0 && goalend != null && goalStart != null){

            return false;
        }
        else{

            this.goal = duration;
            this.goalStart = sDate;
            this.goalend = eDate;
            return true;

        }
    }

    //Remove goal.
    public void removeGoal(){

        this.goal = 0;
        this.goalend = null;
        this.goalStart = null;
    }

    //Goal percentage.
    public double goalPercentage(){

        double exercisehoursdone = goalProgress();
        double goalhours = this.goal;


        return (exercisehoursdone/goalhours);
    }

    //Get goal progress.
    public double goalProgress(){

        int current = 0;
        for(LocalDate d : exerciseTracker.keySet()){

            if(d.isBefore(this.goalend.plusDays(1)) && d.isAfter(this.goalStart.minusDays(1))){

                for(Exercise e : exerciseTracker.get(d)){

                    current = current + e.getDuration();
                }
            }
        }
        //In hours.
        if (current!=0){
        return current;
        }
        else {return 0;}
    }

    //Get goal end.
    public LocalDate getGoalEnd() {

        //Placeholder need to limit selection by current date + 1.
        return this.goalend;
    }

    //Get goal start.
    public LocalDate getGoalStart() {

        //Placeholder need to limit selection by current date + 1.
        return this.goalStart;
    }

    //Get goal value from ui.
    public double getGoalValue() {

        return this.goal;
    }

    //Get Model.exercise type from Ui.
    public String getExerciseType() {

        // !!!!----Placeholder.----!!!!
        return "Football";
    }


    public ArrayList<String> getExerciseTypeList() {
        return exerciseTypeList;
    }

    //Get date from Ui.
    public LocalDate getDate() {

        // !!!!----Placeholder.----!!!!
        //Get day, month, year value from ui.
        int day = 20;
        int month = 4;
        int year = 2019;

        return LocalDate.of(year, month, day);
    }

    //Get start time from Ui.
    public LocalTime getStartTime() {

        // !!!!----Placeholder.----!!!!
        //Get hours and mins value from ui.
        int hours = 18;
        int mins = 0;

        return LocalTime.of(hours, mins);

    }

    //Get end time from Ui.
    public LocalTime getEndTime() {

        // !!!!----Placeholder.----!!!!
        //Get hours and mins value from ui.
        int hours = 19;
        int mins = 0;

        return LocalTime.of(hours, mins);
    }

    //Get Model.exercise currently ui.
    public Exercise getExercise(){

        return new Exercise("Rugby", getStartTime(), getEndTime());
    }

    //Add Model.exercise to tracker and store in Model.exercise list if new.
    public void addExercise(String e_type, LocalTime s_time, LocalTime e_time, LocalDate d) {




            //If not already in list, add to it.
            if (!this.exerciseTypeList.contains(getExerciseType())) {

                this.exerciseTypeList.add(e_type);
            }

            Exercise exercise = new Exercise(e_type, s_time, e_time);


            //If the date is there.
            if (exerciseTracker.containsKey(d)){

                //Get the array and check for overlap.
                ArrayList<Exercise> e_array = exerciseTracker.get(d);
         //No overlap add to array for that day.

                    e_array.add(exercise);
                    exerciseTracker.put(d, e_array);


                //If date is not there.
            }else if (!exerciseTracker.containsKey(d)){

                ArrayList<Exercise> ae = new ArrayList<Exercise>();
                ae.add(exercise);
                exerciseTracker.put(d, ae);
            }


    }

    //Remove Model.exercise.
    public void removeExercise(LocalDate dateremove,Exercise remove){
        System.out.println(exerciseTracker.get(dateremove));
        System.out.println(exerciseTracker.get(dateremove).contains(remove));
        exerciseTracker.get(dateremove).remove(remove);


    }




}
