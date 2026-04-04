import java.awt.EventQueue;

import GUI.GUIForm;
import GUI.ThemePrefs;
import GUI.UITheme;

public class Application {
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UITheme.applyEnterpriseLookAndFeel();
					UITheme.applyTheme(ThemePrefs.load());
				} catch (Exception e) {
					/* fall back to default LAF */
				}
				try {
					GUIForm.login.frame.setVisible(true);
				} catch (Exception e) {
					System.err.println("The application could not start. If this continues, reinstall Java or contact support.");
				}
			}
		});
	}
}
