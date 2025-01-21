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

public class Candidateregister extends JFrame {
    private static String generatedOTP;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField phField;
    private JTextField qualificationField;
    private JTextField cityField;
    private JTextField stateField;
    private JComboBox genderComboBox;
    private JTextField dobField;
    private JTextField passingYearField;
    private JPasswordField passwordField;

    private boolean isEmailVerified = false;

    public Candidateregister() {
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
        JLabel titleLabel = new JLabel("Candidate Registration", SwingConstants.CENTER);
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
        name.setFont(new Font("Arial", Font.BOLD, 18));
        name.setForeground(Color.WHITE); // Set text color
        name.setBounds(60, 150, 350, 50); // Set x, y, width, and height
        contentPanel.add(name);

        // Add the "NAME" text field
        nameField = new JTextField();
        nameField.setFont(new Font("Arial", Font.BOLD, 24));
        nameField.setForeground(Color.BLACK); // Set text color
        nameField.setBounds(165, 160, 200, 30); // Set x, y, width, and height
        contentPanel.add(nameField);

        // Add the "Email" label
        JLabel emailLabel = new JLabel("Email ");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 18));
        emailLabel.setForeground(Color.WHITE); // Set text color
        emailLabel.setBounds(60, 200, 350, 50); // Set x, y, width, and height
        contentPanel.add(emailLabel);

        // Add the "Email" text field
        emailField = new JTextField();
        emailField.setFont(new Font("Arial", Font.BOLD, 24));
        emailField.setForeground(Color.BLACK); // Set text color
        emailField.setBounds(165, 210, 200, 30); // Set x, y, width, and height
        contentPanel.add(emailField);

        JButton emailFieldBtn = new JButton("SEND OTP");
        emailFieldBtn.setFont(new Font("Arial", Font.BOLD, 14));
        emailFieldBtn.setForeground(Color.BLACK); // Set text color
        emailFieldBtn.setBounds(370, 215, 140, 20); // Set x, y, width, and height
        emailFieldBtn.setBackground(Color.CYAN);
        contentPanel.add(emailFieldBtn);

        // Add the "OTP Verification" text field
        JTextField verifyField = new JTextField();
        verifyField.setFont(new Font("Arial", Font.BOLD, 24));
        verifyField.setForeground(Color.BLACK); // Set text color
        verifyField.setBounds(165, 250, 200, 30); // Set x, y, width, and height
        contentPanel.add(verifyField);

        JButton verifyFieldBtn = new JButton("Verify");
        verifyFieldBtn.setFont(new Font("Arial", Font.BOLD, 14));
        verifyFieldBtn.setForeground(Color.BLACK); // Set text color
        verifyFieldBtn.setBounds(370, 255, 140, 20); // Set x, y, width, and height
        verifyFieldBtn.setBackground(Color.BLUE);
        contentPanel.add(verifyFieldBtn);

        // Add the "addresslabel" label
        JLabel qualificationlabel = new JLabel("Qualification ");
        qualificationlabel.setFont(new Font("Arial", Font.BOLD, 16));
        qualificationlabel.setForeground(Color.WHITE); // Set text color
        qualificationlabel.setBounds(60, 290, 350, 50); // Set x, y, width, and height
        contentPanel.add(qualificationlabel);

        // Add the "addField" text field
        qualificationField = new JTextField();
        qualificationField.setFont(new Font("Arial", Font.BOLD, 24));
        qualificationField.setForeground(Color.BLACK); // Set text color
        qualificationField.setBounds(165, 295, 200, 30); // Set x, y, width, and height
        contentPanel.add(qualificationField);
        // Add the "Phone" label
        JLabel phoneLabel = new JLabel("Phone ");
        phoneLabel.setFont(new Font("Arial", Font.BOLD, 16));
        phoneLabel.setForeground(Color.WHITE); // Set text color
        phoneLabel.setBounds(60, 345, 350, 50); // Corrected y position
        contentPanel.add(phoneLabel);

