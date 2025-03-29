package handlers;

import domain.ApplianceUsage;
import enums.Country;
import enums.Room;
import gui.AppliancePanel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ApplianceUsageHandler implements Handler<List<ApplianceUsage>> {
    private final List<AppliancePanel> appliancePanelList;
    private final JFrame frame;
    private Country country;

    public ApplianceUsageHandler(List<AppliancePanel> appliancePanelList, JFrame frame) {
        this.appliancePanelList = appliancePanelList;
        this.frame = frame;
    }


    public void setCountry(Country country) {
        this.country = country;
    }

    @Override
    public List<ApplianceUsage> handle() {
        if (validate()) {
            return parse();
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<ApplianceUsage> parse() {
        if(country==null)
            return Collections.emptyList();
        List<ApplianceUsage> applianceUsages = new ArrayList<>();
        for (AppliancePanel panel : appliancePanelList) {
            String name = Objects.requireNonNull(panel.nameBox.getSelectedItem()).toString();
            Room room = Room.getEnumByCaption(Objects.requireNonNull(panel.roomBox.getSelectedItem()).toString());
            boolean alwaysOn = panel.alwaysOn.isSelected();
            int startTime = alwaysOn ? 0 : AppliancePanel.TIME_MAP.get(panel.startTimeBox.getSelectedItem());
            int endTime = alwaysOn ? 23 : AppliancePanel.TIME_MAP.get(panel.endTimeBox.getSelectedItem());
            applianceUsages.add(new ApplianceUsage(name, room, country, alwaysOn, startTime, endTime)); // Pass the country here
        }
        if (applianceUsages.isEmpty()) Errors.showError(frame, Errors.APPLIANCE_EMPTY);
        return applianceUsages;
    }

    @Override
    public boolean validate() {
        for (AppliancePanel panel : this.appliancePanelList) {
            if ("Select Type".equals(panel.typeBox.getSelectedItem())) {
                Errors.showError(frame, Errors.APPLIANCE_TYPE_ERROR);
                return false;
            }
            if ("Select Model".equals(panel.nameBox.getSelectedItem())) {
                Errors.showError(frame, Errors.APPLIANCE_MODEL_ERROR);
                return false;
            }
            if ("Select Room".equals(panel.roomBox.getSelectedItem())) {
                Errors.showError(frame, Errors.APPLIANCE_ROOM_ERROR);
                return false;
            }
            if (panel.endTimeBox.getSelectedIndex() < panel.startTimeBox.getSelectedIndex()) {
                Errors.showError(frame, Errors.APPLIANCE_TIME_ERROR);
                return false;
            }
        }
        return true;
    }
}