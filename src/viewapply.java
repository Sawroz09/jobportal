import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.*;

class viewapply extends JPanel {
    private JTextField searchField;
    private JTable table;
    private JButton headerDeleteButton;
    private String userName;
    private int canid;

    public viewapply( int canid) {
        this.userName = userName;
        this.canid = canid;

        setLayout(null);
        setOpaque(false); // Make the panel transparent

        // Add the "Welcome" label
        JLabel welcomeLabel = new JLabel("All Applied job");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 26));
        welcomeLabel.setForeground(Color.WHITE); // Set text color
        welcomeLabel.setBounds(400, 10, 550, 50); // Set x, y, width, and height
        add(welcomeLabel);

        // Add the search field and button
        searchField = new JTextField();
        searchField.setBounds(750, 120, 200, 30);
        add(searchField);

        JButton searchButton = new JButton("Search");
        searchButton.setBounds(980, 120, 120, 30);
        searchButton.setForeground(Color.blue);

        // Load image icon (ensure the path is correct)
        ImageIcon searchIcon = new ImageIcon("Images/loupe.png"); // Replace with your image path
        searchButton.setIcon(searchIcon); // Set the image icon

        // Customize button appearance
        ButtonUtils.setButtonRounded(searchButton, 20, Color.LIGHT_GRAY); // Rounded button

        // Set image and text alignment
        searchButton.setHorizontalAlignment(SwingConstants.LEFT); // Align image to the left
        searchButton.setHorizontalTextPosition(SwingConstants.RIGHT); // Place text to the right of the image

        add(searchButton);
        // Add the table
        table = new JTable();
        table.setRowHeight(50); // Set row height to allow more space between rows
        table.setOpaque(false); // Make the table background transparent
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setForeground(Color.white);
        table.setBackground(Color.BLUE);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 24));
        table.getTableHeader().setBackground(Color.LIGHT_GRAY); // Transparent header background
        table.getTableHeader().setForeground(Color.WHITE);

        // Make the table and its cells transparent
        table.setOpaque(false);
        ((DefaultTableCellRenderer) table.getDefaultRenderer(Object.class)).setOpaque(false);
        table.setShowGrid(false); // Remove grid lines

        // Hide the borders
        table.setBorder(null); // Hide border of the table
        table.getTableHeader().setBorder(null); // Hide border of the table header

        // Set up the scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 160, 1100, 400);
        scrollPane.setOpaque(false); // Make the scroll pane transparent
        scrollPane.getViewport().setOpaque(false); // Make the viewport transparent
        add(scrollPane);

        // Set up the search button action
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRecord();
            }
        });
        // Set up the refresh timer
       Timer  refreshTimer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRecord(); // Refresh the records every 5 seconds
            }
        });
        refreshTimer.start(); // Start the timer
        // Initially show all records
        showRecord();
    }

    public void showRecord() {
        String input = searchField.getText().trim(); // Get text from the JTextField and trim any extra spaces
        try (Connection conn = dbconn.connection()) { // Ensure this method is correctly implemented and returns a valid connection
            String sql;
            PreparedStatement pstmt;

            if (input.isEmpty()) {
                // Fetch all records matching the recId
                sql = "SELECT `id`, `title`, `name`, `qualification`, `email`, `upload_date`, `files`, `rec_id` FROM `files` WHERE `canid` = ? ORDER BY `upload_date` DESC";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, canid);
            } else {
                // Search by title or name and matching recId
                sql = "SELECT `id`, `title`, `name`, `qualification`, `email`, `upload_date`, `files`, `rec_id` FROM `files` WHERE `canid` = ? AND (`title` LIKE ? OR `name` LIKE ?) ORDER BY `upload_date` DESC";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, canid);
                pstmt.setString(2, "%" + input + "%");
                pstmt.setString(3, "%" + input + "%");
            }

            ResultSet res = pstmt.executeQuery();
            table.setModel(resultSetToTableModel(res));
            customizeTable(); // Customize table appearance
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
        }
    }

    private TableModel resultSetToTableModel(ResultSet rs) {
        ResultSetMetaData metaData;
        int columnCount;

        try {
            metaData = rs.getMetaData();
            columnCount = metaData.getColumnCount();
        } catch (SQLException e) {
            e.printStackTrace();
            return new DefaultTableModel();
        }

        DefaultTableModel model = new DefaultTableModel();

        // Add column names
        try {
            model.addColumn("ID");
            model.addColumn("Title");
            model.addColumn("Name");
            model.addColumn("Qualification");
            model.addColumn("Email");
            model.addColumn("Upload Date");
            model.addColumn("Actions"); // Add "Actions" column for buttons

            // Add rows
            while (rs.next()) {
                Object[] row = new Object[7];
                row[0] = rs.getObject("id");
                row[1] = rs.getObject("title");
                row[2] = rs.getObject("name");
                row[3] = rs.getObject("qualification");
                row[4] = rs.getObject("email");
                row[5] = rs.getObject("upload_date");
                row[6] = "Actions"; // Placeholder text for the buttons
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return model;
    }

    private void customizeTable() {
        // Customize the table columns
        table.getColumnModel().getColumn(0).setPreferredWidth(90); // Reduced ID column width
        table.getColumnModel().getColumn(1).setPreferredWidth(250); // Title
        table.getColumnModel().getColumn(2).setPreferredWidth(250); // Name
        table.getColumnModel().getColumn(3).setPreferredWidth(250); // Qualification
        table.getColumnModel().getColumn(4).setPreferredWidth(250); // Email
        table.getColumnModel().getColumn(5).setPreferredWidth(250); // Upload Date
        table.getColumnModel().getColumn(6).setPreferredWidth(300); // Increased Actions column width

        // Center data in all cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Set up a custom renderer and editor for the "Actions" column
        table.getColumnModel().getColumn(6).setCellRenderer(new ActionsRenderer());
        table.getColumnModel().getColumn(6).setCellEditor(new ActionsEditor(new JCheckBox()));
    }


    private void downloadFile(int fileId) {
        try (Connection conn = dbconn.connection()) {
            String sql = "SELECT `files` FROM `files` WHERE `id` = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, fileId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                InputStream input = rs.getBinaryStream("files");

                // Show save file dialog
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Save File");
                int userSelection = fileChooser.showSaveDialog(this);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser.getSelectedFile();

                    try (FileOutputStream output = new FileOutputStream(fileToSave)) {
                        byte[] buffer = new byte[1024];
                        while (input.read(buffer) > 0) {
                            output.write(buffer);
                        }
                        JOptionPane.showMessageDialog(null, "File downloaded successfully.");
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to download the file.");
        }
    }


    private void deleteFile(int fileId) {
        try (Connection conn = dbconn.connection()) {
            String sql = "DELETE FROM `files` WHERE `id` = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, fileId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "File deleted successfully.");
                showRecord(); // Refresh the table after deletion
            } else {
                JOptionPane.showMessageDialog(null, "Failed to delete the file.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error occurred while deleting the file.");
        }
    }

    class ActionsRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JPanel panel = new JPanel(new GridLayout(1, 2, 5, 0));
            JButton downloadButton = new JButton("Download");
            JButton deleteButton = new JButton("cancel");

            // Customize button colors
            downloadButton.setBackground(Color.GREEN);
            downloadButton.setForeground(Color.WHITE);
            deleteButton.setBackground(Color.RED);
            deleteButton.setForeground(Color.WHITE);

            panel.add(downloadButton);
            panel.add(deleteButton);

            // Add action listeners to the buttons
            downloadButton.addActionListener(e -> {
                int fileId = (int) table.getValueAt(row, 0); // Assuming 'id' is in the first column
                downloadFile(fileId);
            });

            deleteButton.addActionListener(e -> {
                int fileId = (int) table.getValueAt(row, 0); // Assuming 'id' is in the first column
                int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this file?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    deleteFile(fileId);
                }
            });

            return panel;
        }
    }

    class ActionsEditor extends DefaultCellEditor {
        private JButton downloadButton;
        private JButton deleteButton;
        private JTable table;

        public ActionsEditor(JCheckBox checkBox) {
            super(checkBox);
            downloadButton = new JButton("Download");
            deleteButton = new JButton("Cancel");

            // Customize button colors
            downloadButton.setBackground(Color.GREEN);
            downloadButton.setForeground(Color.WHITE);
            deleteButton.setBackground(Color.RED);
            deleteButton.setForeground(Color.WHITE);

            downloadButton.addActionListener(e -> {
                int row = table.getSelectedRow();
                int fileId = (int) table.getValueAt(row, 0); // Assuming 'id' is in the first column
                downloadFile(fileId);
                fireEditingStopped();
            });

            deleteButton.addActionListener(e -> {
                int row = table.getSelectedRow();
                int fileId = (int) table.getValueAt(row, 0); // Assuming 'id' is in the first column
                int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this file?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    deleteFile(fileId);
                }
                fireEditingStopped();
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.table = table; // Set the table reference
            JPanel panel = new JPanel(new GridLayout(1, 2, 5, 0));
            panel.add(downloadButton);
            panel.add(deleteButton);
            return panel;
        }
    }

}

