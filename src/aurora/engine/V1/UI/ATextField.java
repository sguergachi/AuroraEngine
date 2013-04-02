package aurora.engine.V1.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import aurora.engine.V1.Logic.*;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

public class ATextField extends AImagePane {

    private String imagePath;

    private JTextField textBox;

    private String activeImagePath;

    static final Logger logger = Logger.getLogger(ATextField.class);

    public ATextField() {
        makeTextBox();
    }

    public ATextField(String backgroundImage) {
        this.imagePath = backgroundImage;

        this.setImage(imagePath);
        this.setLayout(new BorderLayout());
        makeTextBox();

    }

    public ATextField(String backgroundImage_INACTIVE,
                      String backgroundImage_ACTIVE) {
        this.imagePath = backgroundImage_INACTIVE;
        this.activeImagePath = backgroundImage_ACTIVE;

        this.setImage(imagePath);
        this.setLayout(new BorderLayout());
        makeTextBox();

    }

    public ATextField(String backgroundImage_INACTIVE,
                      String backgroundImage_ACTIVE, String SurfaceName) {
        this.imagePath = backgroundImage_INACTIVE;
        this.activeImagePath = backgroundImage_ACTIVE;
        this.setSurface(SurfaceName);
        this.setLayout(new BorderLayout());
        this.setImage(imagePath);
        makeTextBox();

    }

    private void makeTextBox() {

        textBox = new JTextField("");
        textBox.setOpaque(false);
        textBox.setBorder(null);
        textBox.setColumns(20);
        textBox.setBorder(BorderFactory.createEmptyBorder());
        textBox.setBackground(new Color(0, 0, 0, 0));
        textBox.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        textBox.addMouseListener(new AContextMenuListener());
        textBox.addFocusListener(new textFocusListener());

        this.add(textBox, BorderLayout.CENTER);

        this.setOpaque(false);
        this.setBorder(BorderFactory.createEmptyBorder());
        this.setBackground(new Color(0, 0, 0, 0));

    }

    public void setText(String text) {
        textBox.setText(text);
    }

    public String getText() {
        return textBox.getText();
    }

    public JTextField getTextBox() {
        return textBox;
    }

    public void setTextboxSize(int Width, int Height) {

        int width = Width;
        int height = Height;

        if (width == 0) {
            width = this.getImageWidth();
        }
        if (height == 0) {
            height = this.getImageHeight();
        }
        this.setPreferredSize(new Dimension(width, height));
        textBox.setPreferredSize(new Dimension(width, height));
        this.setImageSize(width, height);
        this.revalidate();
    }

    public void setBackgroundImage(String backgroundImage) {
        this.imagePath = backgroundImage;
        this.setImageSize(this.getPreferredSize().width,
                this.getPreferredSize().height);
    }

    public class textFocusListener implements FocusListener {

        @Override
        public void focusGained(FocusEvent arg0) {
            if (activeImagePath != null) {
                setImage(activeImagePath);
                setImageSize(getPreferredSize().width, getPreferredSize().height);
            } else {
                setImage(imagePath);

                setImageSize(getPreferredSize().width, getPreferredSize().height);
            }

        }

        @Override
        public void focusLost(FocusEvent arg0) {
            setImage(imagePath);
            setImageSize(getPreferredSize().width, getPreferredSize().height);
        }
    }
}
