package GUI;

import java.awt.BorderLayout;
import java.util.Comparator;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import Bank.BankAccount;
import Bank.CurrentAccount;
import Bank.SavingsAccount;
import Bank.StudentAccount;
import Data.FileIO;

public class DisplayList extends JFrame {

	private static final long serialVersionUID = 1L;

	private final JPanel root;
	private final JLabel lblAccountList;
	private final JLabel lblPrivacy;
	private JLabel lblEmpty;
	private JLabel lblFilter;
	private JTextField tfFilter;
	private JTable table;

	@SuppressWarnings({ })
	public DisplayList() {
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		root = new JPanel(new BorderLayout(10, 10));
		setContentPane(root);
		UITheme.styleFormSurface(this, root, "Account directory");

		JPanel header = new JPanel(new BorderLayout(0, 6));
		header.setOpaque(false);
		lblAccountList = new JLabel("Account directory");
		lblAccountList.setFont(UITheme.fontTitle());
		lblAccountList.setForeground(UITheme.PAGE_HEADING);
		lblAccountList.setHorizontalAlignment(SwingConstants.CENTER);
		header.add(lblAccountList, BorderLayout.NORTH);
		lblPrivacy = new JLabel(UITheme.htmlDiv(
				"Confidential: contains names, Ids, and balances. Do not show this screen to others.", 560,
				UITheme.PAGE_SECONDARY));
		lblPrivacy.setFont(UITheme.fontSmall());
		lblPrivacy.setHorizontalAlignment(SwingConstants.CENTER);
		header.add(lblPrivacy, BorderLayout.SOUTH);
		root.add(header, BorderLayout.NORTH);

		JPanel center = new JPanel(new BorderLayout(8, 8));
		center.setOpaque(false);

		BankAccount[] accounts = FileIO.bank.getAccounts();
		int count = 0;
		for (int i = 0; i < accounts.length; i++) {
			if (accounts[i] == null) {
				break;
			}
			count++;
		}

		if (count == 0) {
			lblEmpty = new JLabel(UITheme.htmlDiv(
					"No accounts yet. Add one from the main menu, then open this list again to refresh.", 480,
					UITheme.PAGE_SECONDARY));
			lblEmpty.setFont(UITheme.fontBody());
			lblEmpty.setHorizontalAlignment(SwingConstants.CENTER);
			center.add(lblEmpty, BorderLayout.CENTER);
		} else {
			lblEmpty = null;
			JPanel filterRow = new JPanel(new BorderLayout(8, 0));
			filterRow.setOpaque(false);
			lblFilter = new JLabel("Filter:");
			UITheme.styleFormLabel(lblFilter);
			tfFilter = new JTextField();
			tfFilter.setFont(UITheme.fontBody());
			UITheme.styleFormTextField(tfFilter);
			tfFilter.setToolTipText("Narrows rows by Id, name, or type as you type");
			filterRow.add(lblFilter, BorderLayout.WEST);
			filterRow.add(tfFilter, BorderLayout.CENTER);
			center.add(filterRow, BorderLayout.NORTH);

			String[] cols = { "#", "Account Id", "Name", "Type", "Balance" };
			DefaultTableModel model = new DefaultTableModel(cols, 0) {
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}

				@Override
				public Class<?> getColumnClass(int columnIndex) {
					if (columnIndex == 0) {
						return Integer.class;
					}
					if (columnIndex == 4) {
						return Double.class;
					}
					return String.class;
				}
			};
			int n = 1;
			for (int i = 0; i < accounts.length; i++) {
				BankAccount acc = accounts[i];
				if (acc == null) {
					break;
				}
				model.addRow(new Object[] { Integer.valueOf(n++), acc.getAccNum(), acc.getName(), typeLabel(acc),
						Double.valueOf(acc.getbalance()) });
			}
			table = new JTable(model);
			table.setFont(UITheme.fontSmall());
			table.setRowHeight(24);
			table.setShowGrid(true);
			styleTableChrome(table);

			table.getTableHeader().setReorderingAllowed(false);
			table.setAutoCreateRowSorter(true);
			@SuppressWarnings("unchecked")
			TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) table.getRowSorter();
			sorter.setComparator(0, Comparator.comparingInt(a -> ((Integer) a).intValue()));
			sorter.setComparator(4, Comparator.comparingDouble(a -> ((Double) a).doubleValue()));

