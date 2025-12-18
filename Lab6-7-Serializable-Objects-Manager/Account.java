import java.io.Serializable;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.MessageFormat;
import java.util.ResourceBundle;

public class Account implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String accountNumber;
    private double balance;
    private boolean isBlockedByClient;
    private boolean isBlockedByAdmin;
    private String blockReason;
    private final String ownerName;
    private String currency;
    private final Date creationDate;
    private static ResourceBundle messages;

    public static void setMessages(ResourceBundle bundle) {
        messages = bundle;
    }

    public String getAccountNumber() {return accountNumber;}
    public Date getCreationDate() {return creationDate;}

    public Account(String ownerName, double balance, String currency) {
        this.accountNumber = "ACC" + ((int)(Math.random() * 900) + 100);
        this.ownerName = ownerName;
        this.balance = balance;
        this.isBlockedByClient = false;
        this.isBlockedByAdmin = false;
        this.blockReason = "";
        this.currency = currency;
        this.creationDate = new Date();
    }

    private void checkAccountAvailability() throws IllegalStateException {
        if (isBlockedByClient) {
            throw new IllegalStateException(messages.getString("error.account_canceled"));
        }
    }

    public void withDraw(double amount){
        checkAccountAvailability();
        if (amount <= 0) {
            throw new IllegalArgumentException(messages.getString("error.summary_positive"));
        }
        if (amount > balance) {
            throw new IllegalArgumentException(messages.getString("error.insufficient_funds"));
        }
        balance -= amount;
    }

    public void deposit(double amount) {
        checkAccountAvailability();
        if (amount > 0) balance += amount;
        else {
            throw new IllegalArgumentException(messages.getString("error.amount_positive"));
        }
    }

    public void blockedByClient() {
        isBlockedByClient = true;
        balance = 0;
    }

    public String toString(SimpleDateFormat dateFormat) {
        String status = messages.getString("status.active");
        if (isBlockedByClient) status = messages.getString("status.canceled");
        return String.format("%-18s | %-10s | %-10s | %12.2f | %-8s | %s",
                ownerName, accountNumber, status, balance, currency, dateFormat.format(creationDate));
    }

    @Override
    public String toString() {
        return toString(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }
}