// Add the "Phone" text field
        phField = new JTextField();
        phField.setFont(new Font("Arial", Font.BOLD, 24));
        phField.setForeground(Color.BLACK); // Set text color
        phField.setBounds(165, 350, 200, 30); // Set x, y, width, and height
        contentPanel.add(phField);

// Add the "City" label
        JLabel cityLabel = new JLabel("City ");
        cityLabel.setFont(new Font("Arial", Font.BOLD, 18));
        cityLabel.setForeground(Color.WHITE); // Set text color
        cityLabel.setBounds(60, 395, 350, 50); // Corrected y position
        contentPanel.add(cityLabel);

// Add the "City" text field
        cityField = new JTextField();
        cityField.setFont(new Font("Arial", Font.BOLD, 24));
        cityField.setForeground(Color.BLACK); // Set text color
        cityField.setBounds(165, 410, 200, 30); // Set x, y, width, and height
        contentPanel.add(cityField);



        // Add the "StateLabel" label
        JLabel StateLabel = new JLabel("State ");
        StateLabel.setFont(new Font("Arial", Font.BOLD, 18));
        StateLabel.setForeground(Color.WHITE); // Set text color
        StateLabel.setBounds(540, 150, 350, 50); // Set x, y, width, and height
        contentPanel.add(StateLabel);

        // Add the "StateLabel" text field
        stateField = new JTextField();
        stateField.setFont(new Font("Arial", Font.BOLD, 24));
        stateField.setForeground(Color.BLACK); // Set text color
        stateField.setBounds(645, 160, 200, 30); // Set x, y, width, and height
        contentPanel.add(stateField);

        // Add the "Gender" label
        JLabel GenderLabel = new JLabel("Gender ");
        GenderLabel.setFont(new Font("Arial", Font.BOLD, 18));
        GenderLabel.setForeground(Color.WHITE); // Set text color
        GenderLabel.setBounds(540, 210, 350, 50); // Set x, y, width, and height
        contentPanel.add(GenderLabel);

// Add the "Gender" text field
        // Add the "Gender" combo box
        String[] genderOptions = {"Male", "Female", "Other"};
        genderComboBox = new JComboBox<>(genderOptions);
        genderComboBox.setFont(new Font("Arial", Font.BOLD, 18));
        genderComboBox.setForeground(Color.BLUE); // Set text color
        genderComboBox.setBounds(645, 220, 200, 30); // Set x, y, width, and height
        contentPanel.add(genderComboBox);

        // Add the "Date of Birth" label
        JLabel dobLabel = new JLabel("DOB ");
        dobLabel.setFont(new Font("Arial", Font.BOLD, 18));
        dobLabel.setForeground(Color.WHITE); // Set text color
        dobLabel.setBounds(540, 270, 350, 50); // Set x, y, width, and height
        contentPanel.add(dobLabel);

// Add the "Date of Birth" text field
       dobField = new JTextField();
        dobField.setFont(new Font("Arial", Font.BOLD, 24));
        dobField.setForeground(Color.BLACK); // Set text color
        dobField.setBounds(645, 280, 200, 30); // Set x, y, width, and height
        contentPanel.add(dobField);

// Add the "Passing Year" label
        JLabel passingYearLabel = new JLabel("Passing Year ");
        passingYearLabel.setFont(new Font("Arial", Font.BOLD, 16));
        passingYearLabel.setForeground(Color.WHITE); // Set text color
        passingYearLabel.setBounds(540, 330, 350, 50); // Set x, y, width, and height
        contentPanel.add(passingYearLabel);

// Add the "Passing Year" text field
        passingYearField = new JTextField();
        passingYearField.setFont(new Font("Arial", Font.BOLD, 24));
        passingYearField.setForeground(Color.BLACK); // Set text color
        passingYearField.setBounds(645, 340, 200, 30); // Set x, y, width, and height
        contentPanel.add(passingYearField);

// Add the "Password" label
        JLabel passwordLabel = new JLabel("Password ");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 18));
        passwordLabel.setForeground(Color.WHITE); // Set text color
        passwordLabel.setBounds(540, 390, 350, 50); // Set x, y, width, and height
        contentPanel.add(passwordLabel);

