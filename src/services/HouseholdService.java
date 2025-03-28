package services;
import domain.ApplianceUsage;
import domain.Household;
import enums.Country;
import enums.EnergyLabel;
import gui.AppliancePanel;
import handlers.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class HouseholdService {
    private static Household household;
    private final CountryHandler countryHandler;
    private final EnergyLabelHandler energyLabelHandler;
    private final ApplianceUsageHandler applianceUsageHandler;

    public HouseholdService(List<AppliancePanel> appliancePanelList, JComboBox<String> countryBox, JComboBox<String> energyLabelBox, JFrame frame) {
        this.countryHandler = new CountryHandler(countryBox,frame);
        this.energyLabelHandler = new EnergyLabelHandler(energyLabelBox, frame);
        this.applianceUsageHandler = new ApplianceUsageHandler(appliancePanelList, frame);
    }

    public void updateHousehold() {
        household = null;

        Country country =  countryHandler.handle();
        applianceUsageHandler.setCountry(country);

        EnergyLabel energyLabel = energyLabelHandler.handle();
        List<ApplianceUsage> appliances = applianceUsageHandler.handle();

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

    public static List<ApplianceUsage> filterAppliancesByRoom(Household household, String roomType) {
        return household.getAppliances().stream()
                .filter(applianceUsage -> applianceUsage.getRoom().getCaption().equals(roomType)).collect(Collectors.toList());
    }

    public static Set<String> getSelectedRooms(Household household) {
        if (household == null) {
            System.out.println("Household data is missing.");
        }
        assert household != null;
        return household.getAppliances().stream()
                .map(applianceUsage -> applianceUsage.getRoom().getCaption())
                .collect(Collectors.toSet());
    }
}