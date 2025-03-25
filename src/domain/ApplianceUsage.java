package domain;
import services.CarbonIntensityService;
import enums.Country;
import services.ApplianceService;
import enums.Room;
import java.util.Comparator;
import java.util.List;

/*
* The class clearly encapsulates ApplianceUsage properties (appliance, time, room, carbon footprint).
*
* Factory Pattern
 * Since ApplianceUsage directly fetches an Appliance from ApplianceService, a Factory Pattern
 * isn't useful because there's no need for complex instantiation logic.
 * The database already acts as a source for appliance objects.
 *
 * Factories make sense when we have multiple ways to create objects or if object creation
 * is complex (e.g., different ApplianceUsage subtypes). Here, we just fetch an Appliance from
 * the database, making a factory redundant.
 *
 *
* Builder Pattern
 * Builders are useful for constructing objects step by step, especially when dealing with numerous
 * optional parameters. ApplianceUsage has a clear constructor with well-defined parameters, so a builder
 * would add unnecessary verbosity.

* *
 **/


public class ApplianceUsage {
    private Appliance appliance;
    private int startTime;
    private int endTime;
    private List<Integer> carbonIntensity;
    private Room room;
    private int carbonFootprint;
    private boolean alwaysOn;
    private static final double GRAMS_TO_KG_CONVERSION_FACTOR = 1000.0;


    public ApplianceUsage(String name, Room room, Country country, boolean alwaysOn, int start, int end) {
        this.appliance = ApplianceService.retrieveAppliance(name);
        this.room = room;
        this.alwaysOn = alwaysOn;
        this.startTime = alwaysOn ? 0 : start;
        this.endTime = alwaysOn ? 23 : end;
        this.carbonIntensity = CarbonIntensityService.getCarbonIntensity(country.getCaption(), this.startTime, this.endTime);
        this.carbonFootprint = calculateCarbonFootPrint();
    }
    private int calculateCarbonFootPrint() {
        double averageCarbonIntensity = this.carbonIntensity.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);

        double operationalEmissions = appliance.getPowerConsumption()
                * getTimeRange()
                * averageCarbonIntensity
                / GRAMS_TO_KG_CONVERSION_FACTOR;

        return (int) Math.round(appliance.getEmbodiedEmissions() + operationalEmissions);
    }

    //Appliance itself is already immutable.
    public Appliance getAppliance() {
        return this.appliance;
    }
    public int getStartTime(){
        return this.startTime;
    }
    public int getEndTime(){
        return this.endTime;
    }
    public int getTimeRange(){
        return this.endTime-this.startTime+1;
    }
    public boolean getUsageMode(){
        return this.alwaysOn;
    }
    public int getCarbonFootprint(){
        return this.carbonFootprint;
    }
    public Room getRoom(){
        return this.room;
    }

    // Comparators
    public static final Comparator<ApplianceUsage> COMPARE_BY_TIME =
            (a1, a2) -> Integer.compare(a1.getTimeRange(), a2.getTimeRange());
    public static final Comparator<ApplianceUsage> COMPARE_BY_CARBON_FOOTPRINT =
            (a2, a1) -> Integer.compare(a1.getCarbonFootprint(), a2.getCarbonFootprint());
    public static final Comparator<ApplianceUsage> COMPARE_BY_ROOM =
            Comparator.comparing(a -> a.getRoom().getCaption());

    @Override
    public String toString(){
        return this.appliance.toString() + "Start Time : "+this.startTime + ", End Time : "+this.endTime;
    }
}
