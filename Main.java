import com.google.gson.Gson;
import org.json.JSONObject;
import appliance.*;
import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args){
        List<String> applianceNames = new ArrayList<>();
        applianceNames = DatabaseService.getApplianceList();
        System.out.print(applianceNames);

        String name =  "Nest Cam IQ Security Camera";
        JSONObject test;
        test = DatabaseService.retrieveAppliance(name);
        System.out.print("\n");
        System.out.print(test);
        System.out.print("\n");

        name = "Test Appliance";
        ApplianceType type = appliance.ApplianceType.valueOf("ROUTER");
        float powerConsumption = 1.9f;
        int embodiedEmissions = 30;

        if (DatabaseService.addAppliance(name, type, powerConsumption, embodiedEmissions)) {
            test = DatabaseService.retrieveAppliance(name);
            System.out.print(test + "\n");
        }
        else {
            System.out.print("appliance already in db\n");
        }
        name = "asdasfasd";
        if (DatabaseService.deleteAppliance(name)) {
            System.out.print("appliance deleted successfully\n");
        }
        else {
            System.out.print("appliance not found\n");
        }


    }
}
