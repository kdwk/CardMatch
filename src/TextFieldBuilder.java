import java.awt.Dimension;
import javax.swing.JTextField;

public class TextFieldBuilder {
    private JTextField textfield = new JTextField();

    public TextFieldBuilder() {}

    public TextFieldBuilder(String id) {Components.reg(id, textfield);}

    public TextFieldBuilder text(String text) {
        this.textfield.setText(text);
        return this;
    }

    public TextFieldBuilder preferredSize(int width, int height) {
        this.textfield.setPreferredSize(new Dimension(width, height));
        return this;
    }

    public JTextField build() {
        return this.textfield;
    }
}
