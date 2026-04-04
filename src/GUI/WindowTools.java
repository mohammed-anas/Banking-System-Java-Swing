package GUI;

import java.awt.Window;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Brings singleton tool windows to the foreground and de-iconifies them — avoids false “already
 * open” situations when a frame is behind others or minimized but still visible to Swing.
 */
final class WindowTools {

	private WindowTools() {
	}

	/**
	 * Shows the window (if needed), restores from minimized, and demands focus / stacking order.
	 * Safe to call from the EDT only; otherwise posts to EDT.
	 */
	static void showAndBringToFront(Window w) {
		if (w == null) {
			return;
		}
		Runnable r = () -> {
			if (w instanceof JFrame) {
				JFrame f = (JFrame) w;
				int state = f.getExtendedState();
				if ((state & JFrame.ICONIFIED) != 0) {
					f.setExtendedState(state & ~JFrame.ICONIFIED);
				}
			}
			w.setVisible(true);
			w.toFront();
			w.repaint();
			w.requestFocus();
		};
		if (SwingUtilities.isEventDispatchThread()) {
			r.run();
		} else {
			SwingUtilities.invokeLater(r);
		}
	}
}
