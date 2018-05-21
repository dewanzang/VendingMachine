package unitTest;

import VendMachine.*;
import org.junit.Ignore;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class VendingMachineTest {
    private static VendingMachine vm;

    @BeforeClass
    public static void setUp(){
        vm = VendingMachineFactory.createVendingMachine();
    }

    @AfterClass
    public static void tearDown(){
        vm = null;
    }

    @Test
    public void testBuyItemWithExactPrice() {
        //select item, price in cents
        long price = vm.selectItemAndGetPrice(Item.COKE);
        //price should be Coke's price
        assertEquals(Item.COKE.getPrice(), price);
        //100 cents paid
        vm.insertCoin(Coin.POUND_ONE);
        //50 cents paid
        vm.insertCoin(Coin.PENCE_FIFTY);

        Bucket<Item, List<Coin>> bucket = vm.collectItemAndChange();
        Item item = bucket.getFirst();
        List<Coin> change = bucket.getSecond();

        //should be Coke
        assertEquals(Item.COKE, item);
        //there should not be any change
        assertTrue(change.isEmpty());
    }

    @Test
    public void testBuyItemWithMorePrice(){

        long price = vm.selectItemAndGetPrice(Item.NUTS);
        assertEquals(Item.NUTS.getPrice(), price);

        vm.insertCoin(Coin.PENCE_FIFTY);
        vm.insertCoin(Coin.PENCE_FIFTY);

        Bucket<Item, List<Coin>> bucket = vm.collectItemAndChange();
        Item item = bucket.getFirst();
        List<Coin> change = bucket.getSecond();

        //should be Nuts
        assertEquals(Item.NUTS, item);
        //there should not be any change
        assertTrue(!change.isEmpty());
        //comparing change
        assertEquals(Math.abs(50 - Item.NUTS.getPrice()), getTotal(change));

    }


    @Test
    public void testRefund(){

        long price = vm.selectItemAndGetPrice(Item.SNACK);
        assertEquals(Item.SNACK.getPrice(), price);
        vm.insertCoin(Coin.POUND_ONE);
        vm.insertCoin(Coin.PENCE_FIFTY);
        vm.insertCoin(Coin.PENCE_TWENTY);
        vm.insertCoin(Coin.PENCE_FIVE);
        vm.insertCoin(Coin.PENCE_ONE);

        assertEquals(176, getTotal(vm.refund()));

    }

    @Test(expected=SoldOutException.class)
    public void testSoldOut(){

        for (int i = 0; i < 6; i++) {
            vm.selectItemAndGetPrice(Item.WATER);
            vm.insertCoin(Coin.POUND_ONE);
            vm.collectItemAndChange();
        }


    }

    @Test(expected=SoldOutException.class)
    public void testReset(){
        VendingMachine vmachine = VendingMachineFactory.createVendingMachine();
        vmachine.reset();

        vmachine.selectItemAndGetPrice(Item.COKE);

    }

    @Ignore
    public void testVendingMachineImpl(){
        VendingMachineImpl vm = new VendingMachineImpl();
    }

    private long getTotal(List<Coin> change){
        long total = 0;
        for(Coin c : change){
            total = total + c.getDenomination();
        }
        return total;
    }

}
