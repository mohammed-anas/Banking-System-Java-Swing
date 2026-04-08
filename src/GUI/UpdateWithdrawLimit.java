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

import Bank.BankAccount;
import Bank.SavingsAccount;
import Data.FileIO;
import Exceptions.AccNotFound;

/**
 * Changes the per-transaction withdrawal cap for savings and student accounts after opening.
 */
public class UpdateWithdrawLimit extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldId;
	private JTextField textFieldLimit;
	private JLabel lblHead;
	private JLabel lblId;
	private JLabel lblLimit;
	private JButton btnApply;
	private JButton btnReset;

	public UpdateWithdrawLimit() {
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		contentPane = new JPanel(new GridBagLayout());
		setContentPane(contentPane);
		UITheme.styleFormSurface(this, contentPane, "Withdrawal limit");

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(6, 10, 6, 10);
		gbc.anchor = GridBagConstraints.WEST;

		lblHead = new JLabel("Update withdrawal limit");
		UITheme.styleHeadlineLabel(lblHead);
		lblHead.setHorizontalAlignment(SwingConstants.CENTER);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.weightx = 1;
		contentPane.add(lblHead, gbc);

		lblId = new JLabel("Account no. (Id):");
		UITheme.styleFormLabel(lblId);
		gbc.gridy++;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridx = 0;
		contentPane.add(lblId, gbc);

		textFieldId = new JTextField();
		textFieldId.setFont(UITheme.fontBody());
		UITheme.styleFormTextField(textFieldId);
		gbc.gridx = 1;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		contentPane.add(textFieldId, gbc);

		lblLimit = new JLabel("New max per transaction:");
		UITheme.styleFormLabel(lblLimit);
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.NONE;
		contentPane.add(lblLimit, gbc);

		textFieldLimit = new JTextField();
		textFieldLimit.setFont(UITheme.fontBody());
		UITheme.styleFormTextField(textFieldLimit);
		gbc.gridx = 1;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		contentPane.add(textFieldLimit, gbc);

		btnApply = new JButton("Update limit");
		UITheme.stylePrimaryButton(btnApply);
		btnApply.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String idRaw = textFieldId.getText().trim();
				String limitRaw = ClientSafeInput.normalizeDecimalInput(textFieldLimit.getText());
				try {
					if (idRaw.isEmpty()) {
						DialogAlerts.showMessage(UpdateWithdrawLimit.this,
								"Please enter the account Id.\n\n"
										+ "You can find it in \"Display Account List\" or in the message when the account was opened.",
								"Account Id required", JOptionPane.WARNING_MESSAGE);
						return;
					}
					String idErr = ClientSafeInput.validateAccountIdFormat(idRaw);
					if (idErr != null) {
						DialogAlerts.showMessage(UpdateWithdrawLimit.this, idErr, "Invalid account number",
								JOptionPane.WARNING_MESSAGE);
						return;
					}
					if (limitRaw.isEmpty()) {
						DialogAlerts.showMessage(UpdateWithdrawLimit.this,
								"Enter the new maximum amount allowed for each withdrawal transaction.",
								"Limit required", JOptionPane.WARNING_MESSAGE);
						return;
					}
					double newLimit = Double.parseDouble(limitRaw);
					String limErr = ClientSafeInput.validateWithdrawLimitField(newLimit);
					if (limErr != null) {
						DialogAlerts.showMessage(UpdateWithdrawLimit.this, limErr, "Invalid limit",
								JOptionPane.WARNING_MESSAGE);
						return;
					}
					if (newLimit <= 0) {
						DialogAlerts.showMessage(UpdateWithdrawLimit.this,
								"The limit must be greater than zero.",
								"Invalid limit", JOptionPane.WARNING_MESSAGE);
						return;
					}
					BankAccount acc = FileIO.bank.findAccount(idRaw);
					if (acc == null) {
						DialogAlerts.showMessage(UpdateWithdrawLimit.this,
								"No account matches the number you entered.\n\n"
										+ "Check each digit or open \"Display Account List\".",
								"Account not found", JOptionPane.WARNING_MESSAGE);
						return;
					}
					if (!(acc instanceof SavingsAccount)) {
						DialogAlerts.showMessage(UpdateWithdrawLimit.this,
								"Only savings and student accounts have a per-transaction withdrawal limit.\n\n"
										+ "Current accounts are not limited this way.",
								"Not available for this account", JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					double before = ((SavingsAccount) acc).getMaxWithdrawLimit();
					if (before == newLimit) {
						DialogAlerts.showMessage(UpdateWithdrawLimit.this,
								String.format("This account already has a per-transaction limit of %.2f.", before),
								"No change", JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					String holderDisplay = ClientSafeInput.maskHolderName(acc.getName());
					String confirmMsg = String.format(
							"Update the withdrawal limit for this account?\n\n"
									+ "• Account Id: %s\n"
									+ "• Name on account: %s\n"
									+ "• Current max per transaction: %.2f\n"
									+ "• New max per transaction: %.2f\n\n"
									+ "Future withdrawals cannot exceed the new limit in a single transaction (other rules still apply).",
							idRaw, holderDisplay, before, newLimit);
					int a = DialogAlerts.showConfirm(UpdateWithdrawLimit.this, confirmMsg, "Confirm limit change",
							JOptionPane.YES_NO_OPTION);
					if (a != JOptionPane.YES_OPTION) {
						return;
					}
					FileIO.bank.setMaxWithdrawPerTransaction(idRaw, newLimit);
					FileIO.Write();
					GUIForm.menu.refreshDashboard();
					GUIForm.UpdateDisplay();
					DialogAlerts.showMessage(UpdateWithdrawLimit.this,
							String.format(
									"The per-transaction withdrawal limit was updated.\n\n"
											+ "Account Id: %s\n"
											+ "New max per transaction: %.2f\n\n"
											+ "This setting is saved with your data.",
									idRaw, newLimit),
							"Limit updated", JOptionPane.INFORMATION_MESSAGE);
					textFieldId.setText(null);
					textFieldLimit.setText(null);
					setVisible(false);
				} catch (NumberFormatException ex) {
					DialogAlerts.showMessage(UpdateWithdrawLimit.this,
							"The limit is not a valid number.\n\n"
									+ "Use digits and at most one decimal point (for example 5000 or 2500.50).",
							"Invalid amount", JOptionPane.ERROR_MESSAGE);
				} catch (AccNotFound ex) {
					DialogAlerts.showMessage(UpdateWithdrawLimit.this,
							"No account matches the number you entered.",
							"Account not found", JOptionPane.WARNING_MESSAGE);
				} catch (IllegalArgumentException ex) {
					DialogAlerts.showMessage(UpdateWithdrawLimit.this, ex.getMessage(), "Cannot update limit",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		btnReset = new JButton("Reset");
		UITheme.styleSecondaryButton(btnReset);
		btnReset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				textFieldId.setText(null);
				textFieldLimit.setText(null);
			}
		});

		JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 8));
		btnRow.setOpaque(false);
		btnRow.add(btnApply);
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
		UITheme.styleFormLabel(lblId);
		UITheme.styleFormLabel(lblLimit);
		UITheme.styleFormTextField(textFieldId);
		UITheme.styleFormTextField(textFieldLimit);
		UITheme.stylePrimaryButton(btnApply);
		UITheme.styleSecondaryButton(btnReset);
	}
}
