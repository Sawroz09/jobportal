import javax.imageio.ImageIO;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
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
import java.util.Properties;
import java.util.Random;

public class Recruiterregister extends JFrame {
    private static String generatedOTP;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField passField;
    private JTextField addField;

    private boolean isEmailVerified = false;

    public Recruiterregister() {
        // Set up the frame
        setSize(960, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);

        // Create a background panel with an image
        BackgroundPanel backgroundPanel = new BackgroundPanel("Images/reg1.png");
        backgroundPanel.setLayout(null); // Use null layout for absolute positioning
        setContentPane(backgroundPanel);

        // Create a panel for the custom title bar
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(new Color(8, 141, 165));
        titleBar.setBounds(0, 0, getWidth(), 45); // Set bounds for the title bar

        // Create a label for the title text
        JLabel titleLabel = new JLabel("Recruiter Registration", SwingConstants.CENTER);
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
        JLabel reg = new JLabel("REGISTRATION");
        reg.setFont(new Font("Arial", Font.BOLD, 24));
        reg.setForeground(Color.WHITE); // Set text color
        reg.setBounds(370, 70, 350, 50); // Set x, y, width, and height
        contentPanel.add(reg);

        // Add the "NAME" label
        JLabel name = new JLabel("NAME ");
        name.setFont(new Font("Arial", Font.BOLD, 24));
        name.setForeground(Color.WHITE); // Set text color
        name.setBounds(60, 150, 350, 50); // Set x, y, width, and height
        contentPanel.add(name);

        // Add the "NAME" text field
      nameField = new JTextField();
        nameField.setFont(new Font("Arial", Font.BOLD, 24));
        nameField.setForeground(Color.BLACK); // Set text color
        nameField.setBounds(150, 160, 200, 30); // Set x, y, width, and height
        contentPanel.add(nameField);

        // Add the "Email" label
        JLabel emailLabel = new JLabel("Email ");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 24));
        emailLabel.setForeground(Color.WHITE); // Set text color
        emailLabel.setBounds(60, 200, 350, 50); // Set x, y, width, and height
        contentPanel.add(emailLabel);

        // Add the "Email" text field
        emailField = new JTextField();
        emailField.setFont(new Font("Arial", Font.BOLD, 24));
        emailField.setForeground(Color.BLACK); // Set text color
        emailField.setBounds(150, 210, 200, 30); // Set x, y, width, and height
        contentPanel.add(emailField);

        JButton emailFieldBtn = new JButton("SEND OTP");
        emailFieldBtn.setFont(new Font("Arial", Font.BOLD, 14));
        emailFieldBtn.setForeground(Color.BLACK); // Set text color
        emailFieldBtn.setBounds(370, 215, 150, 20); // Set x, y, width, and height
        emailFieldBtn.setBackground(Color.CYAN);
        contentPanel.add(emailFieldBtn);

        // Add the "OTP Verification" text field
        JTextField verifyField = new JTextField();
        verifyField.setFont(new Font("Arial", Font.BOLD, 24));
        verifyField.setForeground(Color.BLACK); // Set text color
        verifyField.setBounds(150, 250, 200, 30); // Set x, y, width, and height
        contentPanel.add(verifyField);

        JButton verifyFieldBtn = new JButton("Verify");
        verifyFieldBtn.setFont(new Font("Arial", Font.BOLD, 14));
        verifyFieldBtn.setForeground(Color.BLACK); // Set text color
        verifyFieldBtn.setBounds(370, 255, 150, 20); // Set x, y, width, and height
        verifyFieldBtn.setBackground(Color.BLUE);
        contentPanel.add(verifyFieldBtn);

        // Add the "addresslabel" label
        JLabel addresslabel = new JLabel("Address ");
        addresslabel.setFont(new Font("Arial", Font.BOLD, 18));
        addresslabel.setForeground(Color.WHITE); // Set text color
        addresslabel.setBounds(60, 290, 350, 50); // Set x, y, width, and height
        contentPanel.add(addresslabel);

        // Add the "addField" text field
        addField = new JTextField();
        addField.setFont(new Font("Arial", Font.BOLD, 24));
        addField.setForeground(Color.BLACK); // Set text color
        addField.setBounds(150, 295, 200, 30); // Set x, y, width, and height
        contentPanel.add(addField);
        // Add the "Password" label
        JLabel passwordLabel = new JLabel("Password ");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 18));
        passwordLabel.setForeground(Color.WHITE); // Set text color
        passwordLabel.setBounds(60, 340, 350, 50); // Set x, y, width, and height
        contentPanel.add(passwordLabel);

        // Add the "Password" text field
         passField = new JTextField();
        passField.setFont(new Font("Arial", Font.BOLD, 24));
        passField.setForeground(Color.BLACK); // Set text color
        passField.setBounds(150, 355, 200, 30); // Set x, y, width, and height
        contentPanel.add(passField);

       // registerbutton

        JButton registerbutton=new JButton("REGISTER");

        registerbutton.setFont(new Font("Arial", Font.BOLD, 24));
        registerbutton.setForeground(Color.white); // Set text color
        registerbutton.setBounds(200, 455, 190, 50);
        // Set the rounded border and background color
        contentPanel.add(registerbutton);



        // registerbutton

        JButton  Exitbutton=new JButton("EXIT");

        Exitbutton.setFont(new Font("Arial", Font.BOLD, 24));
        Exitbutton.setForeground(Color.white); // Set text color
        Exitbutton.setBounds(450,455, 190, 50);
        // Set the rounded border and background color
        contentPanel.add(Exitbutton);

        // Load and scale the image
        String imagePath = "Images/logo.png"; // Update with your icon path
        ImageIcon originalIcon = new ImageIcon(imagePath);
        Image originalImage = originalIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(180, 120, Image.SCALE_SMOOTH);

        // Create a new ImageIcon with the scaled image
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel imageLabel = new JLabel(scaledIcon);
        imageLabel.setBounds(650, 170, 190, 100); // Set the bounds for the image label
        contentPanel.add(imageLabel);
        // Add the content panel to the frame
        backgroundPanel.add(contentPanel);


        ButtonUtils.setButtonRounded(registerbutton, 30, Color.GREEN);
        ButtonUtils.setButtonRounded(Exitbutton, 30, Color.red);


        registerbutton.addActionListener(e->{
            registerRecruiter();

        });
        Exitbutton.addActionListener(e->{

            setVisible(false);
        });
        emailFieldBtn.addActionListener(e -> {
            String email = emailField.getText();
            generatedOTP = generateOTP();
            boolean emailSent = sendEmail(email, "Your OTP Code", "Your OTP is: " + generatedOTP);

            if (emailSent) {
                JOptionPane.showMessageDialog(contentPanel, "OTP sent successfully!");
            } else {
                JOptionPane.showMessageDialog(contentPanel, "Failed to send OTP.");
            }
        });

        verifyFieldBtn.addActionListener(e -> {
            String enteredOTP = verifyField.getText();
            if (generatedOTP != null && generatedOTP.equals(enteredOTP)) {
                JOptionPane.showMessageDialog(contentPanel, "OTP verified successfully!");
                isEmailVerified = true; // Set the flag to true
            } else {
                JOptionPane.showMessageDialog(contentPanel, "Invalid OTP. Please try again.");
                isEmailVerified = false; // Ensure the flag remains false
            }
        });


        // Set the frame to be rounded
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 50, 50));

        // Make the frame visible
        setResizable(true); // Make the frame resizable
        setVisible(true);
    }



    private static String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }


    private static boolean sendEmail(String to, String subject, String text) {

        final String username = "sarojbicte"; // Replace with your email
        final String password = "wkwc nvtd aomi tlys"; // Replace with your email password or app-specific password

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(text);
            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
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
    private void registerRecruiter() {
        if (!isEmailVerified) { // Check if the email has been verified
            JOptionPane.showMessageDialog(this, "Please verify your email before registering.");
            return;
        }

        String recruiterName = nameField.getText();
        String recruiterEmail = emailField.getText();
        String recruiterPassword = passField.getText();
        String recruiterAddress = addField.getText();
        String role="RECRUITER";



        if (recruiterName.isEmpty() || recruiterEmail.isEmpty() || recruiterPassword.isEmpty() || recruiterAddress.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        try {
            Connection conn = dbconn.connection(); // Establish the database connection
// Check if the email already exists
            String checkEmailSql = "SELECT COUNT(*) FROM recruiters WHERE email = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkEmailSql);
            checkStmt.setString(1, recruiterEmail);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            if (count > 0) {
                JOptionPane.showMessageDialog(this, "Email already exists. You can't register with the same email.");
                return;
            }


            String sql = "INSERT INTO `recruiters`( `name`, `email`, `password`, `role`, `location`) VALUES (?, ?, ?, ?,?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, recruiterName);
            pstmt.setString(2, recruiterEmail);
            pstmt.setString(3, recruiterPassword);
            pstmt.setString(4, role);
            pstmt.setString(5, recruiterAddress);

            int rowsInserted = pstmt.executeUpdate(); // Execute the SQL statement

            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Registration successful!");
                resetFields();
                setVisible(false);
               login obj= new login(this);
               obj.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Registration failed. Please try again.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void resetFields() {
        nameField.setText("");
        emailField.setText("");
        passField.setText("");
        addField.setText("");
        isEmailVerified = false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Recruiterregister::new); // Ensure UI updates on the Event Dispatch Thread
    }
}
