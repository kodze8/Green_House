package controllers;
import enums.Country;

import javax.swing.*;
import java.util.Objects;

public class CountryHandler implements Handler{
    private JFrame frame;
    JComboBox<String> countryBox;
    public CountryHandler(JComboBox<String> countryBox, JFrame frame){
        this.countryBox = countryBox;
        this.frame = frame;

    }
    @Override
    public Country handle() {
        if (validate()) {
            return parse();
        }else {
            return null;
        }
    }
    @Override
    public boolean validate() {
        if ("Select a country...".equals(this.countryBox.getSelectedItem())) {
            Errors.showError(this.frame, Errors.COUNTRY_ERROR);
            return false;
        }
        return true;
    }

    @Override
    public Country parse() {
        return Country.getEnumByCaption(Objects.requireNonNull(this.countryBox.getSelectedItem()).toString());
    }

}
