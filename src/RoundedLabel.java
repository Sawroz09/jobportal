import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedLabel extends JLabel {
    private static final int ARC_WIDTH = 20; // Width of the rounded corners
    private static final int ARC_HEIGHT = 20; // Height of the rounded corners

    public RoundedLabel(String text) {
        super(text);
        setOpaque(false); // Ensure background is not painted by default
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Create a graphics2D object for better control over the painting
        Graphics2D g2d = (Graphics2D) g.create();
        try {
            // Set anti-aliasing for smooth edges
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Define the rounded rectangle shape
            RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(
                    0, 0, getWidth(), getHeight(), ARC_WIDTH, ARC_HEIGHT
            );

            // Set the background color
            g2d.setColor(new Color(200, 200, 200)); // Change color as needed
            g2d.fill(roundedRectangle);

            // Set the text color and draw the text
            g2d.setColor(Color.BLACK); // Change text color as needed
            super.paintComponent(g2d);
        } finally {
            g2d.dispose();
        }
    }

}
