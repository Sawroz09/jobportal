import javax.swing.*;
import java.awt.*;

class CustomComboBoxRenderer extends JLabel implements ListCellRenderer<String> {

    public CustomComboBoxRenderer() {
        setOpaque(true);
        setFont(new Font("Arial", Font.PLAIN, 20));
        setForeground(Color.BLUE); // Set the text color to blue
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
        setText(value);

        if (isSelected) {
            setBackground(new Color(173, 216, 230)); // Light blue background for selected item
            setForeground(Color.WHITE); // White text for selected item
        } else {
            setBackground(new Color(144, 238, 144)); // Light green background for unselected items
            setForeground(Color.BLUE); // Blue text for unselected items
        }

        return this;
    }
}