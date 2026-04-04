package GUI;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import Bank.BankAccount;
import Data.FileIO;
import Exceptions.AccNotFound;
import Exceptions.InvalidAmount;
import Exceptions.MaxBalance;
import Exceptions.MaxWithdraw;

public class WithdrawAcc extends JFrame implements Serializable {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JLabel lblHead;
	private JLabel lblName;
	private JLabel lblAmount;
	private JButton btnWithdraw;
	private JButton btnReset;

	public WithdrawAcc() {
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		contentPane = new JPanel(new GridBagLayout());
		setContentPane(contentPane);
		UITheme.styleFormSurface(this, contentPane, "Withdraw");

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(6, 10, 6, 10);
		gbc.anchor = GridBagConstraints.WEST;

		lblHead = new JLabel("Withdraw");
		UITheme.styleHeadlineLabel(lblHead);
		lblHead.setHorizontalAlignment(SwingConstants.CENTER);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.weightx = 1;
		contentPane.add(lblHead, gbc);

		lblName = new JLabel("Account no. (Id):");
		UITheme.styleFormLabel(lblName);
		gbc.gridy++;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridx = 0;
		contentPane.add(lblName, gbc);

		textField = new JTextField();
		textField.setFont(UITheme.fontBody());
		UITheme.styleFormTextField(textField);
		gbc.gridx = 1;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		contentPane.add(textField, gbc);

		lblAmount = new JLabel("Amount:");
		UITheme.styleFormLabel(lblAmount);
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.NONE;
		contentPane.add(lblAmount, gbc);

		textField_1 = new JTextField();
		textField_1.setFont(UITheme.fontBody());
		UITheme.styleFormTextField(textField_1);
		gbc.gridx = 1;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		contentPane.add(textField_1, gbc);

		btnWithdraw = new JButton("Withdraw");
		UITheme.stylePrimaryButton(btnWithdraw);
		btnWithdraw.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String aacountNum = textField.getText().trim();
				String amountRaw = ClientSafeInput.normalizeDecimalInput(textField_1.getText());
				try {
					if (aacountNum.isEmpty()) {
						DialogAlerts.showMessage(WithdrawAcc.this,
								"Please enter your account number (the Id for your account).\n\n"
										+ "You can find it in \"Display Account List\" or in the message when the account was opened.",
								"Account number required", JOptionPane.WARNING_MESSAGE);
						return;
					}
					String idErr = ClientSafeInput.validateAccountIdFormat(aacountNum);
					if (idErr != null) {
						DialogAlerts.showMessage(WithdrawAcc.this, idErr, "Invalid account number",
								JOptionPane.WARNING_MESSAGE);
						return;
					}
					if (amountRaw.isEmpty()) {
						DialogAlerts.showMessage(WithdrawAcc.this,
								"Please enter how much money you want to withdraw.\n\n"
										+ "The amount must be a number greater than zero.",
								"Amount required", JOptionPane.WARNING_MESSAGE);
						return;
					}
					double amt = Double.parseDouble(amountRaw);
					if (amt <= 0) {
						DialogAlerts.showMessage(WithdrawAcc.this,
								"Withdrawal amount must be greater than zero.\n\n"
										+ "If you do not want to withdraw anything, close this window or click Reset.",
								"Invalid amount", JOptionPane.WARNING_MESSAGE);
						return;
					}
					String amtErr = ClientSafeInput.validatePositiveTransactionAmount(amt);
					if (amtErr != null) {
						DialogAlerts.showMessage(WithdrawAcc.this, amtErr, "Amount not allowed",
								JOptionPane.WARNING_MESSAGE);
						return;
					}
					BankAccount acc = FileIO.bank.findAccount(aacountNum);
					if (acc == null) {
						DialogAlerts.showMessage(WithdrawAcc.this,
								"No account matches the number you entered.\n\n"
										+ "Check each digit carefully or open \"Display Account List\" to copy the Id.",
								"Account not found", JOptionPane.WARNING_MESSAGE);
						return;
					}
					double balanceBefore = acc.getbalance();
					String holderDisplay = ClientSafeInput.maskHolderName(acc.getName());
					String confirmMsg = String.format(
							"Please confirm this withdrawal:\n\n"
									+ "• Account Id: %s\n"
									+ "• Name on account: %s\n"
									+ "• Amount to withdraw: %.2f\n"
									+ "• Balance before: %.2f\n\n"
									+ "If allowed by your account rules, your new balance would be %.2f.\n"
									+ "(Minimum balance and per-withdrawal limits are checked when you confirm.)",
							aacountNum, holderDisplay, amt, balanceBefore, balanceBefore - amt);
					int a = DialogAlerts.showConfirm(WithdrawAcc.this, confirmMsg, "Confirm withdrawal",
							JOptionPane.YES_NO_OPTION);
					if (a != JOptionPane.YES_OPTION) {
						return;
					}
					FileIO.bank.withdraw(aacountNum, amt);
					double balanceAfter = FileIO.bank.findAccount(aacountNum).getbalance();
					String successMsg = String.format(
							"Your withdrawal was completed successfully.\n\n"
									+ "Account Id: %s\n"
									+ "Name on account: %s\n\n"
									+ "Withdrawn this time: %.2f\n"
									+ "Balance before: %.2f\n"
									+ "New balance: %.2f\n\n"
									+ "Further withdrawals are allowed from the main menu if they meet your account rules.",
							aacountNum, holderDisplay, amt, balanceBefore, balanceAfter)
							+ ClientSafeInput.receiptPrivacyFooter();
					DialogAlerts.showMessage(WithdrawAcc.this, successMsg, "Withdrawal completed",
							JOptionPane.INFORMATION_MESSAGE);
					GUIForm.UpdateDisplay();
					textField.setText(null);
					textField_1.setText(null);
					setVisible(false);
				} catch (NumberFormatException ex) {
					DialogAlerts.showMessage(WithdrawAcc.this,
							"The amount is not a valid number.\n\n"
									+ "Use digits and at most one decimal point (for example 100 or 250.50).",
							"Invalid amount", JOptionPane.ERROR_MESSAGE);
				} catch (MaxBalance e1) {
					DialogAlerts.showMessage(WithdrawAcc.this, e1.getMessage(),
							"Withdrawal not allowed — balance", JOptionPane.WARNING_MESSAGE);
				} catch (AccNotFound e1) {
					DialogAlerts.showMessage(WithdrawAcc.this,
							"No account matches the number you entered.\n\n"
									+ "Check the Id or open \"Display Account List\".",
							"Account not found", JOptionPane.WARNING_MESSAGE);
				} catch (MaxWithdraw e1) {
					DialogAlerts.showMessage(WithdrawAcc.this, e1.getMessage(),
							"Withdrawal limit exceeded", JOptionPane.WARNING_MESSAGE);
				} catch (InvalidAmount e1) {
					DialogAlerts.showMessage(WithdrawAcc.this,
							e1.getMessage() + "\n\nEnter a number greater than zero for the withdrawal.",
							"Invalid amount", JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		btnReset = new JButton("Reset");
		UITheme.styleSecondaryButton(btnReset);
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textField.setText(null);
				textField_1.setText(null);
			}
		});

		JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 8));
		btnRow.setOpaque(false);
		btnRow.add(btnWithdraw);
		btnRow.add(btnReset);
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.gridwidth = 2;
		gbc.weightx = 0;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.SOUTH;
		contentPane.add(btnRow, gbc);

		pack();
		setMinimumSize(getPreferredSize());
		setLocationRelativeTo(null);
	}

	void applyThemeColors() {
		contentPane.setBackground(UITheme.PAGE_BG);
		UITheme.styleHeadlineLabel(lblHead);
		UITheme.styleFormLabel(lblName);
		UITheme.styleFormLabel(lblAmount);
		UITheme.styleFormTextField(textField);
		UITheme.styleFormTextField(textField_1);
		UITheme.stylePrimaryButton(btnWithdraw);
		UITheme.styleSecondaryButton(btnReset);
	}
}
