package gui;

import controllers.ApplianceHandler;
import enums.ApplianceType;
import util.DialogTemplate;

import javax.swing.*;
import java.util.Map;
import java.util.List;

public class DeleteApplianceDialog extends DialogTemplate {
    private final JComboBox<String> typeField;
    private final JComboBox<String> nameField;

    public DeleteApplianceDialog(JFrame parent) {
        super(parent, "Delete Appliance", 400, 200);

        typeField = new JComboBox<>(ApplianceType.getAllEnumCaptions().toArray(new String[0]));
        nameField = new JComboBox<>();

        contentPanel.add(new JLabel("Appliance Type:"));
        contentPanel.add(typeField);
        contentPanel.add(new JLabel("Appliance Name:"));
        contentPanel.add(nameField);

        typeField.addActionListener(e -> updateApplianceDropdown());

        setVisible(true);
    }

    @Override
    protected String getConfirmButtonText() {
        return "Delete";
    }

    @Override
    protected void onConfirm() {
        String selectedName = (String) nameField.getSelectedItem();
        if (selectedName != null && ApplianceHandler.deleteAppliance(selectedName)) {
            JOptionPane.showMessageDialog(this, "Appliance deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to delete appliance.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateApplianceDropdown() {
        nameField.removeAllItems();
        ApplianceType selectedType = ApplianceType.getEnumByCaption((String) typeField.getSelectedItem());
        Map<ApplianceType, List<String>> applianceMap = ApplianceHandler.getApplianceList();

        assert applianceMap != null;
        if (applianceMap.containsKey(selectedType)) {
            for (String name : applianceMap.get(selectedType)) {
                nameField.addItem(name);
            }
        }
    }
}