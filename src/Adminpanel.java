import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicButtonUI;

class Adminpanel extends JFrame {

    private CardLayout cardLayout;
    private JPanel contentPanel;
    private JLabel Home, ViewJob, Candidate,recruiter,report;

    Adminpanel() {
        // Set the frame properties
        setSize(1530, 830);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true); // Remove default frame decorations

        // Specify the path to your icon image
        String iconPath = "Images/icon.png"; // Update this path as necessary
        ImageIcon icon = new ImageIcon(iconPath);
        setIconImage(icon.getImage());

        // Create a background panel with an image
        BackgroundPanel backgroundPanel = new BackgroundPanel("Images/reg1.png");
        backgroundPanel.setLayout(null); // Use null layout for absolute positioning
        setContentPane(backgroundPanel);

        BackgroundPanel logo = new BackgroundPanel("Images/logo.png");
        logo.setLayout(new BorderLayout());
        logo.setBounds(50, 50, 100, 100);
        logo.setBackground(new Color(19, 7, 46));
        backgroundPanel.add(logo);

        // Create a panel for the custom title bar
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(new Color(8, 141, 165));
        titleBar.setPreferredSize(new Dimension(getWidth(), 45));

        // Create a label for the title text
        JLabel titleLabel = new JLabel("Adminpanel", SwingConstants.CENTER);
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
        backgroundPanel.add(joinPanel);

        // Initialize CardLayout and content panel
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBounds(200, 150, 1200, 600);
        contentPanel.setOpaque(false); // Set content panel to be transparent
        joinPanel.add(contentPanel);
        // Get the user's name from the session
        Session1 session1 = Session1.getInstance();
        String userName = session1.getUserName();
        // Create "Home" panel using the new HomePanel class
        HomePanel homePanel = new HomePanel(userName);
        Candidatepanel candidatePanel = new Candidatepanel(userName);
        RecruiterPanel recruiterPanel = new RecruiterPanel(userName);
        ReportPanel reportPanel = new ReportPanel(userName);
        JOBLIST JOBLIST = new JOBLIST();
        Setting settingPanel = new Setting();




        // Add panels to the content panel
        contentPanel.add(homePanel, "Home");
        contentPanel.add(JOBLIST, "ViewJob");
        contentPanel.add(candidatePanel, "candidate");
        contentPanel.add(recruiterPanel, "recruiter");
        contentPanel.add(reportPanel, "report");
        contentPanel.add(settingPanel, "Settingpanel1");

        // Add the "Home" label
        Home = new JLabel("Home");
        Home.setFont(new Font("Arial", Font.BOLD, 26));
        Home.setForeground(Color.CYAN); // Highlighted as default
        Home.setBounds(200, 80, 150, 50);
        joinPanel.add(Home);






        // Add the "View Job" label
        ViewJob = new JLabel("View Job");
        ViewJob.setFont(new Font("Arial", Font.BOLD, 26));
        ViewJob.setForeground(Color.WHITE);
        ViewJob.setBounds(360, 80, 150, 50);
        joinPanel.add(ViewJob);

        // Add the "View Job" label
        Candidate = new JLabel("Candidate report");
        Candidate.setFont(new Font("Arial", Font.BOLD, 26));
        Candidate.setForeground(Color.WHITE);
        Candidate.setBounds(530, 80, 250, 50);
        joinPanel.add(Candidate);
        // Add the "View Job" label
        recruiter = new JLabel("Recruiter report");
        recruiter.setFont(new Font("Arial", Font.BOLD, 26));
        recruiter.setForeground(Color.WHITE);
        recruiter.setBounds(790, 80, 250, 50);
        joinPanel.add(recruiter);


        // Add the "View Job" label
        report = new JLabel("Report");
        report.setFont(new Font("Arial", Font.BOLD, 26));
        report.setForeground(Color.WHITE);
        report.setBounds(1060, 80, 150, 50);
        joinPanel.add(report);
        // Add the "View Job" label
        // Create the label and set the icon
        JLabel settingLabel = new JLabel("Setting");
        ImageIcon originalIcon = new ImageIcon("Images/s.png"); // Replace with the path to your icon image

// Resize the icon
        Image originalImage = originalIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);

// Set the resized icon on the label
        settingLabel.setIcon(resizedIcon);
        settingLabel.setBounds(1210, 80, 40, 40); // Adjust bounds to fit the icon
        joinPanel.add(settingLabel);
