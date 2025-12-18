import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Account[] accounts = {
                new Account("Paul Landers", "ACC1001001", "USD", 4.5, 12500.75),
                new Account("Richard Kruspe", "ACC1001002", "EUR", 3.2, 8500.50),
                new Account("Till Lindemann      |ACC1001003|BYN     |            2.80|        15600.25"),
                new Account("Christoph Schnider", "ACC1001004", "USD", 5.1, 9200.00),
                new Account("Oliver Riedel", "ACC1001005", "BYN", 3.9, 11200.80),
                new Account("Christian Lorenz", "ACC1001006", "EUR", 4.2, 7400.30)
        };

        Scanner in = new Scanner(System.in);

        while (true) {
            System.out.println("\nChoose an action:");
            System.out.println("1 - Withdraw the specified amount");
            System.out.println("2 - Add the specified amount");
            System.out.println("3 - Charge interest");
            System.out.println("4 - Sorting");
            System.out.println("5 - Show current list");
            System.out.println("6 - Exit");
            System.out.print("Your choice: ");

            int choice;
            try {
                choice = in.nextInt();
                in.nextLine();
            } catch (InputMismatchException e) {
                System.err.println("Invalid input. Please enter a number.");
                in.nextLine();
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.print("Enter the owner name: ");
                    String name = in.nextLine();
                    System.out.print("Enter an amount: ");
                    try {
                        double amount = in.nextDouble();
                        in.nextLine();

                        boolean found = false;
                        for (Account acc : accounts) {
                            if (acc.getOwnerName().equalsIgnoreCase(name)) {
                                if (acc.withDraw(amount)) {
                                    System.out.println("Successfully withdrawn " + amount + " from " + name + "'s account");
                                    System.out.println("New balance: " + acc.getBalance());
                                } else {
                                    System.out.println("Withdrawal failed. Insufficient funds or invalid amount.");
                                }
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            System.out.println("Account with owner name '" + name + "' not found.");
                        }
                    } catch (InputMismatchException e) {
                        System.err.println("Invalid amount. Please enter a valid number.");
                        in.nextLine();
                    }
                    break;

                case 2:
                    System.out.print("Enter owner name: ");
                    String name2 = in.nextLine();
                    System.out.print("Enter amount to add: ");
                    try {
                        double amount2 = in.nextDouble();
                        in.nextLine();

                        boolean found2 = false;
                        for (Account account : accounts) {
                            if (account.getOwnerName().equalsIgnoreCase(name2)) {
                                try {
                                    account.add(amount2);
                                    System.out.println("Successfully added " + amount2 + " to " + name2 + "'s account");
                                    System.out.println("New balance: " + account.getBalance());
                                } catch (IllegalArgumentException e) {
                                    System.out.println("Error: " + e.getMessage());
                                }
                                found2 = true;
                                break;
                            }
                        }
                        if (!found2) {
                            System.out.println("Account with owner name '" + name2 + "' not found.");
                        }
                    } catch (InputMismatchException e) {
                        System.err.println("Invalid amount. Please enter a valid number.");
                        in.nextLine();
                    }
                    break;

                case 3:
                    System.out.print("Enter owner name: ");
                    String ownerNameInterest = in.nextLine();
                    System.out.print("Enter number of months: ");
                    try {
                        int months = in.nextInt();
                        in.nextLine();

                        boolean found3 = false;
                        for (Account account : accounts) {
                            if (account.getOwnerName().equalsIgnoreCase(ownerNameInterest)) {
                                try {
                                    double oldBalance = account.getBalance();
                                    account.accrueInterest(months);
                                    double interest = account.getBalance() - oldBalance;
                                    System.out.println("Interest charged: " + String.format("%.2f", interest));
                                    System.out.println("New balance: " + account.getBalance());
                                } catch (IllegalArgumentException | IllegalStateException e) {
                                    System.out.println("Error: " + e.getMessage());
                                }
                                found3 = true;
                                break;
                            }
                        }
                        if (!found3) {
                            System.out.println("Account with owner name '" + ownerNameInterest + "' not found.");
                        }
                    } catch (InputMismatchException e) {
                        System.err.println("Invalid number of months. Please enter a valid integer.");
                        in.nextLine();
                    }
                    break;

                case 4:
                    System.out.println("Sort by: 0-OwnerName, 1-Number, 2-Currency, 3-AnnualInterest, 4-Balance");
                    System.out.print("Your choice: ");
                    try {
                        int sortBy = in.nextInt();
                        in.nextLine();

                        try {
                            Arrays.sort(accounts, Account.getComparator(sortBy));
                            System.out.println("Accounts sorted successfully.");
                        } catch (IndexOutOfBoundsException e) {
                            System.out.println("Invalid sort option. Please choose between 0 and 4.");
                        }
                    } catch (InputMismatchException e) {
                        System.err.println("Invalid input. Please enter a number.");
                        in.nextLine();
                    }
                    break;

                case 5:
                    AccountIO.printAccountsToConsole(accounts);
                    break;

                case 6:
                    System.out.println("Goodbye!");
                    in.close();
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}