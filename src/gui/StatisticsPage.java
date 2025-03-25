package gui;

import domain.ApplianceUsage;
import domain.Household;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class StatisticsPage {
    private static JPanel displayPanel;

    static void openStatisticsWindow(Household household) {
        JFrame statisticsFrame = new JFrame("Statistics");
        statisticsFrame.setSize(900, 600);
        statisticsFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        statisticsFrame.setLayout(new BoxLayout(statisticsFrame.getContentPane(), BoxLayout.Y_AXIS));

        displayPanel = new JPanel();
        displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.Y_AXIS));

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // Align to the left
        controlPanel.setMaximumSize(new Dimension(900, 100));

        JComboBox<String> roomComboBox = new JComboBox<>();
        roomComboBox.setMaximumSize(new Dimension(150, 30));
        populateRoomComboBox(household, roomComboBox);

        roomComboBox.addActionListener(e -> {
            String selectedRoom = (String) roomComboBox.getSelectedItem();
            List<ApplianceUsage> filteredAppliances = filterAppliancesByRoom(household, selectedRoom);
            displaySortedAppliances(statisticsFrame, filteredAppliances);
        });

        controlPanel.add(roomComboBox);


        JButton sortByCarbonFootprintButton = new JButton("Sort by Carbon Footprint");
        sortByCarbonFootprintButton.setPreferredSize(new Dimension(150, 30)); // Set preferred size for the button
        sortByCarbonFootprintButton.addActionListener(e -> {
            String selectedRoom = (String) roomComboBox.getSelectedItem();
            List<ApplianceUsage> sortedAppliances;

            if ("All rooms".equals(selectedRoom)) {
                // Sort all appliances if "Select Room" is chosen
                sortedAppliances = household.sortByCarbonFootprint();
            } else {
                // Filter appliances by the selected room and then sort
                List<ApplianceUsage> filteredAppliances = filterAppliancesByRoom(household, selectedRoom);
                sortedAppliances = filteredAppliances.stream()
                        .sorted(ApplianceUsage.COMPARE_BY_CARBON_FOOTPRINT)
                        .collect(Collectors.toList());
            }

            displaySortedAppliances(statisticsFrame, sortedAppliances);
        });

        controlPanel.add(sortByCarbonFootprintButton);

        JLabel flightConversionLabel = new JLabel("Your yearly carbon footprint is equal to " + household.flightConversion() +" flights from Amsterdam to Paris!");
        statisticsFrame.add(flightConversionLabel);

        statisticsFrame.add(controlPanel);
        statisticsFrame.add(new JScrollPane(displayPanel));
        statisticsFrame.setVisible(true);
    }

    private static void populateRoomComboBox(Household household, JComboBox<String> roomComboBox) {
        Set<String> roomTypes = household.getAppliances().stream()
                .map(applianceUsage -> applianceUsage.getRoom().getCaption())
                .collect(Collectors.toSet());

        roomComboBox.addItem("All rooms");
        for (String roomType : roomTypes) {
            roomComboBox.addItem(roomType);
        }
    }

    private static List<ApplianceUsage> filterAppliancesByRoom(Household household, String roomType) {
        return household.getAppliances().stream()
                .filter(applianceUsage -> applianceUsage.getRoom().getCaption().equals(roomType)).collect(Collectors.toList());
    }

    private static void displaySortedAppliances(JFrame statisticsFrame, List<ApplianceUsage> sortedAppliances) {
        displayPanel.removeAll();

        for (ApplianceUsage applianceUsage : sortedAppliances) {
            JPanel appliancePanel = new JPanel();
            appliancePanel.setBorder(BorderFactory.createTitledBorder(applianceUsage.getAppliance().getName()));
            appliancePanel.setLayout(new GridLayout(1, 0));

            appliancePanel.add(new JLabel("Type: " + applianceUsage.getAppliance().getType().getCaption()));
            appliancePanel.add(new JLabel("Room: " + applianceUsage.getRoom().getCaption()));
            appliancePanel.add(new JLabel("Carbon Footprint: " + applianceUsage.getCarbonFootprint() + "kgCO2e"));
            appliancePanel.add(new JLabel("Start Time: " + applianceUsage.getStartTime()));
            appliancePanel.add(new JLabel("End Time: " + applianceUsage.getEndTime()));

            displayPanel.add(appliancePanel);
        }

        displayPanel.revalidate();
        displayPanel.repaint();
    }
}