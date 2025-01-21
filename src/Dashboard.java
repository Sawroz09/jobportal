import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicButtonUI;

public class Dashboard {

    Dashboard() {
        // Create the frame
        JFrame frame = new JFrame();
        frame.setSize(1530, 830);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Remove default frame decorations
        frame.setUndecorated(true);

        // Specify the path to your icon image
        String iconPath = "Images/icon.png"; // Update this path as necessary
        ImageIcon icon = new ImageIcon(iconPath);
        frame.setIconImage(icon.getImage());

        // Create a background panel with an image
        BackgroundPanel backgroundPanel = new BackgroundPanel("Images/3.png");
        backgroundPanel.setLayout(new BorderLayout());
        frame.setContentPane(backgroundPanel);

        // Load custom cursor image
        Image cursorImage = Toolkit.getDefaultToolkit().getImage("Images/custom_cursorr.png"); // Update with your cursor image path
        Cursor customCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImage, new Point(0, 0), "custom cursor");

        // Create a panel for the custom title bar
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(new Color(8, 141, 165));
        titleBar.setPreferredSize(new Dimension(frame.getWidth(), 45));

        // Create a label for the title text
        JLabel titleLabel = new JLabel("DASHBOARD", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        // Create buttons for minimize, maximize/restore, and close
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonPanel.setOpaque(false);

        JButton minimizeButton = createTitleBarButton("-");
        JButton maximizeButton = createTitleBarButton("⬜");
        JButton closeButton = createTitleBarButton("X");

        buttonPanel.add(minimizeButton);
        buttonPanel.add(maximizeButton);
        buttonPanel.add(closeButton);

        // Load the icon image and resize it
        try {
            BufferedImage originalIcon = ImageIO.read(new File(iconPath));
            Image resizedIcon = originalIcon.getScaledInstance(39, 45, Image.SCALE_SMOOTH);
            ImageIcon i = new ImageIcon(resizedIcon);

            // Add the icon to the title bar
            JLabel iconLabel = new JLabel(i);
            titleBar.add(iconLabel, BorderLayout.WEST);
        } catch (IOException e) {
            System.out.println("Icon file not found or invalid: " + iconPath);
        }

        // Add the title label and button panel to the title bar
        titleBar.add(titleLabel, BorderLayout.CENTER);
        titleBar.add(buttonPanel, BorderLayout.EAST);

        // Add drag functionality to the custom title bar
        MouseAdapter ma = new MouseAdapter() {
            private Point mouseDownCompCoords = null;

            @Override
            public void mouseReleased(MouseEvent e) {
                mouseDownCompCoords = null;
            }

            @Override
            public void mousePressed(MouseEvent e) {
                mouseDownCompCoords = e.getPoint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                Point currCoords = e.getLocationOnScreen();
                frame.setLocation(currCoords.x - mouseDownCompCoords.x, currCoords.y - mouseDownCompCoords.y);
            }
        };

        titleBar.addMouseListener(ma);
        titleBar.addMouseMotionListener(ma);

        // Add the title bar to the background panel
        backgroundPanel.add(titleBar, BorderLayout.NORTH);

        // Button actions
        minimizeButton.addActionListener(e -> frame.setState(Frame.ICONIFIED));
        maximizeButton.addActionListener(e -> {
            if (frame.getExtendedState() == JFrame.MAXIMIZED_BOTH) {
                frame.setExtendedState(JFrame.NORMAL);
                maximizeButton.setText("⬜"); // Restore icon
            } else {
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                maximizeButton.setText("❐"); // Maximize icon
            }
        });
        closeButton.addActionListener(e -> System.exit(0));

        // Create a panel for absolute positioning
        JPanel joinPanel = new JPanel(null);
        joinPanel.setOpaque(false);

        // Create and position the "JOIN US" button
        JButton joinButton = new JButton("JOIN US");
        joinButton.setBounds(180, 530, 190, 50);  // Adjusted position and size
        joinButton.setFont(new Font("Arial", Font.BOLD, 24));
        joinButton.setForeground(Color.WHITE);

        // Set the rounded border and background color
        setButtonRounded(joinButton, 30, Color.blue);

        // Set the icon for the button (after text)
        String imagePath = "Images/login1.png"; // Update with your icon path
        ImageIcon buttonIcon = new ImageIcon(imagePath);
        joinButton.setHorizontalTextPosition(SwingConstants.LEADING); // Position text before icon
        joinButton.setIcon(buttonIcon);

        joinPanel.add(joinButton);

        // Add mouse listener for hover effect
        joinButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                joinButton.setBackground(new Color(19, 30, 40)); // Set background color on hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                setButtonRounded(joinButton, 30, Color.blue); // Reset background color after hover
            }
        });
        joinButton.addActionListener(e -> {
            // Hide the current frame (Dashboard)
            frame.setVisible(false);

            // Create and show the Landingpage frame
            Landingpage l = new Landingpage();
            l.setVisible(true);
        });

        // Add the join panel to the background panel
        backgroundPanel.add(joinPanel);

        // Set the cursor for the dashboard area
        backgroundPanel.setCursor(customCursor);

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                frame.setShape(new RoundRectangle2D.Double(0, 0, frame.getWidth(), frame.getHeight(), 50, 50));
            }
        });

        // Make the frame visible
        frame.setResizable(true);
        frame.setVisible(true);
    }

    private static void setButtonRounded(JButton button, int radius, Color backgroundColor) {
        button.setBackground(backgroundColor);
        button.setOpaque(false); // Make button non-opaque to avoid default background painting
        button.setFocusPainted(true);

        button.setContentAreaFilled(true); // Remove content area painting
        button.setBorder(new Landingpage.RoundedBorder(radius, backgroundColor)); // Set rounded border with background color
        button.setUI(new Landingpage.RoundedButtonUI());
    }

    private static JButton createTitleBarButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(45, 30));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        return button;
    }

    static class RoundedBorder implements Border {
        private int radius;
        private Color backgroundColor;

        RoundedBorder(int radius, Color backgroundColor) {
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

            // Draw border
            g2.setColor(Color.BLACK); // Adjust border color if needed
            g2.draw(borderShape);

            g2.dispose();
        }
    }

    // Custom UI for rounded button
    static class RoundedButtonUI extends BasicButtonUI {
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

    public static void main(String[] args) {
        new Dashboard();
    }
}