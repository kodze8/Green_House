package gui;

import services.DatabaseService;
import util.PageTemplate;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class HomePage extends PageTemplate {

    public HomePage() {
        super("Home");
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

        boolean navigate;
        List<String> invalidEntries = DatabaseService.validateDatabase();
        if (!invalidEntries.isEmpty()) {
            navigate = false;
            String message = "The following entries are invalid:\n" + String.join("\n", invalidEntries);
            JOptionPane.showMessageDialog(frame, message, "Invalid Database Entries", JOptionPane.ERROR_MESSAGE);
        } else {
            navigate = true;
        }

        createActionButton("Update Database",
                () -> { if (navigate) new ApplianceDatabasePage2(); },
                buttonContainer, null);

        createVerticalSpacing(buttonContainer);

        createActionButton("Configure Household",
                () -> { if (navigate) new HouseholdConfigurationPage2(); },
                buttonContainer, null);

        return buttonContainer;
    }

    public static void main(String[] args) {
        new HomePage();
    }
}

