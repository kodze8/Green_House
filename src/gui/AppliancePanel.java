package gui;

import enums.ApplianceType;
import services.ApplianceService;
import enums.Room;
import util.PanelStatics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Set;

public class AppliancePanel {
    JPanel panel;
    static Set<String> roomOptions;
    static Set<String> applianceTypeOptions;
    static String[] applianceNameOptions;
    static String[] timeOptions;
    public static HashMap<String, Integer> TIME_MAP;

    static int PANEL_WIDTH = 1050;
    static int PANEL_HEIGHT = 50;

    static {
        roomOptions = Room.getAllEnumCaptions();
        applianceTypeOptions = ApplianceType.getAllEnumCaptions();

        TIME_MAP = new HashMap<>();
        timeOptions = new String[24];
        for (int h = 0; h < 24; h++) {
            String temp =  String.format("%02d:00", h);;
            timeOptions[h] = temp;
            TIME_MAP.put(temp, h);
        }

    }

    public JComboBox<String> typeBox;
    public JComboBox<String> nameBox;
    public JComboBox<String> roomBox;
    public JComboBox<String> startTimeBox;
    public JComboBox<String> endTimeBox;
    public JCheckBox alwaysOn;
    JButton deleteButton;

    public AppliancePanel() {
        this.panel = new JPanel();
        this.panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        this.panel.setBackground(Color.RED);
        this.panel.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));

        // Initialize dropdown options for time (rounded hours)


        // Initialization
        this.typeBox = new JComboBox<>(applianceTypeOptions.toArray(new String[0]));
        this.nameBox = new JComboBox<>();
        this.nameBox.setEnabled(false);
        this.roomBox = new JComboBox<>(roomOptions.toArray(new String[0]));
        PanelStatics.addPlaceholder( this.typeBox, "Select Type");
        PanelStatics.addPlaceholder( this.nameBox, "Select Model");
        PanelStatics.addPlaceholder( this.roomBox, "Select Room");


        this.startTimeBox = new JComboBox<>(timeOptions);
        this.endTimeBox = new JComboBox<>(timeOptions);
        this.alwaysOn = new JCheckBox("Always On");
        this.deleteButton = new JButton("Delete");

        designInputs();

        this.panel.add(typeBox);
        this.panel.add(nameBox);
        this.panel.add(roomBox);
        this.panel.add(alwaysOn);
        this.panel.add(startTimeBox);
        this.panel.add(endTimeBox);
        this.panel.add(deleteButton);

        typeBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateApplianceNameOptions();
                nameBox.setEnabled(true);
                nameBox.removeAllItems();
                for (String name : applianceNameOptions) {
                    nameBox.addItem(name);
                }
                PanelStatics.addPlaceholder(nameBox, "Select Model");

            }
        });

        this.alwaysOn.addActionListener(e -> {
            if (this.alwaysOn.isSelected()) {
                this.startTimeBox.setSelectedIndex(0);
                this.endTimeBox.setSelectedIndex(23);
                this.startTimeBox.setEnabled(false);
                this.endTimeBox.setEnabled(false);
            } else {
                this.startTimeBox.setSelectedIndex(0);
                this.endTimeBox.setSelectedIndex(0);
                this.startTimeBox.setEnabled(true);
                this.endTimeBox.setEnabled(true);
            }
        });
    }

    private void updateApplianceNameOptions() {
        String selectedType = (String) typeBox.getSelectedItem();
        ApplianceType applianceType = ApplianceType.getEnumByCaption(selectedType);
        applianceNameOptions = ApplianceService.getApplianceList().get(applianceType).toArray(new String[0]);
    }

    public void cleanup() {
        for (ActionListener al : deleteButton.getActionListeners()) {
            deleteButton.removeActionListener(al);
        }
        this.panel = null;
        this.nameBox = null;
        this.typeBox = null;
        this.roomBox = null;
        this.startTimeBox = null;
        this.endTimeBox = null;
        this.deleteButton = null;
    }

    private void designInputs() {
        this.startTimeBox.setPreferredSize(new Dimension(80, 30));
        this.endTimeBox.setPreferredSize(new Dimension(80, 30));
    }

    public static void main(String[] args) {
        System.out.println(TIME_MAP);
    }
}
