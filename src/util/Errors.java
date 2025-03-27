package util;

import javax.swing.*;

public class Errors {

    public final static String  ENERGY_ERROR = "Energy label not inputted.";
    public final static String  COUNTRY_ERROR = "Country not inputted.";
    public final static String  APPLIANCE_TYPE_ERROR = "Appliances type is missing.";
    public final static String  APPLIANCE_MODEL_ERROR = "Appliances model is missing.";
    public final static String  APPLIANCE_ROOM_ERROR = "Appliances room is missing.";
    public final static String  APPLIANCE_TIME_ERROR = "Start and end time can not be equal.";
    public final static String  APPLIANCE_EMPTY = "There is no appliance in the Household.";

    public static void showError(JFrame frame, String message) {
        JOptionPane.showMessageDialog(frame, message, "Validation Failed", JOptionPane.ERROR_MESSAGE);
    }
}
