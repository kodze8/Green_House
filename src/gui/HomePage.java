package gui;

import services.DatabaseService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class HomePage {
    private JFrame frame;
    public HomePage(){
        frame = new JFrame("Home");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLayout(new BorderLayout());

        // Create a wrapper panel to center the button container
        JPanel wrapperPanel = new JPanel(new GridBagLayout());

        // Button container
        JPanel buttonContainer = new JPanel();
        buttonContainer.setLayout(new BoxLayout(buttonContainer, BoxLayout.Y_AXIS));
        buttonContainer.setPreferredSize(new Dimension(200, 100));

        JButton goToDB = new JButton("Update DataBase");
        JButton goToCalculate = new JButton("Calculate Carbon Footprint");

        buttonContainer.add(goToDB);
        buttonContainer.add(goToCalculate);

        goToCalculate.addActionListener(e->{frame.dispose();new HouseholdConfigurationPage();});
        goToDB.addActionListener(e -> {
            List<String> invalidEntries = DatabaseService.validateDatabase();
            //show popup with invalid entries if they exist
            if (!invalidEntries.isEmpty()) {
                StringBuilder message = new StringBuilder("The following entries are invalid:\n");

                for (String entry : invalidEntries) {
                    message.append(entry).append("\n");
                }
                JOptionPane.showMessageDialog(frame, message.toString(), "Invalid Database Entries", JOptionPane.ERROR_MESSAGE);
            }

            frame.dispose();
            new ApplianceDatabasePage();
        });

        wrapperPanel.add(buttonContainer);
        frame.add(wrapperPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new HomePage();
    }
}

