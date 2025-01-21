import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.imageio.ImageIO;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicButtonUI;

class Landingpage extends JFrame {

    Landingpage() {
        // Set the frame properties
        setSize(1530, 830);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true); // Remove default frame decorations

        // Specify the path to your icon image
        String iconPath = "Images/icon.png"; // Update this path as necessary
        ImageIcon icon = new ImageIcon(iconPath);
        setIconImage(icon.getImage());

        // Create a background panel with an image
        BackgroundPanel backgroundPanel = new BackgroundPanel("Images/home.jpg");
        backgroundPanel.setLayout(null); // Use null layout for absolute positioning
        setContentPane(backgroundPanel);

        BackgroundPanel logo = new BackgroundPanel("Images/logo.png");
        logo.setLayout(new BorderLayout());
        logo.setBounds(40, 50, 100, 100);
        logo.setBackground(new Color(19, 7, 46));
        backgroundPanel.add(logo);

        // Creating the JLabel
        JLabel label = new JLabel();
        label.setText("<html><center>\"Connecting Talent<br>with Opportunity\"</center></html>");
        label.setFont(new Font("Courier", Font.BOLD, 34)); // Set font to Jim Nightshade, size 24
        label.setForeground(Color.white); // Set text color

        // Set bounds for the label (adjust as necessary)
        label.setBounds(150, 340, 400, 100);
        backgroundPanel.add(label);

        JButton recruitersignup = new JButton();
        recruitersignup.setText("SIGNUP AS RECRUITER");
        recruitersignup.setBounds(getWidth() - 500, 60, 200, 50); // Set the size and position to the right
        recruitersignup.setFont(new Font("Arial", Font.BOLD, 13));
        setButtonRounded(recruitersignup, 20, Color.white);
        recruitersignup.setForeground(Color.blue);
        backgroundPanel.add(recruitersignup);

        JButton candidatesignup = new JButton();
        candidatesignup.setText("SIGNUP AS CANDIDATE");
        candidatesignup.setBounds(getWidth() - 260, 60, 200, 50); // Set the size and position to the right
        candidatesignup.setFont(new Font("Arial", Font.BOLD, 13));
        setButtonRounded(candidatesignup, 20, Color.white);
        candidatesignup.setForeground(Color.blue);
        backgroundPanel.add(candidatesignup);


        //
        recruitersignup.addActionListener(e -> {
            new Recruiterregister();
        });
        candidatesignup.addActionListener(e -> {
            new Candidateregister();
        });

        // Create a panel for the custom title bar
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(new Color(8, 141, 165));
        titleBar.setPreferredSize(new Dimension(getWidth(), 45));

        // Create a label for the title text
        JLabel titleLabel = new JLabel("LANDING", SwingConstants.CENTER);
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
                setLocation(currCoords.x - mouseDownCompCoords.x, currCoords.y - mouseDownCompCoords.y);
            }
        };

        titleBar.addMouseListener(ma);
        titleBar.addMouseMotionListener(ma);

        // Add the title bar to the background panel
        titleBar.setBounds(0, 0, getWidth(), 45);
        backgroundPanel.add(titleBar);

        // Button actions
        minimizeButton.addActionListener(e -> setState(Frame.ICONIFIED));
        maximizeButton.addActionListener(e -> {
            if (getExtendedState() == JFrame.MAXIMIZED_BOTH) {
                setExtendedState(JFrame.NORMAL);
                maximizeButton.setText("⬜"); // Restore icon
            } else {
                setExtendedState(JFrame.MAXIMIZED_BOTH);
                maximizeButton.setText("❐"); // Maximize icon
            }
        });

        closeButton.addActionListener(e -> System.exit(0));

        // Create a panel for absolute positioning
        JPanel joinPanel = new JPanel(null);
        joinPanel.setOpaque(false); // Ensure it's transparent
        joinPanel.setBounds(0, 0, getWidth(), getHeight()); // Set bounds to fill the frame

        // Create and position the "JOIN US" button
        JButton joinButton = new JButton("LOG IN");
        joinButton.setBounds(180, 530, 190, 50);  // Adjusted position and size
        joinButton.setFont(new Font("Arial", Font.BOLD, 24));
        joinButton.setForeground(Color.WHITE); // Set text color

        // Set the rounded border and background color
        setButtonRounded(joinButton, 30, Color.BLUE);

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
                setButtonRounded(joinButton, 30, Color.BLUE); // Reset background color after hover
            }
        });
        joinButton.addActionListener(e -> {
            new login(this);
                }
                );
        candidatesignup.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                candidatesignup.setBackground(Color.white);
                candidatesignup.setForeground(Color.blue);// Set background color on hover
                setButtonRounded(candidatesignup, 0, Color.blue);
                candidatesignup.setForeground(Color.white);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                candidatesignup.setBackground(Color.white);
                candidatesignup.setForeground(Color.blue);// Set background color after hover
            }
        });

        recruitersignup.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                recruitersignup.setBackground(Color.white);
                recruitersignup.setForeground(Color.blue);// Set background color on hover
                setButtonRounded(recruitersignup, 0, Color.blue);
                recruitersignup.setForeground(Color.white);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                recruitersignup.setBackground(Color.white);
                recruitersignup.setForeground(Color.blue);// Set background color after hover
            }
        });
        try (Connection conn = dbconn.connection()){
            String query = "SELECT `id`, `recruiter`, `candidate`, `content`, `login` FROM `setting` WHERE 1";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                if (rs.next()) {
                    // Retrieve the values from the result set
                    String recruiterText = rs.getString("recruiter");
                    String candidateText = rs.getString("candidate");
                    String contentText = rs.getString("content");
                    String loginText = rs.getString("login");

                    // Set the text for the buttons and label
                    recruitersignup.setText(recruiterText);
                    candidatesignup.setText(candidateText);
                    label.setText("<html><center>\"" + contentText + "<br>with Opportunity\"</center></html>");
                    joinButton.setText(loginText);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Add the join panel to the background panel
        backgroundPanel.add(joinPanel);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 50, 50));
        // Make the frame visible
        setResizable(true); // Make the frame resizable
        setVisible(true);
    }

    private static void setButtonRounded(JButton button, int radius, Color backgroundColor) {
        button.setBackground(backgroundColor);
        button.setOpaque(false); // Make button non-opaque to avoid default background painting
        button.setFocusPainted(true);

        button.setContentAreaFilled(true); // Remove content area painting
       button.setBorder(new RoundedBorder(radius, backgroundColor)); // Set rounded border with background color
        button.setUI(new RoundedButtonUI());
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
        new Landingpage();
    }


}
