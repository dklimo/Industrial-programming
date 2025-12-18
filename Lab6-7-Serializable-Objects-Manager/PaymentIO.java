import java.io.*;
import java.util.List;
import java.text.MessageFormat;
import java.util.ResourceBundle;

class PaymentSystemConnector {
    private String accountsFile;
    private String creditCardsFile;
    private String ordersFile;
    private static ResourceBundle messages;

    public static void setMessages(ResourceBundle bundle) {
        messages = bundle;
    }

    public PaymentSystemConnector() {
        this.accountsFile = "accounts.dat";
        this.creditCardsFile = "creditcards.dat";
        this.ordersFile = "orders.dat";
    }

    public void saveSystem(PaymentSystem system) throws IllegalStateException {
        try {
            saveAccounts(system.getAccounts());
            saveCreditCards(system.getCreditCards());
            saveOrders(system.getOrders());
            System.out.println(messages.getString("success.saved"));
        } catch (IOException e) {
            throw new IllegalStateException(MessageFormat.format(messages.getString("error.save"), e.getMessage()));
        }
    }

    public void loadSystem(PaymentSystem system) throws IllegalStateException {
        if (system == null) {
            throw new IllegalStateException("System is null");
        }

        try {
            loadAccounts(system);
            loadCreditCards(system);
            loadOrders(system);
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException(MessageFormat.format(messages.getString("error.load"), e.getMessage()));
        }
    }

    private void saveAccounts(List<Account> accounts) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(accountsFile))) {
            oos.writeObject(accounts);
            System.out.println(MessageFormat.format(messages.getString("info.accounts_saved"), accountsFile));
        }
    }

    private void saveCreditCards(List<CreditCard> creditCards) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(creditCardsFile))) {
            oos.writeObject(creditCards);
            System.out.println(MessageFormat.format(messages.getString("info.cards_saved"), creditCardsFile));
        }
    }

    private void saveOrders(List<Order> orders) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ordersFile))) {
            oos.writeObject(orders);
            System.out.println(MessageFormat.format(messages.getString("info.orders_saved"), ordersFile));
        }
    }

    @SuppressWarnings("unchecked")
    private void loadAccounts(PaymentSystem system) throws IOException, ClassNotFoundException {
        File file = new File(accountsFile);
        if (!file.exists()) {
            System.out.println(MessageFormat.format(messages.getString("warn.accounts_file_not_exists"), accountsFile));
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(accountsFile))) {
            List<Account> accounts = (List<Account>) ois.readObject();
            for (Account account : accounts) {
                system.addAccount(account);
            }
            System.out.println(MessageFormat.format(messages.getString("info.accounts_loaded"), accountsFile));
        }
    }

    @SuppressWarnings("unchecked")
    private void loadCreditCards(PaymentSystem system) throws IOException, ClassNotFoundException {
        File file = new File(creditCardsFile);
        if (!file.exists()) {
            System.out.println(MessageFormat.format(messages.getString("warn.cards_file_not_exists"), creditCardsFile));
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(creditCardsFile))) {
            List<CreditCard> creditCards = (List<CreditCard>) ois.readObject();
            for (CreditCard card : creditCards) {
                system.addCreditCard(card);
            }
            System.out.println(MessageFormat.format(messages.getString("info.cards_loaded"), creditCardsFile));
        }
    }

    @SuppressWarnings("unchecked")
    private void loadOrders(PaymentSystem system) throws IOException, ClassNotFoundException {
        File file = new File(ordersFile);
        if (!file.exists()) {
            System.out.println(MessageFormat.format(messages.getString("warn.orders_file_not_exists"), ordersFile));
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ordersFile))) {
            List<Order> orders = (List<Order>) ois.readObject();
            for (Order order : orders) {
                system.addOrder(order);
            }
            System.out.println(MessageFormat.format(messages.getString("info.orders_loaded"), ordersFile));
        }
    }

}