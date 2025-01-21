import javax.imageio.ImageIO;
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
import java.sql.*;
class Updatejob extends JFrame {
    private JTextField titleField;
    private JTextArea descriptionArea; // Changed to JTextArea
    private JTextField categoryField;
    private JTextField locationField;
    private JTextField salaryField;
    private JTextField vacancyField;
    private JTextField experienceField;
    private JTextField jobTypeField;
    private JTextField postDateField;
    private JTextField statusField;
    private JButton updateButton;
    private int jobId;

    public Updatejob(int jobId) {
        this.jobId = jobId;
        setTitle("Update Job");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true); // Remove default frame decorations
        setResizable(false); // Disable resizing of the frame
        setLocationRelativeTo(null); // Center the frame on the screen

        // Specify the path to your icon image
        String iconPath = "Images/icon.png"; // Update this path as necessary
        ImageIcon icon = new ImageIcon(iconPath);
        setIconImage(icon.getImage());

        // Create a background panel with an image
        BackgroundPanel backgroundPanel = new BackgroundPanel("Images/reg1.png");
        backgroundPanel.setLayout(null); // Use null layout for absolute positioning
        setContentPane(backgroundPanel);

        // Create a panel for the custom title bar
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(new Color(8, 141, 165));
        titleBar.setPreferredSize(new Dimension(getWidth(), 45));

        // Create a label for the title text
        JLabel titleLabel = new JLabel("Update job application", SwingConstants.CENTER);
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

        closeButton.addActionListener(e -> setVisible(false));

        // Create form labels and text fields with absolute positioning
        // First column of fields
        int baseY = 200; // Initial y position for the first field
        int yIncrement = 50; // Increment for each subsequent field

        JLabel titleLabelForm = new JLabel("Title:");
        titleLabelForm.setBounds(50, baseY, 100, 30);
        titleLabelForm.setForeground(Color.WHITE);
        titleLabelForm.setFont(new Font("Arial", Font.BOLD, 17));
        backgroundPanel.add(titleLabelForm);

        titleField = new JTextField();
        titleField.setBounds(150, baseY, 300, 30);
        backgroundPanel.add(titleField);

        JLabel descriptionLabel = new JLabel("Description:");
        descriptionLabel.setBounds(50, baseY + yIncrement, 100, 30);
        descriptionLabel.setForeground(Color.WHITE);
        descriptionLabel.setFont(new Font("Arial", Font.BOLD, 17));
        backgroundPanel.add(descriptionLabel);

        descriptionArea = new JTextArea(); // Use JTextArea for description
        descriptionArea.setBounds(150, baseY + yIncrement, 300, 80); // Larger height for text area
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descriptionArea); // Add scroll pane for JTextArea
        scrollPane.setBounds(150, baseY + yIncrement, 300, 80);
        backgroundPanel.add(scrollPane);


        JLabel locationLabel = new JLabel("Location:");
        locationLabel.setBounds(50, baseY + yIncrement * 3+10, 100, 30);
        locationLabel.setForeground(Color.WHITE);
        locationLabel.setFont(new Font("Arial", Font.BOLD, 17));
        backgroundPanel.add(locationLabel);

        locationField = new JTextField();
        locationField.setBounds(150, baseY + yIncrement * 3+10, 300, 30);
        backgroundPanel.add(locationField);

        // Second column of fields (after the break)
        JLabel salaryLabel = new JLabel("Salary:");
        salaryLabel.setBounds(500, baseY, 100, 30);
        salaryLabel.setForeground(Color.WHITE);
        salaryLabel.setFont(new Font("Arial", Font.BOLD, 17));
        backgroundPanel.add(salaryLabel);

        salaryField = new JTextField();
        salaryField.setBounds(600, baseY, 300, 30);
        backgroundPanel.add(salaryField);

        JLabel vacancyLabel = new JLabel("Vacancy:");
        vacancyLabel.setBounds(500, baseY + yIncrement, 100, 30);
        vacancyLabel.setForeground(Color.WHITE);
        vacancyLabel.setFont(new Font("Arial", Font.BOLD, 17));
        backgroundPanel.add(vacancyLabel);

        vacancyField = new JTextField();
        vacancyField.setBounds(600, baseY + yIncrement, 300, 30);
        backgroundPanel.add(vacancyField);

        JLabel experienceLabel = new JLabel("Experience:");
        experienceLabel.setBounds(500, baseY + yIncrement * 2, 100, 30);
        experienceLabel.setForeground(Color.WHITE);
        experienceLabel.setFont(new Font("Arial", Font.BOLD, 17));
        backgroundPanel.add(experienceLabel);

        experienceField = new JTextField();
        experienceField.setBounds(600, baseY + yIncrement * 2, 300, 30);
        backgroundPanel.add(experienceField);



      // Update button with custom position
                updateButton = new JButton("Update");
        updateButton.setBounds(450, 440, 150, 40);
        updateButton.setForeground(Color.WHITE);
        updateButton.setFont(new Font("Arial", Font.BOLD, 19));
        setButtonRounded(updateButton, 20,new Color(82, 190, 128)); // Transparent background

// Add hover effect to the button
        updateButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                updateButton.setBackground(new Color(82, 170, 128)); // Darker color on hover
                updateButton.setForeground(Color.YELLOW); // Change text color on hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                updateButton.setBackground(Color.black); // Original background color
                updateButton.setForeground(Color.WHITE); // Original text color
            }
        });

        backgroundPanel.add(updateButton);

        // Load existing job details
        loadJobDetails();

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateJobDetails();
            }
        });
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 50, 50));
        setVisible(true);
    }
    private void loadJobDetails() {
        Connection conn = dbconn.connection();
        try {
            String sql = "SELECT title, description, location, salary, vacancy, experience " +
                    "FROM job WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, jobId);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                titleField.setText(rs.getString("title"));
                descriptionArea.setText(rs.getString("description"));

                locationField.setText(rs.getString("location"));
                salaryField.setText(rs.getString("salary"));
                vacancyField.setText(rs.getString("vacancy"));
                experienceField.setText(rs.getString("experience"));


            }
            pstmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading job details.", "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Connection Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateJobDetails() {
        Connection conn = dbconn.connection();
        try {
            String sql = "UPDATE job SET title = ?, description = ?,  location = ?, salary = ?, vacancy = ?, " +
                    "experience = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, titleField.getText());
            pstmt.setString(2, descriptionArea.getText());
            pstmt.setString(3, locationField.getText());

            pstmt.setString(4, salaryField.getText());
            pstmt.setString(5, vacancyField.getText());
            pstmt.setString(6, experienceField.getText());


            pstmt.setInt(7, jobId);

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Job details updated successfully.");
            dispose();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error updating job details.", "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Connection Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private static void setButtonRounded(JButton button, int radius, Color backgroundColor) {
        button.setBackground(backgroundColor);
        button.setOpaque(false); // Make button non-opaque to avoid default background painting
        button.setFocusPainted(true);

        button.setContentAreaFilled(true); // Remove content area painting
        button.setBorder(new CandidateDashboard.RoundedBorder(radius, backgroundColor)); // Set rounded border with background color
        button.setUI(new CandidateDashboard.RoundedButtonUI());
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

    public static void main(String[] args) {
        new Updatejob(2);
    }
}
