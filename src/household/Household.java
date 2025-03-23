package household;

import appliance.ApplianceUsage;
import carbon_intensity.Country;
import energy_label.EnergyLabel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class Household {
    private static Household household;
    private Country country;
    private List<ApplianceUsage> appliances;
    private EnergyLabel energyLabel;
    private static final int flight = 134; //flight from amsterdam to paris for 1 person

    private Household(Country country, EnergyLabel energyLabel) {
        resetAttributes(country, energyLabel);
    }

// Each time we retrieve an instance, it is initialized with the specified country and label.
// However, the list of appliances is reset upon each request for a new Household instance,
// requiring appliances to be added again afterward.
    public static Household getHouseholdInstance(Country country, EnergyLabel energyLabel){
        if (household==null){
            household = new Household(country, energyLabel);
        }else {
            household.resetAttributes(country, energyLabel);
        }
        return household;
    }


    private void resetAttributes(Country country, EnergyLabel energyLabel){
        setCountry(country);
        setEnergyLabel(energyLabel);
        this.appliances = new ArrayList<>();
    }
    public void addAppliance(ApplianceUsage applianceUsage) {
        if (applianceUsage != null) {
            this.appliances.add(applianceUsage);
        }
    }
    public void setCountry(Country newCountry){
        this.country = newCountry;
    }
    public void setEnergyLabel(EnergyLabel energyLabel){
        this.energyLabel = energyLabel;
    }

    public EnergyLabel getEnergyLabel(){
        return this.energyLabel;
    }

    public void removeAppliance(ApplianceUsage applianceUsage){
        this.appliances.remove(applianceUsage);
    }

    public List<ApplianceUsage> sortByCarbonFootprint() {
        return sortAppliances(ApplianceUsage.COMPARE_BY_CARBON_FOOTPRINT);
    }

    public List<ApplianceUsage> sortByTime() {
        return sortAppliances(ApplianceUsage.COMPARE_BY_TIME);
    }

    public static List<ApplianceUsage> sortAppliances(Comparator<ApplianceUsage> comparator) {
        List<ApplianceUsage> sortedList = new ArrayList<>(household.appliances);
        sortedList.sort(comparator);
        return sortedList;
    }

    public double flightConversion() {
        double conversion = (double) getCarbonFootPrint() / flight;
        return Math.floor(conversion * 100) / 100; //round on 2 decimals
    }

    public int getCarbonFootPrint(){
        int total= 0;
        for (ApplianceUsage applianceUsage: this.appliances){
            total+=applianceUsage.getCarbonFootprint();
        }
        return total;
    }

}
