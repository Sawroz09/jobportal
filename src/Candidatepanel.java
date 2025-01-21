import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

class Candidatepanel extends JPanel {
    private JTextField searchField;
    private JTable table;

    public Candidatepanel(String userName) {
        setLayout(null);
        setOpaque(false); // Make the panel transparent

        // Add the "Welcome" label
        JLabel welcomeLabel = new JLabel("All candidates");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 26));
        welcomeLabel.setForeground(Color.WHITE); // Set text color
        welcomeLabel.setBounds(450, 10, 550, 50); // Set x, y, width, and height
        add(welcomeLabel);

        // Add the search field and button
        searchField = new JTextField();
        searchField.setBounds(850, 120, 200, 30);
        add(searchField);

        JButton searchButton = new JButton("Search");
        searchButton.setBounds(1080, 120, 120, 30);
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
        table.setForeground(Color.WHITE);
        table.setBackground(Color.BLUE);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 24));
        table.getTableHeader().setBackground(Color.LIGHT_GRAY); // Header background
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
        scrollPane.setBounds(0, 160, 1200, 400);
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

    public void showRecord() {
        String input = searchField.getText().trim();
        try (Connection conn = dbconn.connection()) { // Ensure this method is correctly implemented and returns a valid connection
            String sql;
            PreparedStatement pstmt;

            if (input.isEmpty()) {
                sql = "SELECT `id`, `name`, `email`, `qualification`, `city`, `state`, `phoneno`, `pass_year`, `status` FROM `users`";
                pstmt = conn.prepareStatement(sql);
            } else {
                sql = "SELECT `id`, `name`, `email`, `qualification`, `city`, `state`, `phoneno`, `pass_year`, `status` FROM `users` WHERE `name` LIKE ? OR `email` LIKE ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, "%" + input + "%");
                pstmt.setString(2, "%" + input + "%");
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
                // Add "Delete" button placeholder to the last column
                row[columnCount] = "Delete"; // Placeholder text for the button
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return model;
    }

    private void customizeTable() {
        table.getColumnModel().getColumn(0).setPreferredWidth(100); // id
        table.getColumnModel().getColumn(1).setPreferredWidth(200); // name
        table.getColumnModel().getColumn(2).setPreferredWidth(270); // email
        table.getColumnModel().getColumn(3).setPreferredWidth(270); // email
        table.getColumnModel().getColumn(4).setPreferredWidth(200); // city
        table.getColumnModel().getColumn(5).setPreferredWidth(150); // state
        table.getColumnModel().getColumn(6).setPreferredWidth(150); // phoneno
        table.getColumnModel().getColumn(7).setPreferredWidth(200); // pass_year
        table.getColumnModel().getColumn(8).setPreferredWidth(150); // status
        table.getColumnModel().getColumn(9).setPreferredWidth(150); // actions

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        table.getColumnModel().getColumn(8).setCellRenderer(new StatusRenderer());
        table.getColumnModel().getColumn(8).setCellEditor(new StatusEditor());

        table.getColumnModel().getColumn(9).setCellRenderer(new ActionsRenderer());
        table.getColumnModel().getColumn(9).setCellEditor(new ActionsEditor(new JCheckBox()));
    }

    private void deleteSelectedRecord(int candidateId) {
        int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this record?");
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = dbconn.connection();
                 PreparedStatement pstmt = conn.prepareStatement("DELETE FROM `users` WHERE `id` = ?")) {
                pstmt.setInt(1, candidateId);
                pstmt.executeUpdate();
                showRecord(); // Refresh the table to reflect the change
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to delete record.");
            }
        }
    }

    class StatusRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JButton button = new JButton();
            if (value != null && value.toString().equals("1")) {
                button.setText("Active");
                button.setBackground(Color.GREEN);
            } else {
                button.setText("Inactive");
                button.setBackground(Color.RED);
            }
            return button;
        }
    }

    class StatusEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
        private JButton button;
        private String currentStatus;
        private int currentRow;

        public StatusEditor() {
            button = new JButton();
            button.addActionListener(this);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            currentRow = row;
            currentStatus = value != null ? value.toString() : "0"; // Use String value here
            if (currentStatus.equals("1")) {
                button.setText("Active");
                button.setBackground(Color.GREEN);
            } else {
                button.setText("Inactive");
                button.setBackground(Color.RED);
            }
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return currentStatus;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String newStatus = currentStatus.equals("1") ? "0" : "1"; // Toggle status
            int candidateId = (int) table.getModel().getValueAt(currentRow, 0); // Get ID as int

            // Update the status in the database
            try (Connection conn = dbconn.connection();
                 PreparedStatement pstmt = conn.prepareStatement("UPDATE `users` SET `status` = ? WHERE `id` = ?")) {
                pstmt.setString(1, newStatus);
                pstmt.setInt(2, candidateId);
                pstmt.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to update status.");
            }

            currentStatus = newStatus; // Update the currentStatus field
            fireEditingStopped(); // Notify the table that editing has stopped
            showRecord(); // Refresh the table to reflect the change
        }
    }

    class ActionsRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JButton button = new JButton("Delete");
            button.setBackground(Color.RED);
            button.setForeground(Color.WHITE);
            return button;
        }
    }

    class ActionsEditor extends DefaultCellEditor {
        private JButton button;
        private boolean isPushed;
        private int currentRow;

        public ActionsEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("Delete");
            button.setBackground(Color.RED);
            button.setForeground(Color.WHITE);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    isPushed = true;
                    fireEditingStopped(); // Notify that editing is complete
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            currentRow = row;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                int candidateId = (int) table.getValueAt(currentRow, 0); // Get candidate ID
                String status = table.getValueAt(currentRow, 8).toString(); // Get the status value

                if ("1".equals(status)) {
                    JOptionPane.showMessageDialog(null, "Cannot delete an active record.");
                } else {
                    deleteSelectedRecord(candidateId); // Delete the record if status is not 1
                }
                isPushed = false;
            }
            return "Delete";
        }
    }

}
