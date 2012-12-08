package aurora.engine.V1.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import aurora.engine.V1.Logic.*;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ATextField extends AImagePane {

	private String imagePath;
	private JTextField textBox;
	private String activeImagePath;

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
		textBox.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 10));
		textBox.addMouseListener(new AContextMenuListener());
		textBox.addFocusListener(new textFocusListener());

		this.add(textBox, BorderLayout.CENTER);

		this.setOpaque(false);
		this.setBorder(BorderFactory.createEmptyBorder());
		this.setBackground(new Color(0, 0, 0, 0));

	}

	public JTextField getTextBox() {
		return textBox;
	}

	public void setBackgroundImage(String backgroundImage) {
		this.imagePath = backgroundImage;
		this.setImageSize(this.getPreferredSize().width, this.getPreferredSize().height);
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
