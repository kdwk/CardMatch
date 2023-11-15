import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class BoxBuilder {

    private JPanel panel = new JPanel();

    public BoxBuilder() {}

    public BoxBuilder(String id) {Components.reg(id, this.panel);}

    public BoxBuilder add(String id, Component component) {
        JPanel componentPanel = new JPanel();
        componentPanel.add(component);
        this.panel.add(componentPanel);
        Components.reg(id, component);
        return this;
    }

    public BoxBuilder add(Component component) {
        JPanel componentPanel = new JPanel();
        componentPanel.add(component);
        this.panel.add(componentPanel);
        return this;
    }

    public BoxBuilder layoutAxis(int axis) {
        this.panel.setLayout(new BoxLayout(this.panel, axis));
        return this;
    }

    public BoxBuilder alignX(float alignment) {
        this.panel.setAlignmentX(alignment);
        return this;
    }

    public BoxBuilder alignY(float alignment) {
        this.panel.setAlignmentY(alignment);
        return this;
    }

    public BoxBuilder doubleBuffered(boolean doubleBuffered) {
        this.panel.setDoubleBuffered(doubleBuffered);
        return this;
    }

    public BoxBuilder background(Color color) {
        this.panel.setBackground(color);
        return this;
    }

    public BoxBuilder maxSize(int width, int height) {
        this.panel.setMaximumSize(new Dimension(width, height));
        return this;
    }

    public BoxBuilder minSize(int width, int height) {
        this.panel.setMinimumSize(new Dimension(width, height));
        return this;
    }

    public BoxBuilder preferredSize(int width, int height) {
        this.panel.setPreferredSize(new Dimension(width, height));
        return this;
    }

    public JPanel build() {
        return this.panel;
    }
}
