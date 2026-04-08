package Bank;
import java.io.Serializable;
import javax.swing.DefaultListModel;
import Exceptions.AccNotFound;
import Exceptions.InvalidAmount;
import Exceptions.MaxBalance;
import Exceptions.MaxWithdraw;

public class Bank implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BankAccount[] accounts= new BankAccount[100];
	public int addAccount(BankAccount acc)
	{
		int i=0;
		for(i=0;i<100;i++)
		{
			if(getAccounts()[i]==null)
			{
				break;
			}
		}
		if (i >= 100)
		{
			throw new IllegalStateException("Maximum number of accounts (100) reached");
		}
		getAccounts()[i]=acc;
		return i;
	}
	
	public int addAccount(String name, double balance, double maxWithLimit ) throws Exception
	{
		SavingsAccount acc=new SavingsAccount(name, balance, maxWithLimit);
		return this.addAccount(acc);
	}
	
	public int addAccount(String name, double balance, String tradeLicense) throws Exception
	{
		CurrentAccount acc = new CurrentAccount(name, balance,tradeLicense);
		return this.addAccount(acc);
	}
	
	public int addAccount(String name, String  institutionName, double balance, double min_balance) throws Exception
	{
		StudentAccount acc= new StudentAccount(name,balance,institutionName);
		return this.addAccount(acc);
	}
	
	public BankAccount findAccount(String aacountNum)
	{
		if (aacountNum == null) {
			return null;
		}
		String key = aacountNum.trim();
		int i;
		for(i=0;i<100;i++)
		{
			if(getAccounts()[i]==null)
			{
				break;
			}
			String num = getAccounts()[i].getAccNum();
			if (num != null && num.equals(key))
			{
				return getAccounts()[i];
			}
			
		}
		return null;
	}
	
	public void deposit(String aacountNum, double amt) throws InvalidAmount,AccNotFound
	
	{
		if(amt<0)
		{
			throw new InvalidAmount("Invalid Deposit amount");
		}
		BankAccount temp=findAccount(aacountNum);
		if(temp==null)
		{
			throw new AccNotFound("Account Not Found");
		}
		if(temp!=null)
		{
			temp.deposit(amt);
			
		}
		
	}
	
	
	/**
	 * Changes the per-transaction withdrawal cap for a savings or student account.
	 * Current accounts are not limited per transaction and cannot use this operation.
	 */
	public void setMaxWithdrawPerTransaction(String accountNum, double newLimit) throws AccNotFound {
		if (newLimit <= 0) {
			throw new IllegalArgumentException("Maximum withdrawal per transaction must be greater than zero.");
		}
		BankAccount acc = findAccount(accountNum);
		if (acc == null) {
			throw new AccNotFound("Account Not Found");
		}
		if (!(acc instanceof SavingsAccount)) {
			throw new IllegalArgumentException(
					"Only savings and student accounts have a per-transaction withdrawal limit. Current accounts are not limited this way.");
		}
		((SavingsAccount) acc).setMaxWithdrawLimit(newLimit);
	}

	public void withdraw(String aacountNum, double amt) throws MaxBalance,AccNotFound, MaxWithdraw, InvalidAmount
	{
		BankAccount temp=findAccount(aacountNum);
		
		if(temp==null)
		{
			throw new AccNotFound("Account Not Found");
		}
		
		if(amt<=0)
		{
			throw new InvalidAmount("Invalid Amount");
		}
		
		if (amt > temp.getbalance())
		{
			throw new MaxBalance(String.format(
					"The amount you want to withdraw (%.2f) is higher than your current balance (%.2f).\n\n"
							+ "You can withdraw at most %.2f. Please enter a smaller amount.",
					amt, temp.getbalance(), temp.getbalance()));
		}
		if(temp!=null)
		{
			temp.withdraw(amt);
		}
	}
	
	public DefaultListModel<String> display()
	{
		DefaultListModel<String> list=new DefaultListModel<String>();
		int i;
//		String type=null;
	
		for(i=0;i<100;i++)
		{
			if(getAccounts()[i]==null)
			{
				break;
			}
//			if(getAccounts()[i].getClass().toString().equals("class Bank.SavingsAccount"))
//			{
//				type="Type: Savings Account";
//			}
//			
//			else if(getAccounts()[i].getClass().toString().equals("class Bank.CurrentAccount"))
//			{
//				type="Type: Current Account";
//			}
//			
//			else if(getAccounts()[i].getClass().toString().equals("class Bank.StudentAccount"))
//			{
//				type="Type: Student Account";
//			}
			
			list.addElement(getAccounts()[i].toString());
			
			
		}
		
		return list;
	}

	public BankAccount[] getAccounts() {
		return accounts;
	}

	public void setAccounts(BankAccount[] accounts) {
		this.accounts = accounts;
	}
	
}
