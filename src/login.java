import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.imageio.ImageIO;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicButtonUI;

class login extends JFrame {
    private JTextField nameField;
    private JPasswordField passField;
    private JComboBox userField;

    private JFrame Landingpage; // Declare Landingpage as a class member

    login(JFrame landingPage) {
        this.Landingpage = landingPage; // Initialize Landingpage
        setSize(860, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);

        // Create a background panel with an image
        BackgroundPanel backgroundPanel = new BackgroundPanel("Images/login.png");
        backgroundPanel.setLayout(null); // Use null layout for absolute positioning
        setContentPane(backgroundPanel);

        // Create a panel for the custom title bar
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(new Color(8, 141, 165));
        titleBar.setBounds(0, 0, getWidth(), 45); // Set bounds for the title bar

        // Create a label for the title text
        JLabel titleLabel = new JLabel("LOGIN", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        // Create buttons for minimize, maximize/restore, and close
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonPanel.setOpaque(false);

        JButton closeButton = createTitleBarButton("X");
        buttonPanel.add(closeButton);

        // Add the title label and button panel to the title bar
        titleBar.add(titleLabel, BorderLayout.CENTER);
        titleBar.add(buttonPanel, BorderLayout.EAST);

        // Specify the path to your icon image
        String iconPath = "Images/icon.png"; // Update this path as necessary
        ImageIcon icon = new ImageIcon(iconPath);
        setIconImage(icon.getImage());

        // Load the icon image and resize it
        try {
            BufferedImage originalIcon = ImageIO.read(new File(iconPath));
            Image resizedIcon = originalIcon.getScaledInstance(45, 40, Image.SCALE_SMOOTH);
            ImageIcon icon1 = new ImageIcon(resizedIcon);

            // Add the icon to the title bar
            JLabel iconLabel = new JLabel(icon1);
            titleBar.add(iconLabel, BorderLayout.WEST);
        } catch (IOException e) {
            System.out.println("Icon file not found or invalid: " + iconPath);
        }

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

        // Button actions
        closeButton.addActionListener(e ->  setVisible(false));

        // Create a panel to hold the custom title bar and content
        JPanel contentPanel = new JPanel(null); // Use null layout for absolute positioning
        contentPanel.setOpaque(false); // Make the content panel transparent
        contentPanel.setBounds(0, 0, getWidth(), getHeight()); // Set bounds for the content panel

        // Add the title bar to the content panel
        contentPanel.add(titleBar);

        // Add the "RECRUITER REGISTRATION" label
        JLabel reg = new JLabel("SIGNIN");
        reg.setFont(new Font("Arial", Font.BOLD, 24));
        reg.setForeground(Color.WHITE); // Set text color
        reg.setBounds(399, 40, 350, 50); // Set x, y, width, and height
        contentPanel.add(reg);

        // Add the "NAME" label
        JLabel welcometext = new JLabel("Welcome ");
        welcometext.setFont(new Font("Arial", Font.BOLD, 24));
        welcometext.setForeground(Color.WHITE); // Set text color
        welcometext.setBounds(590, 100, 350, 50); // Set x, y, width, and height
        contentPanel.add(welcometext);


        // Add the "NAME" label
        JLabel username = new JLabel("Username ");
        username.setFont(new Font("Arial", Font.BOLD, 16));
        username.setForeground(Color.WHITE); // Set text color
        username.setBounds(510, 140, 350, 50); // Set x, y, width, and height
        contentPanel.add(username);

        // Add the "NAME" text field
        nameField = new JTextField();
        nameField.setFont(new Font("Arial", Font.BOLD, 14));
        nameField.setForeground(Color.BLACK); // Set text color
        nameField.setBounds(510, 185, 200, 30); // Set x, y, width, and height
        contentPanel.add(nameField);

        // Add the "NAME" label
        JLabel Password = new JLabel("Password ");
        Password.setFont(new Font("Arial", Font.BOLD, 16));
        Password.setForeground(Color.WHITE); // Set text color
        Password.setBounds(510, 220, 350, 50); // Set x, y, width, and height
        contentPanel.add(Password);

        // Add the "NAME" text field
        passField = new JPasswordField();
        passField.setFont(new Font("Arial", Font.BOLD, 14));
        passField.setForeground(Color.BLACK); // Set text color
        passField.setBounds(510, 265, 200, 30); // Set x, y, width, and height
        contentPanel.add(passField);


// Add the "UserType" label
        JLabel usertype = new JLabel("UserType ");
        usertype.setFont(new Font("Arial", Font.BOLD, 16));
        usertype.setForeground(Color.WHITE); // Set text color
        usertype.setBounds(510, 290, 350, 50); // Set x, y, width, and height
        contentPanel.add(usertype);
        // Add the "UserType" combo box
        userField = new JComboBox<>();
        userField.setFont(new Font("Arial", Font.BOLD, 14));
        userField.setForeground(Color.BLUE); // Set text color
        userField.setBackground(Color.WHITE); // Set background color
        userField.setBounds(510, 335, 200, 30); // Set x, y, width, and height

// Add options to the combo box
        userField.addItem("ADMIN");
        userField.addItem("CANDIDATE");
        userField.addItem("RECRUITER");

// Customize the rendering of the combo box items
        userField.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setFont(new Font("Arial", Font.BOLD, 14)); // Set font bold
                label.setForeground(Color.BLACK); // Set dropdown text color black
                label.setBackground(isSelected ? Color.LIGHT_GRAY : Color.WHITE); // Set background color for selected and non-selected items
                return label;
            }
        });

