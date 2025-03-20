package GUI;

import appliance.Appliance;
import appliance.ApplianceUsage;
import carbon_intensity.Country;
import controllers.HouseholdController;
import energy_label.EnergyLabel;
import gui_parser.ApplianceParser;
import household.Household;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CalculatePage {

    private JFrame frame;
    private JPanel bodyPanel;
    private JPanel buttonPanel;
    private JScrollPane scrollPane;
    private List<AppliancePanel> appliancePanels;
    private JLabel carbonFootprintLabel;
    protected JComboBox countryBox;
    protected JComboBox energyLabelBox;

    private static final int FRAME_WIDTH = 1100;
    private static final int FRAME_HEIGHT = 700;
    private static final int PANEL_HEIGHT = 50;
    private static final int PANEL_DISTANCE = 10;

    private static final String[] energyLabelOptions = EnergyLabel.getAllEnumCaptions().toArray(new String[0]);
    private static final String[] countryOptions = Country.getAllEnumCaptions().toArray(new String[0]);

    public CalculatePage() {
        this.appliancePanels = new ArrayList<>();
        this.frame = createFrame();
        this.bodyPanel = new JPanel();
        this.bodyPanel.setLayout(new BoxLayout(this.bodyPanel, BoxLayout.Y_AXIS));
        this.buttonPanel = new JPanel();
        this.buttonPanel.setPreferredSize(new Dimension(frame.getWidth(), 80));
        this.buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        this.frame.add(this.buttonPanel, BorderLayout.SOUTH);

        createHeader();
        createScroll();
        createAddApplianceButton();
        createActionButtons();

        this.frame.setVisible(true);
    }

    private JFrame createFrame() {
        JFrame newFrame = new JFrame("Calculate Carbon Footprint");
        newFrame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newFrame.setLayout(new BorderLayout());
        return newFrame;
    }

    private void createActionButtons() {
        createCalculateButton();
        createStatisticsButton();
        createRecommendationsButton();
        /** Those two features are no more implemented.
         *  createSaveButton();
         *  createRestoreButton();
         */
    }

    public void addPanel() {
        JPanel panelWrapper = new JPanel();
        panelWrapper.setLayout(new FlowLayout(FlowLayout.LEFT));
        panelWrapper.setMaximumSize(new Dimension(FRAME_WIDTH, PANEL_HEIGHT));
        AppliancePanel appliancePanel = new AppliancePanel();
        appliancePanel.deleteButton.addActionListener(e -> removePanel(appliancePanel, panelWrapper));
        panelWrapper.add(appliancePanel.panel);
        this.appliancePanels.add(appliancePanel);
        bodyPanel.add(panelWrapper, bodyPanel.getComponentCount() - 1);
        bodyPanel.add(Box.createVerticalStrut(PANEL_DISTANCE), bodyPanel.getComponentCount() - 1);
        bodyPanel.revalidate();
        bodyPanel.repaint();
    }

    private void removePanel(AppliancePanel appliacePanel, JPanel panelWrapper) {
        int index = -1;
        for (int i = 0; i < bodyPanel.getComponentCount(); i++) {
            if (bodyPanel.getComponent(i) == panelWrapper) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            bodyPanel.remove(index);
            if (index < bodyPanel.getComponentCount()) {
                Component nextComponent = bodyPanel.getComponent(index);
                if (nextComponent instanceof Box.Filler) {
                    // remove box filler if included.
                    bodyPanel.remove(index);
                }
            }
        }
        this.appliancePanels.remove(appliacePanel);
        appliacePanel.cleanup();
        bodyPanel.revalidate();
        bodyPanel.repaint();
    }

    private void createHeader() {
        JPanel headPanel = new JPanel();
        this.frame.add(headPanel, BorderLayout.NORTH);
        headPanel.setPreferredSize(new Dimension(frame.getWidth(), 80)); // Increased height
        headPanel.setLayout(new BoxLayout(headPanel, BoxLayout.Y_AXIS));

        JPanel upperHeadPanel = new JPanel(new BorderLayout());

        JButton leftButton = new JButton("Menu");
        leftButton.addActionListener(e -> {
            closeFrame();
            new HomePage();
        });
        upperHeadPanel.add(leftButton, BorderLayout.WEST);

        JLabel title = new JLabel("HouseHold", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setBorder(BorderFactory.createEmptyBorder(0, -80, 0, 0));
        upperHeadPanel.add(title, BorderLayout.CENTER);

        // Lower Head Panel (Inputs)
        JPanel lowerHeadPanel = new JPanel();
        this.countryBox = new JComboBox(countryOptions);
        this.energyLabelBox = new JComboBox(energyLabelOptions);

        PanelStatics.addPlaceholder(this.countryBox, "Select a country...");
        PanelStatics.addPlaceholder(this.energyLabelBox, "Select Energy Label...");

        lowerHeadPanel.add(countryBox);
        lowerHeadPanel.add(energyLabelBox);

        // Add both panels to headPanel
        headPanel.add(upperHeadPanel);
        headPanel.add(lowerHeadPanel);

        frame.add(headPanel, BorderLayout.NORTH);
        frame.setVisible(true);
    }

    private void closeFrame() {
        frame.dispose();
    }

    private void createScroll() {
        this.scrollPane = new JScrollPane(this.bodyPanel);
        this.scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        this.frame.add(scrollPane, BorderLayout.CENTER);
    }

    private void createAddApplianceButton() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addApplianceButton = new JButton("Add Appliance");
        addApplianceButton.setPreferredSize(new Dimension(200, 50));
        addApplianceButton.addActionListener(e -> addPanel());
        buttonPanel.add(addApplianceButton);
        this.bodyPanel.add(Box.createVerticalStrut(20));
        this.bodyPanel.add(buttonPanel);
    }

    private void createCalculateButton() {
        JButton calculateButton = new JButton("Calculate Carbon Footprint");
        calculateButton.addActionListener(e -> {
            // see parser class
            ApplianceParser parser = new ApplianceParser(this.appliancePanels, this.countryBox, this.energyLabelBox);

            if (!parser.validate(this.appliancePanels, this.countryBox, this.energyLabelBox)) {
                JOptionPane.showMessageDialog(frame, "Validation failed\nPlease fill out all the required boxes", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else {
                Household household;
                household = HouseholdController.initiateHousehold(countryBox, energyLabelBox, HouseholdController.parseAppliances(this.appliancePanels, this.countryBox));
                int carbonFootprint = household.calculateTotalCarbonFootprint();
                JOptionPane.showMessageDialog(frame, "Your daily carbon footprint is: " + carbonFootprint + " kg CO₂", "Success", JOptionPane.INFORMATION_MESSAGE);

            }


        });
        this.buttonPanel.add(calculateButton);
    }
//    openNewFrame();
    private void createStatisticsButton() {
        JButton statisticsButton = new JButton("Show Statistics");
        statisticsButton.addActionListener(e -> openNewFrame());
        this.buttonPanel.add(statisticsButton);
    }

    private void createRecommendationsButton() {
        //        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton recommendationButton = new JButton("Give me Recommendations");
        recommendationButton.addActionListener(e -> openNewFrame());
        this.buttonPanel.add(recommendationButton);
    }

    /** Those two features are no more implemented
     *
     * private void createSaveButton() {
     *     JButton saveButton = new JButton("Save Configuration");
     *     this.buttonPanel.add(saveButton);
     * }
     * private void createRestoreButton() {
     *     JButton saveButton = new JButton("Restore Saved Configuration");
     *     this.buttonPanel.add(saveButton);
     * }
     */
    private void openNewFrame() {
        JFrame newFrame = new JFrame("New Frame");
        newFrame.setSize(300, 200);
        newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel label = new JLabel("Hello World", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20)); // Set the font size

        // Add the label to the new frame
        newFrame.add(label);

        // Make the new frame visible
        newFrame.setVisible(true);
    }
    public static void main(String[] args) {
        new CalculatePage();

    }
}

