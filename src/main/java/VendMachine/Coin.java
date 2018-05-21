package VendMachine;

/**
 * Coins supported by Vending Machine.
 * @author Denis E
 */


public enum Coin {
    PENCE_ONE(1), PENCE_FIVE(5), PENCE_TWENTY(20), PENCE_FIFTY(50), POUND_ONE(100);

    private int denomination;

    private Coin(int denomination){
        this.denomination = denomination;
    }

    public int getDenomination(){
        return denomination;
    }

}
