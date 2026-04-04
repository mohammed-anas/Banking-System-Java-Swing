package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.FontUIResource;

/**
 * Enterprise-style colors, typography, and shared chrome. Colors are mutable for runtime themes.
 */
public final class UITheme {

	private UITheme() {
	}

	public static Color NAVY = new Color(22, 42, 68);
	public static Color NAVY_LIGHT = new Color(35, 62, 98);
	public static Color GOLD = new Color(196, 162, 74);
	public static Color GOLD_DIM = new Color(160, 132, 58);
	public static Color PAGE_BG = new Color(236, 240, 245);
	public static Color CARD_BG = Color.WHITE;
	public static Color TEXT_MUTED = new Color(90, 100, 115);
	public static Color TEXT_ON_DARK = new Color(245, 247, 250);
	public static Color ERROR = new Color(176, 32, 48);
	/** Labels on light cards / forms */
	public static Color TEXT_LABEL = new Color(50, 55, 65);
	public static Color MENU_BORDER = new Color(210, 218, 230);
	public static Color TABLE_GRID = new Color(226, 232, 240);
	public static Color TABLE_SELECTION_BG = new Color(232, 238, 248);
	public static Color BANNER_TAGLINE = new Color(200, 208, 220);
	public static Color BANNER_CORNER = new Color(150, 165, 185);
	/** Headings on the main page (not on the navy banner) — must contrast with {@link #PAGE_BG}. */
	public static Color PAGE_HEADING = new Color(22, 42, 68);
	/** Secondary copy on the page (session line, hints). */
	public static Color PAGE_SECONDARY = new Color(90, 100, 115);
	/** Strip behind the dashboard row (slightly distinct from {@link #PAGE_BG}). */
	public static Color DASH_PANEL_BG = new Color(226, 230, 237);
	/** Text fields / password fields — contrasts with {@link #CARD_BG}. */
	public static Color INPUT_FIELD_BG = Color.WHITE;

	public static ThemePrefs.Id currentThemeId = ThemePrefs.Id.light;

	/** For {@code JLabel} HTML bodies — Swing HTML ignores normal foreground. */
	public static String htmlColorRgb(Color c) {
		return c.getRed() + "," + c.getGreen() + "," + c.getBlue();
	}

	/** Centered / wrapping helper text on {@link #PAGE_BG} (embeds color in HTML). */
	public static String htmlDiv(String plainText, int widthPx, Color textColor) {
		String esc = plainText.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
		return "<html><div style='text-align:center;width:" + widthPx + "px;color:rgb(" + htmlColorRgb(textColor)
				+ ");'>" + esc + "</div></html>";
	}

	public static String htmlDivLeft(String plainText, int widthPx, Color textColor) {
		String esc = plainText.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
		return "<html><div style='width:" + widthPx + "px;color:rgb(" + htmlColorRgb(textColor) + ");'>" + esc
				+ "</div></html>";
	}

	public static boolean isDarkTheme() {
		return currentThemeId == ThemePrefs.Id.dark;
	}

