package harel.nimni.babagim;

import java.util.ArrayList;

public class User
{
    private String username;
    private String password;
    private String phoneNumber;
    private int points;
    private ArrayList<Order> savedOrders;

    public User(){}
    public User(String username, String password, int points, String phoneNumber, ArrayList<Order> savedOrders) {
        this.username = username;
        this.password = password;
        this.points = points;
        this.phoneNumber = phoneNumber;
        this.savedOrders = savedOrders;
    }
    public User(String username, String password, String phoneNumber, int points) {
        this.username = username;
        this.password = password;
        this.points = points;
        this.phoneNumber = phoneNumber;
        savedOrders = new ArrayList<>();
    }
    public User(String username, String password, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.points = 0;
        savedOrders = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
    public void addPoints(int newPoints)
    {
        this.points += newPoints;
    }
    public void removePoints(int redeemedPoints)
    {
        this.points -= redeemedPoints;
    }

    public ArrayList<Order> getSavedOrders() {
        return savedOrders;
    }

    public Order getSavedOrder(int i)
    {
        return savedOrders.get(i);
    }

    public void setSavedOrder(Order order)
    {
        this.savedOrders.add(order);
    }

    public void addOrder(Order order)
    {
        if (savedOrders == null) {
            savedOrders = new ArrayList<>(); // Initialize if null
        }
        this.savedOrders.add(order);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", points=" + points +
                '}';
    }
}