// Add mouse event listener to change the icon on click and release
        settingLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Change icon when the label is clicked
                ImageIcon clickedIcon = new ImageIcon("Images/s2.png"); // Replace with the path to the new icon image
                Image clickedImage = clickedIcon.getImage();
                Image resizedClickedImage = clickedImage.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                ImageIcon resizedClickedIcon = new ImageIcon(resizedClickedImage);
                settingLabel.setIcon(resizedClickedIcon);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // Create a Timer to revert to the original icon after 5 seconds
                Timer timer = new Timer(5000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        // Revert to the original icon after the delay
                        settingLabel.setIcon(resizedIcon);
                    }
                });
                timer.setRepeats(false); // Make sure the timer only runs once
                timer.start(); // Start the timer
            }
        });



        // Action listeners for labels
        Home.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                resetLabelColors();
                cardLayout.show(contentPanel, "Home");
                Home.setForeground(Color.CYAN); // Highlight the clicked label
            }
        });

        ViewJob.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                resetLabelColors();
                cardLayout.show(contentPanel, "ViewJob");
                ViewJob.setForeground(Color.CYAN); // Highlight the clicked label
            }
        });

        Candidate.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                resetLabelColors();
                cardLayout.show(contentPanel, "candidate");
                Candidate.setForeground(Color.CYAN); // Highlight the clicked label
            }
        });

        recruiter.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                resetLabelColors();
                cardLayout.show(contentPanel, "recruiter");
                recruiter.setForeground(Color.CYAN); // Highlight the clicked label
            }
        });

        report.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                resetLabelColors();
                cardLayout.show(contentPanel, "report");
                report.setForeground(Color.CYAN); // Highlight the clicked label
            }
        });

        settingLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                resetLabelColors();
                cardLayout.show(contentPanel, "Settingpanel1");
                settingLabel.setForeground(Color.CYAN); // Highlight the clicked label
            }
        });


        // Profile button
        JButton profile = new JButton("Profile");
        profile.setFont(new Font("Arial", Font.BOLD, 15));
        profile.setForeground(Color.WHITE);
        profile.setBounds(1330, 70, 160, 30);

// Set the icon for the button (before text)
        String imgPath = "Images/profile.png";
        ImageIcon bttnIcon = new ImageIcon(imgPath);
        profile.setIcon(bttnIcon);
        profile.setHorizontalTextPosition(SwingConstants.RIGHT);
        profile.setIconTextGap(10); // Space between icon and text

        joinPanel.add(profile);

// Set the rounded border and background color
        setButtonRounded(profile, 20, new Color(0, 0, 0, 0)); // Transparent background

// Create the dropdown menu with a transparent background
        JPopupMenu profileMenu = new JPopupMenu() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f)); // Transparency level
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
            }
        };
        profileMenu.setBackground(new Color(0, 0, 0, 0)); // Transparent background
        profileMenu.setBorder(BorderFactory.createEmptyBorder()); // Remove border

// Create the logout menu item
        JMenuItem logoutItem = new JMenuItem("Logout");

// Add icon to logout menu item
        ImageIcon logoutIcon = new ImageIcon("Images/logout.png"); // Add your logout image path here
        logoutItem.setIcon(logoutIcon);

// Customize the logout item appearance
        logoutItem.setOpaque(false);
        logoutItem.setBackground(new Color(0, 0, 0, 0)); // Transparent background
        logoutItem.setForeground(Color.WHITE);

// Add a mouse listener to change foreground color to red when clicked
        logoutItem.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                logoutItem.setForeground(Color.RED); // Change to red when clicked
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                logoutItem.setForeground(Color.WHITE); // Change back to white when released
            }
        });

        logoutItem.addActionListener(e -> {
            setVisible(false);
            Landingpage obj=new Landingpage();
            obj.setVisible(true);
        });

        profileMenu.add(logoutItem);

// Show the dropdown menu from the right side of the profile button when clicked
        profile.addActionListener(e -> profileMenu.show(profile, profile.getWidth() - profileMenu.getPreferredSize().width, profile.getHeight()));




        // Set the button text with dynamic width calculation
        profile.setText(userName);
        profile.setPreferredSize(new Dimension(profile.getPreferredSize().width + 20, profile.getPreferredSize().height)); // Adjust width as needed







        // Add the join panel to the background panel
        backgroundPanel.add(joinPanel);
        // Add the join panel to the background panel
        backgroundPanel.add(joinPanel);

        // Add mouse listener for hover effect on profile button
        profile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                profile.setBackground(new Color(255, 177, 105)); // Set background color on hover
                profile.setForeground(Color.cyan);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                profile.setBackground(new Color(19, 7, 46)); // Reset background color when not hovering
                profile.setForeground(Color.WHITE); // Reset text color when not hovering
            }
        });

        // Set the frame to be rounded
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 50, 50));
        setLocationRelativeTo(null); // Center the frame
        setVisible(true);
    }
    // Function to reset the label colors
    private void resetLabelColors() {
        Home.setForeground(Color.WHITE);
        ViewJob.setForeground(Color.WHITE);
        Candidate.setForeground(Color.WHITE);
        recruiter.setForeground(Color.WHITE);
        report.setForeground(Color.WHITE);

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
        new Adminpanel();
    }


}