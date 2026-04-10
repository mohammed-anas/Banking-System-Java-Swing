package GUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import Bank.BankAccount;
import Bank.TransactionRecord;
import Data.FileIO;

/**
 * Per-account (user-wise) transaction history report.
 */
public class TransactionReport extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final SimpleDateFormat TIME_FMT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private JPanel root;
	private JLabel lblTitle;
	private JLabel lblHintDesc;
	private JLabel lblId;
	private JTextField textAccountId;
	private JButton btnView;
	private JButton btnExport;
	private JButton btnClose;
	private JTable table;
	private DefaultTableModel tableModel;
	private JLabel lblSummary;
	private String lastReportAccountId = "";
	private String lastReportHolderName = "";

	public TransactionReport() {
		setTitle("ApexTrust — Transaction report");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		root = new JPanel(new BorderLayout(10, 10));
		root.setBackground(UITheme.PAGE_BG);
		root.setBorder(new EmptyBorder(10, 12, 10, 12));
		setContentPane(root);

		JPanel north = new JPanel(new GridBagLayout());
		north.setOpaque(false);
		GridBagConstraints gc = new GridBagConstraints();
		gc.gridx = 0;
		gc.gridy = 0;
		gc.weightx = 1;
		gbcFillWide(gc);
		lblTitle = new JLabel("Transaction report (by account)");
		lblTitle.setFont(UITheme.fontHeadline());
		lblTitle.setForeground(UITheme.PAGE_HEADING);
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		north.add(lblTitle, gc);

		gc.gridy++;
		lblHintDesc = new JLabel(UITheme.htmlDivLeft(
				"Confidential. Type the 5-digit Id, or paste a line from Display Account List (we read the Id: part).",
				520, UITheme.PAGE_SECONDARY));
		lblHintDesc.setFont(UITheme.fontSmall());
		lblHintDesc.setHorizontalAlignment(SwingConstants.CENTER);
		north.add(lblHintDesc, gc);

		gc.gridy++;
		gc.insets = new Insets(10, 0, 0, 0);
		JPanel ctrlRow = new JPanel(new BorderLayout(12, 0));
		ctrlRow.setOpaque(false);
		JPanel westBtns = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
		westBtns.setOpaque(false);
		lblId = new JLabel("Account Id:");
		UITheme.styleFormLabel(lblId);
		textAccountId = new JTextField(10);
		textAccountId.setFont(UITheme.fontBody());
		UITheme.styleFormTextField(textAccountId);
		btnView = new JButton("View report");
		UITheme.stylePrimaryButton(btnView);
		btnView.addActionListener(e -> loadReport());
		textAccountId.addActionListener(e -> loadReport());
		westBtns.add(lblId);
		westBtns.add(textAccountId);
		westBtns.add(btnView);
		ctrlRow.add(westBtns, BorderLayout.WEST);
		JPanel eastBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
		eastBtns.setOpaque(false);
		btnExport = new JButton("Export…");
		UITheme.styleSecondaryButton(btnExport);
		btnExport.addActionListener(e -> exportReport());
		btnClose = new JButton("Close");
		UITheme.styleSecondaryButton(btnClose);
		btnClose.addActionListener(e -> setVisible(false));
		eastBtns.add(btnExport);
		eastBtns.add(btnClose);
		ctrlRow.add(eastBtns, BorderLayout.EAST);
		north.add(ctrlRow, gc);

		root.add(north, BorderLayout.NORTH);

		String[] cols = { "#", "Date / time", "Type", "Amount", "Balance after" };
		tableModel = new DefaultTableModel(cols, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table = new JTable(tableModel);
		table.setFont(UITheme.fontSmall());
		table.setRowHeight(20);
		styleReportTableChrome(table);
		JScrollPane scroll = new JScrollPane(table);
		root.add(scroll, BorderLayout.CENTER);

		lblSummary = new JLabel(" ");
		lblSummary.setFont(UITheme.fontSmall());
		lblSummary.setVerticalAlignment(SwingConstants.TOP);
		JPanel south = new JPanel(new BorderLayout());
		south.setOpaque(false);
		south.add(lblSummary, BorderLayout.CENTER);
		root.add(south, BorderLayout.SOUTH);

		setMinimumSize(new java.awt.Dimension(640, 420));
		pack();
		setLocationRelativeTo(null);
	}

	private static void gbcFillWide(GridBagConstraints gc) {
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.insets = new Insets(0, 0, 4, 0);
		gc.anchor = GridBagConstraints.CENTER;
	}

	private static void styleReportTableChrome(JTable t) {
		t.setGridColor(UITheme.TABLE_GRID);
		t.setSelectionBackground(UITheme.TABLE_SELECTION_BG);
		t.setSelectionForeground(UITheme.PAGE_HEADING);
		t.setForeground(UITheme.TEXT_LABEL);
		t.setBackground(UITheme.INPUT_FIELD_BG);
		t.setOpaque(true);
		t.getTableHeader().setFont(UITheme.fontBody());
		t.getTableHeader().setForeground(UITheme.TEXT_LABEL);
		t.getTableHeader().setBackground(UITheme.INPUT_FIELD_BG);
	}

	private static String escHtml(String s) {
		if (s == null) {
			return "";
		}
		return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
	}

	private void setSummaryHtml(String id, String holder, int n, double balance, boolean anyLegacy) {
		String rgb = UITheme.htmlColorRgb(UITheme.PAGE_SECONDARY);
		String note = anyLegacy
				? " <i>“Balance on file” is an estimated starting line for data saved before full history existed; it is not a deposit.</i>"
				: "";
		lblSummary.setText("<html><div style='width:600px;color:rgb(" + rgb + ")'>Account <b>" + escHtml(id)
				+ "</b> — " + escHtml(holder) + " — <b>" + n + "</b> line(s). "
				+ "Current balance: <b>" + String.format("%.2f", balance) + "</b>. "
				+ "Do not share this report." + note + "</div></html>");
	}

	void applyThemeColors() {
		root.setBackground(UITheme.PAGE_BG);
		lblTitle.setFont(UITheme.fontHeadline());
		lblTitle.setForeground(UITheme.PAGE_HEADING);
		lblHintDesc.setText(UITheme.htmlDivLeft(
				"Confidential. Type the 5-digit Id, or paste a line from Display Account List (we read the Id: part).",
				520, UITheme.PAGE_SECONDARY));
		UITheme.styleFormLabel(lblId);
		UITheme.styleFormTextField(textAccountId);
		UITheme.stylePrimaryButton(btnView);
		UITheme.styleSecondaryButton(btnExport);
		UITheme.styleSecondaryButton(btnClose);
		styleReportTableChrome(table);
		if (!lastReportAccountId.isEmpty()) {
			loadReport();
		}
	}

	private void loadReport() {
		String raw = textAccountId.getText();
		if (raw == null || raw.trim().isEmpty()) {
			DialogAlerts.showMessage(this, "Please enter the account Id.", "Account Id required",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (FileIO.bank == null) {
			FileIO.Read();
		}
		String id = ClientSafeInput.parseAccountIdFromUserInput(raw);
		if (id == null) {
			DialogAlerts.showMessage(this,
					"Could not read a 5-digit account Id from that text.\n\n"
							+ "• Type only the Id (for example 48291), or\n"
							+ "• Paste a full line from Display Account List so it contains \"Id: 48291\".\n\n"
							+ "Do not use the balance as the Id—a balance can also be five digits.",
					"Id not recognized", JOptionPane.WARNING_MESSAGE);
			return;
		}
		textAccountId.setText(id);
		BankAccount acc = FileIO.bank.findAccount(id);
		if (acc == null) {
			DialogAlerts.showMessage(this,
					"No account has Id " + id + " in the current data.\n\n"
							+ "Open \"Display Account List\" for the exact Id, or use the Id from when the account was opened.\n"
							+ "If you just started the app, make sure you did not delete the \"data\" file.",
					"Account not found", JOptionPane.WARNING_MESSAGE);
			return;
		}

		lastReportAccountId = id;
		tableModel.setRowCount(0);
		List<TransactionRecord> history = acc.getTransactionHistory();
		String holder = acc.getName() == null ? "" : acc.getName().trim();
		int n = history.size();

		int rowNum = 1;
		boolean anyLegacy = false;
		for (TransactionRecord r : history) {
			if (r.getKind() == TransactionRecord.Kind.LEGACY_BASELINE) {
				anyLegacy = true;
			}
			String typeLabel = kindLabel(r.getKind());
			String amountStr = String.format("%.2f", r.getAmount());
			if (r.getKind() == TransactionRecord.Kind.WITHDRAW) {
				amountStr = "-" + amountStr;
			}
			tableModel.addRow(new Object[] { Integer.valueOf(rowNum++), TIME_FMT.format(new Date(r.getTimestampMs())),
					typeLabel, amountStr, String.format("%.2f", r.getBalanceAfter()) });
		}
		lastReportHolderName = holder;
		setSummaryHtml(id, holder, n, acc.getbalance(), anyLegacy);
	}

	private void exportReport() {
		if (tableModel.getRowCount() == 0) {
			DialogAlerts.showMessage(this,
					"Load a transaction report first: enter an account Id and click View report.",
					"Nothing to export", JOptionPane.WARNING_MESSAGE);
			return;
		}
		JFileChooser fc = new JFileChooser();
		fc.setAcceptAllFileFilterUsed(true);
		fc.addChoosableFileFilter(new FileNameExtensionFilter("CSV (*.csv)", "csv"));
		fc.addChoosableFileFilter(new FileNameExtensionFilter("Text (*.txt)", "txt"));
		fc.setSelectedFile(new java.io.File("transactions_" + lastReportAccountId + ".csv"));
		if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
			return;
		}
		java.io.File file = fc.getSelectedFile();
		if (file == null) {
			return;
		}
		boolean csv = file.getName().toLowerCase(java.util.Locale.ROOT).endsWith(".csv");
		javax.swing.filechooser.FileFilter chosen = fc.getFileFilter();
		if (chosen.getDescription() != null && chosen.getDescription().toLowerCase(java.util.Locale.ROOT).contains("txt")
				&& !file.getName().contains(".")) {
			file = new java.io.File(file.getParentFile(), file.getName() + ".txt");
			csv = false;
		} else if (chosen.getDescription() != null
				&& chosen.getDescription().toLowerCase(java.util.Locale.ROOT).contains("csv")
				&& !file.getName().toLowerCase(java.util.Locale.ROOT).endsWith(".csv")) {
			file = new java.io.File(file.getParentFile(), file.getName() + ".csv");
			csv = true;
		}
		try (PrintWriter pw = new PrintWriter(
				new OutputStreamWriter(Files.newOutputStream(file.toPath()), StandardCharsets.UTF_8))) {
			if (csv) {
				pw.println(joinCsv("Account Id", lastReportAccountId));
				pw.println(joinCsv("Account holder", lastReportHolderName));
				pw.println(joinCsv("Generated", TIME_FMT.format(new Date())));
				pw.println();
				pw.println(joinCsv("#", "Date/time", "Type", "Amount", "Balance after"));
				int cols = tableModel.getColumnCount();
				for (int r = 0; r < tableModel.getRowCount(); r++) {
					String[] cells = new String[cols];
					for (int c = 0; c < cols; c++) {
						Object v = tableModel.getValueAt(r, c);
						cells[c] = v == null ? "" : v.toString();
					}
					pw.println(joinCsv(cells));
				}
			} else {
				pw.println("ApexTrust — Transaction report");
				pw.println("Account Id: " + lastReportAccountId);
				pw.println("Account holder: " + lastReportHolderName);
				pw.println("Generated: " + TIME_FMT.format(new Date()));
				pw.println();
				for (int r = 0; r < tableModel.getRowCount(); r++) {
					pw.print(tableModel.getValueAt(r, 0));
					pw.print('\t');
					pw.print(tableModel.getValueAt(r, 1));
					pw.print('\t');
					pw.print(tableModel.getValueAt(r, 2));
					pw.print('\t');
					pw.print(tableModel.getValueAt(r, 3));
					pw.print('\t');
					pw.println(tableModel.getValueAt(r, 4));
				}
			}
		} catch (IOException ex) {
			DialogAlerts.showMessage(this, "Could not write the file:\n" + ex.getMessage(), "Export failed",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		DialogAlerts.showMessage(this, "Saved:\n" + file.getAbsolutePath(), "Export complete",
				JOptionPane.INFORMATION_MESSAGE);
	}

	private static String joinCsv(String... cells) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < cells.length; i++) {
			if (i > 0) {
				sb.append(',');
			}
			sb.append(escapeCsv(cells[i]));
		}
		return sb.toString();
	}

	private static String escapeCsv(String s) {
		if (s == null) {
			return "\"\"";
		}
		boolean needQuotes = s.indexOf(',') >= 0 || s.indexOf('"') >= 0 || s.indexOf('\n') >= 0 || s.indexOf('\r') >= 0;
		String t = s.replace("\"", "\"\"");
		return needQuotes ? '"' + t + '"' : t;
	}

	private static String kindLabel(TransactionRecord.Kind k) {
		switch (k) {
		case OPENING:
			return "Opening balance";
		case DEPOSIT:
			return "Deposit";
		case WITHDRAW:
			return "Withdrawal";
		case LEGACY_BASELINE:
			return "Balance on file (no earlier detail)";
		default:
			return k.name();
		}
	}
}
