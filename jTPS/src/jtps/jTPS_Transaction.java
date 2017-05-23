package jtps;

/**
 * @author McKillaGorilla
 * @author Thomas Povinelli
 */
public interface jTPS_Transaction {
    public void undoTransaction();

    public default void redoTransaction() {
        doTransaction();
    }

    public void doTransaction();
}
