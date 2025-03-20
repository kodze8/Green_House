package gui_parser;

import GUI.AppliancePanel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import appliance.*;
import carbon_intensity.Country;
import database_service.ApplianceService;
import database_service.DatabaseService;
import room.*;

public class ApplianceParser {
    public ApplianceParser(List<AppliancePanel> appliancePanelList, JComboBox countryBox,JComboBox energyLabelBox){

        // Validate inputs DONE
        // if anything missing return  with validate false DONE
        // if not we will initiate HouseHold controller class
        // where all the info from the gui will be trnsfered.

    }

    public boolean validate(List<AppliancePanel> appliancePanelList, JComboBox countryBox,JComboBox energyLabelBox){
        return validateAppliances(appliancePanelList) && validateCountry(countryBox) && validateEnergyLabel(energyLabelBox);
    }
/*
    public boolean parseHousehold (List<AppliancePanel> appliancePanelList, JComboBox countryBox,JComboBox energyLabelBox) {

        return true;
    }*/

    private boolean validateAppliances(List<AppliancePanel> appliancePanelList) {
        for (AppliancePanel appliancePanel : appliancePanelList) {
            if (isInvalidAppliance(appliancePanel)) {
                return false;
            }
        }
        return true;
    }

    private boolean isInvalidAppliance(AppliancePanel panel) {
        return "Select Type".equals(panel.typeBox.getSelectedItem()) ||
                "Select Model".equals(panel.nameBox.getSelectedItem()) ||
                "Select Room".equals(panel.roomBox.getSelectedItem()) ||
                panel.endTimeBox.getSelectedIndex() < panel.startTimeBox.getSelectedIndex();
    }

    private boolean validateCountry (JComboBox countryBox) {
        return !(countryBox.getSelectedItem() == "Select a country...");
    }

    private boolean validateEnergyLabel (JComboBox energyLabelBox) {
        return !(energyLabelBox.getSelectedItem() == "Select Energy Label...");
    }
}
