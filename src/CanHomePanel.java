import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class CanHomePanel extends JPanel {
    private JLabel imageLabel;
    private ImageIcon[] imageIcons;
    private int currentImageIndex = 0;
    private Timer imageTimer;
    private Timer fadeTimer;
    private float alpha = 1f; // Start with fully opaque
    private boolean isFirstImage = true; // Flag to check if it's the first image
    public CanHomePanel(String userName) {
        setLayout(null);
        setOpaque(false); // Make the panel transparent

        // Add the "Welcome" label
        JLabel Welcome = new JLabel("Welcome ");
        Welcome.setFont(new Font("Arial", Font.BOLD, 56));
        Welcome.setForeground(Color.WHITE); // Set text color
        Welcome.setBounds(100, 150, 350, 50); // Set x, y, width, and height
        add(Welcome);

        // Add the "Username" label
        JLabel Username = new JLabel(userName);
        Username.setFont(new Font("Arial", Font.BOLD, 46));
        Username.setForeground(Color.WHITE); // Set text color
        Username.setBounds(140, 210, 350, 50); // Set x, y, width, and height
        add(Username);
        // Load the images
        String[] imagePaths = {
                "Images/candash.png",
                "Images/can2.png",
                "Images/can3.png",
                "Images/admin5.png",
                "Images/admin6.png"
        };

        imageIcons = new ImageIcon[imagePaths.length];

        for (int i = 0; i < imagePaths.length; i++) {
            imageIcons[i] = new ImageIcon(new ImageIcon(imagePaths[i])
                    .getImage().getScaledInstance(600, 450, Image.SCALE_SMOOTH));
        }

        // Initialize the label with the first image
        imageLabel = new JLabel(imageIcons[currentImageIndex]);
        imageLabel.setBounds(520, 50, 600, 450); // Adjust the image position
        add(imageLabel);

        // Start the image transition timer
        imageTimer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isFirstImage) {
                    isFirstImage = false; // Skip the fade for the first image
                    return;
                }
                fadeOutAndChangeImage();
            }
        });
        imageTimer.start();
    }

    private void fadeOutAndChangeImage() {
        // Start fading out the current image
        fadeTimer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                alpha -= 0.05f;
                if (alpha <= 0f) {
                    alpha = 0f;
                    ((Timer) e.getSource()).stop(); // Stop fading out
                    currentImageIndex = (currentImageIndex + 1) % imageIcons.length;
                    imageLabel.setIcon(imageIcons[currentImageIndex]);
                    fadeInImage(); // Start fading in the new image
                }
                repaint();
            }
        });
        fadeTimer.start();
    }

    private void fadeInImage() {
        // Start fading in the new image
        alpha = 0f;
        Timer fadeInTimer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                alpha += 0.05f;
                if (alpha >= 1f) {
                    alpha = 1f;
                    ((Timer) e.getSource()).stop(); // Stop fading in
                }
                repaint();
            }
        });
        fadeInTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Draw the current image with the fading effect
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        Image currentImage = imageIcons[currentImageIndex].getImage();
        g2d.drawImage(currentImage, 520, 50, this); // Adjust placement of image
    }}
