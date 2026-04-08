package GUI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import Bank.BankAccount;
import Data.FileIO;

public class Menu extends JFrame {

	private static final long serialVersionUID = 1L;
	/** Full width for the save / log out bar (matches two grid columns + gap). */
	private static final int MENU_SAVE_WIDTH = 500;
	private static final int MENU_GRID_GAP = 10;
	private static final String BANNER_TITLE = "ApexTrust Retail Banking";
	private static final String BANNER_TAG = "Account servicing · Cash movements · Reporting";

	private JPanel rootPanel;
	private JPanel northStack;
	private JPanel bannerPanel;
	private JPanel dashPanel;
	private JPanel themeWell;
	private JPanel footerPanel;
	private JLabel section;
	private JLabel hint;
	private JLabel lblDashboard;
	private JLabel lblSession;
	private JLabel lblTheme;
	private JComboBox<String> themeCombo;
	private boolean themeEventsLive;
	private final List<JButton> taskButtons = new ArrayList<>();
	private JButton btnSaveLogout;

	public Menu() {
		setTitle("ApexTrust — Operations Console");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				saveAndReturnToLogin();
			}

			@Override
			public void windowActivated(WindowEvent e) {
				refreshDashboard();
			}
		});

		rootPanel = new JPanel(new BorderLayout());
		rootPanel.setBackground(UITheme.PAGE_BG);

		northStack = new JPanel(new BorderLayout());
		northStack.setOpaque(false);
		bannerPanel = UITheme.createHeaderBanner(BANNER_TITLE, BANNER_TAG, 88);
		northStack.add(bannerPanel, BorderLayout.NORTH);

		dashPanel = new JPanel(new GridBagLayout());
		dashPanel.setBorder(new EmptyBorder(10, 28, 12, 28));
		dashPanel.setBackground(UITheme.DASH_PANEL_BG);

		GridBagConstraints dc = new GridBagConstraints();
		dc.gridx = 0;
		dc.gridy = 0;
		dc.weightx = 1;
		dc.fill = GridBagConstraints.HORIZONTAL;
		dc.anchor = GridBagConstraints.WEST;
		dc.insets = new Insets(0, 0, 6, 0);
		lblDashboard = new JLabel(" ");
		lblDashboard.setFont(UITheme.fontBody());
		lblDashboard.setForeground(UITheme.PAGE_HEADING);
		dashPanel.add(lblDashboard, dc);

		dc.gridy = 1;
		dc.insets = new Insets(0, 0, 8, 0);
		lblSession = new JLabel(" ");
		lblSession.setFont(UITheme.fontSmall());
		lblSession.setForeground(UITheme.PAGE_SECONDARY);
		dashPanel.add(lblSession, dc);

		dc.gridy = 2;
		dc.insets = new Insets(0, 0, 0, 0);
		JPanel themeRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		themeRow.setOpaque(false);
		themeWell = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
		themeWell.setOpaque(true);
		themeWell.setBackground(UITheme.CARD_BG);
		themeWell.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(UITheme.MENU_BORDER, 1),
				BorderFactory.createEmptyBorder(6, 14, 6, 14)));
		lblTheme = new JLabel("Theme");
		lblTheme.setFont(UITheme.fontBody());
		lblTheme.setForeground(UITheme.PAGE_HEADING);
		themeWell.add(lblTheme);
		themeCombo = new JComboBox<>(new String[] { "Light", "Dark", "Slate" });
		themeCombo.setFont(UITheme.fontBody());
		applyComboThemeColors();
		themeEventsLive = false;
		ThemePrefs.Id loaded = ThemePrefs.load();
		themeCombo.setSelectedIndex(loaded.ordinal());
		themeCombo.addActionListener(e -> onThemeSelected());
		themeWell.add(themeCombo);
		themeRow.add(themeWell);
		dashPanel.add(themeRow, dc);

		northStack.add(dashPanel, BorderLayout.SOUTH);
		themeEventsLive = true;

		rootPanel.add(northStack, BorderLayout.NORTH);

		FileIO.Read();

		menuGridCol = 0;
		menuGridRow = 0;

		JPanel body = new JPanel(new BorderLayout(32, 0));
		body.setOpaque(false);
		body.setBorder(new EmptyBorder(24, 36, 20, 36));

		JPanel actions = new JPanel();
		actions.setLayout(new BoxLayout(actions, BoxLayout.Y_AXIS));
		actions.setOpaque(false);

		section = new JLabel("Operations");
		section.setFont(UITheme.fontHeadline());
		section.setForeground(UITheme.PAGE_HEADING);
		section.setAlignmentX(Component.CENTER_ALIGNMENT);
		actions.add(section);
		actions.add(Box.createVerticalStrut(8));

		hint = new JLabel();
		hint.setFont(UITheme.fontSmall());
		setMenuHintHtml();
		hint.setAlignmentX(Component.CENTER_ALIGNMENT);
		actions.add(hint);
		actions.add(Box.createVerticalStrut(18));

		JPanel taskGrid = new JPanel(new GridBagLayout());
		taskGrid.setOpaque(false);
		taskGrid.setAlignmentX(Component.CENTER_ALIGNMENT);
		GridBagConstraints mg = new GridBagConstraints();
		mg.insets = new Insets(5, MENU_GRID_GAP / 2, 5, MENU_GRID_GAP / 2);
		mg.fill = GridBagConstraints.BOTH;
		mg.weightx = 0.5;
		mg.weighty = 0;

		addMenuTask(taskGrid, mg, new JButton("Add account"), e -> openSingle(GUIForm.addaccount));
		addMenuTask(taskGrid, mg, new JButton("Deposit to account"), e -> openSingle(GUIForm.depositacc));
		addMenuTask(taskGrid, mg, new JButton("Withdraw from account"), e -> openSingle(GUIForm.withdraw));
		addMenuTask(taskGrid, mg, new JButton("Update withdrawal limit"), e -> openSingle(GUIForm.updateWithdrawLimit));
		addMenuTask(taskGrid, mg, new JButton("Display account list"), e -> openSingle(GUIForm.displaylist));
		addMenuTask(taskGrid, mg, new JButton("Transaction report"), e -> openTransactionReport());

		actions.add(taskGrid);
		actions.add(Box.createVerticalStrut(16));

		btnSaveLogout = new JButton("Save and Log out");
		styleSaveLogoutButton();
		btnSaveLogout.addActionListener(e -> saveAndReturnToLogin());
		btnSaveLogout.setAlignmentX(Component.CENTER_ALIGNMENT);
		actions.add(btnSaveLogout);

		body.add(actions, BorderLayout.CENTER);

		JPanel side = new JPanel(new GridLayout(1, 1));
		side.setOpaque(false);
		side.setPreferredSize(new Dimension(220, 260));
		javax.swing.ImageIcon sideLogo = UITheme.loadBankingLogo(200);
		if (sideLogo != null) {
			JLabel img = new JLabel(sideLogo, SwingConstants.CENTER);
			img.setOpaque(false);
			side.add(img);
		} else {
			side.add(new EnterpriseLogo(200));
		}
		body.add(side, BorderLayout.EAST);

		rootPanel.add(body, BorderLayout.CENTER);
		footerPanel = UITheme.createPageFooter("Confidential — ApexTrust internal banking console · v1.0");
		rootPanel.add(footerPanel, BorderLayout.SOUTH);

		setContentPane(rootPanel);
		setMinimumSize(new Dimension(780, 560));
		pack();
		setLocationRelativeTo(null);
		refreshDashboard();
	}

	private int menuGridCol;
	private int menuGridRow;

	private void addMenuTask(JPanel grid, GridBagConstraints mg, JButton btn,
			java.awt.event.ActionListener listener) {
		btn.addActionListener(listener);
		UITheme.styleMenuGridCellButton(btn);
		mg.gridx = menuGridCol;
		mg.gridy = menuGridRow;
		mg.gridwidth = 1;
		grid.add(btn, mg);
		taskButtons.add(btn);
		menuGridCol++;
		if (menuGridCol > 1) {
			menuGridCol = 0;
			menuGridRow++;
		}
	}

	private void styleSaveLogoutButton() {
		UITheme.stylePrimaryButton(btnSaveLogout);
		UITheme.styleMenuActionButton(btnSaveLogout, MENU_SAVE_WIDTH);
		btnSaveLogout.setHorizontalAlignment(SwingConstants.CENTER);
	}

	private void setMenuHintHtml() {
		hint.setText(UITheme.htmlDiv(
				"Select a task below. Each module opens in its own window. Choosing the same task again brings that window to the front (Close or ✕ hides it).",
				480, UITheme.PAGE_SECONDARY));
	}

	private void applyComboThemeColors() {
		themeCombo.setBackground(UITheme.CARD_BG);
		themeCombo.setForeground(UITheme.TEXT_LABEL);
	}

	private void onThemeSelected() {
		if (!themeEventsLive) {
			return;
		}
		ThemePrefs.Id id = ThemePrefs.Id.values()[themeCombo.getSelectedIndex()];
		ThemePrefs.save(id);
		UITheme.applyTheme(id);
		UITheme.refreshAllWindowTrees();
		SwingUtilities.invokeLater(() -> {
			GUIForm.reapplyThemeAcrossApp();
			applyComboThemeColors();
		});
	}

	public void refreshDashboard() {
		BankAccount[] accounts = FileIO.bank.getAccounts();
		int n = 0;
		double sumBal = 0;
		for (int i = 0; i < accounts.length; i++) {
			if (accounts[i] == null) {
				break;
			}
			n++;
			sumBal += accounts[i].getbalance();
		}
		lblDashboard.setText(String.format(
				"Branch — %d account(s)  ·  Total balances %,.2f  ·  Session deposits %,.2f",
				Integer.valueOf(n), Double.valueOf(sumBal), Double.valueOf(SessionContext.getSessionDepositTotal())));
		if (SessionContext.getSessionStartMs() == 0) {
			lblSession.setText("Session: sign in to start a timed console session.");
		} else {
			lblSession.setText(SessionContext.getSessionStartedLine());
		}
	}

	public void reapplyChromeAfterTheme() {
		northStack.remove(bannerPanel);
		bannerPanel = UITheme.createHeaderBanner(BANNER_TITLE, BANNER_TAG, 88);
		northStack.add(bannerPanel, BorderLayout.NORTH);
		northStack.revalidate();
		northStack.repaint();

		rootPanel.setBackground(UITheme.PAGE_BG);
		dashPanel.setBackground(UITheme.DASH_PANEL_BG);
		lblDashboard.setForeground(UITheme.PAGE_HEADING);
		lblSession.setForeground(UITheme.PAGE_SECONDARY);
		lblTheme.setForeground(UITheme.PAGE_HEADING);
		themeWell.setBackground(UITheme.CARD_BG);
		themeWell.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(UITheme.MENU_BORDER, 1),
				BorderFactory.createEmptyBorder(6, 14, 6, 14)));
		applyComboThemeColors();

		section.setForeground(UITheme.PAGE_HEADING);
		setMenuHintHtml();

		for (JButton b : taskButtons) {
			UITheme.styleMenuGridCellButton(b);
		}
		styleSaveLogoutButton();

		footerPanel.setBackground(UITheme.PAGE_BG);
		for (Component c : footerPanel.getComponents()) {
			if (c instanceof JLabel) {
				((JLabel) c).setForeground(UITheme.TEXT_MUTED);
			}
		}
	}

	private void saveAndReturnToLogin() {
		FileIO.Write();
		hideToolWindows();
		setVisible(false);
		GUIForm.login.showAfterLogout();
	}

	private static void hideToolWindows() {
		JFrame[] tools = { GUIForm.addaccount, GUIForm.addcurrentacc, GUIForm.addsavingsaccount,
				GUIForm.addstudentaccount, GUIForm.displaylist, GUIForm.depositacc, GUIForm.withdraw,
				GUIForm.updateWithdrawLimit, GUIForm.transactionReport };
		for (JFrame f : tools) {
			if (f != null && f.isVisible()) {
				f.setVisible(false);
			}
		}
	}

	private void openSingle(JFrame target) {
		target.setLocationRelativeTo(this);
		WindowTools.showAndBringToFront(target);
	}

	private void openTransactionReport() {
		GUIForm.transactionReport.setLocationRelativeTo(this);
		WindowTools.showAndBringToFront(GUIForm.transactionReport);
	}
}
