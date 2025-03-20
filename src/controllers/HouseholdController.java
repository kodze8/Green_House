package controllers;

import GUI.AppliancePanel;
import appliance.ApplianceUsage;
import carbon_intensity.Country;
import energy_label.EnergyLabel;
import household.Household;
import room.Room;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HouseholdController {

    public boolean initiateAppliances(List<AppliancePanel> appliancePanelList) {
        return true;
    }

    public static List<ApplianceUsage> parseAppliances(List<AppliancePanel> appliancePanelList, JComboBox countryBox) {
        List<ApplianceUsage> applianceUsages = new ArrayList<>();
        for (AppliancePanel appliancePanel : appliancePanelList) {
            String applianceName = Objects.requireNonNull(appliancePanel.nameBox.getSelectedItem()).toString();
            //Appliance appliance = ApplianceService.retrieveAppliance(applianceName);

            Room room =  Room.getEnumByCaption(Objects.requireNonNull(appliancePanel.roomBox.getSelectedItem()).toString());
            boolean alwaysOn = appliancePanel.alwaysOn.isSelected();
            int startTime;
            int endTime;
            if (alwaysOn) {
                startTime = 0;
                endTime = 23;
            }
            else {
                startTime = appliancePanel.startTimeBox.getSelectedIndex();
                endTime = appliancePanel.endTimeBox.getSelectedIndex();
            }
            Country country = Country.getEnumByCaption( Objects.requireNonNull(countryBox.getSelectedItem()).toString());

            ApplianceUsage applianceUsage =  new ApplianceUsage(applianceName, room, country, alwaysOn,  startTime,  endTime);
            applianceUsages.add(applianceUsage);
            System.out.print(applianceUsage);

        }
        return applianceUsages;
    }
    public static Household initiateHousehold(JComboBox countryBox, JComboBox energyLabelBox, List<ApplianceUsage> applianceUsages) {
        Household household =  new Household(Country.getEnumByCaption(Objects.requireNonNull(countryBox.getSelectedItem()).toString()),
                                            EnergyLabel.getEnumByCaption(Objects.requireNonNull(energyLabelBox.getSelectedItem()).toString()));
        for (ApplianceUsage applianceUsage : applianceUsages) {
            household.addAppliance(applianceUsage);
        }
        System.out.print("\n" + household + "\n");
        return household;
    }

    // when the calculate button is pressed
    // this class object will be initiated.
    // whenever the caculate is pressed if the object is null
    // the initiate

    // before this class is craeted
    // parser class will parse the information and trsnform adat here.
    // those data will be used to create rhis class.
}
