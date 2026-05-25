package Lecture4_interfaces_abstract_classes;

import org.jetbrains.annotations.NotNull;
import java.util.Calendar;

public class WithdrawalTransaction extends BaseTransaction {

    private BankAccount appliedAccount;   // saved so reverse() can restore it
    private boolean wasApplied = false;
    private double amountNotWithdrawn = 0.0; // Q3: tracks the shortfall

    public WithdrawalTransaction(int amount, @NotNull Calendar date) {
        super(amount, date);
    }

    private boolean checkDepositAmount(int amt) {
        return amt >= 0;
    }

    @Override
    public void printTransactionDetails() {
        System.out.println("Withdrawal Transaction:");
        System.out.println("  ID     : " + getTransactionID());
        System.out.println("  Amount : " + getAmount());
        System.out.println("  Date   : " + getDate().getTime());
    }

   
    /**
     * Standard withdrawal. Uses the throws keyword to propagate
     * InsufficientFundsException to the caller when funds are inadequate.
     */
    @Override
    public void apply(BankAccount ba) throws InsufficientFundsException {
        double currBalance = ba.getBalance();
        if (currBalance < getAmount()) {
            throw new InsufficientFundsException(
                "Insufficient funds: balance is " + currBalance
                + ", withdrawal amount is " + getAmount()
            );
        }
        ba.setBalance(currBalance - getAmount());
        this.appliedAccount = ba;
        this.wasApplied = true;
        System.out.println("Withdrawal applied. New balance: " + ba.getBalance());
        printTransactionDetails();
    }

    

    /**
     * Overloaded apply() that handles the edge case 0 < balance < amount.
     * When allowPartial is true, withdraws whatever balance remains and
     * records the shortfall in amountNotWithdrawn.
     */
    public void apply(BankAccount ba, boolean allowPartial) throws InsufficientFundsException {
        try {
            double currBalance = ba.getBalance();

            if (currBalance <= 0) {
                // Balance is zero or negative — nothing at all to withdraw
                throw new InsufficientFundsException(
                    "No available balance (balance = " + currBalance + ")."
                );
            } else if (currBalance < getAmount()) {
                if (allowPartial) {
                    // Partial path: drain everything available
                    amountNotWithdrawn = getAmount() - currBalance;
                    System.out.println("Partial withdrawal: " + currBalance
                        + " withdrawn. Shortfall: " + amountNotWithdrawn);
                    ba.setBalance(0);
                    this.appliedAccount = ba;
                    this.wasApplied = true;
                    printTransactionDetails();
                } else {
                    throw new InsufficientFundsException(
                        "Insufficient funds: balance is " + currBalance
                        + ", withdrawal amount is " + getAmount()
                    );
                }
            } else {
                // Normal path — full amount available
                ba.setBalance(currBalance - getAmount());
                this.appliedAccount = ba;
                this.wasApplied = true;
                System.out.println("Withdrawal applied (full). New balance: " + ba.getBalance());
                printTransactionDetails();
            }

        } catch (InsufficientFundsException e) {
            System.out.println("Caught InsufficientFundsException: " + e.getMessage());
            throw e; // re-throw so the caller is also informed
        } finally {
            System.out.println("[finally] apply(ba, allowPartial) execution finished.");
        }
    }

    

    /**
     * Reverses this withdrawal by restoring the deducted amount to the
     * account the transaction was originally applied to.
     *
     * @return true if reversal succeeded, false if the transaction was never applied.
     */
    public boolean reverse() {
        if (!wasApplied || appliedAccount == null) {
            System.out.println("Reversal failed: transaction has not been applied.");
            return false;
        }
        double amountDeducted = getAmount() - amountNotWithdrawn; // handles partial case
        appliedAccount.setBalance(appliedAccount.getBalance() + amountDeducted);
        System.out.println("Withdrawal reversed. Restored: " + amountDeducted
            + " | New balance: " + appliedAccount.getBalance());
        wasApplied = false;
        return true;
    }

    public double getAmountNotWithdrawn() {
        return amountNotWithdrawn;
    }
}

