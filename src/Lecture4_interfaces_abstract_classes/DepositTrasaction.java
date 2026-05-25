package Lecture4_interfaces_abstract_classes;

import org.jetbrains.annotations.NotNull;
import java.util.Calendar;

public class DepositTrasaction extends BaseTransaction {

    public DepositTrasaction(int amount, @NotNull Calendar date) {
        super(amount, date);
    }

    private boolean checkDepositAmount(int amt) {
        return amt >= 0;
    }

    @Override
    public void printTransactionDetails() {
        System.out.println("Deposit Transaction:");
        System.out.println("  ID     : " + getTransactionID());
        System.out.println("  Amount : " + getAmount());
        System.out.println("  Date   : " + getDate().getTime());
    }

    /**
     * Deposits are irreversible — credits the account unconditionally.
     * throws clause is required to satisfy the interface signature,
     * but a deposit never actually throws it.
     */
    @Override
    public void apply(BankAccount ba) throws InsufficientFundsException {
        double newBalance = ba.getBalance() + getAmount();
        ba.setBalance(newBalance);
        System.out.println("Deposit applied. New balance: " + ba.getBalance());
        printTransactionDetails();
    }
}
