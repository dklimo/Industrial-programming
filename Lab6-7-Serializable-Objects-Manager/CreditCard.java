import java.io.Serializable;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.MessageFormat;
import java.util.ResourceBundle;

public class CreditCard implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String cardNumber;
    private final double creditLimit;
    private double currentDebt;
    private boolean isBlockedbyClient;
    private boolean isBlockedbyAdmin;
    private String blockReason;
    private final String ownerName;
    private final String currency;
    private final Date creationDate;
    private static ResourceBundle messages;

    public static void setMessages(ResourceBundle bundle) {
        messages = bundle;
    }

    public double getAvailableCredit() { return creditLimit - currentDebt; }
    public String getCardNumber(){ return cardNumber; }
    public Date getCreationDate() {return creationDate;}

    public CreditCard(String ownerName, double creditLimit, String currency){
        this.cardNumber = "CC" + ((int)(Math.random() * 900) + 100);
        this.ownerName = ownerName;
        this.creditLimit = creditLimit;
        this.currentDebt = 0;
        this.isBlockedbyClient = false;
        this.isBlockedbyAdmin = false;
        this.blockReason = "";
        this.currency = currency;
        this.creationDate = new Date();
    }

    private void checkCreditCardAvailability() throws IllegalStateException {
        if (isBlockedbyClient) {
            throw new IllegalStateException(messages.getString("error.card_blocked"));
        }
        if (isBlockedbyAdmin) {
            throw new IllegalStateException(MessageFormat.format(messages.getString("error.card_blocked_by_admin"), blockReason));
        }
    }

    public void makePay(double amount){
        checkCreditCardAvailability();
        if (amount <= 0) {
            throw new IllegalArgumentException(messages.getString("error.summary_positive"));
        }
        if(amount > getAvailableCredit()){
            blockByAdmin(messages.getString("error.exceeding_limit"));
        }
        currentDebt+=amount;
    }

    public void repay(double amount){
        if (amount <= 0) {
            throw new IllegalArgumentException(messages.getString("error.summary_positive"));
        }
        if(amount> currentDebt){
            amount=currentDebt;
        }
        currentDebt -=amount;
        if(currentDebt < creditLimit){
            unblockByAdmin();
        }
    }

    public void blockedByClient() {
        isBlockedbyClient = true;
    }

    public void blockByAdmin(String reason) {
        isBlockedbyAdmin = true;
        blockReason = reason;
    }

    public void unblockByAdmin() {
        isBlockedbyAdmin = false;
        blockReason = "";
    }

    public String toString(SimpleDateFormat dateFormat) {
        String status = messages.getString("status.active");
        if (isBlockedbyClient) status = messages.getString("status.canceled");
        else if (isBlockedbyAdmin) status = messages.getString("status.blocked");

        return String.format("%-18s | %-10s | %-10s | %12.2f | %12.2f | %-8s | %s",
                ownerName, cardNumber, status, currentDebt, creditLimit, currency, dateFormat.format(creationDate));
    }

    @Override
    public String toString() {
        return toString(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }
}
