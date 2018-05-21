package VendMachine;

/**
 * Items or products supported by Vending Machine.
 * @author Denis E
 */

public enum Item {
    CANDY("Candy", 10), SNACK("Snack", 50), NUTS("Nut", 75), COKE("Coke", 150), WATER("Water", 100);

    private String name;
    private int price;

    private Item(String name, int price){
        this.name = name;
        this.price = price;
    }

    public String getName(){
        return name;
    }

    public long getPrice(){
        return price;
    }

}
