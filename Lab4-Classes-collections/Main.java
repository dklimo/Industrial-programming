import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        SubjectIO io = new SubjectIO();
        SubjectIndex subject = null;

        try {
            subject = io.loadFromFile("input.txt");
        } catch (IOException e) {
            System.err.println("Error loading file: " + e.getMessage());
            subject = new SubjectIndex();
        }

        System.out.println("======= Initial List =======");
        subject.printIndex();

        while (true) {
            System.out.println("\nChoose an action:");
            System.out.println("1 - Add a word");
            System.out.println("2 - Remove a word");
            System.out.println("3 - Add a page to a word");
            System.out.println("4 - Remove a page from a word");
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
                    try {
                        System.out.print("Enter a new word: ");
                        String newWord = in.nextLine();
                        if (subject.addComponent(newWord)) {
                            System.out.println("Word added.");
                        } else {
                            System.out.println("Word already exists.");
                        }
                    } catch (IllegalArgumentException e) {
                        System.err.println("Error: " + e.getMessage());
                    }
                    break;

                case 2:
                    System.out.print("Enter the word to remove: ");
                    String wordToDelete = in.nextLine();
                    if (subject.removeComponent(wordToDelete)) {
                        System.out.println("Word removed.");
                    } else {
                        System.out.println("Word not found.");
                    }
                    break;

                case 3:
                    try {
                        System.out.print("Enter the word: ");
                        String wordAddPage = in.nextLine();
                        if (subject.GetComponent(wordAddPage) == null) {
                            System.out.println("Error: Word not found.");
                            break;
                        }
                        System.out.print("Enter the page number: ");
                        int pageToAdd = in.nextInt();
                        in.nextLine();
                        subject.AddPageToWord(wordAddPage, pageToAdd);
                        System.out.println("Page added.");
                    } catch (IllegalArgumentException | IllegalStateException e) {
                        System.err.println("Error: " + e.getMessage());
                    } catch (InputMismatchException e) {
                        System.err.println("Invalid input. Page number must be an integer.");
                        in.nextLine();
                    }
                    break;

                case 4:
                    System.out.print("Enter the word: ");
                    String wordRemovePage = in.nextLine();
                    if (subject.GetComponent(wordRemovePage) == null) {
                        System.out.println("Error: Word not found.");
                        break;
                    }
                    System.out.print("Enter the page number to remove: ");
                    try {
                        int pageToRemove = in.nextInt();
                        in.nextLine();
                        if (subject.RemovePageFromWord(wordRemovePage, pageToRemove)) {
                            System.out.println("Page removed.");
                        } else {
                            System.out.println("Page not found.");
                        }
                    } catch (InputMismatchException e) {
                        System.err.println("Invalid input. Page number must be an integer.");
                        in.nextLine();
                    }
                    break;

                case 5:
                    System.out.println("======= Current List =======");
                    subject.printIndex();
                    break;

                case 6:
                    System.out.println("======= Final List =======");
                    subject.printIndex();
                    System.out.println("Exiting...");
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
