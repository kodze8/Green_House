package handlers;

import domain.Appliance;
import enums.ApplianceType;
import org.json.JSONArray;
import org.json.JSONObject;
import services.DatabaseService;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ApplianceHandler {

    public static Appliance retrieveAppliance(String name) {
        JSONObject object = DatabaseService.retrieveObject(name);

        if (object != null) {
            String applianceName = object.getString(DatabaseService.DataBaseType.NAME);
            ApplianceType applianceType = ApplianceType.valueOf(object.getString(DatabaseService.DataBaseType.TYPE));
            float appliancePC = (float)object.getDouble(DatabaseService.DataBaseType.POWER_CONSUMPTION);
            int embodiedEmissions = object.getInt(DatabaseService.DataBaseType.EMBODIED_EMISSION);

            return new Appliance(applianceName, applianceType, appliancePC, embodiedEmissions);
        }
        return null;
    }

    public static Map<ApplianceType, List<String>> getApplianceList() {
        Map<ApplianceType, List<String>> applianceMap = new HashMap<>();

        JSONArray jsonArray = DatabaseService.readDB();
        if (jsonArray == null) {
            return Collections.emptyMap();
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject appliance = jsonArray.getJSONObject(i);
            String name = appliance.getString(DatabaseService.DataBaseType.NAME);
            // In database String value are Enum Equivalent not Caption
//            ApplianceType.getEnumByCaption("Refrigerator"); reference only. we use value of here not our function as database contains capital letter namings.
            ApplianceType type = ApplianceType.valueOf(appliance.getString(DatabaseService.DataBaseType.TYPE));
            applianceMap.putIfAbsent(type, new ArrayList<>());
            applianceMap.get(type).add(name);
        }

        return applianceMap;
    }

    public static boolean addAppliance(String name, ApplianceType type, float powerConsumption, int embodiedEmissions) {
        // Additional input validation can go here
        JSONArray jsonArray = DatabaseService.readDB();
        if (jsonArray == null) {
            return false;
        }
        if (DatabaseService.retrieveObject(name) != null) {
            return false;
        }
        JSONObject newApplianceJson = new JSONObject();
        newApplianceJson.put(DatabaseService.DataBaseType.NAME, name);
        newApplianceJson.put(DatabaseService.DataBaseType.TYPE, type.toString());
        newApplianceJson.put(DatabaseService.DataBaseType.POWER_CONSUMPTION, powerConsumption);
        newApplianceJson.put(DatabaseService.DataBaseType.EMBODIED_EMISSION, embodiedEmissions);

        if(DatabaseService.validateInput(newApplianceJson)) {
            jsonArray.put(newApplianceJson);
            return DatabaseService.writeDB(jsonArray);
        }
        else {
            return false;
        }
    }

    public static boolean updateAppliance(String name, ApplianceType type, float powerConsumption, int embodiedEmissions) {
        JSONArray jsonArray = DatabaseService.readDB();
        if (jsonArray == null) {
            return false;
        }

        boolean applianceFound = false;
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject appliance = jsonArray.getJSONObject(i);
            if (appliance.getString(DatabaseService.DataBaseType.NAME).equals(name)) {
                appliance.put(DatabaseService.DataBaseType.TYPE, type.toString());
                appliance.put(DatabaseService.DataBaseType.POWER_CONSUMPTION, powerConsumption);
                appliance.put(DatabaseService.DataBaseType.EMBODIED_EMISSION, embodiedEmissions);
                applianceFound = true;
                break;
            }
        }
        return applianceFound && DatabaseService.writeDB(jsonArray);
    }

    public static boolean deleteAppliance(String name) {
        JSONObject applianceToDelete = DatabaseService.retrieveObject(name);
        if (applianceToDelete == null) {
            return false;
        }

        JSONArray jsonArray = DatabaseService.readDB();
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
            return DatabaseService.writeDB(jsonArray);
        }
    }

}


