package household;

import gui.AppliancePanel;
import appliance.ApplianceUsage;
import carbon_intensity.Country;
import enums.EnergyLabel;
import enums.Room;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HouseholdController {
    private static Household household;
    private final List<AppliancePanel> appliancePanelList;
    private final JComboBox<String> countryBox;
    private final JComboBox<String> energyLabelBox;
    public HouseholdController(List<AppliancePanel> appliancePanelList, JComboBox<String> countryBox, JComboBox<String> energyLabelBox) {
        this.appliancePanelList = appliancePanelList;
        this.countryBox = countryBox;
        this.energyLabelBox = energyLabelBox;
    }

    public Household getHousehold(){
        updateHousehold();
        return household;
    }

    private void updateHousehold() {
        Country country = parseCountry();
        EnergyLabel energyLabel = parseEnergyLabel();
        List<ApplianceUsage> appliances = parseAppliances();

        household = Household.getHouseholdInstance(country, energyLabel);
        for (ApplianceUsage applianceUsage: appliances){
            household.addAppliance(applianceUsage);
        }
    }

    private List<ApplianceUsage> parseAppliances() {
        List<ApplianceUsage> applianceUsages = new ArrayList<>();

        for (AppliancePanel panel : appliancePanelList) {
            String name = Objects.requireNonNull(panel.nameBox.getSelectedItem()).toString();
            Room room = Room.getEnumByCaption(Objects.requireNonNull(panel.roomBox.getSelectedItem()).toString());
            boolean alwaysOn = panel.alwaysOn.isSelected();
            int startTime = alwaysOn ? 0 : AppliancePanel.TIME_MAP.get(panel.startTimeBox.getSelectedItem());
            int endTime = alwaysOn ? 23 : AppliancePanel.TIME_MAP.get(panel.endTimeBox.getSelectedItem());

            applianceUsages.add(new ApplianceUsage(name, room, parseCountry(), alwaysOn, startTime, endTime));
        }
        return applianceUsages;
    }
    private Country parseCountry(){
        return Country.getEnumByCaption(Objects.requireNonNull(this.countryBox.getSelectedItem()).toString());
    }
    private EnergyLabel parseEnergyLabel(){
        return EnergyLabel.getEnumByCaption(Objects.requireNonNull(this.energyLabelBox.getSelectedItem()).toString());
    }

}

