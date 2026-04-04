package GUI;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

/**
 * Message and confirm dialogs with predictable width and line wrapping so long
 * copy does not stretch the whole screen.
 */
public final class DialogAlerts {

	private static final int TEXT_COLUMNS = 46;
	private static final int MAX_SCROLL_HEIGHT = 260;

	private DialogAlerts() {
	}

	public static void showMessage(Component parent, String message, String title, int messageType) {
		JOptionPane.showMessageDialog(parent, wrapText(message), title, messageType);
	}

	public static int showConfirm(Component parent, String message, String title, int optionType) {
		return JOptionPane.showConfirmDialog(parent, wrapText(message), title, optionType,
				JOptionPane.QUESTION_MESSAGE);
	}

	private static JScrollPane wrapText(String text) {
		if (text == null) {
			text = "";
		}
		JTextArea area = new JTextArea(text);
		area.setEditable(false);
		area.setLineWrap(true);
		area.setWrapStyleWord(true);
		area.setColumns(TEXT_COLUMNS);
		area.setRows(0);
		area.setOpaque(false);
		area.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
		area.setFont(UITheme.fontBody());
		area.setBackground(UIManager.getColor("Panel.background"));
		JScrollPane sp = new JScrollPane(area);
		sp.setBorder(BorderFactory.createEmptyBorder());
		Dimension inner = area.getPreferredSize();
		int w = Math.min(540, inner.width + 24);
		int h = Math.min(MAX_SCROLL_HEIGHT, inner.height + 12);
		h = Math.max(h, 64);
		sp.setPreferredSize(new Dimension(w, h));
		return sp;
	}
}
