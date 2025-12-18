import java.io.Serializable;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class Order implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String orderNumber;
    private final String description;
    private final double amount;
    private boolean isPaid;
    private final Date creationDate;
    private static ResourceBundle messages;

    public static void setMessages(ResourceBundle bundle) {
        messages = bundle;
    }

    public boolean isPaid() { return isPaid; }
    public String getOrderNumber(){ return orderNumber;}
    public double getAmount(){ return amount;}
    public Date getCreationDate() {return creationDate;}

    public Order(String description, double amount){
        this.orderNumber = "№" + ((int)(Math.random() * 900) + 100);
        this.description = description;
        this.amount = amount;
        this.isPaid = false;
        this.creationDate = new Date();
    }

    public void Paid(){
        isPaid = true;
    }

    public String toString(SimpleDateFormat dateFormat) {
        String status = messages.getString("status.not_paid");
        if (isPaid) status = messages.getString("status.paid");
        return String.format("%-18s | %-18s | %12.2f | %-8s | %s",
                orderNumber,
                description.length() > 18 ? description.substring(0, 15) + "..." : description,
                amount,
                status,
                dateFormat.format(creationDate));
    }

    @Override
    public String toString() {
        return toString(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }
}