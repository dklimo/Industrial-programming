import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.text.MessageFormat;
import java.util.ResourceBundle;

public class PaymentSystem implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Account> accounts;
    private List<CreditCard> creditCards;
    private List<Order> orders;
    private static ResourceBundle messages;

    public static void setMessages(ResourceBundle bundle) {
        messages = bundle;
        Account.setMessages(bundle);
        CreditCard.setMessages(bundle);
        Order.setMessages(bundle);
    }

    public PaymentSystem(){
        this.accounts = new ArrayList<>();
        this.creditCards = new ArrayList<>();
        this.orders = new ArrayList<>();
    }

    public void addAccount(Account account){
        accounts.add(account);
    }
    public Account findAccount(String accountnumber) throws IllegalStateException {
        for(Account account : accounts){
            if(account.getAccountNumber().equals(accountnumber)){
                return account;
            }
        }
        throw new IllegalStateException(MessageFormat.format(messages.getString("error.account_not_found"), accountnumber));
    }
    public void addCreditCard(CreditCard card){
        creditCards.add(card);
    }
    public CreditCard findCreditCard(String cardNumber) throws IllegalStateException {
        for (CreditCard card : creditCards) {
            if (card.getCardNumber().equals(cardNumber)) {
                return card;
            }
        }
        throw new IllegalStateException(MessageFormat.format(messages.getString("error.card_not_found"), cardNumber));
    }
    public void addOrder(Order order) {
        orders.add(order);
    }

    public Order findOrder(String orderNumber) throws IllegalStateException {
        for (Order order : orders) {
            if (order.getOrderNumber().equals(orderNumber)) {
                return order;
            }
        }
        throw new IllegalStateException(MessageFormat.format(messages.getString("error.order_not_found"), orderNumber));
    }
    public void payOrderFromAccount(String orderNumber, String accountNumber) throws IllegalStateException{
        Order order = findOrder(orderNumber);
        Account account = findAccount(accountNumber);

        if(order.isPaid()){
            throw new IllegalStateException(messages.getString("error.order_already_paid"));
        }
        account.withDraw(order.getAmount());
        order.Paid();
    }
    public void payOrderByCreditCard(String orderNumber, String cardNumber) throws IllegalStateException{
        Order order = findOrder(orderNumber);
        CreditCard card = findCreditCard(cardNumber);

        if(order.isPaid()){
            throw new IllegalStateException(messages.getString("error.order_already_paid"));
        }
        card.makePay(order.getAmount());
        order.Paid();
    }
    public void RepayCreditCard(String cardNumber, double amount) throws IllegalStateException{
        CreditCard card = findCreditCard(cardNumber);
        card.repay(amount);
    }
    public void transferBetweenAccounts(String fromAccountNumber, String toAccountNumber, double amount) throws IllegalStateException{
        Account fromAcc = findAccount(fromAccountNumber);
        Account toAcc = findAccount(toAccountNumber);
        fromAcc.withDraw(amount);
        toAcc.deposit(amount);

    }
    public void AnnulAccount(String accountNumber) throws IllegalStateException{
        Account account = findAccount(accountNumber);
        account.blockedByClient();
    }
    public void BlockCardByClient(String cardNumber) throws IllegalStateException{
        CreditCard card = findCreditCard(cardNumber);
        card.blockedByClient();
    }
    public List<Account> getAccounts() { return accounts; }
    public List<CreditCard> getCreditCards() { return creditCards; }
    public List<Order> getOrders() { return orders; }
}