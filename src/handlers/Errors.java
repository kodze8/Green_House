package handlers;

import javax.swing.*;

public class Errors {

    public static final String  ENERGY_ERROR = "Energy label not inputted.";
    public static final String  COUNTRY_ERROR = "Country not inputted.";
    public static final String  APPLIANCE_TYPE_ERROR = "Appliances type is missing.";
    public static final String  APPLIANCE_MODEL_ERROR = "Appliances model is missing.";
    public static final String  APPLIANCE_ROOM_ERROR = "Appliances room is missing.";
    public static final String  APPLIANCE_TIME_ERROR = "Start and end time can not be equal.";
    public static final String  APPLIANCE_EMPTY = "There is no appliance in the Household.";

    public static void showError(JFrame frame, String message) {
        JOptionPane.showMessageDialog(frame, message, "Validation Failed", JOptionPane.ERROR_MESSAGE);
    }
}
