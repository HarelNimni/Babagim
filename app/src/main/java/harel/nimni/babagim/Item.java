package harel.nimni.babagim;

import java.util.ArrayList;

public class Item
{
    private String name;
    private ArrayList<String> toppings = new ArrayList<>();
    private int countOfPepole;
    private int price;
    private boolean isVeggie;
    private boolean moreMeat;
    private boolean chicken;
    private boolean veal;
    private boolean hodu;
    private boolean mix;
    private String type;
    final int vealPrice = 5;
    final int hoduPrice = 5;
    final int mixPrice = 3;
    final int moreMeatPrice = 10;
    public Item()
    {

    }

    public Item(String name, ArrayList<String> toppings, int countOfPepole, int price, boolean isVeggie, boolean moreMeat, boolean chicken, boolean veal, boolean hodu, boolean mix, String type) {
        this.name = name;
        this.toppings = toppings;
        this.countOfPepole = countOfPepole;
        this.price = price;
        this.isVeggie = isVeggie;
        this.moreMeat = moreMeat;
        this.chicken = chicken;
        this.veal = veal;
        this.hodu = hodu;
        this.mix = mix;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTop(int i) {
        return toppings.get(i);
    }

    public ArrayList<String> getToppings() {
        return toppings;
    }

    public void addTopping(String top) {
        this.toppings.add(top);
    }

    public int getCountOfPepole() {
        return countOfPepole;
    }

    public void setCountOfPepole(int countOfPepole) {
        this.countOfPepole = countOfPepole;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isVeggie() {
        return isVeggie;
    }

    public void setVeggie(boolean veggie) {
        isVeggie = veggie;
    }

    public boolean isMoreMeat() {
        return moreMeat;
    }

    public void setMoreMeat(boolean moreMeat) {
        this.moreMeat = moreMeat;
    }

    public boolean isChicken() {
        return chicken;
    }

    public void setChicken(boolean chicken) {
        this.chicken = chicken;
    }

    public boolean isVeal() {
        return veal;
    }

    public void setVeal(boolean veal) {
        this.veal = veal;
    }

    public boolean isHodu() {
        return hodu;
    }

    public void setHodu(boolean hodu) {
        this.hodu = hodu;
    }


    public boolean isMix() {
        return mix;
    }

    public void setMix(boolean mix) {
        this.mix = mix;
    }

    public void addHodu()
    {
        this.price += hoduPrice;
        this.hodu = true;
    }
    public void addVeal()
    {
        this.price += vealPrice;
        this.veal = true;
    }
    public void addMeat()
    {
        this.price += moreMeatPrice;
        this.moreMeat = true;
    }
    public void addMix()
    {
        this.price += mixPrice;
        this.mix = true;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        String itemDetails = this.name;

        if(this.getType().equals("Shawarma"))
        {
            if(this.isChicken())
            {
                itemDetails += " פרגית";
            }
            else if(this.isVeal())
            {
                itemDetails += " עגל";
            }
            else if(this.isHodu())
            {
                itemDetails += " הודו";
            }
            else
            {
                itemDetails += " מיקס";
            }
        }

        if(this.getToppings().size()>0)
        {
            itemDetails += " עם: ";
            for(int i=0; i<this.getToppings().size()-1; i++)
            {
                itemDetails += this.getTop(i)+", ";
            }
            itemDetails += this.getTop(this.getToppings().size()-1)+".";
        }

        if(this.isMoreMeat())
        {
            itemDetails += " אקסטרה בשר.";
        }

        return itemDetails;
    }
}
