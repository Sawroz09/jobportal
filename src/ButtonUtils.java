import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class ButtonUtils {


    public static void setButtonRounded(JButton button, int radius, Color backgroundColor) {
        button.setBackground(backgroundColor);
        button.setOpaque(false); // Make button non-opaque to avoid default background painting
        button.setFocusPainted(true);

        button.setContentAreaFilled(true); // Remove content area painting
        button.setBorder(new RoundedBorder(radius, backgroundColor)); // Set rounded border with background color
        button.setUI(new RoundedButtonUI());
    }

    public static class RoundedBorder implements Border {
        private int radius;
        private Color backgroundColor;

        public RoundedBorder(int radius, Color backgroundColor) {
            this.radius = radius;
            this.backgroundColor = backgroundColor;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius, this.radius, this.radius, this.radius);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Set the clip to ensure text doesn't get clipped by the border
            Shape borderShape = new RoundRectangle2D.Double(x, y, width - 1, height - 1, radius, radius);
            g2.clip(borderShape);

            // Fill background
            // g2.setColor(backgroundColor);
            // g2.fill(borderShape);

            // Draw border
            g2.setColor(Color.BLACK); // Adjust border color if needed
            g2.draw(borderShape);

            g2.dispose();
        }
    }

    // Custom UI for rounded button
    public static class RoundedButtonUI extends BasicButtonUI {
        @Override
        protected void paintButtonPressed(Graphics g, AbstractButton b) {
            if (b.isContentAreaFilled()) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(b.getBackground().darker());
                g2.fillRoundRect(0, 0, b.getWidth(), b.getHeight(), 30, 30);
                g2.dispose();
            }
        }

        @Override
        public void paint(Graphics g, JComponent c) {
            AbstractButton b = (AbstractButton) c;
            paintBackground(g, b);
            super.paint(g, c);
        }

        private void paintBackground(Graphics g, AbstractButton b) {
            if (b.isContentAreaFilled()) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(b.getBackground());
                g2.fillRoundRect(0, 0, b.getWidth() - 1, b.getHeight() - 1, 30, 30); // Adjusted dimensions
                g2.dispose();
            }
        }
    }
}
