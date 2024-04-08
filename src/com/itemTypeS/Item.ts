// @ts-ignore
import * as fs from 'fs';

// Define the 'Item' interface with 'name' and 'price' properties
interface Item {
    name: string;
    price: number;
}

// Define the 'Transaction' interface with 'items' and 'amountPaid' properties
interface Transaction {
    items: Item[];
    amountPaid: number;
}

// Initialize the 'till' with denominations and their quantities
const till = {
    'R50': 5,
    'R20': 5,
    'R10': 6,
    'R5': 12,
    'R2': 10,
    'R1': 10
};

// Function to calculate the change given the transaction total and amount paid
const calculateChange = (transactionTotal: number, amountPaid: number): number[] => {
    let change = amountPaid - transactionTotal;
    const denominations = ['R50', 'R20', 'R10', 'R5', 'R2', 'R1'];
    const changeBreakdown: number[] = [];

    // Calculate the number of each denomination to return as change
    denominations.forEach(denomination => {
        const count = Math.floor(change / parseInt(denomination.slice(1)));
        change -= count * parseInt(denomination.slice(1));
        changeBreakdown.push(count);
    });

    return changeBreakdown;
}

// Function to process a transaction and output the result
const processTransaction = (transaction: Transaction) => {
    let tillTotal = Object.keys(till).reduce((acc, denomination) => {
        return acc + parseInt(denomination.slice(1)) * till[denomination];
    }, 0);
    const transactionTotal = transaction.items.reduce((acc, item) => acc + item.price, 0);
    const change = calculateChange(transactionTotal, transaction.amountPaid);

    // Output the transaction details
    console.log(`Till Start: R${tillTotal}, Transaction Total: R${transactionTotal}, Paid: R${transaction.amountPaid}, Change Total: R${transaction.amountPaid - transactionTotal}, Change Breakdown: R${change.join('-')}`);
}

// Read the input file and process each transaction
fs.readFile('input.txt', 'utf8', (err, data) => {
    if (err) {
        console.error(err);
        return;
    }

    // Split the data by lines to get each transaction
    const transactions = data.split('\n');

    transactions.forEach(transaction => {
        // Split the transaction into items and amount paid
        const items = transaction.split(';')[0].split(',');
        const amountPaid = parseInt(transaction.split(';')[1]);
        const itemObjects: Item[] = [];

        // Create item objects from the split data
        items.forEach(item => {
            const [name, price] = item.split(' R');
            itemObjects.push({ name, price: parseInt(price) });
        });

        // Process the transaction with the created item objects and amount paid
        processTransaction({ items: itemObjects, amountPaid });
    });

    // Calculate the total amount in the till after all transactions
    const tillEndTotal = Object.keys(till).reduce((acc, denomination) => {
        return acc + parseInt(denomination.slice(1)) * till[denomination];
    }, 0);

    // Output the final total in the till
    console.log(`Till End: R${tillEndTotal}`);
});