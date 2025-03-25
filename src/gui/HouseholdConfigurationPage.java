package gui;

import enums.Country;
import controllers.HouseholdController;
import enums.EnergyLabel;
import domain.Household;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class HouseholdConfigurationPage {
    private JFrame frame;
    private JPanel bodyPanel;
    private JPanel buttonPanel;
    private JScrollPane scrollPane;
    private List<AppliancePanel> appliancePanels;
    protected JComboBox countryBox;
    protected JComboBox energyLabelBox;

    private static final int FRAME_WIDTH = 1100;
    private static final int FRAME_HEIGHT = 700;
    private static final int PANEL_HEIGHT = 50;
    private static final int PANEL_DISTANCE = 10;

    private static final String[] energyLabelOptions = EnergyLabel.getAllEnumCaptions().toArray(new String[0]);
    private static final String[] countryOptions = Country.getAllEnumCaptions().toArray(new String[0]);


    public HouseholdConfigurationPage() {
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
        /** Those two features are no more implemented.
         * createRecommendationsButton();
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
        PanelStatics.addPlaceholder(this.energyLabelBox, "Select an energy Label...");

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

//    private Household createHousehold(){
//        ApplianceValidator validator = new ApplianceValidator(this.frame);
//        boolean validInputs = validator.validateInputs(this.appliancePanels, this.countryBox, this.energyLabelBox);
//        if (validInputs) {
//            HouseholdController householdController = new HouseholdController(this.appliancePanels, this.countryBox, this.energyLabelBox);
//            return householdController.getHousehold();
//        }
//        return null;
//    }

    private Household createHousehold(){
        HouseholdController householdController = new HouseholdController(this.appliancePanels, this.countryBox, this.energyLabelBox, this.frame);
        return householdController.getHousehold();
    }
    private void handleButtonClick(Consumer<Household> nextStep) {
        Household household = createHousehold();
        if (household != null) {
            nextStep.accept(household);
        }
    }
    private void createCalculateButton() {
        JButton calculateButton = new JButton("Calculate Carbon Footprint");
        calculateButton.addActionListener(e -> handleButtonClick(this::openCFWindow));
        buttonPanel.add(calculateButton);
    }

    private void createStatisticsButton() {
        JButton statisticsButton = new JButton("Show Statistics");
        statisticsButton.addActionListener(e -> handleButtonClick(this::openStatisticsWindow));
        buttonPanel.add(statisticsButton);
    }

    private void openCFWindow(Household household) {
        openNewFrame(household.getCarbonFootPrint());
    }

    private void openStatisticsWindow(Household household) {
        StatisticsPage.openStatisticsWindow(household);
    }
    

    /** Those two features are no more implemented
     *
     * private void createRecommendationsButton() {
     *    JButton recommendationButton = new JButton("Give me Recommendations");
     *    recommendationButton.addActionListener(e -> openNewFrame(0));
     *    this.buttonPanel.add(recommendationButton);
     * }
     * private void createSaveButton() {
     *     JButton saveButton = new JButton("Save Configuration");
     *     this.buttonPanel.add(saveButton);
     * }
     * private void createRestoreButton() {
     *     JButton saveButton = new JButton("Restore Saved Configuration");
     *     this.buttonPanel.add(saveButton);
     * }
     */
    private void openNewFrame(Integer cf) {
        JFrame newFrame = new JFrame("New Frame");
        newFrame.setSize(300, 200);
        newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel label = new JLabel(""+cf, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20)); // Set the font size

        // Add the label to the new frame
        newFrame.add(label);

        // Make the new frame visible
        newFrame.setVisible(true);
    }
    public static void main(String[] args) {
        new HouseholdConfigurationPage();

    }
}

