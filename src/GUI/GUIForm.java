package GUI;

import java.awt.Component;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class GUIForm {

	public static Login login= new Login();
	public static Menu menu= new Menu();
	public static AddAccount addaccount= new AddAccount();
	public static AddCurrentAccount addcurrentacc= new AddCurrentAccount();
	public static AddSavingsAccount addsavingsaccount = new AddSavingsAccount();
	public static AddStudentAccount addstudentaccount = new AddStudentAccount();
	public static DisplayList displaylist= new DisplayList();
	public static DepositAcc depositacc= new DepositAcc();
	public static WithdrawAcc withdraw = new WithdrawAcc();
	public static TransactionReport transactionReport = new TransactionReport();
	
	public static void UpdateDisplay()
	{
		
		if(displaylist.isVisible())
		{
			Point O= displaylist.getLocation();
			displaylist.dispose();
			displaylist = new DisplayList();
			displaylist.setVisible(true);
			displaylist.setLocation(O);;
		}
		
		else {
			displaylist.dispose();
			displaylist = new DisplayList();
		}
		
	}

	/** After theme change: re-tint chrome and repaint tool windows. */
	public static void reapplyThemeAcrossApp() {
		if (menu != null) {
			menu.reapplyChromeAfterTheme();
		}
		if (login != null) {
			login.reapplyChromeAfterTheme();
		}
		JFrame[] tools = { addaccount, addcurrentacc, addsavingsaccount, addstudentaccount, displaylist, depositacc,
				withdraw, transactionReport };
		for (JFrame f : tools) {
			if (f == null) {
				continue;
			}
			Component cp = f.getContentPane();
			if (cp instanceof JPanel) {
				((JPanel) cp).setBackground(UITheme.PAGE_BG);
			}
			SwingUtilities.updateComponentTreeUI(f);
		}
		if (addaccount != null) {
			addaccount.applyThemeColors();
		}
		if (addcurrentacc != null) {
			addcurrentacc.applyThemeColors();
		}
		if (addsavingsaccount != null) {
			addsavingsaccount.applyThemeColors();
		}
		if (addstudentaccount != null) {
			addstudentaccount.applyThemeColors();
		}
		if (displaylist != null) {
			displaylist.applyThemeColors();
		}
		if (depositacc != null) {
			depositacc.applyThemeColors();
		}
		if (withdraw != null) {
			withdraw.applyThemeColors();
		}
		if (transactionReport != null) {
			transactionReport.applyThemeColors();
		}
	}

}
