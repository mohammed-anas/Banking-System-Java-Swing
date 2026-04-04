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

public class AddStudentAccount extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JLabel lblHead;
	private JLabel lblName;
	private JLabel lblBalance;
	private JLabel lblInst;
	private JButton btnAdd;
	private JButton btnReset;

	public AddStudentAccount() {
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		contentPane = new JPanel(new GridBagLayout());
		setContentPane(contentPane);
		UITheme.styleFormSurface(this, contentPane, "New student account");

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(6, 10, 6, 10);
		gbc.anchor = GridBagConstraints.WEST;

		lblHead = new JLabel("Student account");
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

		lblInst = new JLabel("Institution name:");
		UITheme.styleFormLabel(lblInst);
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.NONE;
		contentPane.add(lblInst, gbc);

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
					String name = textField.getText().trim();
					String insname = textField_2.getText().trim();
					String nameErr = ClientSafeInput.validatePersonName(name);
					if (nameErr != null) {
						DialogAlerts.showMessage(AddStudentAccount.this, nameErr, "Invalid name",
								JOptionPane.WARNING_MESSAGE);
						return;
					}
					if (name.isEmpty() || insname.isEmpty()) {
						DialogAlerts.showMessage(AddStudentAccount.this,
								"Please enter the student name and institution name.",
								"Missing information", JOptionPane.WARNING_MESSAGE);
						return;
					}
					if (insname.length() > 120) {
						DialogAlerts.showMessage(AddStudentAccount.this,
								"Institution name is too long (120 characters maximum).",
								"Invalid institution", JOptionPane.WARNING_MESSAGE);
						return;
					}
					String balRaw = ClientSafeInput.normalizeDecimalInput(textField_1.getText());
					if (balRaw.isEmpty()) {
						DialogAlerts.showMessage(AddStudentAccount.this, "Please enter the opening balance.",
								"Balance required", JOptionPane.WARNING_MESSAGE);
						return;
					}
					double bal = Double.parseDouble(balRaw);
					String balOk = ClientSafeInput.validateOpeningBalance(bal);
					if (balOk != null) {
						DialogAlerts.showMessage(AddStudentAccount.this, balOk, "Invalid balance",
								JOptionPane.WARNING_MESSAGE);
						return;
					}
					if (bal < 100) {
						DialogAlerts.showMessage(AddStudentAccount.this,
								"A student account must open with at least 100.00 in balance.",
								"Minimum balance", JOptionPane.WARNING_MESSAGE);
						return;
					}
					if (bal <= 0) {
						DialogAlerts.showMessage(AddStudentAccount.this,
								"Opening balance must be greater than zero.",
								"Invalid balance", JOptionPane.WARNING_MESSAGE);
						return;
					}
					String holderDisplay = ClientSafeInput.maskHolderName(name);
					String insShort = insname.length() > 24 ? insname.substring(0, 21) + "…" : insname;
					String confirmMsg = String.format(
							"Open this student account?\n\n"
									+ "• Student: %s\n"
									+ "• Institution: %s\n"
									+ "• Opening balance: %.2f",
							holderDisplay, insShort, bal);
					int ch = DialogAlerts.showConfirm(AddStudentAccount.this, confirmMsg, "Confirm new account",
							JOptionPane.YES_NO_OPTION);
					if (ch != JOptionPane.YES_OPTION) {
						return;
					}
					int index = FileIO.bank.addAccount(name, insname, bal, 100);
					String accNum = FileIO.bank.getAccounts()[index].getAccNum();
					DialogAlerts.showMessage(AddStudentAccount.this,
							String.format(
									"The student account was created.\n\n"
											+ "Account Id: %s\n"
											+ "Name on account: %s\n\n"
											+ "Use this Id for deposits and withdrawals.",
									accNum, holderDisplay) + ClientSafeInput.receiptPrivacyFooter(),
							"Account opened", JOptionPane.INFORMATION_MESSAGE);
					GUIForm.UpdateDisplay();
					textField.setText(null);
					textField_1.setText(null);
					textField_2.setText(null);
					setVisible(false);
				} catch (NumberFormatException ex) {
					DialogAlerts.showMessage(AddStudentAccount.this,
							"Opening balance must be a valid number (for example 500).",
							"Invalid input", JOptionPane.ERROR_MESSAGE);
				} catch (IllegalStateException ex) {
					DialogAlerts.showMessage(AddStudentAccount.this,
							"This bank already holds the maximum number of accounts.",
							"Limit reached", JOptionPane.WARNING_MESSAGE);
				} catch (Exception ex) {
					String detail = ex.getMessage();
					if (detail != null && detail.toLowerCase().contains("minimum")) {
						DialogAlerts.showMessage(AddStudentAccount.this, detail, "Cannot open account",
								JOptionPane.WARNING_MESSAGE);
					} else {
						DialogAlerts.showMessage(AddStudentAccount.this,
								"We could not open this account. Check all fields and try again.",
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
		UITheme.styleFormLabel(lblInst);
		UITheme.styleFormTextField(textField);
		UITheme.styleFormTextField(textField_1);
		UITheme.styleFormTextField(textField_2);
		UITheme.stylePrimaryButton(btnAdd);
		UITheme.styleSecondaryButton(btnReset);
	}
}
