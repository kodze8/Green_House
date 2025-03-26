package util;

import javax.swing.*;
import java.awt.*;

public abstract class DialogTemplate extends JDialog {
    protected JPanel contentPanel;
    protected JButton confirmButton;

    public DialogTemplate(JFrame parent, String title, int width, int height) {
        super(parent, title, true);
        setSize(width, height);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        contentPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        add(contentPanel, BorderLayout.CENTER);

        confirmButton = new JButton(getConfirmButtonText());
        confirmButton.addActionListener(e -> onConfirm());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(confirmButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    protected abstract String getConfirmButtonText();  // e.g., "Add", "Update", "Delete"

    protected abstract void onConfirm(); // Implement logic for button action
}
