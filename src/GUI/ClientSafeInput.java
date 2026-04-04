package GUI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Shared checks and privacy-minded formatting for anything shown to the user.
 */
public final class ClientSafeInput {

	private static final Pattern ID_IN_LINE = Pattern.compile("Id:\\s*(\\d{5})\\b", Pattern.CASE_INSENSITIVE);

	private ClientSafeInput() {
	}

	/** Upper bound per single deposit / withdrawal (demo safety). */
	public static final double MAX_TRANSACTION_AMOUNT = 1_000_000_000d;

	/** Upper bound when opening an account. */
	public static final double MAX_OPENING_BALANCE = MAX_TRANSACTION_AMOUNT;

	public static String normalizeDecimalInput(String raw) {
		if (raw == null) {
			return "";
		}
		return raw.trim().replace(",", "");
	}

	/**
	 * @return null if the amount is acceptable for a deposit/withdrawal (positive finite, within cap); otherwise a full user message.
	 */
	public static String validatePositiveTransactionAmount(double amt) {
		if (!Double.isFinite(amt)) {
			return "The amount is not a valid number for this transaction.\n\n"
					+ "Use a plain number (for example 100 or 250.50).";
		}
		if (amt <= 0) {
			return null;
		}
		if (amt > MAX_TRANSACTION_AMOUNT) {
			return String.format(
					"For this system, a single transaction cannot be more than %.0f.\n\n"
							+ "Enter a smaller amount, or split it into more than one transaction if your bank rules allow.",
					MAX_TRANSACTION_AMOUNT);
		}
		return null;
	}

	/**
	 * @return null if valid; otherwise user message.
	 */
	public static String validateAccountIdFormat(String id) {
		if (id == null || id.isEmpty()) {
			return null;
		}
		if (!id.matches("[0-9]{5}")) {
			return "Account numbers must be exactly 5 digits (numbers only), for example 48291.\n\n"
					+ "Use the Id from \"Display Account List\" or from when the account was opened.";
		}
		return null;
	}

	/**
	 * Accepts plain {@code 48291}, optional spaces, or a pasted list line containing {@code Id: 48291}.
	 * Avoids mistaking a 5-digit balance for the Id when the line format is used.
	 *
	 * @return normalized 5-digit id, or null if none can be read
	 */
	public static String parseAccountIdFromUserInput(String raw) {
		if (raw == null) {
			return null;
		}
		String t = raw.trim();
		if (t.isEmpty()) {
			return null;
		}
		Matcher m = ID_IN_LINE.matcher(t);
		if (m.find()) {
			return m.group(1);
		}
		if (t.matches("[0-9]{5}")) {
			return t;
		}
		String noSpace = t.replaceAll("\\s+", "");
		if (noSpace.matches("[0-9]{5}")) {
			return noSpace;
		}
		return null;
	}

	public static String validateOpeningBalance(double bal) {
		if (!Double.isFinite(bal)) {
			return "Enter a valid number for the balance.";
		}
		if (bal > MAX_OPENING_BALANCE) {
			return String.format("Opening balance cannot exceed %.0f in this demo.", MAX_OPENING_BALANCE);
		}
		return null;
	}

	public static String validateWithdrawLimitField(double maxw) {
		if (!Double.isFinite(maxw)) {
			return "Enter a valid number for the maximum withdrawal limit.";
		}
		if (maxw > MAX_TRANSACTION_AMOUNT) {
			return String.format("Maximum withdrawal limit cannot exceed %.0f in this demo.", MAX_TRANSACTION_AMOUNT);
		}
		return null;
	}

	public static String validatePersonName(String name) {
		if (name == null || name.trim().isEmpty()) {
			return null;
		}
		if (name.length() > 120) {
			return "Please use a shorter name (120 characters or less).";
		}
		return null;
	}

	/**
	 * Shorter display form for on-screen receipts (not full legal name).
	 */
	public static String maskHolderName(String fullName) {
		if (fullName == null || fullName.trim().isEmpty()) {
			return "Customer";
		}
		String[] p = fullName.trim().split("\\s+");
		if (p.length == 1) {
			return p[0];
		}
		String last = p[p.length - 1];
		char li = last.length() > 0 ? last.charAt(0) : '?';
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < p.length - 1; i++) {
			if (i > 0) {
				sb.append(' ');
			}
			sb.append(p[i]);
		}
		sb.append(' ').append(Character.toUpperCase(li)).append('.');
		return sb.toString();
	}

	/**
	 * Mask licence or reference numbers in messages (show last digits only).
	 */
	public static String maskTailIdentifier(String value, int tailLen) {
		if (value == null || value.trim().isEmpty()) {
			return "";
		}
		String v = value.trim();
		if (v.length() <= tailLen) {
			return v;
		}
		return "…" + v.substring(v.length() - tailLen);
	}

	public static String receiptPrivacyFooter() {
		return "\n\nPrivacy: Treat this like a bank receipt. Do not share screenshots or printouts in public.";
	}
}
