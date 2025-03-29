package gui;

import services.DatabaseService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class HomePage extends PageTemplate {

    public HomePage() {
        super("Home");
        buildUI();
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

        createActionButton("Update Database", () -> handleButtonClick(ApplianceDatabasePage::new), buttonContainer, null);
        createVerticalSpacing(buttonContainer);
        createActionButton("Configure Household", () -> handleButtonClick(HouseholdConfigurationPage::new), buttonContainer, null);

        return buttonContainer;
    }

    private void handleButtonClick(Runnable nextStep) {
        List<String> invalidEntries = DatabaseService.validateDatabase();
        if (!invalidEntries.isEmpty()) {
            String message = "The following entries are invalid:\n" + String.join("\n", invalidEntries);
            JOptionPane.showMessageDialog(frame, message, "Invalid Database Entries", JOptionPane.ERROR_MESSAGE);
        } else {
            nextStep.run();
        }
    }

    public static void main(String[] args) {
        new HomePage();
    }
}

