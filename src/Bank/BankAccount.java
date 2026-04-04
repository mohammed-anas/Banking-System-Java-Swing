package Bank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Exceptions.InvalidAmount;
import Exceptions.MaxBalance;
import Exceptions.MaxWithdraw;

public class BankAccount implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private double balance;
	protected double min_balance;
	private String acc_num;
	private ArrayList<TransactionRecord> transactions;
	//String type;

	private void ensureTransactionLog() {
		if (transactions == null) {
			transactions = new ArrayList<>();
		}
	}

	/**
	 * Old saves had no {@code transactions} list; fills one baseline row from the current balance so reports are not empty.
	 * Skipped when real rows already exist (e.g. OPENING from a newly constructed account).
	 */
	private void ensureLegacyBaselineIfNeeded() {
		ensureTransactionLog();
		if (!transactions.isEmpty() || acc_num == null) {
			return;
		}
		transactions.add(new TransactionRecord(TransactionRecord.Kind.LEGACY_BASELINE, balance, balance,
				System.currentTimeMillis()));
	}

	private void logTx(TransactionRecord.Kind kind, double amount, double balanceAfter) {
		ensureTransactionLog();
		transactions.add(new TransactionRecord(kind, amount, balanceAfter, System.currentTimeMillis()));
	}
	
	
	public BankAccount(String name, double balance, double min_balance) throws Exception {
    if (balance < min_balance) {
        throw new Exception("Initial balance cannot be less than the minimum required balance: " + min_balance);
    }
    this.name = name;
    this.balance = balance;
    this.min_balance = min_balance;
    this.acc_num = 10000 + (int) (Math.random() * 89999) + "";
    logTx(TransactionRecord.Kind.OPENING, balance, balance);
}


	public void deposit(double amount) throws InvalidAmount
	{
		if (amount <= 0){
			throw new InvalidAmount("Deposit amount must be greater than zero.");
		}
		ensureLegacyBaselineIfNeeded();
		balance+=amount;
		logTx(TransactionRecord.Kind.DEPOSIT, amount, balance);
	}
	
	public void withdraw(double amount) throws MaxWithdraw, MaxBalance
	{
		if ((balance - amount) >= min_balance)
		{
			ensureLegacyBaselineIfNeeded();
			balance -= amount;
			logTx(TransactionRecord.Kind.WITHDRAW, amount, balance);
		}
		else
		{
			double maxYouCanWithdraw = Math.max(0, balance - min_balance);
			String msg = String.format(
					"To keep your account above the minimum balance required (%.2f), you cannot withdraw that much right now.\n\n"
							+ "• Your current balance: %.2f\n"
							+ "• Largest withdrawal allowed now: %.2f\n\n"
							+ "Try a smaller amount, or deposit first if you need access to more funds.",
					min_balance, balance, maxYouCanWithdraw);
			throw new MaxBalance(msg);
		}
	}
	
	public double getbalance()
	{
		return balance;
	}

	public String getName()
	{
		return name;
	}

	public String getAccNum()
	{
		return acc_num;
	}

	/**
	 * User-wise transaction history for this account (newest last).
	 */
	public List<TransactionRecord> getTransactionHistory() {
		ensureTransactionLog();
		ensureLegacyBaselineIfNeeded();
		return Collections.unmodifiableList(transactions);
	}
	
	@Override
	public String toString() {
		return "Name: " + name + ", Id: " + acc_num + ", Balance: " + balance + ", Type: " + this.getClass().getSimpleName();
	}
}
