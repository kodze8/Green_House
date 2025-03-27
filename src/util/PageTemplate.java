package util;

import javax.swing.*;
import java.awt.*;

public abstract class PageTemplate {
    protected JFrame frame;
    private static final int FRAME_WIDTH = 1100;
    private static final int FRAME_HEIGHT = 700;
    private static final int DISTANCE = 20;

    public PageTemplate(String title) {
        frame = createFrame(title);


        JPanel headerPanel = createHeaderPanel();
        JPanel contentPanel = createContentPanel(); // Abstract method for custom content
        JPanel footerPanel = createFooterPanel();

        if (headerPanel != null) {
            frame.add(headerPanel, BorderLayout.NORTH);
        }
        if (contentPanel != null) {
            frame.add(contentPanel, BorderLayout.CENTER);
            createScroll(contentPanel);
        }
        if (footerPanel != null){
            frame.add(footerPanel, BorderLayout.SOUTH); // Add to the bottom of the bodyPanel
        }

        frame.setVisible(true);
    }

    protected JFrame createFrame(String title) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setLayout(new BorderLayout());
        return frame;
    }

    // Abstract method to be implemented by child classes
    protected abstract JPanel createContentPanel();
    protected abstract JPanel createHeaderPanel();
    protected abstract JPanel createFooterPanel();

    protected void createActionButton(String label, Runnable event, JPanel buttonContainer, Object position){
        JButton newButton = new JButton(label);
        newButton.addActionListener(e -> event.run());
        buttonContainer.add(newButton, position);
    }

    protected JComboBox<String> createDropdown(String[] options, String placeholder){
        JComboBox<String> newDropdown = new JComboBox<>(options);
        newDropdown.insertItemAt(placeholder,0);
        newDropdown.setSelectedIndex(0);
        return newDropdown;
    }

    protected JTextField createInputField(String label, JPanel inputContainer){
        JTextField inputField = new JTextField();
        JLabel newLabel = new JLabel(label);
        inputContainer.add(newLabel);
        inputContainer.add(inputField);
        return inputField;
    }

    protected JPanel createPanel(LayoutManager layout) {
        JPanel panel = new JPanel();

        if (layout != null) {
            panel.setLayout(layout);
        }

        return panel;
    }

    private void createScroll(JPanel panel) {
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        this.frame.add(scrollPane, BorderLayout.CENTER);
    }

    protected void createVerticalSpacing(JPanel panel) {
        panel.add(Box.createVerticalStrut(DISTANCE));
    }
}
