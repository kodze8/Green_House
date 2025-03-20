package GUI;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public class PanelStatics {
    static void addPlaceholder(JComboBox jComboBox, String placeHolder){
        jComboBox.insertItemAt(placeHolder, 0);
        jComboBox.setSelectedIndex(0);
        jComboBox.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                if (jComboBox.getItemAt(0).equals(placeHolder)) {
                    jComboBox.removeItemAt(0);
                }
            }
            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {}
        });

    }
}
