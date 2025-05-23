package harel.nimni.babagim;

import java.util.ArrayList;
import java.util.Arrays;

public class Order
{
    private ArrayList<Item> userItems;
    private ArrayList<Integer> orderImages;
    private String orderName;

    public Order(ArrayList<Item> userItems) {
        this.userItems = userItems;
        this.orderImages = new ArrayList<>();
        this.orderName = "";
    }

    public Order(ArrayList<Item> userItems, ArrayList<Integer> orderImages) {
        this.userItems = userItems;
        this.orderImages = orderImages;
        this.orderName = "";
    }

    public Order()
    {
        userItems = new ArrayList<>();
        this.orderImages = new ArrayList<>();
        this.orderName = "";
    }

    public Order(ArrayList<Item> userItems, ArrayList<Integer> orderImages, String orderName) {
        this.userItems = userItems;
        this.orderImages = orderImages;
        this.orderName = orderName;
    }

    public ArrayList<Item> getUserItems() {
        return userItems;
    }

    public Item getItem(int i) {
        return userItems.get(i);
    }

    public void addItem(Item newItem) {
        this.userItems.add(newItem);
    }

    public int getSize()
    {
        return this.userItems.size();
    }

    public ArrayList<Integer> getOrderImages() {
        return orderImages;
    }
    public int getImage(int i) {
        return orderImages.get(i);
    }

    public void setOrderImages(ArrayList<Integer> orderImages) {
        this.orderImages = orderImages;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public void addImage(int image)
    {
        this.orderImages.add(image);
    }

    public int calcPrice()
    {
        int price=0;
        for(int i=0; i<this.userItems.size(); i++)
        {
            price += userItems.get(i).getPrice();
        }
        return price;
    }
    public void removeItem(int position)
    {
        orderImages.remove(position);
        userItems.remove(position);
    }

    @Override
    public String toString() {
        String orderSummery="ההזמנה: \n";
        for(int i=0; i<this.userItems.size(); i++)
        {
            orderSummery += ((i+1)+") ");
            orderSummery += this.userItems.get(i).toString();

            orderSummery += "\n";
        }
        orderSummery += "לתשלום: "+PaymentScreen.newPrice+" שח ";
        orderSummery += "\nתודה שבחרת באבאג'ים!";
        return orderSummery;
    }
}
