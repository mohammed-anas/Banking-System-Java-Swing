package GUI;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import javax.swing.BorderFactory;

public class Login {

	public JFrame frame;
	private JTextField textField;
	private JPasswordField textField_1;
	private JTextArea statusArea;

	private JPanel loginRoot;
	private JPanel loginBanner;
	private JPanel loginCard;
	private JPanel loginFooter;
	private JLabel cardTitle;
	private JLabel cardSub;
	private JLabel lblUsername;
	private JLabel lblPassword;
	private JButton btnLogin;

	public Login() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("ApexTrust — Sign in");
		frame.setMinimumSize(new java.awt.Dimension(460, 420));

		loginRoot = new JPanel(new BorderLayout());
		loginRoot.setBackground(UITheme.PAGE_BG);

		loginBanner = UITheme.createHeaderBanner("ApexTrust Retail Banking", "Authorized personnel — secure session", 72);
		loginRoot.add(loginBanner, BorderLayout.NORTH);

		JPanel centerWrap = new JPanel(new BorderLayout());
		centerWrap.setOpaque(false);
		centerWrap.setBorder(new EmptyBorder(28, 36, 20, 36));

		loginCard = new JPanel(new GridBagLayout());
		loginCard.setBackground(UITheme.CARD_BG);
		loginCard.setBorder(javax.swing.BorderFactory.createCompoundBorder(
				javax.swing.BorderFactory.createLineBorder(UITheme.MENU_BORDER, 1),
				new EmptyBorder(28, 32, 28, 32)));

		cardTitle = new JLabel("Secure sign-in");
		cardTitle.setFont(UITheme.fontHeadline());
		cardTitle.setForeground(UITheme.PAGE_HEADING);

		cardSub = new JLabel();
		cardSub.setFont(UITheme.fontSmall());
		setCardSubHtml();

		lblUsername = new JLabel("Username");
		lblUsername.setFont(UITheme.fontBody());
		lblUsername.setForeground(UITheme.TEXT_LABEL);

		textField = new JTextField(18);
		textField.setFont(UITheme.fontBody());
		textField.setText("admin");

		lblPassword = new JLabel("Password");
		lblPassword.setFont(UITheme.fontBody());
		lblPassword.setForeground(UITheme.TEXT_LABEL);

		textField_1 = new JPasswordField(18);
		textField_1.setFont(UITheme.fontBody());

		styleLoginInputs();

		statusArea = new JTextArea(" ");
		statusArea.setEditable(false);
		statusArea.setLineWrap(true);
		statusArea.setWrapStyleWord(true);
		statusArea.setOpaque(false);
		statusArea.setFont(UITheme.fontSmall());
		statusArea.setForeground(UITheme.ERROR);
		statusArea.setRows(0);
		statusArea.setColumns(32);
		statusArea.setBorder(javax.swing.BorderFactory.createEmptyBorder());

		btnLogin = new JButton("Sign in");
		UITheme.stylePrimaryButton(btnLogin);
		btnLogin.addActionListener(e -> doLogin());

