package gui;

import appliance.ApplianceUsage;
import household.Household;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StatisticsPanel {
    private static JPanel displayPanel;

    static void openStatisticsWindow(Household household) {
        JFrame statisticsFrame = new JFrame("Statistics");
        statisticsFrame.setSize(900, 600);
        statisticsFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        statisticsFrame.setLayout(new BoxLayout(statisticsFrame.getContentPane(), BoxLayout.Y_AXIS));


        displayPanel = new JPanel();
        displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.Y_AXIS));

        JButton sortByRoomButton = new JButton("Sort by Room");
        sortByRoomButton.addActionListener(e -> {
            List<ApplianceUsage> sortedByRoom = Household.sortAppliances(ApplianceUsage.COMPARE_BY_ROOM);
            displaySortedAppliances(statisticsFrame, sortedByRoom);
        });

        JButton sortByCarbonFootprintButton = new JButton("Sort by Carbon Footprint");
        sortByCarbonFootprintButton.addActionListener(e -> {
            List<ApplianceUsage> sortedByCF = Household.sortAppliances(ApplianceUsage.COMPARE_BY_CARBON_FOOTPRINT);
            displaySortedAppliances(statisticsFrame, sortedByCF);
        });

        statisticsFrame.add(sortByRoomButton);
        statisticsFrame.add(sortByCarbonFootprintButton);

        JLabel flightConversionLabel = new JLabel("Your carbon footprint is equal to " + household.flightConversion() +" flights from Amsterdam to Paris.");
        statisticsFrame.add(flightConversionLabel);

        statisticsFrame.add(new JScrollPane(displayPanel));
        statisticsFrame.setVisible(true);
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