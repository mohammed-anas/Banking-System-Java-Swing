package Bank;
import Exceptions.MaxBalance;
import Exceptions.MaxWithdraw;

public class SavingsAccount extends BankAccount {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	float rate= .05f;
	double maxWithLimit;
//	String type;
	
	protected SavingsAccount(String name, double balance, double maxWithLimit, double minBalance) throws Exception {
		super(name, balance, minBalance);
		this.maxWithLimit = maxWithLimit;
	}

	public SavingsAccount(String name, double balance, double maxWithLimit) throws Exception {
		this(name, balance, maxWithLimit, 2000);
	}

	/** Cap for a single withdrawal transaction (savings and student accounts). */
	public double getMaxWithdrawLimit() {
		return maxWithLimit;
	}

	public void setMaxWithdrawLimit(double maxWithLimit) {
		if (maxWithLimit <= 0) {
			throw new IllegalArgumentException("Maximum withdrawal per transaction must be greater than zero.");
		}
		this.maxWithLimit = maxWithLimit;
	}

	public double getNetBalance()
	{
		double NetBalance= getbalance()+(getbalance()*rate);
		return NetBalance;
	}
	
	public void withdraw(double amount) throws MaxWithdraw, MaxBalance
	{
		if(amount <= maxWithLimit)
		{
			super.withdraw(amount);
			
		}
		else
		{
			String msg = String.format(
					"On this account, each withdrawal is limited to %.2f per transaction (the maximum set for this account).\n\n"
							+ "You tried to withdraw %.2f, which is above that limit.\n\n"
							+ "Please withdraw %.2f or less, or make several smaller withdrawals if your bank rules allow it.",
					maxWithLimit, amount, maxWithLimit);
			throw new MaxWithdraw(msg);
		}
		
	}
	
	
}
