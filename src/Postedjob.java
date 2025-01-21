import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

class Postedjob extends JPanel {
    private JTextField searchField;
    private JTable table;
    private int recruiter_Id;
    private String recEmail;

    public Postedjob(int recruiter_Id, String recEmail) {
        this.recruiter_Id = recruiter_Id;
        this.recEmail = recEmail;
        setLayout(null);
        setOpaque(false); // Make the panel transparent

        // Add the search field and button
        JLabel postedjob = new JLabel("All Posted Jobs");
        postedjob.setBounds(500, 10, 250, 30);
        postedjob.setFont(new Font("Segoe UI", Font.BOLD, 28));
        postedjob.setForeground(Color.WHITE);
        add(postedjob);

        searchField = new JTextField();
        searchField.setBounds(850, 80, 200, 30);
        add(searchField);

        JButton searchButton = new JButton("Search");
        searchButton.setBounds(1080, 80, 120, 30);
        searchButton.setForeground(Color.BLUE);

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
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(0, 120, 1220, 400);
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

        // Initially show all records
        showRecord();
    }

    private void showRecord() {
        String input = searchField.getText().trim();
        Connection conn = dbconn.connection(); // Ensure this method is correctly implemented and returns a valid connection

        try {
            String sql;
            PreparedStatement pstmt;

            if (input.isEmpty()) {
                // If input is empty, fetch all records for the recruiter
                sql = "SELECT id, title, description, category, status, location, pdate, vacancy, salary, experience, job_type " +
                        "FROM job WHERE jobrec_id = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, recruiter_Id);
            } else {
                boolean isNumeric = input.matches("\\d+");

                if (isNumeric) {
                    // If input is numeric, search by job ID
                    sql = "SELECT id, title, description, category, status, location, pdate, vacancy, salary, experience, job_type " +
                            "FROM job WHERE jobrec_id = ? AND id = ?";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1, recruiter_Id);
                    pstmt.setInt(2, Integer.parseInt(input));
                } else {
                    // If input is alphanumeric, search by job title
                    sql = "SELECT id, title, description, category, status, location, pdate, vacancy, salary, experience, job_type " +
                            "FROM job WHERE jobrec_id = ? AND title LIKE ?";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1, recruiter_Id);
                    pstmt.setString(2, "%" + input + "%");
                }
            }

            ResultSet res = pstmt.executeQuery();
            table.setModel(resultSetToTableModel(res));
            customizeTable(); // Customize table appearance
            pstmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Connection Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
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
            for (int column = 1; column <= columnCount; column++) {
                model.addColumn(metaData.getColumnName(column));
            }
            model.addColumn("Actions"); // Add "Actions" column for buttons

            // Add rows
            while (rs.next()) {
                Object[] row = new Object[columnCount + 1];
                for (int column = 1; column <= columnCount; column++) {
                    row[column - 1] = rs.getObject(column);
                }
                // Add placeholder for "Update" and "Delete" buttons
                row[columnCount] = "Actions"; // Placeholder text
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return model;
    }

    private void customizeTable() {
        // Customize the table columns
        table.getColumnModel().getColumn(0).setPreferredWidth(50); // ID
        table.getColumnModel().getColumn(1).setPreferredWidth(150); // Title
        table.getColumnModel().getColumn(2).setPreferredWidth(250); // Description
        table.getColumnModel().getColumn(3).setPreferredWidth(110); // Category
        table.getColumnModel().getColumn(4).setPreferredWidth(100); // Status
        table.getColumnModel().getColumn(5).setPreferredWidth(120); // Location
        table.getColumnModel().getColumn(6).setPreferredWidth(150); // Publish Date
        table.getColumnModel().getColumn(7).setPreferredWidth(80); // Vacancy
        table.getColumnModel().getColumn(8).setPreferredWidth(80); // Salary
        table.getColumnModel().getColumn(9).setPreferredWidth(120); // Experience
        table.getColumnModel().getColumn(10).setPreferredWidth(90); // Job Type
        table.getColumnModel().getColumn(11).setPreferredWidth(250); // Actions

        // Increase row height
        table.setRowHeight(40);
        table.setBackground(Color.blue);
        table.setForeground(Color.white);
        // Center data in all cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Set up custom renderer and editor for the "Status" column
        table.getColumnModel().getColumn(4).setCellRenderer(new StatusRenderer());
        table.getColumnModel().getColumn(4).setCellEditor(new StatusEditor());

        // Set up custom renderer and editor for the "Actions" column
        table.getColumnModel().getColumn(11).setCellRenderer(new ActionsRenderer());
        table.getColumnModel().getColumn(11).setCellEditor(new ActionsEditor());
    }

