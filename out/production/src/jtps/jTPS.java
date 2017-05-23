package jtps;

import javafx.scene.control.Button;

import java.util.ArrayList;

/**
 * @author McKillaGorilla
 * @author Thomas Povinelli
 */
public class jTPS {
    private ArrayList<jTPS_Transaction> transactions = new ArrayList();
    private int mostRecentTransaction = -1;
    private Button undoButton, redoButton;

    public jTPS() {
    }

    public void clear() {
        transactions.clear();
        mostRecentTransaction = -1;
    }

    public void reset() {
        clear();
        undoButton = null;
        redoButton = null;
    }

    public void registerButtons(Button u, Button r) {
        undoButton = u;
        redoButton = r;
    }

    public void addTransaction(jTPS_Transaction transaction) {
        // IS THIS THE FIRST TRANSACTION?
        if (mostRecentTransaction < 0) {
            // DO WE HAVE TO CHOP THE LIST?
            if (transactions.size() > 0) {
                transactions = new ArrayList();
            }
            transactions.add(transaction);
        }
        // ARE WE ERASING ALL THE REDO TRANSACTIONS?
        else if (mostRecentTransaction < (transactions.size() - 1)) {
            transactions.set(mostRecentTransaction + 1, transaction);
            transactions = new ArrayList(
              transactions.subList(0, mostRecentTransaction + 2));
        }
        // IS IT JUST A TRANSACTION TO APPEND TO THE END?
        else {
            transactions.add(transaction);
        }
        doTransaction();
    }

    public void doTransaction() {
        if (mostRecentTransaction < (transactions.size() - 1)) {
            jTPS_Transaction transaction = transactions.get(
              mostRecentTransaction + 1);
            transaction.doTransaction();
            mostRecentTransaction++;
            undoButton.setDisable(false);
        }
    }

    public void undoTransaction() {
        if (mostRecentTransaction >= 0) {
            jTPS_Transaction transaction = transactions.get(
              mostRecentTransaction);
            transaction.undoTransaction();
            mostRecentTransaction--;
            if (mostRecentTransaction < 0) {
                undoButton.setDisable(true);
            }
            redoButton.setDisable(false);
        }
    }

    public void redoTransaction() {
        if (mostRecentTransaction < (transactions.size() - 1)) {
            jTPS_Transaction transaction = transactions.get(
              mostRecentTransaction + 1);
            transaction.redoTransaction();
            mostRecentTransaction++;
            if (mostRecentTransaction >= (transactions.size() - 1)) {
                redoButton.setDisable(true);
            }
            undoButton.setDisable(false);
        }
    }

    public String toString() {
        String text = "--Number of Transactions: " + transactions.size() + "\n";
        text += "--Current Index on Stack: " + mostRecentTransaction + "\n";
        text += "--Current Transaction Stack:\n";
        for (int i = 0; i <= mostRecentTransaction; i++) {
            jTPS_Transaction jT = transactions.get(i);
            text += "----" + jT.toString() + "\n";
        }
        return text;
    }
}