// Add the "Password" field
         passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.BOLD, 24));
        passwordField.setForeground(Color.BLACK); // Set text color
        passwordField.setBounds(645, 400, 200, 30); // Set x, y, width, and height
        contentPanel.add(passwordField);

        // registerbutton

        JButton registerbutton=new JButton("REGISTER");

        registerbutton.setFont(new Font("Arial", Font.BOLD, 24));
        registerbutton.setForeground(Color.white); // Set text color
        registerbutton.setBounds(200, 465, 190, 50);
        // Set the rounded border and background color
        contentPanel.add(registerbutton);



        // registerbutton

        JButton  Exitbutton=new JButton("EXIT");

        Exitbutton.setFont(new Font("Arial", Font.BOLD, 24));
        Exitbutton.setForeground(Color.white); // Set text color
        Exitbutton.setBounds(450,465, 190, 50);
        // Set the rounded border and background color
        contentPanel.add(Exitbutton);
//
//        // Load and scale the image
//        String imagePath = "Images/logo.png"; // Update with your icon path
//        ImageIcon originalIcon = new ImageIcon(imagePath);
//        Image originalImage = originalIcon.getImage();
//        Image scaledImage = originalImage.getScaledInstance(180, 120, Image.SCALE_SMOOTH);

//        // Create a new ImageIcon with the scaled image
//        ImageIcon scaledIcon = new ImageIcon(scaledImage);
//        JLabel imageLabel = new JLabel(scaledIcon);
//        imageLabel.setBounds(650, 170, 190, 100); // Set the bounds for the image label
//        contentPanel.add(imageLabel);
        // Add the content panel to the frame
        backgroundPanel.add(contentPanel);


        ButtonUtils.setButtonRounded(registerbutton, 30, Color.GREEN);
        ButtonUtils.setButtonRounded(Exitbutton, 30, Color.red);


        registerbutton.addActionListener(e->{
            registerCandidate();

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
    private void registerCandidate() {


        String recruiterName = nameField.getText();
        String recruiterEmail = emailField.getText();
        String qualification = qualificationField.getText();
        String phone = phField.getText();
        String city = cityField.getText();
        String state = stateField.getText();
        String gender = (String) genderComboBox.getSelectedItem();
        String dob = dobField.getText();
        String passingYear = passingYearField.getText();
        String password = new String(passwordField.getPassword());
        String role = "CANDIDATE";


        if (recruiterName.isEmpty() || recruiterEmail.isEmpty() || password.isEmpty() || qualification.isEmpty() || phone.isEmpty() || city.isEmpty() || state.isEmpty() || dob.isEmpty() || passingYear.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }
        if (!isEmailVerified) { // Check if the email has been verified
            JOptionPane.showMessageDialog(this, "Please verify your email before registering.");
            return;
        }
        try {
            Connection conn = dbconn.connection(); // Establish the database connection

            // Check if the email already exists
            String checkEmailSql = "SELECT COUNT(*) FROM recruiters,users WHERE email = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkEmailSql);
            checkStmt.setString(1, recruiterEmail);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            if (count > 0) {
                JOptionPane.showMessageDialog(this, "Email already exists. You can't register with the same email.");
                return;
            }

            String sql = "INSERT INTO `users`( `name`, `email`, `password`, `qualification`, `role`, `city`, `state`, `phoneno`, `gender`, `pass_year`, `dob`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, recruiterName);
            pstmt.setString(2, recruiterEmail);
            pstmt.setString(3, password);
            pstmt.setString(4, qualification);
            pstmt.setString(5, role);
            pstmt.setString(6, city);
            pstmt.setString(7, state);
            pstmt.setString(8, phone);
            pstmt.setString(9, gender);
            pstmt.setString(10, passingYear);
            pstmt.setString(11, dob);


            int rowsInserted = pstmt.executeUpdate(); // Execute the SQL statement

            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Registration successful!");
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




    public static void main(String[] args) {
        SwingUtilities.invokeLater(Candidateregister::new); // Ensure UI updates on the Event Dispatch Thread
    }
}

