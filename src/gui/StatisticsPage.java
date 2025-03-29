package gui;

import domain.ApplianceUsage;
import domain.Household;
import services.HouseholdService;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class StatisticsPage extends PageTemplate {
    private static JPanel bodyPanel;
    private final Household household;

    public StatisticsPage(Household household) {
        super("Statistics");
        this.household = household;
        buildUI();
    }

    @Override
    protected JPanel createContentPanel() {
        JPanel contentPanel = createPanel(new BorderLayout()); // Use BorderLayout
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        bodyPanel = contentPanel;

        return bodyPanel;
    }

    @Override
    protected JPanel createHeaderPanel() {
        JPanel headerPanel = createPanel(null);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        // Title Section
        JPanel titlePanel = createPanel(new BorderLayout());
        createActionButton("< Back", () -> frame.dispose(), titlePanel, BorderLayout.WEST);
        JLabel title = new JLabel("Statistics", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setBorder(BorderFactory.createEmptyBorder(0, -80, 0, 0));
        titlePanel.add(title, BorderLayout.CENTER);

        // Flight conversion section
        JPanel flightConversionPanel = createPanel(new BorderLayout());
        JLabel flightConversionLabel = new JLabel("Your yearly carbon footprint is equal to " + household.flightConversion(household.getCarbonFootPrint()) +" flights from Amsterdam to Paris!", SwingConstants.CENTER);
        flightConversionPanel.add(flightConversionLabel, BorderLayout.CENTER);

        // Household fields Section
        JPanel householdFieldsPanel = createPanel(new FlowLayout(FlowLayout.CENTER));
        String[] roomTypes = HouseholdService.getSelectedRooms(this.household).toArray(new String[0]);
        JComboBox<String> roomBox = createDropdown(roomTypes, "All rooms");

        roomBox.addActionListener(e -> {
            String selectedRoom = (String) roomBox.getSelectedItem();
            List<ApplianceUsage> filteredAppliances = HouseholdService.filterAppliancesByRoom(this.household, selectedRoom);
            displaySortedAppliances(filteredAppliances);
        });

        householdFieldsPanel.add(roomBox);
        createActionButton("Sort by Carbon Footprint", () -> sortAndDisplayAppliances((String) roomBox.getSelectedItem()), householdFieldsPanel, null);

        headerPanel.add(titlePanel);
        headerPanel.add(flightConversionPanel);
        headerPanel.add(householdFieldsPanel);
        return headerPanel;
    }

    @Override
    protected JPanel createFooterPanel() {
        return null;
    }

    private void sortAndDisplayAppliances(String selectedRoom) {
        List<ApplianceUsage> sortedAppliances;

        if ("All rooms".equals(selectedRoom)) {
            // Sort all appliances if "All rooms" is chosen
            sortedAppliances = HouseholdService.sortByCarbonFootprint();
        } else {
            // Filter appliances by the selected room and then sort
            List<ApplianceUsage> filteredAppliances = HouseholdService.filterAppliancesByRoom(this.household, selectedRoom);
            sortedAppliances = filteredAppliances.stream()
                    .sorted(ApplianceUsage.COMPARE_BY_CARBON_FOOTPRINT)
                    .collect(Collectors.toList());
        }

        displaySortedAppliances(sortedAppliances);
    }

    private static void displaySortedAppliances(List<ApplianceUsage> sortedAppliances) {
        bodyPanel.removeAll();

        for (ApplianceUsage applianceUsage : sortedAppliances) {
            JPanel appliancePanel = new JPanel();
            appliancePanel.setBorder(BorderFactory.createTitledBorder(applianceUsage.getAppliance().getName()));
            appliancePanel.setLayout(new GridLayout(1, 0));

            appliancePanel.add(new JLabel("Type: " + applianceUsage.getAppliance().getType().getCaption()));
            appliancePanel.add(new JLabel("Room: " + applianceUsage.getRoom().getCaption()));
            appliancePanel.add(new JLabel("Carbon Footprint: " + applianceUsage.getCarbonFootprint() + "kgCO2e"));
            appliancePanel.add(new JLabel("Start Time: " + applianceUsage.getStartTime()));
            appliancePanel.add(new JLabel("End Time: " + applianceUsage.getEndTime()));

            bodyPanel.add(appliancePanel);
        }

        bodyPanel.revalidate();
        bodyPanel.repaint();
    }
}
