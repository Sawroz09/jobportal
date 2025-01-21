import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class ApplyForm extends JFrame {
    private JTextField nameField, qualificationField, emailField, jobPostField;
    private JButton uploadButton, applyButton, exitButton;
    private File selectedFile;

    public ApplyForm(String jobId, String topic, String companyname, String userName, String Qualification, String Email, String Topic,String jobrec_id,int canid ,String rec_email) {
        // Set up the frame
        setSize(960, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);
        System.out.println(jobId);
        System.out.println(userName);

        // Create a background panel with an image
        BackgroundPanel backgroundPanel = new BackgroundPanel("Images/reg1.png");
        backgroundPanel.setLayout(null);
        setContentPane(backgroundPanel);

        // Create a panel for the custom title bar
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(new Color(8, 141, 165));
        titleBar.setBounds(0, 0, getWidth(), 45);

        // Create a label for the title text
        JLabel titleLabel = new JLabel("Job Application Form", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        // Create buttons for minimize, maximize/restore, and close
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 20));
        buttonPanel.setOpaque(false);

        JButton closeButton = createTitleBarButton("X");
        closeButton.setSize(40, 40);
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
        closeButton.addActionListener(e -> setVisible(false));

        // Create a panel to hold the custom title bar and content
        JPanel contentPanel = new JPanel(null);
        contentPanel.setOpaque(false);
        contentPanel.setBounds(0, 0, getWidth(), getHeight());

        // Add the title bar to the content panel
        contentPanel.add(titleBar);

        // Add labels and fields
        JLabel reg = new JLabel("JOB APPLICATION");
        reg.setFont(new Font("Arial", Font.BOLD, 24));
        reg.setForeground(Color.WHITE);
        reg.setBounds(370, 70, 350, 50);
        contentPanel.add(reg);

        JLabel nameLabel = new JLabel("NAME:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 24));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setBounds(200, 150, 350, 50);
        contentPanel.add(nameLabel);

        nameField = new JTextField(userName);
        nameField.setFont(new Font("Arial", Font.BOLD, 24));
        nameField.setForeground(Color.BLACK);
        nameField.setBounds(360, 160, 200, 30);
        contentPanel.add(nameField);

        JLabel qualificationLabel = new JLabel("Qualification: ");
        qualificationLabel.setFont(new Font("Arial", Font.BOLD, 24));
        qualificationLabel.setForeground(Color.WHITE);
        qualificationLabel.setBounds(200, 200, 350, 50);
        contentPanel.add(qualificationLabel);

        qualificationField = new JTextField(Qualification);
        qualificationField.setFont(new Font("Arial", Font.BOLD, 24));
        qualificationField.setForeground(Color.BLACK);
        qualificationField.setBounds(360, 210, 200, 30);
        contentPanel.add(qualificationField);

        JLabel emailLabel = new JLabel("Email: ");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 24));
        emailLabel.setForeground(Color.WHITE);
        emailLabel.setBounds(200, 250, 350, 50);
        contentPanel.add(emailLabel);

        emailField = new JTextField(Email);
        emailField.setFont(new Font("Arial", Font.BOLD, 14));
        emailField.setForeground(Color.BLACK);
        emailField.setBounds(360, 260, 200, 30);
        contentPanel.add(emailField);

        JLabel jobPostLabel = new JLabel("Job Post:");
        jobPostLabel.setFont(new Font("Arial", Font.BOLD, 24));
        jobPostLabel.setForeground(Color.WHITE);
        jobPostLabel.setBounds(200, 300, 350, 50);
        contentPanel.add(jobPostLabel);

        jobPostField = new JTextField(Topic);
        jobPostField.setFont(new Font("Arial", Font.BOLD, 24));
        jobPostField.setForeground(Color.BLACK);
        jobPostField.setBounds(360, 310, 200, 30);
        contentPanel.add(jobPostField);

        JLabel upload = new JLabel("Resume:");
        upload.setFont(new Font("Arial", Font.BOLD, 24));
        upload.setForeground(Color.WHITE);
        upload.setBounds(200, 350, 350, 50);
        contentPanel.add(upload);

        uploadButton = new JButton("Upload Resume");
        uploadButton.setFont(new Font("Arial", Font.BOLD, 14));
        uploadButton.setForeground(Color.BLACK);
        uploadButton.setBounds(360, 360, 200, 30);
        uploadButton.setBackground(Color.CYAN);
        contentPanel.add(uploadButton);

        applyButton = new JButton("APPLY");
        applyButton.setFont(new Font("Arial", Font.BOLD, 24));
        applyButton.setForeground(Color.WHITE);
        applyButton.setBounds(200, 455, 190, 50);
        applyButton.setBackground(Color.GREEN);

        exitButton = new JButton("EXIT");
        exitButton.setFont(new Font("Arial", Font.BOLD, 24));
        exitButton.setForeground(Color.WHITE);
        exitButton.setBounds(450, 455, 190, 50);
        exitButton.setBackground(Color.RED);
        // Customize button appearance
        ButtonUtils.setButtonRounded(applyButton, 20, Color.GREEN); // Rounded button
        // Customize button appearance
        ButtonUtils.setButtonRounded(exitButton, 20, Color.RED); // Rounded button

        applyButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                applyButton.setBackground(Color.GREEN); // Green background on hover
                applyButton.setForeground(Color.WHITE); // White text on hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                applyButton.setBackground(Color.BLACK); // Reset to neon green
                applyButton.setForeground(Color.WHITE); // Reset to white text
            }
        });
        contentPanel.add(applyButton);
        contentPanel.add(exitButton);

        backgroundPanel.add(contentPanel);

        uploadButton.addActionListener(e -> handleUpload());
        applyButton.addActionListener(e -> handleApply(jobrec_id,jobId,canid,rec_email));
        exitButton.addActionListener(e -> System.exit(0));

        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 50, 50));

        setResizable(true);
        setVisible(true);
    }

    private JButton createTitleBarButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(204, 0, 0));
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFocusPainted(false);
        return button;
    }

    private void handleUpload() {
        JFileChooser fileChooser = new JFileChooser();
        // Set file filter for PDF and Word files
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PDF and Word Documents", "pdf", "doc", "docx"));
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            uploadButton.setText(selectedFile.getName());
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
        }
    }


    private void handleApply(String jobrec_id,String jobid,int canid,String rec_email) {
        String name = nameField.getText();
        String qualification = qualificationField.getText();
        String email = emailField.getText();
        String jobPost = jobPostField.getText();

        if (name.isEmpty() || qualification.isEmpty() || email.isEmpty() || jobPost.isEmpty() || selectedFile == null) {
            JOptionPane.showMessageDialog(this, "Please fill all fields and upload a file.");
            return;
        }

        try {
            // Read the file into a byte array
            byte[] fileData = readFileToByteArray(selectedFile);

            // Insert the data into the database
            insertApplicationIntoDatabase(name, qualification, email, jobPost, fileData,jobrec_id,jobid,canid);

            JOptionPane.showMessageDialog(this, "Application submitted successfully!");

            boolean recruiterEmailSent = sendEmail(rec_email, "New Application Received", capitalizeName(name) + " has applied to your job posting: " + jobPost);
            boolean applicantEmailSent = sendEmail(email, "Application Received", "Dear " + capitalizeName(name) + ",\n\nThank you for applying for the position of " + jobPost + ".");
            // Send an email notification
            if (recruiterEmailSent && applicantEmailSent) {
                JOptionPane.showMessageDialog(this, "Application and both notification emails sent successfully!");

            } else {
                JOptionPane.showMessageDialog(this, "Application submitted, but there was an issue sending one or both notification emails.");

            }

            // Reset the form
            resetForm();
            setVisible(false);


        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error while submitting application: " + e.getMessage());
        }
    }

    private String capitalizeName(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }
    private byte[] readFileToByteArray(File file) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
        }
        return bos.toByteArray();
    }

    private void insertApplicationIntoDatabase(String name, String qualification, String email, String jobPost, byte[] resumeData, String jobrec_id,String jobid ,int canid) throws SQLException {

        String sql = "INSERT INTO files (name, qualification, email, title, files,rec_id,job_id,canid) VALUES (?, ?, ?, ?, ?,?,?,?)";

        try (Connection conn=dbconn.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, qualification);
            pstmt.setString(3, email);
            pstmt.setString(4, jobPost);
            pstmt.setBytes(5, resumeData);
            pstmt.setString(6, jobrec_id);
            pstmt.setString(7, jobid);
            pstmt.setInt(8, canid);
            pstmt.executeUpdate();
        }
    }

    private class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            try {
                backgroundImage = ImageIO.read(new File(imagePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
    private void resetForm() {
        nameField.setText("");
        qualificationField.setText("");
        emailField.setText("");
        jobPostField.setText("");
        uploadButton.setText("Upload Resume");
        selectedFile = null;
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

}
