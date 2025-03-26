package gui;

import util.PageTemplate;

import javax.swing.*;
import java.awt.*;

public class ApplianceDatabasePage2 extends PageTemplate {

    public ApplianceDatabasePage2() {
        super("Update Appliance Database", 500, 500);
    }

    @Override
    protected JPanel createContentPanel() {
        JPanel buttonContainer = createPanel(frame.getWidth(), 350, null);
        buttonContainer.setLayout(new BoxLayout(buttonContainer, BoxLayout.Y_AXIS));

        // Action buttons
        createActionButton("Add Appliance", () -> new AddApplianceDialog(frame), buttonContainer, BorderLayout.CENTER);
        buttonContainer.add(Box.createVerticalStrut(20)); // Adds spacing
        createActionButton("Delete Appliance", () -> new DeleteApplianceDialog(frame), buttonContainer, BorderLayout.CENTER);
        buttonContainer.add(Box.createVerticalStrut(20)); // Adds spacing
        createActionButton("Update Appliance", () -> new UpdateApplianceDialog(frame), buttonContainer, BorderLayout.CENTER);

        // Button section
        JPanel centerPanel = createPanel(0, 0, new BorderLayout());
        centerPanel.add(buttonContainer, BorderLayout.CENTER);

        return centerPanel;
    }

    @Override
    protected JPanel createHeaderPanel() {
        JPanel headerPanel = createPanel(frame.getWidth(), 80, null);
        headerPanel.setLayout(new BorderLayout());

        JLabel title = new JLabel("Database Configuration", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
//        title.setBorder(BorderFactory.createEmptyBorder(0, -80, 0, 0));
        headerPanel.add(title, BorderLayout.CENTER);
        createActionButton("Menu", () -> navigateTo(new HomePage()), headerPanel, BorderLayout.WEST);

        return headerPanel;
    }


}
