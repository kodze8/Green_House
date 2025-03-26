package gui;

import util.PageTemplate;

import javax.swing.*;
import java.awt.*;

public class HomePage extends PageTemplate {

    public HomePage() {
        super("Home", 500, 500);
    }

    @Override
    protected JPanel createHeaderPanel() {
        return null;
    }

    @Override
    protected JPanel createContentPanel() {
        JPanel buttonContainer = createPanel(200, 150, null);
        buttonContainer.setLayout(new BoxLayout(buttonContainer, BoxLayout.Y_AXIS));

        createActionButton("Update Database", () -> navigateTo(new ApplianceDatabasePage2()), buttonContainer, BorderLayout.CENTER);
        buttonContainer.add(Box.createVerticalStrut(10)); // Adds spacing
        createActionButton("Configure Household", () -> navigateTo(new HouseholdConfigurationPage2()), buttonContainer, BorderLayout.CENTER);

        return buttonContainer;
    }

    public static void main(String[] args) {
        new HomePage();
    }
}

