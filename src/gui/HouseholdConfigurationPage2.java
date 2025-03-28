package gui;

import enums.Country;
import services.HouseholdService;
import enums.EnergyLabel;
import domain.Household;
import util.PageTemplate;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class HouseholdConfigurationPage2 extends PageTemplate {
    private JPanel bodyPanel;
    private JComboBox<String> countryBox;
    private JComboBox<String> energyLabelBox;
    private final List<AppliancePanel> appliancePanels;

    private static final int PANEL_HEIGHT = 50;

    private static final String[] energyLabelOptions = EnergyLabel.getAllEnumCaptions().toArray(new String[0]);
    private static final String[] countryOptions = Country.getAllEnumCaptions().toArray(new String[0]);


    public HouseholdConfigurationPage2() {
        super("Household Configuration");
        this.appliancePanels = new ArrayList<>();
    }

    @Override
    protected JPanel createHeaderPanel() {
        JPanel headerPanel = createPanel(null);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        // Title Section
        JPanel titlePanel = createPanel(new BorderLayout());
        createActionButton("< Back", () -> frame.dispose(), titlePanel, BorderLayout.WEST);
        JLabel title = new JLabel("Household Configuration", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setBorder(BorderFactory.createEmptyBorder(0, -80, 0, 0));
        titlePanel.add(title, BorderLayout.CENTER);

        // Household fields Section
        JPanel householdFieldsPanel = createPanel(new FlowLayout(FlowLayout.CENTER));
        this.countryBox = createDropdown(countryOptions, "Select Country");
        this.energyLabelBox = createDropdown(energyLabelOptions, "Select Energy Label");
        householdFieldsPanel.add(countryBox);
        householdFieldsPanel.add(energyLabelBox);

        // Appliances configuration
        JPanel buttonAppliancePanel = createPanel(new FlowLayout(FlowLayout.LEFT));
        createActionButton("Add Appliance", this::addAppliancePanel, buttonAppliancePanel, null);

        headerPanel.add(titlePanel);
        headerPanel.add(householdFieldsPanel);
        headerPanel.add(buttonAppliancePanel);

        return headerPanel;
    }

    @Override
    protected JPanel createFooterPanel() {
        // Action buttons
        JPanel buttonActionsPanel = createPanel(new FlowLayout(FlowLayout.CENTER));
        createActionButton("Calculate Carbon Footprint", () -> handleButtonClick(this::openCFWindow), buttonActionsPanel, null);
        createActionButton("Show Statistics", () -> handleButtonClick(this::openStatisticsWindow), buttonActionsPanel, null);
        return buttonActionsPanel;
    }

    @Override
    protected JPanel createContentPanel() {
        JPanel contentPanel = createPanel(new BorderLayout()); // Use BorderLayout
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        this.bodyPanel = contentPanel;

        return this.bodyPanel;
    }

    private void addAppliancePanel() {
        JPanel panelWrapper = createPanel(new FlowLayout(FlowLayout.LEFT));
        panelWrapper.setMaximumSize(new Dimension(frame.getWidth(), PANEL_HEIGHT));
        AppliancePanel appliancePanel = new AppliancePanel();
        appliancePanel.deleteButton.addActionListener(e -> removeAppliancePanel(appliancePanel, panelWrapper));
        panelWrapper.add(appliancePanel.panel);
        this.appliancePanels.add(appliancePanel);
        bodyPanel.add(panelWrapper, bodyPanel.getComponentCount() - 1);
        bodyPanel.add(Box.createVerticalStrut(10), bodyPanel.getComponentCount() - 1);
        bodyPanel.revalidate();
        bodyPanel.repaint();
    }

    private void removeAppliancePanel(AppliancePanel appliancePanel, JPanel panelWrapper) {
        bodyPanel.remove(panelWrapper);
        this.appliancePanels.remove(appliancePanel);
        appliancePanel.cleanup();
        bodyPanel.revalidate();
        bodyPanel.repaint();
    }

    private Household createHousehold() {
        HouseholdService householdService = new HouseholdService(this.appliancePanels, this.countryBox, this.energyLabelBox, this.frame);
        return householdService.getHousehold();
    }

    private void handleButtonClick(Consumer<Household> nextStep) {
        Household household = createHousehold();
        if (household != null) {
            nextStep.accept(household);
        }
    }

    private void openCFWindow(Household household) {
        int carbonFootprint = household.getCarbonFootPrint();
        int adjustedCarbonFootprint = household.energyLabelConversion();
        String message = "Your carbon footprint is " + carbonFootprint + " kgCO2e.\nYour carbon footprint adjusted based on your energy label is " + adjustedCarbonFootprint + " kgCO2e.";
        JOptionPane.showMessageDialog(frame, message, "Carbon footprint", JOptionPane.INFORMATION_MESSAGE);
    }

    private void openStatisticsWindow(Household household) {
        StatisticsPage.openStatisticsWindow(household);
    }

    public static void main(String[] args) {
        new HouseholdConfigurationPage2();
    }
}

