package database_service;

import appliance.ApplianceType;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class DatabaseService {

    static String DB_PATH = "src/database_service/appliance_db.json";

    public static class DataBaseType{
        final static String NAME = "name";
        final static String POWER_CONSUMPTION = "power_consumption_kwh";
        final static String TYPE = "type";
        final static String EMBODIED_EMISSION = "embodied_emissions_kgCO2e";
    }

    static boolean validateInput(JSONObject appliance) {
        return (!invalidName(appliance) && !invalidType(appliance) && !invalidEmissions(appliance) && !invalidPowerConsumption(appliance));
    }

    private static boolean invalidName(JSONObject appliance) {
        return (!appliance.has("name") || !(appliance.get("name") instanceof String) || appliance.getString("name").trim().isEmpty());
    }
    private static boolean invalidPowerConsumption(JSONObject appliance) {
        return (!appliance.has("power_consumption_kwh") || !(appliance.get("power_consumption_kwh") instanceof Number) || appliance.getDouble("power_consumption_kwh") <= 0);
    }
    private static boolean invalidEmissions(JSONObject appliance) {
        return (!appliance.has("embodied_emissions_kgCO2e") || !(appliance.get("embodied_emissions_kgCO2e") instanceof Number) || appliance.getInt("embodied_emissions_kgCO2e") < 0);
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


    static JSONArray readDB(){
        try {
            FileReader reader = new FileReader(DB_PATH);
            JSONArray jsonArray = new JSONArray(new JSONTokener(reader));
            reader.close();
            return jsonArray;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    static boolean writeDB(JSONArray jsonArray){
        try {
            FileWriter fileWriter = new FileWriter(DB_PATH);
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
                if (!invalidName(appliance)) {
                    if (appliance.getString(DataBaseType.NAME).equals(name)) {
                        return appliance; // Return the matching appliance
                    }
                }
            }
        }
        return null;
    }
    static boolean deleteAppliance(String name) {
        JSONObject applianceToDelete = retrieveObject(name);
        if (applianceToDelete == null) {
            return false;
        }

        JSONArray jsonArray = readDB();
        if (jsonArray == null) {
            return false;
        }

        else {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject appliance = jsonArray.getJSONObject(i);
                if (appliance.getString("name").equals(name)) {
                    jsonArray.remove(i);
                    break;
                }
            }
            return writeDB(jsonArray);
        }
    }
}