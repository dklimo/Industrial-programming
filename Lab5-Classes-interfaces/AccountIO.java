import java.io.*;
import java.util.*;

public class AccountIO {
    public static void printAccountsToConsole(Account[] accounts) {
        System.out.println("+--------------------+------------+----------+-----------------+-----------------+");
        System.out.printf("| %-18s | %-10s | %-8s | %-15s | %-15s |%n",
                "Owner Name", "Account", "Currency", "Annual Interest", "Balance");
        System.out.println("+====================+============+==========+=================+=================+");

        for (Account acc : accounts) {
            System.out.println("| " + acc.toString().replace("|", " | ") + " |");
        }

        System.out.println("+--------------------+------------+----------+-----------------+-----------------+");
    }
}