import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

/**
 * A class to allow building a JPanel with BoxLayout using the Builder Pattern
 * @see <a href="https://en.wikipedia.org/wiki/Builder_pattern">Builder Pattern</a>
 */
public class BoxBuilder {

    private JPanel panel = new JPanel();
    
    /**
     * Construct a new BoxBuilder
     */
    public BoxBuilder() {}
    
    /**
     * Construct a new BoxBuilder and register it in the global Components registry
     * @param id
     */
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
