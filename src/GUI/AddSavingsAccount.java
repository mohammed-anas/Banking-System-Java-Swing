package GUI;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import Data.FileIO;

public class AddSavingsAccount extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JLabel lblHead;
	private JLabel lblName;
	private JLabel lblBalance;
	private JLabel lblMax;
	private JButton btnAdd;
	private JButton btnReset;

	public AddSavingsAccount() {
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		contentPane = new JPanel(new GridBagLayout());
		setContentPane(contentPane);
		UITheme.styleFormSurface(this, contentPane, "New savings account");

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(6, 10, 6, 10);
		gbc.anchor = GridBagConstraints.WEST;

		lblHead = new JLabel("Savings account");
		UITheme.styleHeadlineLabel(lblHead);
		lblHead.setHorizontalAlignment(SwingConstants.CENTER);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.weightx = 1;
		contentPane.add(lblHead, gbc);

		lblName = new JLabel("Name:");
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

		lblBalance = new JLabel("Balance:");
		UITheme.styleFormLabel(lblBalance);
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.NONE;
		contentPane.add(lblBalance, gbc);

		textField_1 = new JTextField();
		textField_1.setFont(UITheme.fontBody());
		UITheme.styleFormTextField(textField_1);
		gbc.gridx = 1;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		contentPane.add(textField_1, gbc);

		lblMax = new JLabel("Maximum withdraw limit:");
		UITheme.styleFormLabel(lblMax);
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.NONE;
		contentPane.add(lblMax, gbc);

		textField_2 = new JTextField();
		textField_2.setFont(UITheme.fontBody());
		UITheme.styleFormTextField(textField_2);
		gbc.gridx = 1;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		contentPane.add(textField_2, gbc);

		btnAdd = new JButton("Create account");
		UITheme.stylePrimaryButton(btnAdd);
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String nameErr = ClientSafeInput.validatePersonName(textField.getText().trim());
					if (nameErr != null) {
						DialogAlerts.showMessage(AddSavingsAccount.this, nameErr, "Invalid name",
								JOptionPane.WARNING_MESSAGE);
						return;
					}
					String name = textField.getText().trim();
					if (name.isEmpty()) {
						DialogAlerts.showMessage(AddSavingsAccount.this, "Please enter the customer name.",
								"Name required", JOptionPane.WARNING_MESSAGE);
						return;
					}
					String balRaw = ClientSafeInput.normalizeDecimalInput(textField_1.getText());
					String maxwRaw = ClientSafeInput.normalizeDecimalInput(textField_2.getText());
					if (balRaw.isEmpty() || maxwRaw.isEmpty()) {
						DialogAlerts.showMessage(AddSavingsAccount.this,
								"Please enter both opening balance and maximum withdrawal limit (per transaction).",
								"Missing numbers", JOptionPane.WARNING_MESSAGE);
						return;
					}
					double bal = Double.parseDouble(balRaw);
					double maxw = Double.parseDouble(maxwRaw);
					String balOk = ClientSafeInput.validateOpeningBalance(bal);
					if (balOk != null) {
						DialogAlerts.showMessage(AddSavingsAccount.this, balOk, "Invalid balance",
								JOptionPane.WARNING_MESSAGE);
						return;
					}
					String limErr = ClientSafeInput.validateWithdrawLimitField(maxw);
					if (limErr != null) {
						DialogAlerts.showMessage(AddSavingsAccount.this, limErr, "Invalid limit",
								JOptionPane.WARNING_MESSAGE);
						return;
					}
					if (bal < 2000) {
						DialogAlerts.showMessage(AddSavingsAccount.this,
								"A savings account here must start with at least 2,000.00 in balance.",
								"Minimum balance", JOptionPane.WARNING_MESSAGE);
						return;
					}
					if (bal <= 0 || maxw <= 0) {
						DialogAlerts.showMessage(AddSavingsAccount.this,
								"Balance and maximum withdraw limit must be greater than zero.",
								"Invalid values", JOptionPane.WARNING_MESSAGE);
						return;
					}
					String holderDisplay = ClientSafeInput.maskHolderName(name);
					String confirmMsg = String.format(
							"Open this savings account?\n\n"
									+ "• Customer: %s\n"
									+ "• Opening balance: %.2f\n"
									+ "• Max withdrawal per transaction: %.2f\n\n"
									+ "You will receive a new 5-digit account Id after confirming.",
							holderDisplay, bal, maxw);
					int ch = DialogAlerts.showConfirm(AddSavingsAccount.this, confirmMsg, "Confirm new account",
							JOptionPane.YES_NO_OPTION);
					if (ch != JOptionPane.YES_OPTION) {
						return;
					}
					int index = FileIO.bank.addAccount(name, bal, maxw);
					String accNum = FileIO.bank.getAccounts()[index].getAccNum();
					DialogAlerts.showMessage(AddSavingsAccount.this,
							String.format(
									"The savings account was created.\n\n"
											+ "Account Id: %s\n"
											+ "Name on account: %s\n\n"
											+ "Use this Id for deposits and withdrawals.\n"
											+ "Write it down and store it securely.",
									accNum, holderDisplay) + ClientSafeInput.receiptPrivacyFooter(),
							"Account opened", JOptionPane.INFORMATION_MESSAGE);
					GUIForm.UpdateDisplay();
					textField.setText(null);
					textField_1.setText(null);
					textField_2.setText(null);
					setVisible(false);
				} catch (NumberFormatException ex) {
					DialogAlerts.showMessage(AddSavingsAccount.this,
							"Balance and withdrawal limit must be valid numbers (for example 5000 and 2000).",
							"Invalid input", JOptionPane.ERROR_MESSAGE);
				} catch (IllegalStateException ex) {
					DialogAlerts.showMessage(AddSavingsAccount.this,
							"This bank already holds the maximum number of accounts.\n"
									+ "No new account can be opened until the system is reset.",
							"Limit reached", JOptionPane.WARNING_MESSAGE);
				} catch (Exception ex) {
					String detail = ex.getMessage();
					if (detail != null && detail.toLowerCase().contains("minimum")) {
						DialogAlerts.showMessage(AddSavingsAccount.this, detail, "Cannot open account",
								JOptionPane.WARNING_MESSAGE);
					} else {
						DialogAlerts.showMessage(AddSavingsAccount.this,
								"We could not open this account with the details provided.\n\n"
										+ "Check all fields and try again.",
								"Something went wrong", JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});

		btnReset = new JButton("Reset");
		UITheme.styleSecondaryButton(btnReset);
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textField.setText(null);
				textField_1.setText(null);
				textField_2.setText(null);
			}
		});

		JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 8));
		btnRow.setOpaque(false);
		btnRow.add(btnAdd);
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
		UITheme.styleFormLabel(lblBalance);
		UITheme.styleFormLabel(lblMax);
		UITheme.styleFormTextField(textField);
		UITheme.styleFormTextField(textField_1);
		UITheme.styleFormTextField(textField_2);
		UITheme.stylePrimaryButton(btnAdd);
		UITheme.styleSecondaryButton(btnReset);
	}
}
