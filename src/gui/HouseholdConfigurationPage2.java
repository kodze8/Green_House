package gui;

import enums.Country;
import controllers.HouseholdController;
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

    private static final String[] energyLabelOptions = EnergyLabel.getAllEnumCaptions().toArray(new String[0]);
    private static final String[] countryOptions = Country.getAllEnumCaptions().toArray(new String[0]);


    public HouseholdConfigurationPage2() {
        super("Household Configuration", 1100, 700);
        this.appliancePanels = new ArrayList<>();
    }

    @Override
    protected JPanel createHeaderPanel() {
        JPanel headerPanel = createPanel(frame.getWidth(), 80, null);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        // Title Section
        JPanel titlePanel = createPanel(0, 0, new BorderLayout());
        createActionButton("Menu", () -> navigateTo(new HomePage()), titlePanel, BorderLayout.WEST);
        JLabel title = new JLabel("Household Configuration", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setBorder(BorderFactory.createEmptyBorder(0, -80, 0, 0));
        titlePanel.add(title, BorderLayout.CENTER);

        // Household fields Section
        JPanel householdFieldsPanel = createPanel(0, 0, new FlowLayout(FlowLayout.LEFT));
        this.countryBox = createDropdown(countryOptions, "Select Country");
        this.energyLabelBox = createDropdown(energyLabelOptions, "Select Energy Label");
        householdFieldsPanel.add(countryBox);
        householdFieldsPanel.add(energyLabelBox);

        headerPanel.add(titlePanel);
        headerPanel.add(householdFieldsPanel);

        //frame.add(headerPanel, BorderLayout.NORTH);

        return headerPanel;
    }

    @Override
    protected JPanel createContentPanel() {
        JPanel contentPanel = createPanel(1100, 700, new BorderLayout()); // Use BorderLayout
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        this.bodyPanel = contentPanel;

        // Appliances configuration
        JPanel buttonAppliancePanel = createPanel(0, 0, new FlowLayout(FlowLayout.LEFT));
        createActionButton("Add Appliance", this::addPanel, buttonAppliancePanel, null);
        this.bodyPanel.add(Box.createVerticalStrut(20));
        this.bodyPanel.add(buttonAppliancePanel);

        // Action buttons
        JPanel buttonActionsPanel = createPanel(frame.getWidth(), 80, null);
        buttonActionsPanel.setLayout(new BoxLayout(buttonActionsPanel, BoxLayout.X_AXIS));
        createActionButton("Calculate Carbon Footprint", () -> handleButtonClick(this::openCFWindow), buttonActionsPanel, BorderLayout.WEST);
        createActionButton("Show Statistics", () -> handleButtonClick(this::openStatisticsWindow), buttonActionsPanel, BorderLayout.WEST);
        this.bodyPanel.add(buttonActionsPanel, BorderLayout.SOUTH); // Add to the bottom of the bodyPanel

        return contentPanel;
    }

    private void addPanel() {
        JPanel panelWrapper = new JPanel();
        panelWrapper.setLayout(new FlowLayout(FlowLayout.LEFT));
        panelWrapper.setMaximumSize(new Dimension(1100, 50));
        AppliancePanel appliancePanel = new AppliancePanel();
        appliancePanel.deleteButton.addActionListener(e -> removePanel(appliancePanel, panelWrapper));
        panelWrapper.add(appliancePanel.panel);
        this.appliancePanels.add(appliancePanel);
        bodyPanel.add(panelWrapper, bodyPanel.getComponentCount() - 1);
        bodyPanel.add(Box.createVerticalStrut(10), bodyPanel.getComponentCount() - 1);
        bodyPanel.revalidate();
        bodyPanel.repaint();
    }

    private void removePanel(AppliancePanel appliancePanel, JPanel panelWrapper) {
        bodyPanel.remove(panelWrapper);
        this.appliancePanels.remove(appliancePanel);
        appliancePanel.cleanup();
        bodyPanel.revalidate();
        bodyPanel.repaint();
    }

    private Household createHousehold() {
        HouseholdController householdController = new HouseholdController(this.appliancePanels, this.countryBox, this.energyLabelBox, this.frame);
        return householdController.getHousehold();
    }

    private void handleButtonClick(Consumer<Household> nextStep) {
        Household household = createHousehold();
        if (household != null) {
            nextStep.accept(household);
        }
    }

    private void openCFWindow(Household household) {
        openNewFrame(household);
    }

    private void openStatisticsWindow(Household household) {
        StatisticsPage.openStatisticsWindow(household);
    }

    private void openNewFrame(Household household) {
        JFrame newFrame = new JFrame("Carbon Footprint");
        newFrame.setSize(400, 500);
        newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        int cf = household.getCarbonFootPrint();
        JLabel carbonFootprint = new JLabel("Your carbon footprint is: \n" + cf + " kgCO2e");
        carbonFootprint.setFont(new Font("Arial", Font.BOLD, 20));

        double adjustedCarbonFootprint = cf * household.getEnergyLabel().getEfficiencyFactor();
        JLabel adjustedCF = new JLabel("Your carbon footprint adjusted for energy label is: \n" + (int) adjustedCarbonFootprint + " kgCO2e");
        adjustedCF.setFont(new Font("Arial", Font.BOLD, 20));

        panel.add(carbonFootprint);
        panel.add(adjustedCF);

        newFrame.add(panel);
        newFrame.setVisible(true);
    }

    public static void main(String[] args) {
        new HouseholdConfigurationPage2();
    }
}

