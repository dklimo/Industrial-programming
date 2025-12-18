import java.util.*;
import java.text.SimpleDateFormat;
import java.text.MessageFormat;

public class Main {
    private static ResourceBundle messages;
    private static SimpleDateFormat dateFormat;

    public static void main(String[] args) {
        Locale locale = getLocaleFromArgs(args);
        messages = ResourceBundle.getBundle("messages", locale);
        PaymentSystem.setMessages(messages);
        PaymentSystemConnector.setMessages(messages);
        if (locale.equals(Locale.UK)) {
            dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.UK);
        } else if (locale.equals(new Locale("be", "BY"))) {
            dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", new Locale("be", "BY"));
        } else {
            dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", new Locale("ru", "RU"));
        }

        PaymentSystemConnector connector = new PaymentSystemConnector();
        PaymentSystem system = new PaymentSystem();

        try {
            connector.loadSystem(system);
            if (system.getAccounts().isEmpty() &&
                    system.getCreditCards().isEmpty() &&
                    system.getOrders().isEmpty()) {

                System.out.println(messages.getString("error.load_empty"));
                initializeSampleData(system);
            } else {
                System.out.println(messages.getString("success.loaded"));
            }
        } catch (IllegalStateException e) {
            System.out.println(MessageFormat.format(messages.getString("error.load"), e.getMessage()));
            System.out.println(messages.getString("error.load_empty"));
            system = new PaymentSystem();
            initializeSampleData(system);
        }

