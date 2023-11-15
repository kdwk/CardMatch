import javax.swing.*;
import java.awt.*;

public class FrameBuilder {
    private JFrame frame = new JFrame();

    public FrameBuilder() {
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public FrameBuilder(String id) {
        Components.reg(id, this.frame);
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public FrameBuilder name(String title) {
        this.frame.setName(title);
        return this;
    }

    public FrameBuilder minSize(int width, int height) {
        this.frame.setMinimumSize(new Dimension(width, height));
        return this;
    }

    public FrameBuilder maxSize(int width, int height) {
        this.frame.setMaximumSize(new Dimension(width, height));
        return this;
    }

    public FrameBuilder size(int width, int height) {
        this.frame.setSize(width, height);
        return this;
    }

    public FrameBuilder add(Component component) {
        // this.frame.getContentPane().add(component);
        this.frame.add(component);
        return this;
    }

    public FrameBuilder closeOperation(int closeOperation) {
        this.frame.setDefaultCloseOperation(closeOperation);
        return this;
    }

    public FrameBuilder pack() {
        this.frame.pack();
        return this;
    }

    public FrameBuilder menuBar(JMenu menu) {
        JMenuBar menubar = new JMenuBar();
        menubar.add(menu);
        this.frame.setJMenuBar(menubar);
        return this;
    }

    public JFrame build() {
        return this.frame;
    }
}