		textField_1.addActionListener(e -> btnLogin.doClick());

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(0, 0, 4, 0);
		loginCard.add(cardTitle, c);
		c.gridy = 1;
		c.insets = new Insets(0, 0, 20, 0);
		loginCard.add(cardSub, c);
		c.gridy = 2;
		c.gridwidth = 1;
		c.insets = new Insets(0, 0, 6, 12);
		loginCard.add(lblUsername, c);
		c.gridy = 3;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 0, 14, 0);
		loginCard.add(textField, c);
		c.gridy = 4;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(0, 0, 6, 12);
		loginCard.add(lblPassword, c);
		c.gridy = 5;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 0, 16, 0);
		loginCard.add(textField_1, c);
		c.gridy = 6;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 0, 8, 0);
		loginCard.add(btnLogin, c);
		c.gridy = 7;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.insets = new Insets(4, 0, 0, 0);
		loginCard.add(statusArea, c);

		centerWrap.add(loginCard, BorderLayout.CENTER);
		loginRoot.add(centerWrap, BorderLayout.CENTER);

		loginFooter = UITheme.createPageFooter("© Demo institution — Internal use only. Do not use production customer data.");
		loginRoot.add(loginFooter, BorderLayout.SOUTH);

		frame.setContentPane(loginRoot);
		frame.pack();
		frame.setLocationRelativeTo(null);
	}

	/**
	 * Swing HTML ignores {@link JLabel#setForeground}; embed color in the markup.
	 */
	private void setCardSubHtml() {
		String rgb = UITheme.htmlColorRgb(UITheme.PAGE_SECONDARY);
		cardSub.setText("<html><body style='width:340px;color:rgb(" + rgb
				+ ");'>Enter your operator credentials to open the enterprise console.</body></html>");
	}

	private void styleLoginInputs() {
		textField.setForeground(UITheme.TEXT_LABEL);
		textField.setBackground(UITheme.INPUT_FIELD_BG);
		textField.setCaretColor(UITheme.TEXT_LABEL);
		textField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(UITheme.MENU_BORDER, 1),
				BorderFactory.createEmptyBorder(6, 10, 6, 10)));
		textField_1.setForeground(UITheme.TEXT_LABEL);
		textField_1.setBackground(UITheme.INPUT_FIELD_BG);
		textField_1.setCaretColor(UITheme.TEXT_LABEL);
		textField_1.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(UITheme.MENU_BORDER, 1),
				BorderFactory.createEmptyBorder(6, 10, 6, 10)));
	}

	public void reapplyChromeAfterTheme() {
		loginRoot.remove(loginBanner);
		loginBanner = UITheme.createHeaderBanner("ApexTrust Retail Banking", "Authorized personnel — secure session", 72);
		loginRoot.add(loginBanner, BorderLayout.NORTH);
		loginRoot.setBackground(UITheme.PAGE_BG);
		loginCard.setBackground(UITheme.CARD_BG);
		loginCard.setBorder(javax.swing.BorderFactory.createCompoundBorder(
				javax.swing.BorderFactory.createLineBorder(UITheme.MENU_BORDER, 1),
				new EmptyBorder(28, 32, 28, 32)));
		cardTitle.setForeground(UITheme.PAGE_HEADING);
		setCardSubHtml();
		lblUsername.setForeground(UITheme.TEXT_LABEL);
		lblPassword.setForeground(UITheme.TEXT_LABEL);
		styleLoginInputs();
		UITheme.stylePrimaryButton(btnLogin);
		statusArea.setForeground(UITheme.ERROR);
		loginFooter.setBackground(UITheme.PAGE_BG);
		for (java.awt.Component child : loginFooter.getComponents()) {
			if (child instanceof JLabel) {
				((JLabel) child).setForeground(UITheme.TEXT_MUTED);
			}
		}
		loginRoot.revalidate();
		loginRoot.repaint();
	}

	private void doLogin() {
		char[] passChars = textField_1.getPassword();
		try {
			String user = textField.getText().trim();
			String pass = new String(passChars);
			if (user.isEmpty() && pass.isEmpty()) {
				statusArea.setText("Please enter your username and password.");
				return;
			}
			if (user.isEmpty()) {
				statusArea.setText("Please enter your username.");
				return;
			}
			if (pass.isEmpty()) {
				statusArea.setText("Please enter your password.");
				return;
			}
			if (user.equals("admin") && pass.equals("admin")) {
				statusArea.setText(" ");
				textField_1.setText("");
				SessionContext.startNewSession();
				frame.setVisible(false);
				GUIForm.menu.refreshDashboard();
				GUIForm.menu.setLocationRelativeTo(null);
				GUIForm.menu.setVisible(true);
				GUIForm.menu.toFront();
				GUIForm.menu.requestFocus();
			} else {
				statusArea.setText(
						"Those details do not match our records. Check your username and password and try again.");
				textField_1.setText("");
			}
		} finally {
			if (passChars != null) {
				Arrays.fill(passChars, '\0');
			}
		}
	}

	/** Called after saving data from the menu — shows sign-in again with no success popup. */
	public void showAfterLogout() {
		statusArea.setText(" ");
		textField_1.setText("");
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.toFront();
		frame.requestFocus();
	}
}
