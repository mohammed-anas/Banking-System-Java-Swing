package Bank;

import java.io.Serializable;

/**
 * One line in a customer account's transaction history.
 */
public class TransactionRecord implements Serializable {

	private static final long serialVersionUID = 1L;

	public enum Kind {
		/** Set when the account was opened (normal path). */
		OPENING,
		DEPOSIT,
		WITHDRAW,
		/**
		 * Carried balance for accounts saved before transaction logging existed, or with no stored lines yet.
		 * Not a real movement—only documents the balance on file when the log was first used.
		 */
		LEGACY_BASELINE
	}

	private final Kind kind;
	private final double amount;
	private final double balanceAfter;
	private final long timestampMs;

	public TransactionRecord(Kind kind, double amount, double balanceAfter, long timestampMs) {
		this.kind = kind;
		this.amount = amount;
		this.balanceAfter = balanceAfter;
		this.timestampMs = timestampMs;
	}

	public Kind getKind() {
		return kind;
	}

	public double getAmount() {
		return amount;
	}

	public double getBalanceAfter() {
		return balanceAfter;
	}

	public long getTimestampMs() {
		return timestampMs;
	}
}
