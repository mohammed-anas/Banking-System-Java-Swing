package GUI;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;

import javax.swing.JComponent;

/**
 * Vector “brand mark” when no PNG logo is on the classpath — enterprise shield + pillar motif.
 */
public class EnterpriseLogo extends JComponent {

	private static final long serialVersionUID = 1L;
	private final int preferred;

	public EnterpriseLogo(int preferredSize) {
		this.preferred = preferredSize;
		setOpaque(false);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(preferred, preferred);
	}

	@Override
	public Dimension getMinimumSize() {
		return getPreferredSize();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int s = Math.min(getWidth(), getHeight()) - 4;
		float x = (getWidth() - s) / 2f;
		float y = (getHeight() - s) / 2f;

		GeneralPath shield = new GeneralPath();
		shield.moveTo(x + s * 0.5f, y + s * 0.08f);
		shield.lineTo(x + s * 0.88f, y + s * 0.2f);
		shield.quadTo(x + s * 0.92f, y + s * 0.55f, x + s * 0.5f, y + s * 0.94f);
		shield.quadTo(x + s * 0.08f, y + s * 0.55f, x + s * 0.12f, y + s * 0.2f);
		shield.closePath();

		g2.setColor(UITheme.NAVY);
		g2.fill(shield);

		g2.setStroke(new BasicStroke(Math.max(1.5f, s * 0.04f), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g2.setColor(UITheme.GOLD);
		g2.draw(shield);

		float cx = x + s * 0.5f;
		float colW = s * 0.12f;
		g2.setColor(UITheme.GOLD);
		g2.fillRoundRect((int) (cx - colW / 2), (int) (y + s * 0.28f), (int) colW, (int) (s * 0.38f), 4, 4);
		g2.setColor(UITheme.NAVY);
		g2.fillRoundRect((int) (cx - colW * 0.35), (int) (y + s * 0.22f), (int) (colW * 0.7), (int) (s * 0.08f), 3, 3);

		g2.dispose();
	}
}
