package domain;
import enums.Country;
import enums.EnergyLabel;

import java.util.ArrayList;
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
    private static final int FLIGHT = 134; //Estimated carbon footprint of a flight from Amsterdam to Paris for 1 person

    private Household(Country country, EnergyLabel energyLabel) {
        resetAttributes(country, energyLabel);
    }

    public static Household getHouseholdInstance(Country country, EnergyLabel energyLabel){
        if (household==null){
            household = new Household(country, energyLabel);
        } else {
            household.resetAttributes(country, energyLabel);
        }
        return household;
    }


    private void resetAttributes(Country country, EnergyLabel energyLabel){
        this.country = country;
        this.energyLabel = energyLabel;
        this.appliances = new ArrayList<>();
    }

    public void addAppliance(ApplianceUsage applianceUsage) {
        if (applianceUsage != null) {
            this.appliances.add(applianceUsage);
        }
    }

    public int flightConversion(int carbonFootPrint) {
        double conversion = (double) (carbonFootPrint * 365) / FLIGHT;
        return (int) (Math.floor(conversion * 100) / 100); //round on 2 decimals
    }

    public int energyLabelConversion(int carbonFootprint){
        double efficiencyFactor = energyLabel.getEfficiencyFactor();
        return (int) (carbonFootprint * efficiencyFactor);
    }

    public int getCarbonFootPrint(){
        int total= 0;
        for (ApplianceUsage applianceUsage: this.appliances){
            total+=applianceUsage.getCarbonFootprint();
        }
        return total;
    }

    public List<ApplianceUsage> getAppliances() {return this.appliances;}
}





