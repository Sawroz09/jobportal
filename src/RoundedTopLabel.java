import javax.swing.*;
import java.awt.*;

public class RoundedTopLabel extends JLabel {

    private int arcWidth;
    private int arcHeight;

    public RoundedTopLabel(Icon icon, String text, int arcWidth, int arcHeight) {
        super(text, icon, CENTER); // Align text and icon to the center
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
        setOpaque(false); // Make the background transparent for custom painting
        setHorizontalAlignment(CENTER); // Center the text and icon horizontally
        setVerticalAlignment(CENTER); // Center the text and icon vertically
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // Draw the rounded rectangle at the top only
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, width, height + arcHeight, arcWidth, arcHeight);
        g2.fillRect(0, arcHeight, width, height - arcHeight); // Fill the bottom part to make it rectangular

        super.paintComponent(g2);
        g2.dispose();
    }
}
