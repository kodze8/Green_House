package services;

import enums.ApplianceType;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static services.DatabaseService.DataBaseType.EMBODIED_EMISSION;
import static services.DatabaseService.DataBaseType.POWER_CONSUMPTION;


public class DatabaseService {

    static String dbPath = "src/data/appliance_db.json";

    public static class DataBaseType{
        public static final String NAME = "name";
        public static final String POWER_CONSUMPTION = "power_consumption_kwh";
        public static final String TYPE = "type";
        public static final String EMBODIED_EMISSION = "embodied_emissions_kgCO2e";
    }

    public static boolean validateInput(JSONObject appliance) {
        return (!invalidName(appliance) && !invalidType(appliance) && !invalidEmissions(appliance) && !invalidPowerConsumption(appliance));
    }

    private static boolean invalidName(JSONObject appliance) {
        return (!appliance.has("name") || !(appliance.get("name") instanceof String) || appliance.getString("name").trim().isEmpty());
    }
    private static boolean invalidPowerConsumption(JSONObject appliance) {
        return (!appliance.has(POWER_CONSUMPTION) || !(appliance.get(POWER_CONSUMPTION) instanceof Number) || appliance.getDouble(POWER_CONSUMPTION) <= 0);
    }
    private static boolean invalidEmissions(JSONObject appliance) {
        return (!appliance.has(EMBODIED_EMISSION) || !(appliance.get(EMBODIED_EMISSION) instanceof Number) || appliance.getInt(EMBODIED_EMISSION) < 0);
    }
    private static boolean invalidType(JSONObject appliance) {
        return (!appliance.has("type") || isValidApplianceType(appliance.getString("type")));
    }
    private static boolean isValidApplianceType(String type) {
        try {
            ApplianceType.valueOf(type);
            return false;
        }
        catch (IllegalArgumentException e) {
            return true;
        }
    }


    public static List<String> validateDatabase() {
        JSONArray jsonArray = readDB();
        List<String> invalidEntries = new ArrayList<>();

        if (jsonArray == null) {
            return invalidEntries;
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject appliance = jsonArray.getJSONObject(i);

            if (invalidName(appliance)) {
                invalidEntries.add("Invalid name at index: " + i + " - " + appliance);
            }
            else if (invalidPowerConsumption(appliance)) {
                invalidEntries.add("Invalid power consumption at index: " + i + " - " + appliance);
            }
            else if (invalidEmissions(appliance)) {
                invalidEntries.add("Invalid emissions at index: " + i + " - " + appliance);
            }
            else if (invalidType(appliance)) {
                invalidEntries.add("Invalid type at index: " + i + " - " + appliance);
            }
        }

        return invalidEntries;
    }


    public static JSONArray readDB(){
        try {
            FileReader reader = new FileReader(dbPath);
            JSONArray jsonArray = new JSONArray(new JSONTokener(reader));
            reader.close();
            return jsonArray;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean writeDB(JSONArray jsonArray){
        try {
            FileWriter fileWriter = new FileWriter(dbPath);
            fileWriter.write(jsonArray.toString(4));
            fileWriter.close();
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static JSONObject retrieveObject(String name){
        JSONArray jsonArray = readDB();
        if (jsonArray == null) {
            return null;
        }
        else {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject appliance = jsonArray.getJSONObject(i);
                if (!invalidName(appliance) && appliance.getString(DataBaseType.NAME).equals(name)) {
                        return appliance; // Return the matching appliance
                    }

            }
        }
        return null;
    }

}