			DefaultTableCellRenderer centerR = new DefaultTableCellRenderer();
			centerR.setHorizontalAlignment(SwingConstants.CENTER);
			table.getColumnModel().getColumn(0).setCellRenderer(centerR);
			table.getColumnModel().getColumn(1).setCellRenderer(centerR);

			table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
				private static final long serialVersionUID = 1L;

				@Override
				public void setValue(Object value) {
					if (value instanceof Number) {
						super.setValue(String.format("%.2f", ((Number) value).doubleValue()));
					} else {
						super.setValue(value);
					}
					setHorizontalAlignment(SwingConstants.RIGHT);
				}
			});

			table.getColumnModel().getColumn(0).setPreferredWidth(36);
			table.getColumnModel().getColumn(1).setPreferredWidth(88);
			table.getColumnModel().getColumn(2).setPreferredWidth(200);
			table.getColumnModel().getColumn(3).setPreferredWidth(96);
			table.getColumnModel().getColumn(4).setPreferredWidth(100);

			tfFilter.getDocument().addDocumentListener(new DocumentListener() {
				private void applyFilter() {
					String t = tfFilter.getText();
					if (t == null || t.trim().isEmpty()) {
						sorter.setRowFilter(null);
						return;
					}
					String needle = t.trim();
					sorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
						@Override
						public boolean include(
								Entry<? extends DefaultTableModel, ? extends Integer> entry) {
							for (int col : new int[] { 1, 2, 3 }) {
								Object v = entry.getValue(col);
								if (v != null
										&& v.toString().toLowerCase(java.util.Locale.ROOT)
												.contains(needle.toLowerCase(java.util.Locale.ROOT))) {
									return true;
								}
							}
							if (needle.chars().allMatch(Character::isDigit)) {
								try {
									Double bal = (Double) entry.getValue(4);
									String balStr = String.format("%.2f", bal.doubleValue());
									if (balStr.contains(needle)) {
										return true;
									}
								} catch (Exception ignored) {
								}
							}
							return false;
						}
					});
				}

				@Override
				public void insertUpdate(DocumentEvent e) {
					applyFilter();
				}

				@Override
				public void removeUpdate(DocumentEvent e) {
					applyFilter();
				}

				@Override
				public void changedUpdate(DocumentEvent e) {
					applyFilter();
				}
			});

			JScrollPane scrollPane = new JScrollPane(table);
			scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			center.add(scrollPane, BorderLayout.CENTER);
		}
		root.add(center, BorderLayout.CENTER);

		setMinimumSize(new java.awt.Dimension(680, 460));
		pack();
		setLocationRelativeTo(null);
	}

	private static void styleTableChrome(JTable table) {
		table.setGridColor(UITheme.TABLE_GRID);
		table.setSelectionBackground(UITheme.TABLE_SELECTION_BG);
		table.setSelectionForeground(UITheme.PAGE_HEADING);
		table.getTableHeader().setFont(UITheme.fontBody());
		table.getTableHeader().setForeground(UITheme.TEXT_LABEL);
		table.getTableHeader().setBackground(UITheme.INPUT_FIELD_BG);
		table.setForeground(UITheme.TEXT_LABEL);
		table.setBackground(UITheme.INPUT_FIELD_BG);
		table.setOpaque(true);
	}

	void applyThemeColors() {
		root.setBackground(UITheme.PAGE_BG);
		lblAccountList.setFont(UITheme.fontTitle());
		lblAccountList.setForeground(UITheme.PAGE_HEADING);
		lblPrivacy.setText(UITheme.htmlDiv(
				"Confidential: contains names, Ids, and balances. Do not show this screen to others.", 560,
				UITheme.PAGE_SECONDARY));
		if (lblEmpty != null) {
			lblEmpty.setText(UITheme.htmlDiv(
					"No accounts yet. Add one from the main menu, then open this list again to refresh.", 480,
					UITheme.PAGE_SECONDARY));
		}
		if (lblFilter != null) {
			UITheme.styleFormLabel(lblFilter);
		}
		if (tfFilter != null) {
			UITheme.styleFormTextField(tfFilter);
		}
		if (table != null) {
			styleTableChrome(table);
		}
	}

	private static String typeLabel(BankAccount a) {
		if (a instanceof StudentAccount) {
			return "Student";
		}
		if (a instanceof CurrentAccount) {
			return "Current";
		}
		if (a instanceof SavingsAccount) {
			return "Savings";
		}
		return "Account";
	}
}
