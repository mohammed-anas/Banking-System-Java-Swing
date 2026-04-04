package GUI;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Persists UI theme choice under the user home directory.
 */
public final class ThemePrefs {

	public enum Id {
		light, dark, slate
	}

	private static final String KEY_THEME = "theme";
	private static final String DIR = ".apextrust-banking";
	private static final String FILE = "prefs.properties";

	private ThemePrefs() {
	}

	private static Path prefsPath() {
		return Paths.get(System.getProperty("user.home"), DIR, FILE);
	}

	public static Id load() {
		Path p = prefsPath();
		if (!Files.isRegularFile(p)) {
			return Id.light;
		}
		Properties props = new Properties();
		try (InputStream in = Files.newInputStream(p)) {
			props.load(in);
		} catch (Exception e) {
			return Id.light;
		}
		try {
			return Id.valueOf(props.getProperty(KEY_THEME, "light").trim().toLowerCase());
		} catch (Exception e) {
			return Id.light;
		}
	}

	public static void save(Id id) {
		Path p = prefsPath();
		try {
			Files.createDirectories(p.getParent());
			Properties props = new Properties();
			props.setProperty(KEY_THEME, id.name());
			try (OutputStream out = Files.newOutputStream(p)) {
				props.store(out, "ApexTrust Banking UI");
			}
		} catch (Exception ignored) {
		}
	}
}
