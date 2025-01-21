import javax.swing.*;
import java.awt.*;

public class SplashScreen {

    SplashScreen() {
        // Create a splash screen frame
        JFrame splashFrame = new JFrame();
        splashFrame.setUndecorated(true);
        splashFrame.setSize(600, 500); // Set splash screen size
        splashFrame.setLocationRelativeTo(null); // Center on the screen

        // Create a panel with a background color or image
        JPanel splashPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(8, 141, 165)); // Background color
                g.fillRect(0, 0, getWidth(), getHeight());

                // Draw the logo in the center
                ImageIcon logoIcon = new ImageIcon("Images/icon.png"); // Update with your logo path
                int logoX = 40;
                int logoY = 40; // Position above the progress bar
                g.drawImage(logoIcon.getImage(), logoX, logoY, null);
            }
        };
        splashPanel.setLayout(new BorderLayout());
        splashFrame.add(splashPanel);

        // Create a label for the loading message
        JLabel loadingLabel = new JLabel("Loading, please wait...", SwingConstants.CENTER);
        loadingLabel.setForeground(Color.WHITE);
        loadingLabel.setFont(new Font("Arial", Font.BOLD, 18));
        splashPanel.add(loadingLabel, BorderLayout.SOUTH);

        // Create a progress bar
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true); // Animated progress bar
        progressBar.setBackground(new Color(8, 141, 165));
        progressBar.setForeground(new Color(255, 255, 255));
        splashPanel.add(progressBar, BorderLayout.NORTH);

        // Set the splash screen visible
        splashFrame.setOpacity(0f);
        splashFrame.setVisible(true);

        // Fade in the splash screen
        fadeIn(splashFrame);

        // Create a timer to close the splash screen after 4 seconds
        Timer timer = new Timer(4000, e -> {
            // Fade out before closing
            fadeOut(splashFrame);

            splashFrame.dispose(); // Close the splash screen
            new Dashboard(); // Open the Dashboard
        });
        timer.setRepeats(false); // Only run once
        timer.start(); // Start the timer
    }

    private void fadeIn(JFrame frame) {
        for (float i = 0; i <= 1; i += 0.05) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            frame.setOpacity(i);
        }
    }

    private void fadeOut(JFrame frame) {
        for (float i = 1; i >= 0; i -= 0.05) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            frame.setOpacity(i);
        }
    }

    public static void main(String[] args) {
        new SplashScreen(); // Start with the splash screen
    }
}
