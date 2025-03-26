package gui;

import enums.ApplianceType;
import services.ApplianceService;
import util.DialogTemplate;

import javax.swing.*;

public class AddApplianceDialog extends DialogTemplate {
    private final JTextField nameField;
    private final JTextField powerConsumptionField;
    private final JTextField embodiedEmissionsField;
    private final JComboBox<String> typeField;

    public AddApplianceDialog(JFrame parent) {
        super(parent, "Add Appliance", 600, 300);

        nameField = new JTextField();
        powerConsumptionField = new JTextField();
        embodiedEmissionsField = new JTextField();
        typeField = new JComboBox<>(ApplianceType.getAllEnumCaptions().toArray(new String[0]));

        contentPanel.add(new JLabel("Name:"));
        contentPanel.add(nameField);
        contentPanel.add(new JLabel("Appliance Type:"));
        contentPanel.add(typeField);
        contentPanel.add(new JLabel("Power Consumption (kWh):"));
        contentPanel.add(powerConsumptionField);
        contentPanel.add(new JLabel("Embodied Emissions (kgCO2e):"));
        contentPanel.add(embodiedEmissionsField);

        setVisible(true);
    }

    @Override
    protected String getConfirmButtonText() {
        return "Add";
    }

    @Override
    protected void onConfirm() {
        try {
            String name = nameField.getText();
            ApplianceType type = ApplianceType.getEnumByCaption((String) typeField.getSelectedItem());
            float power = Float.parseFloat(powerConsumptionField.getText());
            int emissions = Integer.parseInt(embodiedEmissionsField.getText());

            if (ApplianceService.addAppliance(name, type, power, emissions)) {
                JOptionPane.showMessageDialog(this, "Appliance added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add appliance.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input. Enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
