import java.util.Arrays;
import java.util.*;
import java.util.Iterator;
import java.util.Comparator;
import java.util.NoSuchElementException;

public class Account implements Comparable<Account>, Iterable<Object> {
    private String ownerName;
    private String number;
    private String currency;
    private double annualInterest;
    private double balance;
    public static final String[] user = {
            "OWNERNAME",
            "NUMBER",
            "CURRENCY",
            "ANNUALINTEREST",
            "BALANCE"
    };
    public Account(String ownerName, String number, String currency, double annualInterest, double balance) {
        this.ownerName = ownerName;
        this.number = number;
        this.currency = currency;
        this.annualInterest = annualInterest;
        this.balance = balance;
    }
    public Account(String textLine) {
        String[] parts = textLine.split("\\|");
        if (parts.length != 5) {
            throw new IllegalArgumentException("Invalid string format for Account");
        }
        this.ownerName = parts[0].trim();
        this.number = parts[1].trim();
        this.currency = parts[2].trim();
        this.annualInterest = Double.parseDouble(parts[3].trim());
        this.balance = Double.parseDouble(parts[4].trim());
    }
    public boolean withDraw(double amount){
        if(amount > 0 && amount<=balance){
            balance-=amount;
            return true;
        }
        return false;
    }
    public void add(double amount){
        if(amount> 0) balance+=amount;
        else{
            throw new IllegalArgumentException("Amount must be positive");
        }
    }
    public void accrueInterest(int months){
        if (months <= 0) {
            throw new IllegalArgumentException("Period must be positive");
        }
        if (balance <= 0) {
            throw new IllegalStateException("Balance must be positive");
        }

        double interest = balance * (annualInterest / 100) * (months / 12.0);
        balance+=interest;
    }
    @Override
    public int compareTo(Account other) {
        int nameCompare = this.ownerName.compareTo(other.ownerName);
        if (nameCompare != 0) {
            return nameCompare;
        }
        return this.number.compareTo(other.number);
    }
    public static Comparator<Account> getComparator(final int sortBy) {
        if (sortBy >= user.length || sortBy < 0) {
            throw new IndexOutOfBoundsException();
        }
        return new Comparator<Account>() {
            @Override
            public int compare(Account a0, Account a1) {
               switch(sortBy){
                   case 0: return a0.ownerName.compareTo(a1.ownerName);
                   case 1: return a0.number.compareTo(a1.number);
                   case 2: return a0.currency.compareTo(a1.currency);
                   case 3: return Double.compare(a0.annualInterest, a1.annualInterest);
                   case 4: return Double.compare(a0.balance, a1.balance);
                   default: return 0;
               }
            }
        };
    }
    @Override
    public Iterator<Object> iterator() {
        return new Iterator<Object>() {
            private int idx = 0;
            @Override
            public boolean hasNext() {
                return idx < 5;
            }

            @Override
            public Object next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                switch (idx++) {
                    case 0: return ownerName;
                    case 1: return number;
                    case 2: return currency;
                    case 3: return annualInterest;
                    case 4: return balance;
                    default: throw new NoSuchElementException();
                }
            }
            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

        };
    }
    @Override
    public String toString() {
        return String.format("%-18s|%-10s|%-8s|%15.2f|%15.2f",
                ownerName,
                number,
                currency,
                annualInterest,
                balance);
    }

    public String getOwnerName() { return ownerName; }
    public String getNumber() { return number; }
    public String getCurrency() { return currency; }
    public double getAnnualInterest() { return annualInterest; }
    public double getBalance() { return balance; }
}
