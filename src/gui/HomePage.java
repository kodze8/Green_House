package gui;

import services.DatabaseService;
import util.PageTemplate;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class HomePage extends PageTemplate {

    public HomePage() {
        super("Home");
        validateDatabase();
    }

    @Override
    protected JPanel createHeaderPanel() {
        return null;
    }

    @Override
    protected JPanel createFooterPanel() {
        return null;
    }

    @Override
    protected JPanel createContentPanel() {
        JPanel buttonContainer = createPanel(new FlowLayout(FlowLayout.CENTER));

        createActionButton("Update Database", ApplianceDatabasePage2::new, buttonContainer, null);
        createVerticalSpacing(buttonContainer);
        createActionButton("Configure Household", HouseholdConfigurationPage2::new, buttonContainer, null);

        return buttonContainer;
    }

    private void validateDatabase() {
        List<String> invalidEntries = DatabaseService.validateDatabase();
        if (!invalidEntries.isEmpty()) {
            String message = "The following entries are invalid:\n" + String.join("\n", invalidEntries);
            JOptionPane.showMessageDialog(frame, message, "Invalid Database Entries", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new HomePage();
    }
}

