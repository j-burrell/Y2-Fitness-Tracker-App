package unitTest;

import Model.email.Email;
import Model.group.Group;
import org.junit.Test;
import Model.reminder.Reminder;
import Model.user.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Class UnitTest
 *The UnitTest provides several tests the unit functionaries of the application
 *@version 1.0
 */
public class UnitTest {


    /**
     *Unit test for calculateBMI method in User class
     */
    @Test
    public void testCalculateBMI()
    {

        User tester =new User();
        tester.setHeight(170);
        tester.setWeight(60);

        assertEquals(20.76,tester.calculateBMI(),0.02);
    }

    /**
     * Method testAddUser
     * Test method addUser in class Group for user to join a group
     */
    @Test
    public void testJoinGroup()
    {
        //Create a new user
        User tester1=new User();
        tester1.setName("Tester1");
        User tester2=new User();
        //Create second new user
        tester2.setName("Tester2");
        //create a new group
        Group testGroup=new Group(tester1,"test Group");

        //before adding tester2 to the group
        System.out.println("Before adding tester2 to the group:");
        System.out.println("Group members:");
        for(User u:testGroup.getUsers())
        {
            System.out.println(u.getName());
        }
        System.out.println();
        //after adding tester2 to the group
        System.out.println("After adding tester2 to the group:");
        System.out.println("Group members:");
        testGroup.addUser(tester2);
        for(User u:testGroup.getUsers())
        {
            System.out.println(u.getName());
        }
    }

    @Test
    public void testAddReminds()
    {
        //Create a new user
        User tester1=new User();
        tester1.setName("Tester1");
        Reminder remind=new Reminder();
        tester1.getReminderTracker().addReminder(remind);
    }
    @Test
    public void testSendEmail()
    {
        Email.sendEmail("clf199711@gmail.com","subject","message");
    }
}
