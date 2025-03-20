package GUI;

import appliance.ApplianceType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.Set;

import database_service.*;



public class DatabasePage {
    JFrame frame;
    ApplianceType selectedType;
    static Set<String> applianceTypeOptions;
    static {applianceTypeOptions = ApplianceType.getAllEnumCaptions();}

    public DatabasePage(){
        this.frame = new JFrame("Update Appliance Database");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(new Dimension(500,500));
        this.frame.setLayout(new BorderLayout());

        JButton menu = new JButton();
        menu.setText("Menu");
        menu.addActionListener(e -> {
            frame.dispose();
            new HomePage();

        });
        this.frame.add(menu, BorderLayout.NORTH);

        JPanel buttonContainer = getJPanel();

        this.frame.add(buttonContainer, BorderLayout.CENTER);
        this.frame.setVisible(true);
    }

    private JPanel getJPanel() {
        JPanel buttonContainer = new JPanel();
        buttonContainer.setLayout(new BoxLayout(buttonContainer, BoxLayout.Y_AXIS));
        buttonContainer.setPreferredSize(new Dimension(200, 100));

        // Add appliance
        JButton addDB = new JButton("Add Appliance");
        addDB.addActionListener(e -> openAddApplianceDialog());
        buttonContainer.add(addDB);

        // delete appliance
        JButton deleteDB = new JButton("Delete Appliance");
        deleteDB.addActionListener(e -> openDeleteApplianceDialog());
        buttonContainer.add(deleteDB);

        //update appliance
        JButton updateDB = new JButton("Update Appliance");
        updateDB.addActionListener(e -> openUpdateApplianceDialog());
        buttonContainer.add(updateDB);
        return buttonContainer;
    }

    private void openAddApplianceDialog() {
        JDialog dialog = new JDialog(frame, "Add Appliance", true);
        dialog.setLayout(new GridLayout(5, 2));
        dialog.setSize(600, 300);


        JTextField nameField = new JTextField();
        JComboBox<String> typeField = new JComboBox<>(ApplianceType.getAllEnumCaptions().toArray(new String[0]));
        JTextField powerConsumptionField = new JTextField();
        JTextField embodiedEmissionsField = new JTextField();


        dialog.add(new JLabel("Name:"));
        dialog.add(nameField);
        dialog.add(new JLabel("Appliance Type:"));
        dialog.add(typeField);
        dialog.add(new JLabel("Power Consumption (kWh):"));
        dialog.add(powerConsumptionField);
        dialog.add(new JLabel("Embodied Emissions (kgCO2e):"));
        dialog.add(embodiedEmissionsField);

        JButton addButton = new JButton("Add");

        addButton.addActionListener(e -> {
            String name = nameField.getText();
            String selectedTypeCaption = (String) typeField.getSelectedItem();
            ApplianceType type = ApplianceType.getEnumByCaption(selectedTypeCaption);
            float powerConsumption = Float.parseFloat(powerConsumptionField.getText());
            int embodiedEmissions = Integer.parseInt(embodiedEmissionsField.getText());

            if (ApplianceService.addAppliance(name, type, powerConsumption, embodiedEmissions)) {
                JOptionPane.showMessageDialog(dialog, "Appliance added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
            else {
                JOptionPane.showMessageDialog(dialog, "Add appliance failed.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            dialog.dispose();
        });

        dialog.add(addButton);
        dialog.setVisible(true);
    }

    private void openDeleteApplianceDialog() {
        JDialog dialog = new JDialog(frame, "Delete Appliance", true);
        dialog.setLayout(new GridLayout(3, 2));
        dialog.setSize(300, 150);

        JComboBox<String> typeField = new JComboBox<>(ApplianceType.getAllEnumCaptions().toArray(new String[0]));
        JComboBox<String> nameField = new JComboBox<>();

        dialog.add(new JLabel("Appliance Type:"));
        dialog.add(typeField);
        dialog.add(new JLabel("Appliance Name:"));
        dialog.add(nameField);

        typeField.addActionListener(e -> {
            String selectedTypeCaption = (String) typeField.getSelectedItem();
            ApplianceType selectedType = ApplianceType.getEnumByCaption(selectedTypeCaption);
            nameField.removeAllItems();

            Map<ApplianceType, List<String>> applianceMap = ApplianceService.getApplianceList();


            if (applianceMap != null && applianceMap.containsKey(selectedType)) {
                List<String> applianceNames = applianceMap.get(selectedType);
                if (applianceNames != null) {
                    for (String name : applianceNames) {
                        nameField.addItem(name);
                    }
                }
            }
        });

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            String selectedName = (String) nameField.getSelectedItem();
            if (selectedName != null) {
                if (ApplianceService.deleteAppliance(selectedName)) {
                    JOptionPane.showMessageDialog(dialog, "Appliance deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(dialog, "Delete appliance failed.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                dialog.dispose();
            }
        });

        dialog.add(deleteButton);
        dialog.setVisible(true);
    }

    private void openUpdateApplianceDialog() {
        JDialog dialog = new JDialog(frame, "Update Appliance", true);
        dialog.setLayout(new GridLayout(6, 2));
        dialog.setSize(600, 300);

        JComboBox<String> typeField = new JComboBox<>(ApplianceType.getAllEnumCaptions().toArray(new String[0]));
        JComboBox<String> nameField = new JComboBox<>();
        JTextField powerConsumptionField = new JTextField();
        JTextField embodiedEmissionsField = new JTextField();

        dialog.add(new JLabel("Appliance Type:"));
        dialog.add(typeField);
        dialog.add(new JLabel("Appliance Name:"));
        dialog.add(nameField);
        dialog.add(new JLabel("Power Consumption (kWh):"));
        dialog.add(powerConsumptionField);
        dialog.add(new JLabel("Embodied Emissions (kgCO2e):"));
        dialog.add(embodiedEmissionsField);

        typeField.addActionListener(e -> {
            String selectedTypeCaption = (String) typeField.getSelectedItem();
            ApplianceType selectedType = ApplianceType.getEnumByCaption(selectedTypeCaption);
            nameField.removeAllItems();

            Map<ApplianceType, List<String>> applianceMap = ApplianceService.getApplianceList();


            if (applianceMap != null && applianceMap.containsKey(selectedType)) {
                List<String> applianceNames = applianceMap.get(selectedType);
                if (applianceNames != null) {
                    for (String name : applianceNames) {
                        nameField.addItem(name);
                    }
                }
            }
        });


        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(e -> {
            String selectedName = (String) nameField.getSelectedItem();
            String selectedTypeCaption = (String) typeField.getSelectedItem();
            ApplianceType selectedType = ApplianceType.getEnumByCaption(selectedTypeCaption);

            if (selectedName != null && selectedType != null) {
                float powerConsumption = Float.parseFloat(powerConsumptionField.getText());
                int embodiedEmissions = Integer.parseInt(embodiedEmissionsField.getText());

                if (ApplianceService.updateAppliance(selectedName, selectedType, powerConsumption, embodiedEmissions)) {
                    JOptionPane.showMessageDialog(dialog, "Appliance updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(dialog, "Update appliance failed.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Please select a valid appliance type.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });


        dialog.dispose();

        dialog.add(updateButton);
        dialog.setVisible(true);

    }



}