// Add the combo box to the content panel
        contentPanel.add(userField);


        // registerbutton


        // Load and scale the image
        String imagePath = "Images/loginc.png"; // Update with your icon path
        ImageIcon originalIcon = new ImageIcon(imagePath);
        Image originalImage = originalIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(300, 250, Image.SCALE_SMOOTH);

        // Create a new ImageIcon with the scaled image
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel imageLabel = new JLabel(scaledIcon);
        imageLabel.setBounds(10, 90, 500, 500); // Set the bounds for the image label
        contentPanel.add(imageLabel);
        // Add the content panel to the frame
        backgroundPanel.add(contentPanel);



// Create a panel for absolute positioning


        // Create and position the "JOIN US" button
        JButton joinButton = new JButton("LOG IN");
        joinButton.setBounds(540, 390, 170, 40);  // Adjusted position and size
        joinButton.setFont(new Font("Arial", Font.BOLD, 17));
        joinButton.setForeground(Color.WHITE); // Set text color

        // Set the rounded border and background color
        setButtonRounded(joinButton, 30, Color.BLUE);

        // Set the icon for the button (after text)
        String iPath = "Images/login1.png"; // Update with your icon path
        ImageIcon buttonIcon = new ImageIcon(iPath);
        joinButton.setHorizontalTextPosition(SwingConstants.LEADING); // Position text before icon
        joinButton.setIcon(buttonIcon);

        contentPanel.add(joinButton);

        // Add mouse listener for hover effect
        joinButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                joinButton.setBackground(new Color(19, 30, 40)); // Set background color on hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                joinButton.setBackground(Color.lightGray); // Set background color on hover
            }
        });
        joinButton.addActionListener(e -> {
                loginUser();
                }
        );

      //  ButtonUtils.setButtonRounded(Exitbutton, 30, Color.red);






        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 50, 50));
        // Make the frame visible
        setResizable(true); // Make the frame resizable
        setVisible(true);
    }
    private void loginUser() {
        String name = nameField.getText();
        String password = new String(passField.getPassword());
        if (name.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter all fields");
            return;
        }
        // Add your login logic here
        Connection conn = dbconn.connection();
        try {
            // Getting input values
            String userEmail = nameField.getText();
            String userPass = new String(passField.getPassword()); // getPassword returns a char array, not a String
            String Utype = (String) userField.getSelectedItem();  // Assuming Utype holds the role (e.g., 'admin', 'user', 'recruiter')

// Query for admins table
            String adminSql = "SELECT * FROM admins WHERE email = ? AND password = ? AND role = ?";
            PreparedStatement adminPstmt = conn.prepareStatement(adminSql);
            adminPstmt.setString(1, userEmail);
            adminPstmt.setString(2, userPass);
            adminPstmt.setString(3, Utype);
            ResultSet adminRs = adminPstmt.executeQuery();

// Query for users table
            String userSql = "SELECT * FROM users WHERE email = ? AND password = ? AND role = ?";
            PreparedStatement userPstmt = conn.prepareStatement(userSql);
            userPstmt.setString(1, userEmail);
            userPstmt.setString(2, userPass);
            userPstmt.setString(3, Utype);
            ResultSet userRs = userPstmt.executeQuery();

// Query for recruiters table
            String recruiterSql = "SELECT * FROM recruiters WHERE email = ? AND password = ? AND role = ?";
            PreparedStatement recruiterPstmt = conn.prepareStatement(recruiterSql);
            recruiterPstmt.setString(1, userEmail);
            recruiterPstmt.setString(2, userPass);
            recruiterPstmt.setString(3, Utype);
            ResultSet recruiterRs = recruiterPstmt.executeQuery();

// Check results
            if (adminRs.next()) {
                Session1 session = Session1.getInstance();
                session.setUserName(userEmail);
                //session.setUserType(Utype);
                session.setRecruiterId(adminRs.getInt("id")); // Assuming 'id' is the column name for user ID




                session.setUserName(adminRs.getString("name"));
                // Hide the login frame
                this.setVisible(false); // this refers to the current JFrame (login)

                if (Landingpage != null) {
                    Landingpage.setVisible(false);
                }
              Adminpanel obj=new Adminpanel();
              obj.setVisible(true);
            } else if (userRs.next()) {



                Session2 session = Session2.getInstance();
               // Session1.setUserName(userEmail);
// Assuming 'id' is the column name for user ID and 'name' is the column name for user name
             session.setUserId(userRs.getInt("id")); // Correctly set the user ID
               session.setCanName(userRs.getString("name"));
               session.setQualification(userRs.getString("qualification"));
                session.setEmail(userRs.getString("email"));// Correctly set the user name
                // User is a regular user
                // Hide the login frame
                this.setVisible(false); // this refers to the current JFrame (login)

                if (Landingpage != null) {
                    Landingpage.setVisible(false);
                }
                CandidateDashboard obj=new CandidateDashboard();
                obj.setVisible(true);
            } else if (recruiterRs.next()) {
                // User is a recruiter

                     Session3 session = Session3.getInstance();
                     session.setUserId(recruiterRs.getInt("id")); // Correctly set the user ID
                     session.setRecName(recruiterRs.getString("name"));
                     session.setRecEmail(recruiterRs.getString("email"));
                // Check the status of the recruiter
                int status = recruiterRs.getInt("status"); // Retrieve status from result set

                if (status == 0) { // Assuming status is an integer column
                    // Inactive recruiter
                    JOptionPane.showMessageDialog(null, "You have been marked as inactive, please contact customer service!");
                } else {
                     this.setVisible(false); // this refers to the current JFrame (login)

                     if (Landingpage != null) {
                         Landingpage.setVisible(false);
                     }
                     RecruiterDashboard obj = new RecruiterDashboard();
                     obj.setVisible(true);
                 }
            } else {
                // No match found
              JOptionPane.showMessageDialog(this,"Invalid login credentials or role!");
            }

// Close resources
            adminRs.close();
            adminPstmt.close();
            userRs.close();
            userPstmt.close();
            recruiterRs.close();
            recruiterPstmt.close();
        }catch (Exception e){
            System.out.println(e);
        }
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
        JFrame landingPage = new Landingpage();
        landingPage.setVisible(true); // Ensure the landing page is visible before login
        new login(landingPage);
    }


}

