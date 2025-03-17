package appliance;
import com.google.gson.annotations.SerializedName;

public class Appliance {

    @SerializedName("name")
    private String name;

    @SerializedName("power_consumption_kwh")
    private float powerConsumption;

    @SerializedName("embodied_emissions_kgCO2e")
    private int embodiedEmissions;

    @SerializedName("type")
    private ApplianceType applianceType;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ApplianceType getType() {
        return this.applianceType;
    }

    public void setType(ApplianceType type) {
        this.applianceType = type;
    }

    public float getPowerConsumption() {
        return this.powerConsumption;
    }

    public void setPowerConsumption(int powerConsumption) {
        this.powerConsumption = powerConsumption;
    }

    public int getEmbodiedEmissions() {
        return this.embodiedEmissions;
    }

    public void setEmbodiedEmissions(int embodiedEmissions) {
        this.embodiedEmissions = embodiedEmissions;
    }

}



