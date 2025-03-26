package util;

import gui.HomePage;
import services.DatabaseService;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Set;

public abstract class PageTemplate {
    protected JFrame frame;

    public PageTemplate(String title, int width, int height) {
        validateDatabase();
        frame = createFrame(title, width, height);

        // Wrapper panel for centering content
        JPanel wrapperPanel = createPanel(0,0,new GridBagLayout());
        JPanel headerPanel = createHeaderPanel();
        JPanel contentPanel = createContentPanel(); // Abstract method for custom content

        if (headerPanel != null) {
            wrapperPanel.add(headerPanel);
        }

        if (contentPanel != null) {
            wrapperPanel.add(contentPanel);
        }

        frame.add(wrapperPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    protected JFrame createFrame(String title, int width, int height) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.setLayout(new BorderLayout());
        return frame;
    }

    // Abstract method to be implemented by child classes
    protected abstract JPanel createContentPanel();
    protected abstract JPanel createHeaderPanel();

    // Allow subclasses to define their layout
    protected LayoutManager getDefaultLayout() {
        return new GridBagLayout();
    }

    // Utility method for navigation
    protected void navigateTo(PageTemplate newPage) {
        frame.dispose();
    }


    private void validateDatabase() {
        List<String> invalidEntries = DatabaseService.validateDatabase();
        if (!invalidEntries.isEmpty()) {
            String message = "The following entries are invalid:\n" + String.join("\n", invalidEntries);
            JOptionPane.showMessageDialog(frame, message, "Invalid Database Entries", JOptionPane.ERROR_MESSAGE);
            navigateTo(new HomePage());
        }
    }

    protected void createActionButton(String label, Runnable event, JPanel buttonContainer, Object position){
        JButton newButton = new JButton(label);
        newButton.addActionListener(e -> event.run());
        buttonContainer.add(newButton, position);
    }

    protected JComboBox<String> createDropdown(String[] options, String placeholder){
        JComboBox<String> newDropdown = new JComboBox<>(options);
        PanelStatics.addPlaceholder(newDropdown, placeholder);
        newDropdown.addActionListener(e -> {
            String selectedOption = (String) newDropdown.getSelectedItem();
            assert selectedOption != null;
            if (selectedOption.equals(placeholder)) {
                newDropdown.setSelectedIndex(0);
            }
        });
        return newDropdown;
    }

    protected JTextField createInputField(String label, JPanel inputContainer){
        JTextField inputField = new JTextField();
        JLabel newLabel = new JLabel(label);
        inputContainer.add(newLabel);
        inputContainer.add(inputField);
        return inputField;
    }

    protected JPanel createPanel(int width, int height, LayoutManager layout) {
        JPanel panel = new JPanel();
        if (width > 0 && height > 0) {
            panel.setPreferredSize(new Dimension(width, height));
        }

        if (layout != null) {
            panel.setLayout(layout);
        }

        return panel;
    }

    protected void removePanelFromFrame(JPanel panel) {}

    protected void addPanelToFrame(JPanel panel, String position) {
        frame.add(panel, position);
        frame.revalidate();
        frame.repaint();
    }

    private void createScroll(JPanel panel) {
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        this.frame.add(scrollPane, BorderLayout.CENTER);
    }
}
