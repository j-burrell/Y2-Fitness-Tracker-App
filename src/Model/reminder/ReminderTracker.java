package Model.reminder;

import Model.email.Email;
import Model.reminder.Reminder;
import home.AdminClass;
import Model.user.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public class ReminderTracker {
    private User user;
    private LocalDate currentDate;



    private ArrayList<Reminder> reminders;
    public ReminderTracker(User user)
    {
        this.user=user;
        //this.reminders=reminders;
        reminders=new ArrayList<>();


        currentDate=LocalDate.now();
    }
    public ArrayList<Reminder> getReminders() {

        return reminders;
    }

    public void setReminders(ArrayList<Reminder> reminders) {
        this.reminders = reminders;
    }
    public void addReminder(Reminder reminder)
    {
        reminders.add(reminder);
    }

    void sendRemind()
    {
        for(Reminder r:reminders)
        {
            long noOfDaysBetween = ChronoUnit.DAYS.between(currentDate, r.getDate());
            if(noOfDaysBetween<5)
            {
                Email.sendEmail(user.getEmail(),"","");
            }
        }
    }
    public void getReminds(){
        for(Map.Entry<Reminder,String> entry: AdminClass.getReminders().entrySet())
        {

            if(entry.getValue().equals(user.getUsername()))
            {
                reminders.add(entry.getKey());
            }
        }
    }
}
