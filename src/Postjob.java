import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

class Postjob extends JPanel {

    private JTextField titleField, locationField, vacancyField, salaryField, experienceField, emailField, tagsField;
    private JTextArea descriptionArea;
    private JComboBox<String> categoryBox, jobTypeBox;
    private int jobId; // Add jobId as an instance variable
    private int recruiter_Id;
    private String recruiterUsername;
    public Postjob(int recruiter_Id, String recEmail,String recruiterUsername) {
        this.recruiter_Id = recruiter_Id; // Initialize recruiter_Id
        this.recruiterUsername=recruiterUsername;
        setLayout(null);
        setOpaque(false); // Make the panel transparent

        // Add the "All Jobs" label
        JLabel welcomeLabel = new JLabel("ALL JOBS");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 56));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setBounds(460, 50, 350, 50);
        add(welcomeLabel);

        // First Column: Title to Post Date
        JLabel titleLabel = new JLabel("Job Title:");
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(0, 150, 200, 30);
        add(titleLabel);

        titleField = new JTextField();
        titleField.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        titleField.setBounds(200, 150, 400, 30);
        add(titleField);

        JLabel descriptionLabel = new JLabel("Description:");
        descriptionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        descriptionLabel.setForeground(Color.WHITE);
        descriptionLabel.setBounds(0, 200, 200, 30);
        add(descriptionLabel);

        descriptionArea = new JTextArea();
        descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        scrollPane.setBounds(200, 200, 400, 100);
        add(scrollPane);

        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        categoryLabel.setForeground(Color.WHITE);
        categoryLabel.setBounds(0, 320, 200, 30);
        add(categoryLabel);

        categoryBox = new JComboBox<>(new String[]{"IT", "Finance", "Healthcare", "Education", "Other"});
        categoryBox.setBackground(Color.WHITE);
        categoryBox.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        categoryBox.setBounds(200, 320, 200, 30);
        categoryBox.setRenderer(new CustomComboBoxRenderer());
        add(categoryBox);

        JLabel locationLabel = new JLabel("Location:");
        locationLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        locationLabel.setForeground(Color.WHITE);
        locationLabel.setBounds(0, 370, 200, 30);
        add(locationLabel);

        locationField = new JTextField();
        locationField.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        locationField.setBounds(200, 370, 400, 30);
        add(locationField);

        // Second Column: Vacancy to Recruiter Email
        JLabel vacancyLabel = new JLabel("Vacancy:");
        vacancyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        vacancyLabel.setForeground(Color.WHITE);
        vacancyLabel.setBounds(700, 150, 200, 30);
        add(vacancyLabel);

        vacancyField = new JTextField();
        vacancyField.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        vacancyField.setBounds(900, 150, 400, 30);
        add(vacancyField);

        JLabel salaryLabel = new JLabel("Salary:");
        salaryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        salaryLabel.setForeground(Color.WHITE);
        salaryLabel.setBounds(700, 200, 200, 30);
        add(salaryLabel);

        salaryField = new JTextField();
        salaryField.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        salaryField.setBounds(900, 200, 400, 30);
        add(salaryField);

        JLabel experienceLabel = new JLabel("Experience:");
        experienceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        experienceLabel.setForeground(Color.WHITE);
        experienceLabel.setBounds(700, 250, 200, 30);
        add(experienceLabel);

        experienceField = new JTextField();
        experienceField.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        experienceField.setBounds(900, 250, 400, 30);
        add(experienceField);

        JLabel jobTypeLabel = new JLabel("Job Type:");
        jobTypeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        jobTypeLabel.setForeground(Color.WHITE);
        jobTypeLabel.setBounds(700, 300, 200, 30);
        add(jobTypeLabel);

        jobTypeBox = new JComboBox<>(new String[]{"Full-time", "Part-time", "Contract", "Internship"});
        jobTypeBox.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        jobTypeBox.setBackground(Color.WHITE);
        jobTypeBox.setBounds(900, 300, 200, 30);
        jobTypeBox.setRenderer(new CustomComboBoxRenderer());
        add(jobTypeBox);

        JLabel emailLabel = new JLabel("Recruiter Email:");
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        emailLabel.setForeground(Color.WHITE);
        emailLabel.setBounds(700, 350, 200, 30);
        add(emailLabel);

        emailField = new JTextField();
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        emailField.setBounds(900, 350, 400, 30);
        emailField.setText(recEmail);
        add(emailField);

        // Add the "Tags" label
        JLabel tagsLabel = new JLabel("Tags:");
        tagsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        tagsLabel.setForeground(Color.WHITE);
        tagsLabel.setBounds(700, 420, 200, 30);
        add(tagsLabel);

        tagsField = new JTextField();
        tagsField.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        tagsField.setBounds(900, 420, 400, 30);
        add(tagsField);

        // "Post Job" button
        JButton postJobButton = new JButton("Post Job");
        postJobButton.setFont(new Font("Segoe UI", Font.BOLD, 24));
        postJobButton.setBounds(460, 530, 200, 40);
        ButtonUtils.setButtonRounded(postJobButton, 30, Color.GREEN);
        postJobButton.setForeground(Color.white);
        add(postJobButton);

        // Add action listener for the button
        postJobButton.addActionListener(e -> {
            this.recruiter_Id = recruiter_Id; // Initialize recruiter_Id
            String title = titleField.getText();
            String description = descriptionArea.getText();
            String category = (String) categoryBox.getSelectedItem();
            String location = locationField.getText();
            String vacancy = vacancyField.getText();
            String salary = salaryField.getText();
            String experience = experienceField.getText();
            String jobType = (String) jobTypeBox.getSelectedItem();
            String email = emailField.getText();
            String tags = tagsField.getText();
// Check if any fields are empty
            if (title.isEmpty() || description.isEmpty() || category == null || location.isEmpty() ||
                    vacancy.isEmpty() || salary.isEmpty() || experience.isEmpty() || jobType == null ||
                    email.isEmpty() || tags.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all the required fields before submitting the job post.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
            String sql = "INSERT INTO job (title, description, category, location, vacancy, salary, experience, job_type, recruiter_email,jobrec_id,rec_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)";

            try (Connection conn = dbconn.connection();
                 PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                pstmt.setString(1, title);
                pstmt.setString(2, description);
                pstmt.setString(3, category);
                pstmt.setString(4, location);
                pstmt.setString(5, vacancy);
                pstmt.setString(6, salary);
                pstmt.setString(7, experience);
                pstmt.setString(8, jobType);
                pstmt.setString(9, email);
                pstmt.setString(10, String.valueOf(recruiter_Id));
                pstmt.setString(11,recruiterUsername);

                int affectedRows = pstmt.executeUpdate();

                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            jobId = generatedKeys.getInt(1);
                            JOptionPane.showMessageDialog(this, "Job Posted Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

                            // Save tags to the database
                            saveJobTags(jobId, tags);

                            // Reset all fields after posting the job
                            titleField.setText("");
                            descriptionArea.setText("");
                            categoryBox.setSelectedIndex(0);
                            locationField.setText("");
                            vacancyField.setText("");
                            salaryField.setText("");
                            experienceField.setText("");
                            jobTypeBox.setSelectedIndex(0);

                            tagsField.setText("");
                        }
                    }
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }}
        });

        // Add mouse listener for hover effect
        postJobButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                postJobButton.setBackground(Color.GREEN); // Set background color on hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                ButtonUtils.setButtonRounded(postJobButton, 30, Color.GREEN); // Reset background color after hover
            }
        });
    }

    private void saveJobTags(int jobId, String tags) {
        String[] tagArray = tags.split("\\s*,\\s*"); // Split tags by comma

        try (Connection conn = dbconn.connection();
             PreparedStatement pstmtTagInsert = conn.prepareStatement("INSERT INTO Tags (name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
             PreparedStatement pstmtJobTagInsert = conn.prepareStatement("INSERT INTO JobTags (job_id, tag_id) VALUES (?, ?)")) {

            for (String tag : tagArray) {
                int tagId;
                try (PreparedStatement pstmtTagSelect = conn.prepareStatement("SELECT id FROM Tags WHERE name = ?")) {
                    pstmtTagSelect.setString(1, tag);
                    ResultSet rs = pstmtTagSelect.executeQuery();
                    if (rs.next()) {
                        tagId = rs.getInt("id");
                    } else {
                        pstmtTagInsert.setString(1, tag);
                        pstmtTagInsert.executeUpdate();
                        try (ResultSet generatedKeys = pstmtTagInsert.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                tagId = generatedKeys.getInt(1);
                            } else {
                                throw new SQLException("Creating tag failed, no ID obtained.");
                            }
                        }
                    }
                }

                // Associate tag with the job
                pstmtJobTagInsert.setInt(1, jobId);
                pstmtJobTagInsert.setInt(2, tagId);
                pstmtJobTagInsert.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }}