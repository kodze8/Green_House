package handlers;
import enums.*;

import javax.swing.*;
import java.util.Objects;

public class EnergyLabelHandler implements Handler<EnergyLabel> {
    JComboBox<String> energyLabelBox;
    private final JFrame frame;
    public EnergyLabelHandler(JComboBox<String> energyLabelBox, JFrame frame){
        this.energyLabelBox = energyLabelBox;
        this.frame = frame;
    }
    @Override
    public EnergyLabel handle() {
        if (validate()) {
           return parse();
        }else {
            return null;
        }
    }
    @Override
    public EnergyLabel parse() {
        return EnergyLabel.getEnumByCaption(Objects.requireNonNull(this.energyLabelBox.getSelectedItem()).toString());
    }

    @Override
    public boolean validate() {
        if ("Select Energy Label".equals(this.energyLabelBox.getSelectedItem())) {
            Errors.showError(this.frame, Errors.ENERGY_ERROR);
            return false;
        }
        return true;
    }
}
