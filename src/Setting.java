import javax.swing.*;
import java.awt.*;
import java.sql.*;

class Setting extends JPanel {
    private JTextField candidateField, recruiterField, contentField, loginField;
    private JButton updateButton;

    public Setting() {
        setLayout(null);
        setOpaque(false); // Make the panel transparent

        // Add the "Welcome" label
        JLabel welcomeLabel = new JLabel("All Setting");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 26));
        welcomeLabel.setForeground(Color.WHITE); // Set text color
        welcomeLabel.setBounds(400, 10, 550, 50); // Set x, y, width, and height
        add(welcomeLabel);

        // Register Candidate label and text field
        JLabel registerCandidateLabel = new JLabel("Candidate Label:");
        registerCandidateLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        registerCandidateLabel.setForeground(Color.WHITE);
        registerCandidateLabel.setBounds(150, 100, 200, 30);
        add(registerCandidateLabel);

        candidateField = new JTextField();
        candidateField.setBounds(350, 100, 300, 30);
        add(candidateField);

        // Register Recruiter label and text field
        JLabel registerRecruiterLabel = new JLabel("Recruiter Label:");
        registerRecruiterLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        registerRecruiterLabel.setForeground(Color.WHITE);
        registerRecruiterLabel.setBounds(150, 150, 200, 30);
        add(registerRecruiterLabel);

        recruiterField = new JTextField();
        recruiterField.setBounds(350, 150, 300, 30);
        add(recruiterField);

        // Content label and text field
        JLabel contentLabel = new JLabel("Content Label:");
        contentLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        contentLabel.setForeground(Color.WHITE);
        contentLabel.setBounds(150, 200, 200, 30);
        add(contentLabel);

        contentField = new JTextField();
        contentField.setBounds(350, 200, 300, 30);
        add(contentField);

        // Login label and text field
        JLabel loginLabel = new JLabel("Login Label:");
        loginLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        loginLabel.setForeground(Color.WHITE);
        loginLabel.setBounds(150, 250, 200, 30);
        add(loginLabel);

        loginField = new JTextField();
        loginField.setBounds(350, 250, 300, 30);
        add(loginField);

        // Update button
        updateButton = new JButton("Update");
        updateButton.setFont(new Font("Arial", Font.BOLD, 20));
        updateButton.setForeground(Color.white);
        updateButton.setBounds(380, 300, 150, 40);
        add(updateButton);
        ButtonUtils.setButtonRounded(updateButton, 20, Color.blue);

        // Load the settings from the database when the panel is created
        loadSettings();

        // Add action listener to the update button
        updateButton.addActionListener(e -> updateSettings());
    }

    // Method to load settings from the database and display them in the text fields
    private void loadSettings() {
        try (Connection connection = dbconn.connection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT recruiter, candidate, content, login FROM setting WHERE id = 1")) {
            if (resultSet.next()) {
                recruiterField.setText(resultSet.getString("candidate"));
                candidateField.setText(resultSet.getString("recruiter"));
                contentField.setText(resultSet.getString("content"));
                loginField.setText(resultSet.getString("login"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading settings from database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to update the settings in the database when the user clicks "Update"
    private void updateSettings() {
        String candidate = candidateField.getText();
        String recruiter = recruiterField.getText();
        String content = contentField.getText();
        String login = loginField.getText();

        try (Connection connection = dbconn.connection();
             PreparedStatement statement = connection.prepareStatement("UPDATE setting SET recruiter = ?, candidate = ?, content = ?, login = ? WHERE id = 1")) {

            statement.setString(1, candidate);
            statement.setString(2, recruiter);
            statement.setString(3, content);
            statement.setString(4, login);

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Settings updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No settings were updated.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating settings in database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
