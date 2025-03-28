package gui;

import javax.swing.*;
import java.awt.*;

public class ApplianceDatabasePage extends PageTemplate {

    public ApplianceDatabasePage() {
        super("Update Appliance Database");
    }

    @Override
    protected JPanel createContentPanel() {
        JPanel buttonContainer = createPanel(new FlowLayout(FlowLayout.CENTER));

        // Action buttons
        createActionButton("Add Appliance", () -> new AddApplianceDialog(frame), buttonContainer, null);
        createVerticalSpacing(buttonContainer);
        createActionButton("Delete Appliance", () -> new DeleteApplianceDialog(frame), buttonContainer, null);
        createVerticalSpacing(buttonContainer);
        createActionButton("Update Appliance", () -> new UpdateApplianceDialog(frame), buttonContainer, null);

        return buttonContainer;
    }

    @Override
    protected JPanel createHeaderPanel() {
        JPanel headerPanel = createPanel(new BorderLayout());

        JLabel title = new JLabel("Database Configuration", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(title, BorderLayout.CENTER);
        createActionButton("< Back", () -> frame.dispose(), headerPanel, BorderLayout.WEST);

        return headerPanel;
    }

    @Override
    protected JPanel createFooterPanel() {
        return null;
    }


}
