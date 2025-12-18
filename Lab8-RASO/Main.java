import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        try {
            DrugIO drugIO = new DrugIO("drugs.dat");

            List<Drug> testData = Arrays.asList(
                    new Drug("001", "Aspirin", 50, 2.5, "2025-09-10", "2026-09-10"),
                    new Drug("002", "Ibuprofen", 30, 3.0, "2024-02-15", "2026-02-15"),
                    new Drug("003", "Paracetamol", 100, 1.8, "2025-03-01", "2026-03-01")
            );
            drugIO.fillTestData(testData);
            System.out.println("File filled with test data.");


            while (true) {
                System.out.println("\n=== Drug Management System ===");
                System.out.println("1 - Show all drugs (sequential)");
                System.out.println("2 - Build index by field");
                System.out.println("3 - Show by index (ascending)");
                System.out.println("4 - Show by index (descending)");
                System.out.println("5 - Search by index (equal)");
                System.out.println("6 - Search by index (greater)");
                System.out.println("7 - Search by index (less)");
                System.out.println("8 - Delete by index");
                System.out.println("9 - Exit");
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

                try {
                    switch (choice) {
                        case 1:
                            drugIO.printAllDrugInfo();
                            break;

                        case 2:
                            System.out.print("Enter field for indexing (drugstoreNum, drugName, receiptDate, expirationDate): ");
                            String field = in.nextLine();
                            drugIO.buildIndexByField(field);
                            System.out.println("Index built by field: " + field);
                            break;

                        case 3:
                            drugIO.printByIndex(true);
                            break;

                        case 4:
                            drugIO.printByIndex(false);
                            break;

                        case 5:
                            System.out.println("Current index field: " + drugIO.getCurrentIndexField());
                            System.out.print("Enter key for search: ");
                            String keyEqual = in.nextLine();
                            drugIO.findEqual(keyEqual);
                            break;

                        case 6:
                            System.out.println("Current index field: " + drugIO.getCurrentIndexField());
                            System.out.print("Enter key: ");
                            String keyGreater = in.nextLine();
                            drugIO.findGreater(keyGreater);
                            break;

                        case 7:
                            System.out.println("Current index field: " + drugIO.getCurrentIndexField());
                            System.out.print("Enter key: ");
                            String keyLess = in.nextLine();
                            drugIO.findLess(keyLess);
                            break;

                        case 8:
                            System.out.println("Current index field: " + drugIO.getCurrentIndexField());
                            System.out.print("Enter key to delete: ");
                            String keyDelete = in.nextLine();
                            drugIO.deleteByIndex(keyDelete);
                            break;

                        case 9:
                            System.out.println("Exiting program.");
                            return;

                        default:
                            System.out.println("Invalid choice. Try again.");
                    }
                } catch (Exception e) {
                    System.err.println("Error: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("File not found: " + e.getMessage());
        }
    }
}
