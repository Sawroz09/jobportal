import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Timer;
import java.util.TimerTask;

public class ReportPanel extends JPanel {
    private JLabel jobsLabel;
    private JLabel activeJobsLabel;
    private JLabel inactiveJobsLabel;
    private JLabel recruitersLabel;
    private JLabel activeRecruitersLabel;
    private JLabel inactiveRecruitersLabel;
    private JLabel usersLabel;
    private Timer timer;

    public ReportPanel(String userName) {
        setLayout(null);
        setOpaque(false); // Make the panel transparent to display parent background

        // Add the "Welcome" label
        JLabel welcomeLabel = new JLabel("Report");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 56));
        welcomeLabel.setForeground(Color.pink);
        welcomeLabel.setBounds(400, 20, 350, 50); // Centered horizontally
        add(welcomeLabel);

        // Initialize labels for the data
        jobsLabel = new JLabel();
        activeJobsLabel = new JLabel();
        inactiveJobsLabel = new JLabel();
        recruitersLabel = new JLabel();
        activeRecruitersLabel = new JLabel();
        inactiveRecruitersLabel = new JLabel();
        usersLabel = new JLabel();

        // Add colored panels with labels and numbers
        addColoredPanel("Number of Jobs", jobsLabel, new Color(0, 0, 255, 180), 100, 140); // First row - Semi-transparent blue
        addColoredPanel("Number of Active Jobs", activeJobsLabel, new Color(0, 128, 0, 180), 370, 140); // First row - Semi-transparent green
        addColoredPanel("Number of Inactive Jobs", inactiveJobsLabel, new Color(255, 0, 0, 180), 640, 140); // First row - Semi-transparent red

        addColoredPanel("Number of Recruiters", recruitersLabel, new Color(255, 255, 0, 180), 100, 280); // Second row - Semi-transparent yellow
        addColoredPanel("Number of Active Recruiters", activeRecruitersLabel, new Color(255, 182, 193, 180), 370, 280); // Second row - Semi-transparent pink
        addColoredPanel("Number of Inactive Recruiters", inactiveRecruitersLabel, new Color(128, 128, 128, 180), 640, 280); // Second row - Semi-transparent gray

        addColoredPanel("Number of Users", usersLabel, new Color(128, 0, 128, 180), 100, 420); // Third row - Semi-transparent purple

        // Refresh the data when the panel is created
        refreshData();

        // Set up the timer to refresh data every 5 seconds
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> refreshData()); // Ensure updates are on the Event Dispatch Thread
            }
        }, 0, 5000); // Delay of 0 ms before first execution, repeat every 5000 ms (5 seconds)
    }

    private void addColoredPanel(String title, JLabel numberLabel, Color color, int x, int y) {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(color);
        panel.setBounds(x, y, 250, 120); // Increased width and height
        panel.setOpaque(true); // Panels themselves are not transparent
        add(panel);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(10, 10, 230, 20); // Adjusted to fit new panel size
        panel.add(titleLabel);

        numberLabel.setFont(new Font("Arial", Font.BOLD, 36));
        numberLabel.setForeground(Color.WHITE);
        numberLabel.setBounds(190, 60, 100, 40); // Adjusted to fit new panel size
        panel.add(numberLabel);
    }

    private void refreshData() {
        int numberOfJobs = fetchDataFromDatabase("SELECT COUNT(*) FROM job");
        int numberOfActiveJobs = fetchDataFromDatabase("SELECT COUNT(*) FROM job WHERE status = 1");
        int numberOfInactiveJobs = fetchDataFromDatabase("SELECT COUNT(*) FROM job WHERE status = 0");
        int numberOfRecruiters = fetchDataFromDatabase("SELECT COUNT(*) FROM recruiters");
        int numberOfActiveRecruiters = fetchDataFromDatabase("SELECT COUNT(*) FROM recruiters WHERE status = 1");
        int numberOfInactiveRecruiters = fetchDataFromDatabase("SELECT COUNT(*) FROM recruiters WHERE status = 0");
        int numberOfUsers = fetchDataFromDatabase("SELECT COUNT(*) FROM users");

        // Update the labels with the new data
        jobsLabel.setText(String.valueOf(numberOfJobs));
        activeJobsLabel.setText(String.valueOf(numberOfActiveJobs));
        inactiveJobsLabel.setText(String.valueOf(numberOfInactiveJobs));
        recruitersLabel.setText(String.valueOf(numberOfRecruiters));
        activeRecruitersLabel.setText(String.valueOf(numberOfActiveRecruiters));
        inactiveRecruitersLabel.setText(String.valueOf(numberOfInactiveRecruiters));
        usersLabel.setText(String.valueOf(numberOfUsers));
    }

    private int fetchDataFromDatabase(String query) {
        int result = 0;
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = dbconn.connection(); // Use the connection method from dbconn class
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            if (rs.next()) {
                result = rs.getInt(1); // Get the result from the first column
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error fetching data: " + e.getMessage());
        } finally {
            // Close resources
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error closing resources: " + e.getMessage());
            }
        }
        return result;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700); // Increased size for better centering
        frame.setLocationRelativeTo(null); // Center the JFrame on the screen
        frame.add(new ReportPanel("Username Here")); // Replace "Username Here" with the desired username
        frame.getContentPane().setBackground(new Color(240, 240, 240)); // Set a background color for the frame
        frame.setVisible(true);
    }
}
