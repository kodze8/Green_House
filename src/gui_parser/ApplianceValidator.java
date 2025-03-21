package gui_parser;

import GUI.AppliancePanel;
import javax.swing.*;
import java.util.List;

public class ApplianceValidator {
    private final JFrame frame;

    public ApplianceValidator(JFrame frame) {
        this.frame = frame;
    }

    public boolean validateInputs(List<AppliancePanel> appliancePanels, JComboBox<String> countryBox, JComboBox<String> energyLabelBox) {
        return validateCountry(countryBox) && validateEnergyLabel(energyLabelBox) && validateAppliances(appliancePanels);
    }

    private boolean validateAppliances(List<AppliancePanel> appliancePanels) {
        for (AppliancePanel panel : appliancePanels) {
            if ("Select Type".equals(panel.typeBox.getSelectedItem())) {
                showError("Appliance type not inputted.");
                return false;
            }
            if ("Select Model".equals(panel.nameBox.getSelectedItem())) {
                showError("Appliance model not found.");
                return false;
            }
            if ("Select Room".equals(panel.roomBox.getSelectedItem())) {
                showError("Room selection is missing.");
                return false;
            }
            if (panel.startTimeBox.getSelectedItem().equals(panel.endTimeBox.getSelectedItem())) {
                showError("Start time and end time cannot be the same.");
                return false;
            }
        }
        return true;
    }

    private boolean validateCountry(JComboBox<String> countryBox) {
        if ("Select a country...".equals(countryBox.getSelectedItem())) {
            showError("Country not inputted.");
            return false;
        }
        return true;
    }

    private boolean validateEnergyLabel(JComboBox<String> energyLabelBox) {
        if ("Select Energy Label...".equals(energyLabelBox.getSelectedItem())) {
            showError("Energy label not inputted.");
            return false;
        }
        return true;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(frame, message, "Validation Failed", JOptionPane.INFORMATION_MESSAGE);
    }
}