	public static void styleFormTextField(javax.swing.JTextField tf) {
		tf.setForeground(TEXT_LABEL);
		tf.setBackground(INPUT_FIELD_BG);
		tf.setCaretColor(TEXT_LABEL);
		tf.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(MENU_BORDER, 1),
				BorderFactory.createEmptyBorder(6, 10, 6, 10)));
	}

	public static Font fontTitle() {
		return new Font(Font.SANS_SERIF, Font.BOLD, 22);
	}

	public static Font fontHeadline() {
		return new Font(Font.SANS_SERIF, Font.BOLD, 15);
	}

	public static Font fontBody() {
		return new Font(Font.SANS_SERIF, Font.PLAIN, 13);
	}

	public static Font fontSmall() {
		return new Font(Font.SANS_SERIF, Font.PLAIN, 11);
	}

	public static Font fontButton() {
		return new Font(Font.SANS_SERIF, Font.BOLD, 13);
	}

	public static void styleHeadlineLabel(JLabel l) {
		l.setFont(fontHeadline());
		l.setForeground(PAGE_HEADING);
	}

	public static void styleFormLabel(JLabel l) {
		l.setFont(fontBody());
		l.setForeground(TEXT_LABEL);
	}

	public static void styleFormSurface(JFrame frame, JPanel panel, String titleSuffix) {
		frame.setTitle("ApexTrust — " + titleSuffix);
		panel.setBackground(PAGE_BG);
		panel.setBorder(new EmptyBorder(12, 16, 12, 16));
	}

	public static void applyEnterpriseLookAndFeel() {
		try {
			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					return;
				}
			}
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			try {
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			} catch (Exception ignored) {
			}
		}
	}

	/**
	 * Sets palette + key Nimbus overrides. Call after {@link #applyEnterpriseLookAndFeel()}.
	 */
	public static void applyTheme(ThemePrefs.Id id) {
		currentThemeId = id;
		switch (id) {
		case dark:
			NAVY = new Color(52, 58, 74);
			NAVY_LIGHT = new Color(72, 84, 108);
			GOLD = new Color(212, 180, 95);
			GOLD_DIM = new Color(170, 145, 72);
			PAGE_BG = new Color(32, 36, 44);
			CARD_BG = new Color(48, 54, 66);
			TEXT_MUTED = new Color(185, 192, 205);
			TEXT_ON_DARK = new Color(245, 247, 250);
			TEXT_LABEL = new Color(232, 236, 245);
			PAGE_HEADING = new Color(240, 242, 248);
			PAGE_SECONDARY = new Color(200, 206, 218);
			DASH_PANEL_BG = new Color(40, 44, 54);
			INPUT_FIELD_BG = new Color(58, 64, 78);
			ERROR = new Color(232, 96, 110);
			MENU_BORDER = new Color(82, 92, 110);
			TABLE_GRID = new Color(60, 68, 82);
			TABLE_SELECTION_BG = new Color(72, 84, 120);
			BANNER_TAGLINE = new Color(185, 192, 205);
			BANNER_CORNER = new Color(130, 140, 158);
			putNimbus(id);
			break;
		case slate:
			NAVY = new Color(28, 48, 72);
			NAVY_LIGHT = new Color(42, 68, 98);
			GOLD = new Color(188, 158, 72);
			GOLD_DIM = new Color(150, 126, 58);
			PAGE_BG = new Color(220, 226, 234);
			CARD_BG = new Color(252, 253, 255);
			TEXT_MUTED = new Color(78, 88, 102);
			TEXT_ON_DARK = new Color(245, 247, 250);
			TEXT_LABEL = new Color(40, 46, 58);
			PAGE_HEADING = new Color(28, 48, 72);
			PAGE_SECONDARY = new Color(78, 88, 102);
			DASH_PANEL_BG = new Color(208, 214, 226);
			INPUT_FIELD_BG = Color.WHITE;
			ERROR = new Color(176, 32, 48);
			MENU_BORDER = new Color(198, 206, 218);
			TABLE_GRID = new Color(210, 218, 230);
			TABLE_SELECTION_BG = new Color(220, 228, 242);
			BANNER_TAGLINE = new Color(195, 205, 220);
			BANNER_CORNER = new Color(135, 150, 170);
			putNimbus(id);
			break;
		case light:
		default:
			NAVY = new Color(22, 42, 68);
			NAVY_LIGHT = new Color(35, 62, 98);
			GOLD = new Color(196, 162, 74);
			GOLD_DIM = new Color(160, 132, 58);
			PAGE_BG = new Color(236, 240, 245);
			CARD_BG = Color.WHITE;
			TEXT_MUTED = new Color(90, 100, 115);
			TEXT_ON_DARK = new Color(245, 247, 250);
			TEXT_LABEL = new Color(50, 55, 65);
			ERROR = new Color(176, 32, 48);
			MENU_BORDER = new Color(210, 218, 230);
			TABLE_GRID = new Color(226, 232, 240);
			TABLE_SELECTION_BG = new Color(232, 238, 248);
			BANNER_TAGLINE = new Color(200, 208, 220);
			BANNER_CORNER = new Color(150, 165, 185);
			PAGE_HEADING = new Color(22, 42, 68);
			PAGE_SECONDARY = new Color(90, 100, 115);
			DASH_PANEL_BG = new Color(226, 230, 237);
			INPUT_FIELD_BG = Color.WHITE;
			putNimbus(id);
			break;
		}
	}

	private static void putNimbus(ThemePrefs.Id id) {
		javax.swing.LookAndFeel lf = UIManager.getLookAndFeel();
		if (lf == null || !"Nimbus".equals(lf.getName())) {
			return;
		}
		if (id == ThemePrefs.Id.dark) {
			UIManager.put("control", new Color(48, 54, 66));
			UIManager.put("text", TEXT_LABEL);
			UIManager.put("Label.foreground", TEXT_LABEL);
			UIManager.put("nimbusFocus", new Color(GOLD.getRed(), GOLD.getGreen(), GOLD.getBlue(), 180));
			UIManager.put("ComboBox.background", CARD_BG);
			UIManager.put("ComboBox.foreground", TEXT_LABEL);
			UIManager.put("ComboBox.selectionBackground", NAVY_LIGHT);
			UIManager.put("ComboBox.selectionForeground", TEXT_ON_DARK);
			UIManager.put("TextField.background", INPUT_FIELD_BG);
			UIManager.put("TextField.foreground", TEXT_LABEL);
			UIManager.put("TextField.inactiveForeground", PAGE_SECONDARY);
			UIManager.put("FormattedTextField.background", INPUT_FIELD_BG);
			UIManager.put("FormattedTextField.foreground", TEXT_LABEL);
			UIManager.put("PasswordField.background", INPUT_FIELD_BG);
			UIManager.put("PasswordField.foreground", TEXT_LABEL);
			UIManager.put("TextArea.background", INPUT_FIELD_BG);
			UIManager.put("TextArea.foreground", TEXT_LABEL);
		} else {
			UIManager.put("control", new Color(245, 247, 250));
			UIManager.put("text", new Color(30, 35, 45));
			UIManager.put("Label.foreground", new Color(30, 35, 45));
			UIManager.put("nimbusFocus", new Color(GOLD.getRed(), GOLD.getGreen(), GOLD.getBlue(), 180));
			UIManager.put("ComboBox.background", Color.WHITE);
			UIManager.put("ComboBox.foreground", new Color(30, 35, 45));
			UIManager.put("ComboBox.selectionBackground", new Color(200, 210, 230));
			UIManager.put("ComboBox.selectionForeground", new Color(22, 42, 68));
			UIManager.put("TextField.background", Color.WHITE);
			UIManager.put("TextField.foreground", new Color(30, 35, 45));
			UIManager.put("FormattedTextField.background", Color.WHITE);
			UIManager.put("FormattedTextField.foreground", new Color(30, 35, 45));
			UIManager.put("PasswordField.background", Color.WHITE);
			UIManager.put("PasswordField.foreground", new Color(30, 35, 45));
			UIManager.put("TextArea.background", Color.WHITE);
			UIManager.put("TextArea.foreground", new Color(30, 35, 45));
		}
		UIManager.put("defaultFont", new FontUIResource(fontBody()));
	}

	/** Re-skin all Swing windows after a theme change. */
	public static void refreshAllWindowTrees() {
		for (Window w : Window.getWindows()) {
			if (w.isDisplayable()) {
				SwingUtilities.updateComponentTreeUI(w);
			}
		}
	}

	public static ImageIcon loadBankingLogo(int maxSize) {
		java.net.URL url = UITheme.class.getResource("/img/banking-logo.png");
		if (url == null) {
			url = UITheme.class.getResource("/img/1.png");
		}
		if (url == null) {
			return null;
		}
		ImageIcon raw = new ImageIcon(url);
		if (raw.getIconWidth() <= 0) {
			return null;
		}
		int w = raw.getIconWidth();
		int h = raw.getIconHeight();
		if (w <= maxSize && h <= maxSize) {
			return raw;
		}
		double scale = Math.min(maxSize / (double) w, maxSize / (double) h);
		int nw = Math.max(1, (int) (w * scale));
		int nh = Math.max(1, (int) (h * scale));
		java.awt.Image img = raw.getImage().getScaledInstance(nw, nh, java.awt.Image.SCALE_SMOOTH);
		return new ImageIcon(img);
	}

	public static JPanel createHeaderBanner(String productName, String tagline, int logoSize) {
		JPanel banner = new JPanel(new BorderLayout(16, 0));
		banner.setBackground(NAVY);
		banner.setBorder(new EmptyBorder(20, 28, 20, 28));

		JPanel left = new JPanel(new BorderLayout(16, 4));
		left.setOpaque(false);

		ImageIcon logoIcon = loadBankingLogo(logoSize);
		if (logoIcon != null) {
			JLabel pic = new JLabel(logoIcon);
			pic.setOpaque(false);
			left.add(pic, BorderLayout.WEST);
		} else {
			left.add(new EnterpriseLogo(logoSize), BorderLayout.WEST);
		}

		JPanel titles = new JPanel(new GridBagLayout());
		titles.setOpaque(false);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		JLabel h1 = new JLabel(productName);
		h1.setFont(fontTitle());
		h1.setForeground(TEXT_ON_DARK);
		titles.add(h1, c);
		c.gridy = 1;
		c.insets = new Insets(4, 0, 0, 0);
		JLabel sub = new JLabel(tagline);
		sub.setFont(fontBody());
		sub.setForeground(BANNER_TAGLINE);
		titles.add(sub, c);
		left.add(titles, BorderLayout.CENTER);

		banner.add(left, BorderLayout.CENTER);

		JLabel enterprise = new JLabel("Enterprise Console", SwingConstants.RIGHT);
		enterprise.setFont(fontSmall());
		enterprise.setForeground(BANNER_CORNER);
		banner.add(enterprise, BorderLayout.EAST);

		return banner;
	}

	public static JPanel createPageFooter(String text) {
		JPanel foot = new JPanel(new BorderLayout());
		foot.setBackground(PAGE_BG);
		foot.setBorder(new EmptyBorder(10, 28, 14, 28));
		JLabel l = new JLabel(text);
		l.setFont(fontSmall());
		l.setForeground(TEXT_MUTED);
		foot.add(l, BorderLayout.CENTER);
		return foot;
	}

	public static void stylePrimaryButton(javax.swing.JButton b) {
		b.setFont(fontButton());
		b.setBackground(NAVY_LIGHT);
		b.setForeground(TEXT_ON_DARK);
		b.setFocusPainted(false);
		b.setOpaque(true);
		b.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
	}

	public static void styleMenuActionButton(javax.swing.JButton b, int width) {
		b.setFont(fontButton());
		b.setHorizontalAlignment(SwingConstants.LEFT);
		b.setOpaque(true);
		b.setFocusPainted(false);
		if (isDarkTheme()) {
			b.setBackground(INPUT_FIELD_BG);
			b.setForeground(TEXT_LABEL);
		} else {
			b.setBackground(Color.WHITE);
			b.setForeground(PAGE_HEADING);
		}
		b.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(MENU_BORDER, 1),
				BorderFactory.createEmptyBorder(12, 16, 12, 16)));
		b.setPreferredSize(new Dimension(width, 46));
		b.setMaximumSize(new Dimension(width, 46));
		b.setAlignmentX(Component.CENTER_ALIGNMENT);
	}

	/** Outlined actions (Reset, Close, View report, …) on form surfaces. */
	public static void styleSecondaryButton(javax.swing.JButton b) {
		b.setFont(fontBody());
		b.setOpaque(true);
		b.setFocusPainted(false);
		if (isDarkTheme()) {
			b.setBackground(INPUT_FIELD_BG);
			b.setForeground(TEXT_LABEL);
		} else {
			b.setBackground(Color.WHITE);
			b.setForeground(PAGE_HEADING);
		}
		b.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(MENU_BORDER, 1),
				BorderFactory.createEmptyBorder(8, 16, 8, 16)));
	}

	public static JPanel wrapMenuButtonRow(javax.swing.JButton b) {
		JPanel row = new JPanel(new BorderLayout());
		row.setOpaque(false);
		row.add(b, BorderLayout.CENTER);
		return row;
	}
}
