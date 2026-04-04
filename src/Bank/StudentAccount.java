package Bank;

public class StudentAccount extends SavingsAccount {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String institutionName;

	public StudentAccount(String name, double balance ,String  institutionName) throws Exception {
		super(name, balance, 20000, 100);
		this.institutionName = institutionName;
	}

	@Override
	public String toString() {
		return super.toString() + ", Institution: " + institutionName;
	}


}
