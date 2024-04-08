package com.tillManagementJava;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

class Item {
    String name;
    int price;

    public Item(String name, int price) {
        this.name = name;
        this.price = price;
    }
}

class Transaction {
    Item[] items;
    int amountPaid;

    public Transaction(Item[] items, int amountPaid) {
        this.items = items;
        this.amountPaid = amountPaid;
    }
}

class TillTransaction {

    // Initialize a map to store the denominations and their quantities in the till
    static Map<String, Integer> till = new HashMap<>();

    // Populate the till with initial denominations and quantities
    static {
        till.put("R50", 5);
        till.put("R20", 5);
        till.put("R10", 6);
        till.put("R5", 12);
        till.put("R2", 10);
        till.put("R1", 10);
    }

    // Calculate the change to be given to the customer
    public static int[] calculateChange(int transactionTotal, int amountPaid) {
        int change = amountPaid - transactionTotal;
        int[] changeBreakdown = new int[6];
        int[] denominations = {50, 20, 10, 5, 2, 1};

        // Iterate through each denomination
        for (int i = 0; i < denominations.length; i++) {
            int count = change / denominations[i];
            change -= count * denominations[i];
            changeBreakdown[i] = count;
        }

        return changeBreakdown;
    }

    // Process the transaction and print relevant information
    public static void processTransaction(Transaction transaction) {
        // Calculate the total value of the till
        int tillTotal = till.entrySet().stream().mapToInt(entry -> parseInt(entry.getKey().substring(1)) * entry.getValue()).sum();
        int totalTransaction = 0;

        // Calculate the total price of items in the transaction
        for (Item item : transaction.items) {
            totalTransaction += item.price;
        }

        // Calculate the change to be given
        int[] change = calculateChange(totalTransaction, transaction.amountPaid);

        // Print the transaction details
        System.out.printf("Till Start: R%d, Transaction Total: R%d, Paid: R%d, Change Total: R%d, Change Breakdown: R%d-%d-%d-%d-%d-%d\n",
                tillTotal, totalTransaction, transaction.amountPaid, transaction.amountPaid - totalTransaction,
                change[0], change[1], change[2], change[3], change[4], change[5]);
    }
    public static void main(String[] args) {
        File inputFile = new File("input.txt");
        try {
            Scanner scanner = new Scanner(inputFile);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(";");       

                // Parse item details
                String[] itemDetails = parts[0].split(",");
                Item[] items = new Item[itemDetails.length];
                for (int i = 0; i < itemDetails.length; i++) {
                    String[] itemInfo = itemDetails[i].split(" R");
                    String itemName = itemInfo[0]; // Extract the item name
                    int itemPrice = Integer.parseInt(itemInfo[1]); // Parse the item price
                    items[i] = new Item(itemName, itemPrice);
                }
                // Parse amount paid
                int amountPaid = Integer.parseInt(parts[1].split("-")[0].substring(1));

                // Create and process transaction
                Transaction transaction = new Transaction(items, amountPaid);
                processTransaction(transaction);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
    }
}

