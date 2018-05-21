package VendMachine;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * Sample implementation of Vending Machine in Java
 * @author Denis E
 */

public class VendingMachineImpl implements VendingMachine {
    private Inventory<Coin> cashInventory = new Inventory<Coin>();
    private Inventory<Item> itemInventory = new Inventory<Item>();
    private long totalSales;
    private Item currentItem;
    private long currentBalance;

    public VendingMachineImpl(){
        initialize();
    }

    private void initialize(){
        //initialize machine with 5 coins of each denomination
        //and 5 cans of each Item
        for(Coin c : Coin.values()){
            cashInventory.put(c, 5);
        }

        for(Item i : Item.values()){
            itemInventory.put(i, 5);
        }

    }

    @Override
    public long selectItemAndGetPrice(Item item) {
        if(itemInventory.hasItem(item)){
            currentItem = item;
            return currentItem.getPrice();
        }
        throw new SoldOutException("Sold Out, Please buy another item");
    }

    @Override
    public void insertCoin(Coin coin) {
        currentBalance = currentBalance + coin.getDenomination();
        cashInventory.add(coin);
    }

    @Override
    public Bucket<Item, List<Coin>> collectItemAndChange() {
        Item item = collectItem();
        totalSales = totalSales + currentItem.getPrice();

        List<Coin> change = collectChange();

        return new Bucket<Item, List<Coin>>(item, change);
    }

    private Item collectItem() throws NotSufficientChangeException, NotFullPaidException{
        if(isFullPaid()){
            if(hasSufficientChange()){
                itemInventory.deduct(currentItem);
                return currentItem;
            }
            throw new NotSufficientChangeException("Not Sufficient change in Inventory");

        }
        long remainingBalance = currentItem.getPrice() - currentBalance;
        throw new NotFullPaidException("Price not full paid, remaining : ",
                remainingBalance);
    }

    private List<Coin> collectChange() {
        long changeAmount = currentBalance - currentItem.getPrice();
        List<Coin> change = getChange(changeAmount);
        updateCashInventory(change);
        currentBalance = 0;
        currentItem = null;
        return change;
    }

    @Override
    public List<Coin> refund(){
        List<Coin> refund = getChange(currentBalance);
        updateCashInventory(refund);
        currentBalance = 0;
        currentItem = null;
        return refund;
    }


    private boolean isFullPaid() {
        if(currentBalance >= currentItem.getPrice()){
            return true;
        }
        return false;
    }


    private List<Coin> getChange(long amount) throws NotSufficientChangeException{
        List<Coin> changes = Collections.EMPTY_LIST;

        if(amount > 0){
            changes = new ArrayList<Coin>();
            long balance = amount;
            while(balance > 0){
                if(balance >= Coin.POUND_ONE.getDenomination()
                        && cashInventory.hasItem(Coin.POUND_ONE)){
                    changes.add(Coin.POUND_ONE);
                    balance = balance - Coin.POUND_ONE.getDenomination();
                    continue;

                }else if(balance >= Coin.PENCE_FIFTY.getDenomination()
                        && cashInventory.hasItem(Coin.PENCE_FIFTY)) {
                    changes.add(Coin.PENCE_FIFTY);
                    balance = balance - Coin.PENCE_FIFTY.getDenomination();
                    continue;

                }else if(balance >= Coin.PENCE_TWENTY.getDenomination()
                        && cashInventory.hasItem(Coin.PENCE_TWENTY)) {
                    changes.add(Coin.PENCE_TWENTY);
                    balance = balance - Coin.PENCE_TWENTY.getDenomination();
                    continue;
                }else if(balance >= Coin.PENCE_FIVE.getDenomination()
                        && cashInventory.hasItem(Coin.PENCE_FIVE)) {
                    changes.add(Coin.PENCE_FIVE);
                    balance = balance - Coin.PENCE_FIVE.getDenomination();
                    continue;

                }else if(balance >= Coin.PENCE_ONE.getDenomination()
                        && cashInventory.hasItem(Coin.PENCE_ONE)) {
                    changes.add(Coin.PENCE_ONE);
                    balance = balance - Coin.PENCE_ONE.getDenomination();
                    continue;

                }else{
                    throw new NotSufficientChangeException("NotSufficientChange, Please try another product");
                }
            }
        }

        return changes;
    }

    @Override
    public void reset(){
        cashInventory.clear();
        itemInventory.clear();
        totalSales = 0;
        currentItem = null;
        currentBalance = 0;
    }

    public void printStats(){
        System.out.println("Total Sales : " + totalSales);
        System.out.println("Current Item Inventory : " + itemInventory);
        System.out.println("Current Cash Inventory : " + cashInventory);
    }

    private boolean hasSufficientChange(){
        return hasSufficientChangeForAmount(currentBalance - currentItem.getPrice());
    }

    private boolean hasSufficientChangeForAmount(long amount){
        boolean hasChange = true;
        try{
            getChange(amount);
        }catch(NotSufficientChangeException nsce){
            return hasChange = false;
        }

        return hasChange;
    }

    private void updateCashInventory(List change) {
        for(Object c : change){
            cashInventory.deduct((Coin) c);
        }
    }

    public long getTotalSales(){
        return totalSales;
    }

}
