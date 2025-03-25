package controllers;

import javax.swing.*;

public class Errors {

    final static String  ENERGY_ERROR = "Energy label not inputted.";
    final static String  COUNTRY_ERROR = "Country not inputted.";
    final static String  APPLIANCE_TYPE_ERROR = "Appliances type is missing.";
    final static String  APPLIANCE_MODEL_ERROR = "Appliances model is missing.";
    final static String  APPLIANCE_ROOM_ERROR = "Appliances room is missing.";
    final static String  APPLIANCE_TIME_ERROR = "Start and end time can not be equal.";


    public static void showError(JFrame frame, String message) {
        JOptionPane.showMessageDialog(frame, message, "Validation Failed", JOptionPane.INFORMATION_MESSAGE);
    }
}
