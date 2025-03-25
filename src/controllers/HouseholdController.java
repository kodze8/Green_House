package controllers;
import domain.ApplianceUsage;
import domain.Household;
import enums.Country;
import enums.EnergyLabel;
import gui.AppliancePanel;

import javax.swing.*;
import java.util.List;

public class HouseholdController {
    private static Household household;
    private final CountryHandler countryHandler;
    private final EnergyLabelHandler energyLabelHandler;
    private final ApplianceHandler applianceHandler;

    public HouseholdController(List<AppliancePanel> appliancePanelList, JComboBox<String> countryBox, JComboBox<String> energyLabelBox, JFrame frame) {
        this.countryHandler = new CountryHandler(countryBox,frame);
        this.energyLabelHandler = new EnergyLabelHandler(energyLabelBox, frame);
        this.applianceHandler = new ApplianceHandler(appliancePanelList, frame);
    }

    public void updateHousehold() {
        household = null;
        Country country =  countryHandler.handle();
        if (country!=null)
            applianceHandler.setCountry(country);
        else
            return;

        EnergyLabel energyLabel = energyLabelHandler.handle();
        if (energyLabel==null)
            return;

        List<ApplianceUsage> appliances = applianceHandler.handle();
        if (appliances==null || appliances.isEmpty())
            return;

        household = Household.getHouseholdInstance(country, energyLabel);
        for (ApplianceUsage applianceUsage: appliances){
            household.addAppliance(applianceUsage);
        }
    }

    public Household getHousehold() {
        updateHousehold();
        return household;
    }
}

