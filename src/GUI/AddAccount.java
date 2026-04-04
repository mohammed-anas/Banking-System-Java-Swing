package GUI;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class AddAccount extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final int BTN_W = 300;

	private JPanel contentPane;
	private JLabel lblAddAccount;
	private JButton btnSavings;
	private JButton btnCurrent;
	private JButton btnStudent;

	public AddAccount() {
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		contentPane = new JPanel();
		setContentPane(contentPane);
		UITheme.styleFormSurface(this, contentPane, "New account");
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		contentPane.add(Box.createVerticalStrut(16));

		lblAddAccount = new JLabel("Choose account type");
		UITheme.styleHeadlineLabel(lblAddAccount);
		lblAddAccount.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblAddAccount.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblAddAccount);
		contentPane.add(Box.createVerticalStrut(20));

		btnSavings = new JButton("Savings account");
		UITheme.styleMenuActionButton(btnSavings, BTN_W);
		btnSavings.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnSavings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WindowTools.showAndBringToFront(GUIForm.addsavingsaccount);
				setVisible(false);
			}
		});
		contentPane.add(btnSavings);
		contentPane.add(Box.createVerticalStrut(10));

		btnCurrent = new JButton("Current account");
		UITheme.styleMenuActionButton(btnCurrent, BTN_W);
		btnCurrent.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnCurrent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WindowTools.showAndBringToFront(GUIForm.addcurrentacc);
				setVisible(false);
			}
		});
		contentPane.add(btnCurrent);
		contentPane.add(Box.createVerticalStrut(10));

		btnStudent = new JButton("Student account");
		UITheme.styleMenuActionButton(btnStudent, BTN_W);
		btnStudent.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnStudent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WindowTools.showAndBringToFront(GUIForm.addstudentaccount);
				setVisible(false);
			}
		});
		contentPane.add(btnStudent);
		contentPane.add(Box.createVerticalGlue());

		pack();
		Dimension d = getPreferredSize();
		setMinimumSize(new Dimension(Math.max(420, d.width), Math.max(320, d.height)));
		setLocationRelativeTo(null);
	}

	void applyThemeColors() {
		contentPane.setBackground(UITheme.PAGE_BG);
		UITheme.styleHeadlineLabel(lblAddAccount);
		UITheme.styleMenuActionButton(btnSavings, BTN_W);
		UITheme.styleMenuActionButton(btnCurrent, BTN_W);
		UITheme.styleMenuActionButton(btnStudent, BTN_W);
	}
}