        Scanner in = new Scanner(System.in);
        runMenu(system, connector, in);
    }

    private static Locale getLocaleFromArgs(String[] args) {
        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "ru_ru": return new Locale("ru", "RU");
                case "be_by": return new Locale("be", "BY");
                case "en_gb": return Locale.UK;
                default: return Locale.getDefault();
            }
        }
        return Locale.getDefault();
    }

    private static void runMenu(PaymentSystem system, PaymentSystemConnector connector, Scanner in) {
        while (true) {
            System.out.println("\n=== " + messages.getString("app.title") + " ===");
            System.out.println(messages.getString("menu.choose"));
            System.out.println("1 - " + messages.getString("menu.pay_account"));
            System.out.println("2 - " + messages.getString("menu.pay_card"));
            System.out.println("3 - " + messages.getString("menu.repay_card"));
            System.out.println("4 - " + messages.getString("menu.transfer"));
            System.out.println("5 - " + messages.getString("menu.cancel_account"));
            System.out.println("6 - " + messages.getString("menu.block_card"));
            System.out.println("7 - " + messages.getString("menu.show_accounts"));
            System.out.println("8 - " + messages.getString("menu.show_cards"));
            System.out.println("9 - " + messages.getString("menu.show_orders"));
            System.out.println("10 - " + messages.getString("menu.add_account"));
            System.out.println("11 - " + messages.getString("menu.add_card"));
            System.out.println("12 - " + messages.getString("menu.add_order"));
            System.out.println("13 - " + messages.getString("menu.save"));
            System.out.println("14 - " + messages.getString("menu.exit"));
            System.out.print(messages.getString("menu.choice") + " ");

            int choice;
            try {
                choice = in.nextInt();
                in.nextLine();
            } catch (InputMismatchException e) {
                System.err.println(messages.getString("error.invalid_input"));
                in.nextLine();
                continue;
            }

            switch (choice) {
                case 1:
                    showOrdersIfRequested(system, in);
                    showAccountsIfRequested(system, in);
                    payOrderFromAccount(system, in);
                    break;
                case 2:
                    showOrdersIfRequested(system, in);
                    showCardsIfRequested(system, in);
                    payOrderByCreditCard(system, in);
                    break;
                case 3:
                    showCardsIfRequested(system, in);
                    repayCreditCard(system, in);
                    break;
                case 4:
                    showAccountsIfRequested(system, in);
                    transferBetweenAccounts(system, in);
                    break;
                case 5:
                    showAccountsIfRequested(system, in);
                    cancelAccount(system, in);
                    break;
                case 6:
                    showCardsIfRequested(system, in);
                    blockCard(system, in);
                    break;
                case 7: showAllAccounts(system); break;
                case 8: showAllCreditCards(system); break;
                case 9: showAllOrders(system); break;
                case 10: addNewAccount(system, in); break;
                case 11: addNewCreditCard(system, in); break;
                case 12: addNewOrder(system, in); break;
                case 13: saveSystem(connector, system); break;
                case 14: exit(connector, system, in); return;
                default: System.out.println(messages.getString("error.invalid_input"));
            }
        }
    }
    private static boolean askToShowList(Scanner in, String messageKey) {
        System.out.print(messages.getString(messageKey));
        String answer = in.nextLine().trim().toLowerCase();
        return answer.equals("y") || answer.equals("yes") || answer.equals("д") || answer.equals("да");
    }

    private static void showAccountsIfRequested(PaymentSystem system, Scanner in) {
        if (askToShowList(in, "menu.show_accounts_before_action")) {
            showAllAccounts(system);
        }
    }

    private static void showCardsIfRequested(PaymentSystem system, Scanner in) {
        if (askToShowList(in, "menu.show_cards_before_action")) {
            showAllCreditCards(system);
        }
    }

    private static void showOrdersIfRequested(PaymentSystem system, Scanner in) {
        if (askToShowList(in, "menu.show_orders_before_action")) {
            showAllOrders(system);
        }
    }
    private static void payOrderFromAccount(PaymentSystem system, Scanner in) {
        System.out.print(messages.getString("enter.order_number"));
        String orderNumber = in.nextLine();
        System.out.print(messages.getString("enter.account_number"));
        String accountNumber = in.nextLine();

        try {
            system.payOrderFromAccount(orderNumber, accountNumber);
            System.out.println(MessageFormat.format(messages.getString("success.order_paid_account"), orderNumber, accountNumber));
        } catch (IllegalStateException e) {
            System.out.println(MessageFormat.format(messages.getString("error.general"), e.getMessage()));
        }
    }

    private static void payOrderByCreditCard(PaymentSystem system, Scanner in) {
        System.out.print(messages.getString("enter.order_number"));
        String orderNum = in.nextLine();
        System.out.print(messages.getString("enter.card_number"));
        String cardNum = in.nextLine();

        try {
            system.payOrderByCreditCard(orderNum, cardNum);
            System.out.println(MessageFormat.format(messages.getString("success.order_paid_card"), orderNum, cardNum));
        } catch (IllegalStateException e) {
            System.out.println(MessageFormat.format(messages.getString("error.general"), e.getMessage()));
        }
    }

    private static void repayCreditCard(PaymentSystem system, Scanner in) {
        System.out.print(messages.getString("enter.card_number"));
        String repayCardNum = in.nextLine();
        System.out.print(messages.getString("enter.repay_amount"));
        try {
            double repayAmount = in.nextDouble();
            in.nextLine();

            try {
                system.RepayCreditCard(repayCardNum, repayAmount);
                System.out.println(MessageFormat.format(messages.getString("success.repaid"), repayAmount, repayCardNum));
            } catch (IllegalStateException e) {
                System.out.println(MessageFormat.format(messages.getString("error.general"), e.getMessage()));
            }
        } catch (InputMismatchException e) {
            System.err.println(messages.getString("error.invalid_amount"));
            in.nextLine();
        }
    }

    private static void transferBetweenAccounts(PaymentSystem system, Scanner in) {
        System.out.print(messages.getString("enter.from_account"));
        String fromAccount = in.nextLine();
        System.out.print(messages.getString("enter.to_account"));
        String toAccount = in.nextLine();
        System.out.print(messages.getString("enter.transfer_amount"));
        try {
            double transferAmount = in.nextDouble();
            in.nextLine();

            try {
                system.transferBetweenAccounts(fromAccount, toAccount, transferAmount);
                System.out.println(MessageFormat.format(messages.getString("success.transferred"), transferAmount, fromAccount, toAccount));
            } catch (IllegalStateException e) {
                System.out.println(MessageFormat.format(messages.getString("error.general"), e.getMessage()));
            }
        } catch (InputMismatchException e) {
            System.err.println(messages.getString("error.invalid_amount"));
            in.nextLine();
        }
    }

    private static void cancelAccount(PaymentSystem system, Scanner in) {
        System.out.print(messages.getString("enter.cancel_account"));
        String cancelAccount = in.nextLine();

        try {
            system.AnnulAccount(cancelAccount);
            System.out.println(MessageFormat.format(messages.getString("success.account_canceled"), cancelAccount));
        } catch (IllegalStateException e) {
            System.out.println(MessageFormat.format(messages.getString("error.general"), e.getMessage()));
        }
    }

    private static void blockCard(PaymentSystem system, Scanner in) {
        System.out.print(messages.getString("enter.block_card"));
        String blockCard = in.nextLine();

        try {
            system.BlockCardByClient(blockCard);
            System.out.println(MessageFormat.format(messages.getString("success.card_blocked"), blockCard));
        } catch (IllegalStateException e) {
            System.out.println(MessageFormat.format(messages.getString("error.general"), e.getMessage()));
        }
    }

    private static void showAllAccounts(PaymentSystem system) {
        System.out.println("\n=== " + messages.getString("accounts.title") + " ===");
        System.out.println(String.format("%-18s | %-10s | %-10s | %12s | %-8s | %s",
                messages.getString("table.header.owner_name"),
                messages.getString("table.header.number"),
                messages.getString("table.header.status"),
                messages.getString("table.header.balance"),
                messages.getString("table.header.currency"),
                messages.getString("table.header.created")));
        System.out.println("-------------------------------------------------------------------------------------------");

        for (Account account : system.getAccounts()) {
            System.out.println(account.toString(dateFormat));
        }

        if (system.getAccounts().isEmpty()) {
            System.out.println(messages.getString("no_accounts"));
        }
    }

    private static void showAllCreditCards(PaymentSystem system) {
        System.out.println("\n=== " + messages.getString("cards.title") + " ===");
        System.out.println(String.format("%-18s | %-10s | %-10s | %12s | %12s | %-8s | %s",
                messages.getString("table.header.owner_name"),
                messages.getString("table.header.number"),
                messages.getString("table.header.status"),
                messages.getString("table.header.current_debt"),
                messages.getString("table.header.credit_limit"),
                messages.getString("table.header.currency"),
                messages.getString("table.header.created")));
        System.out.println("-------------------------------------------------------------------------------------------------------------------");

        for (CreditCard card : system.getCreditCards()) {
            System.out.println(card.toString(dateFormat));
        }

        if (system.getCreditCards().isEmpty()) {
            System.out.println(messages.getString("no_cards"));
        }
    }

    private static void showAllOrders(PaymentSystem system) {
        System.out.println("\n=== " + messages.getString("orders.title") + " ===");
        System.out.println(String.format("%-18s | %-18s | %12s | %-8s | %s",
                messages.getString("table.header.order_number"),
                messages.getString("table.header.description"),
                messages.getString("table.header.amount"),
                messages.getString("table.header.status"),
                messages.getString("table.header.created")));
        System.out.println("---------------------------------------------------------------------------------------------------");

        for (Order order : system.getOrders()) {
            System.out.println(order.toString(dateFormat));
        }

        if (system.getOrders().isEmpty()) {
            System.out.println(messages.getString("no_orders"));
        }
    }

    private static void addNewAccount(PaymentSystem system, Scanner in) {
        System.out.print(messages.getString("enter.owner_name"));
        String ownerName = in.nextLine();
        System.out.print(messages.getString("enter.balance"));
        try {
            double balance = in.nextDouble();
            in.nextLine();
            System.out.print(messages.getString("enter.currency"));
            String currency = in.nextLine();

            Account newAccount = new Account(ownerName, balance, currency);
            system.addAccount(newAccount);
            System.out.println(MessageFormat.format(messages.getString("success.account_created"), newAccount.getAccountNumber()));

        } catch (InputMismatchException e) {
            System.err.println(messages.getString("error.invalid_amount"));
            in.nextLine();
        }
    }

    private static void addNewCreditCard(PaymentSystem system, Scanner in) {
        System.out.print(messages.getString("enter.owner_name"));
        String ownerName = in.nextLine();
        System.out.print(messages.getString("enter.limit"));
        try {
            double limit = in.nextDouble();
            in.nextLine();
            System.out.print(messages.getString("enter.currency"));
            String currency = in.nextLine();

            CreditCard newCard = new CreditCard(ownerName, limit, currency);
            system.addCreditCard(newCard);
            System.out.println(MessageFormat.format(messages.getString("success.card_created"), newCard.getCardNumber()));

        } catch (InputMismatchException e) {
            System.err.println(messages.getString("error.invalid_amount"));
            in.nextLine();
        }
    }

    private static void addNewOrder(PaymentSystem system, Scanner in) {
        System.out.print(messages.getString("enter.description"));
        String description = in.nextLine();
        System.out.print(messages.getString("enter.amount"));
        try {
            double amount = in.nextDouble();
            in.nextLine();

            Order newOrder = new Order(description, amount);
            system.addOrder(newOrder);
            System.out.println(MessageFormat.format(messages.getString("success.order_created"), newOrder.getOrderNumber()));

        } catch (InputMismatchException e) {
            System.err.println(messages.getString("error.invalid_amount"));
            in.nextLine();
        }
    }

    private static void saveSystem(PaymentSystemConnector connector, PaymentSystem system) {
        try {
            connector.saveSystem(system);
        } catch (IllegalStateException e) {
            System.out.println(MessageFormat.format(messages.getString("error.save"), e.getMessage()));
        }
    }

    private static void exit(PaymentSystemConnector connector, PaymentSystem system, Scanner in) {
        try {
            connector.saveSystem(system);
        } catch (IllegalStateException e) {
            System.out.println(MessageFormat.format(messages.getString("error.save"), e.getMessage()));
        }
        System.out.println(messages.getString("goodbye"));
        in.close();
    }

    private static void initializeSampleData(PaymentSystem system) {
        system.addAccount(new Account("Paul Landers", 12500.75, "USD"));
        system.addAccount(new Account("Richard Kruspe", 8500.50, "EUR"));
        system.addAccount(new Account("Till Lindemann", 15600.25, "BYN"));

        system.addCreditCard(new CreditCard("Paul Landers", 5000.0, "USD"));
        system.addCreditCard(new CreditCard("Richard Kruspe", 3000.0, "EUR"));
        system.addCreditCard(new CreditCard("Till Lindemann", 7000.0, "BYN"));

        system.addOrder(new Order("Laptop purchase", 1200.0));
        system.addOrder(new Order("Internet payment", 50.0));
        system.addOrder(new Order("Phone purchase", 800.0));

        System.out.println(messages.getString("info.sample_data_initialized"));
    }
}