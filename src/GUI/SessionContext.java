package GUI;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Per-login session stats for the dashboard (not persisted).
 */
public final class SessionContext {

	private static final SimpleDateFormat FMT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static long sessionStartMs;
	private static double sessionDepositTotal;

	private SessionContext() {
	}

	public static void startNewSession() {
		sessionStartMs = System.currentTimeMillis();
		sessionDepositTotal = 0;
	}

	public static void recordDeposit(double amount) {
		if (amount > 0) {
			sessionDepositTotal += amount;
		}
	}

	public static long getSessionStartMs() {
		return sessionStartMs;
	}

	public static String getSessionStartedLine() {
		return "Session started: " + FMT.format(new Date(sessionStartMs));
	}

	public static double getSessionDepositTotal() {
		return sessionDepositTotal;
	}
}
