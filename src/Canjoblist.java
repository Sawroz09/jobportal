import org.w3c.dom.Text;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

class Canjoblist extends JPanel {

    private JTextField locationField, tagsField;
    private JComboBox<String> categoryBox;
    private JButton searchButton;

    public Canjoblist( String userName,String Qualification,String Email ,int canid) {
        setLayout(null);
        setOpaque(false); // To allow the custom background to show through

        // "ALL JOBS" label
        JLabel allJobsLabel = new JLabel("ALL JOBS");
        allJobsLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        allJobsLabel.setForeground(new Color(194, 255, 12)); // Neon green color
        allJobsLabel.setBounds(530, 10, 350, 50); // Centered for width 1430
        add(allJobsLabel);

        // Filter Panel
        RoundedPanel filterPanel = new RoundedPanel(20);
        filterPanel.setLayout(null);
        filterPanel.setBackground(Color.LIGHT_GRAY);
        filterPanel.setBounds(930, 70, 270, 350); // Positioned to the right side


        JLabel FILTERLabel = new JLabel("FILTERS");
        FILTERLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        FILTERLabel.setForeground(Color.red);
        FILTERLabel.setBounds(70, 10, 150, 30);
        filterPanel.add(FILTERLabel);

        JLabel locationLabel = new JLabel("Location:");
        locationLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        locationLabel.setBounds(20, 40, 80, 30);
        filterPanel.add(locationLabel);

        locationField = new JTextField();
        locationField.setBounds(20, 70, 220, 30);
        filterPanel.add(locationField);

        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        categoryLabel.setBounds(20, 100, 80, 30);
        filterPanel.add(categoryLabel);

        // Category ComboBox
        categoryBox = new JComboBox<>(new String[]{"All", "IT", "Finance", "Healthcare", "Education", "Other"});
        categoryBox.setBackground(Color.blue);
        categoryBox.setForeground(Color.LIGHT_GRAY);
        categoryBox.setBounds(20, 130, 220, 30);
        filterPanel.add(categoryBox);

        JLabel tagsLabel = new JLabel("Tags:");
        tagsLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tagsLabel.setBounds(20, 160, 80, 30);
        filterPanel.add(tagsLabel);

        tagsField = new JTextField();
        tagsField.setBounds(20, 190, 220, 30);
        filterPanel.add(tagsField);

        JButton searchButton = new JButton("Search");
        searchButton.setBounds(70, 250, 150, 40);
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 22));
        searchButton.setForeground(Color.WHITE);
        searchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                searchButton.setBackground(Color.black); // Green background on hover
                searchButton.setForeground(Color.WHITE); // White text on hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                searchButton.setBackground(Color.BLUE); // Reset to neon green
                searchButton.setForeground(Color.WHITE); // Reset to white text
            }
        });

        // Load image icon (ensure the path is correct)
        ImageIcon searchIcon = new ImageIcon("Images/loupe.png"); // Replace with your image path
        searchButton.setIcon(searchIcon); // Set the image icon

        // Customize button appearance
        ButtonUtils.setButtonRounded(searchButton, 20, Color.BLUE); // Rounded button

        // Set image and text alignment
        searchButton.setHorizontalAlignment(SwingConstants.LEFT); // Align image to the left
        searchButton.setHorizontalTextPosition(SwingConstants.RIGHT); // Place text to the right of the image
        filterPanel.add(searchButton);

        add(filterPanel);

        // Scrollable Job Panel
        JPanel jobPanel = new JPanel();
        jobPanel.setLayout(null);
        jobPanel.setOpaque(false);
        jobPanel.setBackground(Color.LIGHT_GRAY);

        JScrollPane scrollPane = new JScrollPane(jobPanel);
        scrollPane.setBounds(20, 70, 1300, 450); // Positioned to the left of the filter panel
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null); // Remove border for a cleaner look
        add(scrollPane);

        // Extracting and displaying job posts from the database
        jobPanel.setBackground(Color.black);
        displayJobPostings(jobPanel, null, null, null,userName,Qualification,Email,canid);

        // Add action listener to search button
        searchButton.addActionListener(e -> {
            String location = locationField.getText().trim();
            String category = (String) categoryBox.getSelectedItem();
            String tags = tagsField.getText().trim();
            displayJobPostings(jobPanel, location, category.equals("All") ? null : category, tags,userName,Qualification,Email,canid);
        });
    }

    private JLabel createDetailLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setForeground(new Color(194, 22, 22)); // Red color
        label.setOpaque(true);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return label;
    }

    private void displayJobPostings(JPanel jobPanel, String locationFilter, String categoryFilter, String tagsFilter ,String userName,String Qualification,String Email,int canid) {
        try {
            Connection connection = dbconn.connection();
            Statement statement = connection.createStatement();
            StringBuilder query = new StringBuilder("SELECT * FROM job WHERE 1=1");

            // Apply filters
            if (locationFilter != null && !locationFilter.isEmpty()) {
                query.append(" AND location LIKE '%").append(locationFilter).append("%'");
            }
            if (categoryFilter != null && !categoryFilter.isEmpty()) {
                query.append(" AND category LIKE '%").append(categoryFilter).append("%'");
            }
            if (tagsFilter != null && !tagsFilter.isEmpty()) {
                query.append(" AND id IN (SELECT job_id FROM jobtags JOIN tags ON jobtags.tag_id = tags.id WHERE tags.name LIKE '%").append(tagsFilter).append("%')");
            }

            query.append(" ORDER BY id DESC");
            ResultSet rs = statement.executeQuery(query.toString());

            jobPanel.removeAll(); // Clear previous job postings
            boolean jobsFound = false;

            int yPosition = 0;
            while (rs.next()) {

                jobsFound = true;
                String jobId = rs.getString("id");
                String topic = rs.getString("title");
                String description = rs.getString("description");
                String location = rs.getString("location");
                String category = rs.getString("category");
                String status = rs.getInt("status") == 0 ? "closed" : "open";
                String salary = rs.getString("salary");
                String vacancy = rs.getString("vacancy");
                String jobType = rs.getString("job_type");
                String publishDate = rs.getString("pdate");
                String companyname = rs.getString("rec_name");
                String jobrec_id = rs.getString("jobrec_id");
                String rec_email = rs.getString("recruiter_email");

                // Create a new panel for each job posting
                RoundedPanel jobPostPanel = new RoundedPanel(20);
                jobPostPanel.setLayout(null);
                jobPostPanel.setBackground(new Color(214, 234, 248 ));
                jobPostPanel.setBounds(10, yPosition, 760, 430); // Adjust size and position

                // Load the image icon and resize it to 20x20 pixels
                ImageIcon companyIcon = new ImageIcon("Images/com.png");
                Image img = companyIcon.getImage();
                Image resizedImage = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
                companyIcon = new ImageIcon(resizedImage);


                RoundedTopLabel companyLabel = new RoundedTopLabel(companyIcon, " " + companyname, 20, 20);
                companyLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
                companyLabel.setBounds(0, 0, 760, 45);
                companyLabel.setForeground(Color.GREEN);
                companyLabel.setBackground(Color.BLUE);

// Get the size of the label after setting the text and icon
              //  companyLabel.setSize(companyLabel.getPreferredSize());



// Add the label to the panel
                jobPostPanel.add(companyLabel);

                JLabel topicLabel = new JLabel("TITLE: " + topic);
                topicLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
                topicLabel.setForeground(new Color(125, 60, 152));
                topicLabel.setBounds(20, 60, 720, 30);
                jobPostPanel.add(topicLabel);

                JTextPane descriptionArea = new JTextPane();
                descriptionArea.setFont(new Font("Arial", Font.BOLD, 13));
                descriptionArea.setBackground(new Color(214, 234, 248 ));
                descriptionArea.setEditable(false);

                // Add padding to the JTextPane
                descriptionArea.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 5));

                // Create styled document
                StyledDocument doc = descriptionArea.getStyledDocument();

                // Style for "DESCRIPTION:"
                SimpleAttributeSet boldStyle = new SimpleAttributeSet();
                StyleConstants.setBold(boldStyle, true);
                StyleConstants.setFontSize(boldStyle, 24);
                StyleConstants.setForeground(boldStyle, new Color(100, 30, 22));

                // Style for the rest of the description
                SimpleAttributeSet regularStyle = new SimpleAttributeSet();
                StyleConstants.setBold(regularStyle, true);
                StyleConstants.setFontSize(regularStyle, 16);
                StyleConstants.setForeground(regularStyle, new Color(100, 30, 22 ));

                try {
                    doc.insertString(doc.getLength(), "DESCRIPTION: ", boldStyle);
                    doc.insertString(doc.getLength(), description, regularStyle);
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }

                // Set single line spacing and word wrapping
                descriptionArea.setParagraphAttributes(regularStyle, true);

                // Adjust bounds to accommodate text wrapping and padding
                descriptionArea.setBounds(10, 100, 650, 100);
                jobPostPanel.add(descriptionArea);

                // Placeholders for other job details
                RoundedLabel locationPlaceholder = new RoundedLabel("   Location: " + location);
                locationPlaceholder.setBounds(50, 210, 150, 30);
                locationPlaceholder.setForeground(new Color(201, 14, 14));
                jobPostPanel.add(locationPlaceholder);

                RoundedLabel categoryPlaceholder = new RoundedLabel("  Category: " + category);
                categoryPlaceholder.setBounds(230, 210, 150, 30);
                categoryPlaceholder.setForeground(new Color(201, 14, 14));
                jobPostPanel.add(categoryPlaceholder);

                RoundedLabel statusPlaceholder = new RoundedLabel("   Status: " + status);
                statusPlaceholder.setBounds(410, 210, 170, 30);
                statusPlaceholder.setForeground(new Color(201, 14, 14));
                jobPostPanel.add(statusPlaceholder);

                RoundedLabel salaryPlaceholder = new RoundedLabel("  Salary: " + salary);
                salaryPlaceholder.setBounds(50, 250, 150, 30);
                salaryPlaceholder.setForeground(new Color(201, 14, 14));
                jobPostPanel.add(salaryPlaceholder);

                RoundedLabel vacancyPlaceholder = new RoundedLabel("  Vacancy: " + vacancy);
                vacancyPlaceholder.setBounds(230, 250, 150, 30);
                vacancyPlaceholder.setForeground(new Color(201, 14, 14));
                jobPostPanel.add(vacancyPlaceholder);

                RoundedLabel jobTypePlaceholder = new RoundedLabel("  Job Type: " + jobType);
                jobTypePlaceholder.setBounds(410, 250, 170, 30);
                jobTypePlaceholder.setForeground(new Color(201, 14, 14));
                jobPostPanel.add(jobTypePlaceholder);

                // Calculate and display time since posting in hours, minutes, and seconds
                LocalDateTime publishDateTime = LocalDateTime.parse(publishDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                Duration duration = Duration.between(publishDateTime, LocalDateTime.now());
                String timeAgo;
                long seconds = duration.getSeconds();
                long hours = seconds / 3600;
                long minutes = (seconds % 3600) / 60;
                long secs = seconds % 60;

                if (hours < 24) {
                    timeAgo = String.format("%02d hrs %02d mins %02d secs ago", hours, minutes, secs);
                } else {
                    long days = hours / 24;
                    hours = hours % 24;
                    timeAgo = String.format("%d days %02d hrs %02d mins %02d secs ago", days, hours, minutes, secs);
                }

                JLabel publishDateLabel = new JLabel("PUBLISHED: " + timeAgo);
                publishDateLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
                publishDateLabel.setForeground(new Color(49, 204, 113));
                publishDateLabel.setBounds(20, 290, 500, 30);
                jobPostPanel.add(publishDateLabel);
// Displaying tags associated with the job post
                ArrayList<String> tagsList = getJobTags(jobId, connection);

// Y-coordinate for placing tags, initially set at 330 (or adjust as needed)
                int zPosition = 330;
                int xPosition = 20;
                int gap = 10; // Gap between tags

                for (String tag : tagsList) {
                    // Create a label for each tag
                    RoundedLabel tagLabel = new RoundedLabel("#" + tag.trim()); // Trim to remove any extra spaces
                    tagLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
                    tagLabel.setForeground(Color.yellow);
                    tagLabel.setOpaque(true); // Make the background visible
                    tagLabel.setBackground(Color.LIGHT_GRAY); // Placeholder background color
                    tagLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center the text inside the label

                    // Calculate the size of the label based on the text length
                    tagLabel.setSize(tagLabel.getPreferredSize());
                    tagLabel.setBounds(xPosition, zPosition, tagLabel.getWidth() + gap, tagLabel.getHeight() + gap); // Add padding

                    // Add the tag label to the panel
                    jobPostPanel.add(tagLabel);

                    // Update xPosition for the next tag, with a 10px gap between tags
                    xPosition += tagLabel.getWidth() + gap;

                    // If tags go beyond the panel width, move to the next line
                    if (xPosition > jobPostPanel.getWidth() - 100) {
                        xPosition = 20; // Reset x position
                        zPosition += tagLabel.getHeight() + gap; // Move to the next line
                    }
                }


                JButton applyButton = new JButton("APPLY");
                applyButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
                applyButton.setBackground(new Color(0, 255, 0)); // Neon green background color
                applyButton.setForeground(Color.WHITE);
                applyButton.setBounds(300, 370, 150, 40);
                ButtonUtils.setButtonRounded(applyButton, 30, Color.black);
                jobPostPanel.add(applyButton);

                jobPanel.add(jobPostPanel);
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

                applyButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (status.equals("closed")) {
                            applyButton.setText("CLOSED");
                            // Show dialog box if the job is closed
                            JOptionPane.showMessageDialog(null, "The job is closed and cannot be applied for.", "Application Closed", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            // Pass job-specific details to the ApplyForm or the relevant method
                            new ApplyForm(jobId, topic, companyname, userName, Qualification, Email, topic, jobrec_id,canid,rec_email); // Assuming ApplyForm has a constructor that accepts these details
                        } }
                });



                yPosition += 470; // Adjust space between job posts
            }

            if (!jobsFound) {
                JLabel noJobsLabel = new JLabel("NO JOBS FOUND!");
                noJobsLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
                noJobsLabel.setForeground(Color.RED);
                noJobsLabel.setBounds(200, 40, 300, 30);
                jobPanel.add(noJobsLabel);
            }

            jobPanel.setPreferredSize(new Dimension(780, yPosition + 0)); // Adjust the size to fit content
            jobPanel.revalidate();
            jobPanel.repaint();
            rs.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }






    private ArrayList<String> getJobTags(String jobId, Connection connection) {
        ArrayList<String> tagsList = new ArrayList<>();
        Statement tagStatement = null;
        ResultSet rsTags = null;
        try {
            tagStatement = connection.createStatement();
            String tagQuery = "SELECT t.name FROM tags t JOIN jobtags jt ON t.id = jt.tag_id WHERE jt.job_id = " + jobId;
            rsTags = tagStatement.executeQuery(tagQuery);
            while (rsTags.next()) {
                tagsList.add(rsTags.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rsTags != null) rsTags.close();
                if (tagStatement != null) tagStatement.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return tagsList;
    }


}