import java.awt.event.ActionListener;
import javax.swing.JButton;

public class ButtonBuilder {
    private JButton button = new JButton();

    public ButtonBuilder() {}

    public ButtonBuilder(String id) {Components.reg(id, this.button);}

    public ButtonBuilder text(String text) {
        this.button.setText(text);
        return this;
    }

    public ButtonBuilder enabled(boolean enabled) {
        this.button.setEnabled(enabled);
        return this;
    }

    public ButtonBuilder actionListener(ActionListener actionListener) {
        this.button.addActionListener(actionListener);
        return this;
    }

    public JButton build() {
        return this.button;
    }
}
