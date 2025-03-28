package services;
import domain.ApplianceUsage;
import domain.Household;
import enums.Country;
import enums.EnergyLabel;
import gui.AppliancePanel;
import controllers.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HouseholdService {
    private static Household household;
    private final CountryHandler countryHandler;
    private final EnergyLabelHandler energyLabelHandler;
    private final ApplianceUsageHandler ApplianceUsageHandler;

    public HouseholdService(List<AppliancePanel> appliancePanelList, JComboBox<String> countryBox, JComboBox<String> energyLabelBox, JFrame frame) {
        this.countryHandler = new CountryHandler(countryBox,frame);
        this.energyLabelHandler = new EnergyLabelHandler(energyLabelBox, frame);
        this.ApplianceUsageHandler = new ApplianceUsageHandler(appliancePanelList, frame);
    }

    public void updateHousehold() {
        household = null;

        Country country =  countryHandler.handle();
        ApplianceUsageHandler.setCountry(country);

        EnergyLabel energyLabel = energyLabelHandler.handle();
        List<ApplianceUsage> appliances = ApplianceUsageHandler.handle();

        if (country==null || energyLabel==null || appliances==null || appliances.isEmpty())
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

    public static List<ApplianceUsage> sortAppliances(Comparator<ApplianceUsage> comparator) {
        List<ApplianceUsage> sortedList = new ArrayList<>(household.getAppliances());
        sortedList.sort(comparator);
        return sortedList;
    }

    public static List<ApplianceUsage> sortByCarbonFootprint() {
        return sortAppliances(ApplianceUsage.COMPARE_BY_CARBON_FOOTPRINT);
    }
}