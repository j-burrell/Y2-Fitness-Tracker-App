/*********************************************************
 *
 *                   Group.java (Model)
 *
 *
 **********************************************************/

package Model.group;

import Model.user.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;

public class Group  {


    private ArrayList<User> users;
    private ArrayList<GroupGoal> groupGoal;
    private String groupName;
    private User admin;

    /*
     *
     * Description: Default constructor for Group class
     * Param: void
     * Returns: void
     *
     */
    public Group()
    {
        users=new ArrayList<>();

    }

    /*
     *
     * Description: Secondary Constructor
     * Param: User u, String name
     * Returns: void
     *
     */
    public Group(User u, String name)
    {
        this.admin=u;
        this.groupName=name;
        users=new ArrayList<>();
        users.add(u);
        groupGoal=new ArrayList<>();
    }


    /*
     *
     * Description: Method that adds a Controller.user to a Group
     * Param: User Controller.user
     * Returns: boolean
     *
     */
    public boolean addUser(User user)
    {
        for(User u:users)
        {
            if(u==user)
                return false;
        }
        users.add(user);
        return true;
    }

    /*
     *
     * Description: Method that removes a Controller.user from a Group
     * Param: User Controller.user
     * Returns: boolean
     *
     */
    public boolean removeUser(User user)
    {

        try
        {
            users.remove(user);
        }
        catch (Exception e)
        {
            System.err.println(e);
            return false;
        }
        return true;
    }

    /*
     *
     * Description: Accessor method for users in a Group
     * Param: void
     * Returns: ArrayList<User>
     *
     */
    public ArrayList<User> getUsers()
    {
        return users;
    }


    /*
     *
     * Description: Accessor method for Controller.group name
     * Param: void
     * Returns: String
     *
     */
    public String getGroupName() {
        return groupName;
    }


    /*
     *
     * Description: Setter method for Controller.group name
     * Param: String name
     * Returns: void
     *
     */
    public void setGroupName(String name)
    {
        this.groupName=name;
    }


    /*
     *
     * Description: Accessor method for Controller.group goal
     * Param: void
     * Returns: ArrayList<GroupGoal>
     *
     */
    public ArrayList<GroupGoal> getGroupGoal() {
        return groupGoal;
    }


    /*
     *
     * Description: Accessor method for Controller.group admin
     * Param: void
     * Returns: User
     *
     */
    public User getAdmin()
    {
        return admin;
    }


    /*
     *
     * Description: Setter method for admin
     * Param: User Controller.user
     * Returns: void
     *
     */
    public void setAdmin(User user)
    {
        admin=user;
    }


    /*
     *
     * Description: Gets the name
     * Param: void
     * Returns: StringProperty
     *
     */
    public StringProperty getNameProperty(){return new SimpleStringProperty(groupName);}


    /*
     *
     * Description: Method that adds a goal to the Controller.group
     * Param: GroupGoal g
     * Returns: void
     *
     */
    public void addGoal(GroupGoal g){this.groupGoal.add(g);}


}
