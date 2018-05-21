package VendMachine;

/**
 * The Vending Machine throws this exception if the user
 * request for a product which is sold out
 * @author Denis E
 */

public class SoldOutException extends RuntimeException {
    private String message;

    public SoldOutException(String string) {
        this.message = string;
    }

    @Override
    public String getMessage(){
        return message;
    }

}
