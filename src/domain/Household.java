package domain;
import enums.Country;
import enums.EnergyLabel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/* Singleton
Since a Household should only have one instance at any given time,
the Singleton ensures that all the data related to a household (such as appliances,
country, and energy label) is managed centrally. This prevents unnecessary duplication
and ensures consistency across the application.

Think about making Singleton actually functional by builder, that will set attributes separately.
*/

public class Household {
    private static Household household;
    private Country country;
    private EnergyLabel energyLabel;
    private List<ApplianceUsage> appliances;
    private static final int flight = 134; //flight from amsterdam to paris for 1 person

    private Household(Country country, EnergyLabel energyLabel) {
        resetAttributes(country, energyLabel);
    }

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