    private void deleteSelectedRecord(int jobId) {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this record?");
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = dbconn.connection();
                 PreparedStatement pstmt = conn.prepareStatement("DELETE FROM job WHERE id = ?")) {
                pstmt.setInt(1, jobId);
                pstmt.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to delete record.");
            }
            showRecord(); // Refresh the table to reflect the change
        }
    }

    class StatusRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JButton button = new JButton();
            if (value != null && value.toString().equals("1")) {
                button.setText("Open");
                button.setBackground(Color.GREEN);
            } else {
                button.setText("Closed");
                button.setBackground(Color.RED);
            }
            return button;
        }
    }

    class StatusEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
        private JButton button;
        private String currentStatus;
        private int currentRow;
        private int jobId;

        public StatusEditor() {
            button = new JButton();
            button.addActionListener(this);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            jobId = (int) table.getValueAt(row, 0); // Get the job ID
            currentStatus = value != null ? value.toString() : "0"; // Get current status
            currentRow = row;
            updateButton();
            return button;
        }

        private void updateButton() {
            if (currentStatus.equals("1")) {
                button.setText("Open");
                button.setBackground(Color.GREEN);
            } else {
                button.setText("Closed");
                button.setBackground(Color.RED);
            }
        }

        @Override
        public Object getCellEditorValue() {
            return currentStatus;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try (Connection conn = dbconn.connection()) {
                String newStatus = currentStatus.equals("1") ? "0" : "1";
                PreparedStatement pstmt = conn.prepareStatement("UPDATE job SET status = ? WHERE id = ?");
                pstmt.setString(1, newStatus);
                pstmt.setInt(2, jobId);
                pstmt.executeUpdate();

                currentStatus = newStatus;
                updateButton();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(button, "Failed to update status.");
            }
            fireEditingStopped();
        }
    }

    class ActionsRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            panel.setOpaque(true); // Make panel opaque for visibility
            JButton updateButton = new JButton("Update");
            updateButton.setForeground(Color.WHITE);
            updateButton.setBackground(Color.BLUE);
            JButton deleteButton = new JButton("Delete");
            deleteButton.setForeground(Color.WHITE);
            deleteButton.setBackground(Color.RED);
            panel.add(updateButton);
            panel.add(deleteButton);
            return panel;
        }
    }

    class ActionsEditor extends AbstractCellEditor implements TableCellEditor {
        private JPanel panel;
        private JButton updateButton;
        private JButton deleteButton;
        private int jobId;

        public ActionsEditor() {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            panel.setOpaque(true); // Make panel opaque for visibility
            updateButton = new JButton("Update");
            updateButton.setForeground(Color.WHITE);
            updateButton.setBackground(Color.BLUE);
            updateButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Handle update action
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        jobId = (int) table.getValueAt(selectedRow, 0); // Get job ID
                        new Updatejob(jobId);
                    }
                    fireEditingStopped();
                }
            });

            deleteButton = new JButton("Delete");
            deleteButton.setForeground(Color.WHITE);
            deleteButton.setBackground(Color.RED);
            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        jobId = (int) table.getValueAt(selectedRow, 0); // Get job ID
                        deleteSelectedRecord(jobId); // Delete the selected job
                    }
                    fireEditingStopped();
                }
            });

            panel.add(updateButton);
            panel.add(deleteButton);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            jobId = (int) table.getValueAt(row, 0); // Get the job ID from the table
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return jobId;
        }
    }
}
