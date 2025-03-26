package gui;

import enums.ApplianceType;
import services.ApplianceService;
import util.DialogTemplate;

import javax.swing.*;
import java.util.Map;
import java.util.List;

public class UpdateApplianceDialog extends DialogTemplate {
    private final JComboBox<String> typeField;
    private final JComboBox<String> nameField;
    private final JTextField powerConsumptionField;
    private final JTextField embodiedEmissionsField;

    public UpdateApplianceDialog(JFrame parent) {
        super(parent, "Update Appliance", 600, 300);

        typeField = new JComboBox<>(ApplianceType.getAllEnumCaptions().toArray(new String[0]));
        nameField = new JComboBox<>();
        powerConsumptionField = new JTextField();
        embodiedEmissionsField = new JTextField();

        contentPanel.add(new JLabel("Appliance Type:"));
        contentPanel.add(typeField);
        contentPanel.add(new JLabel("Appliance Name:"));
        contentPanel.add(nameField);
        contentPanel.add(new JLabel("Power Consumption (kWh):"));
        contentPanel.add(powerConsumptionField);
        contentPanel.add(new JLabel("Embodied Emissions (kgCO2e):"));
        contentPanel.add(embodiedEmissionsField);

        typeField.addActionListener(e -> updateApplianceDropdown());

        setVisible(true);
    }

    @Override
    protected String getConfirmButtonText() {
        return "Update";
    }

    @Override
    protected void onConfirm() {
        try {
            String selectedName = (String) nameField.getSelectedItem();
            ApplianceType selectedType = ApplianceType.getEnumByCaption((String) typeField.getSelectedItem());
            float powerConsumption = Float.parseFloat(powerConsumptionField.getText());
            int embodiedEmissions = Integer.parseInt(embodiedEmissionsField.getText());

            if (ApplianceService.updateAppliance(selectedName, selectedType, powerConsumption, embodiedEmissions)) {
                JOptionPane.showMessageDialog(this, "Appliance updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update appliance.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input format. Enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateApplianceDropdown() {
        nameField.removeAllItems();
        ApplianceType selectedType = ApplianceType.getEnumByCaption((String) typeField.getSelectedItem());
        Map<ApplianceType, List<String>> applianceMap = ApplianceService.getApplianceList();

        assert applianceMap != null;
        if (applianceMap.containsKey(selectedType)) {
            for (String name : applianceMap.get(selectedType)) {
                nameField.addItem(name);
            }
        }
    }
}
