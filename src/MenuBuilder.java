import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class MenuBuilder {
    private String id;
    private String label;
    private ArrayList<JMenuItem> menuItems = new ArrayList<>();
    private ActionListener actionListener = null;

    public MenuBuilder() {}

    public MenuBuilder(String id) {this.id = id;}

    public MenuBuilder label(String label) {
        this.label = label;
        return this;
    }

    public MenuBuilder add(JMenuItem menuItem) {
        this.menuItems.add(menuItem);
        return this;
    }

    public MenuBuilder itemsActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
        return this;
    }

    public JMenu build() {
        JMenu menu = new JMenu(this.label);
        for (JMenuItem menuItem: this.menuItems) {
            menuItem.addActionListener(this.actionListener);
            menu.add(menuItem);
        }
        Components.reg(this.id, menu);
        return menu;
    }
}
