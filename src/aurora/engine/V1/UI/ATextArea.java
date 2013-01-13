package aurora.engine.V1.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import aurora.engine.V1.Logic.*;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class ATextArea extends AImagePane {

	private String imagePath;
	private JTextArea textArea;
	private String activeImagePath;
	private String scrollThumb;
	private String scrollTrack;
	private AImage imgScrollThumb;
	private AImage imgScrollTrack;

	public ATextArea() {
		makeTextBox();
	}

	public ATextArea(String backgroundImage) {
		this.imagePath = backgroundImage;

		this.setImage(imagePath);
		this.setLayout(new BorderLayout());
		makeTextBox();

	}

	public ATextArea(String backgroundImage_INACTIVE,
			String backgroundImage_ACTIVE) {
		this.imagePath = backgroundImage_INACTIVE;
		this.activeImagePath = backgroundImage_ACTIVE;

		this.setImage(imagePath);
		this.setLayout(new BorderLayout());
		makeTextBox();

	}

	public ATextArea(String backgroundImage_INACTIVE,
			String backgroundImage_ACTIVE, String SurfaceName) {
		this.imagePath = backgroundImage_INACTIVE;
		this.activeImagePath = backgroundImage_ACTIVE;
		this.setSurface(SurfaceName);
		this.setLayout(new BorderLayout());
		this.setImage(imagePath);
		makeTextBox();

	}

	public ATextArea(String backgroundImage_INACTIVE,
			String backgroundImage_ACTIVE, String ScrollThumb,
			String ScrollTrack, String SurfaceName) {
		this.imagePath = backgroundImage_INACTIVE;
		this.activeImagePath = backgroundImage_ACTIVE;
		this.setSurface(SurfaceName);
		this.setLayout(new BorderLayout());
		this.setImage(imagePath);
		this.scrollThumb = ScrollThumb;
		this.scrollTrack = ScrollTrack;
		makeTextBox();

	}

	private void makeTextBox() {

		textArea = new JTextArea("");
		textArea.setOpaque(false);
		textArea.setBorder(null);
		textArea.setColumns(20);
		textArea.setBorder(BorderFactory.createEmptyBorder());
		textArea.setBackground(new Color(0, 0, 0, 0));
		textArea.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
		textArea.addMouseListener(new AContextMenuListener());
		textArea.addFocusListener(new textFocusListener());

		JScrollPane areaScrollPane = new JScrollPane(textArea);
		areaScrollPane.setOpaque(false);
		areaScrollPane.getViewport().setOpaque(false);
		areaScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 3, 10, 3));
		areaScrollPane.setViewportBorder(BorderFactory.createEmptyBorder());
		areaScrollPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		//Make Scrollbar move a little to the left
		areaScrollPane.setPreferredSize(new Dimension(this.getWidth() - 7, this
				.getHeight()));
		this.add(areaScrollPane, BorderLayout.WEST);

		this.setOpaque(false);
		this.setBorder(BorderFactory.createEmptyBorder());
		this.setBackground(new Color(0, 0, 0, 0));

		if (scrollThumb != null || scrollTrack != null) {

			imgScrollThumb = new AImage(scrollThumb);
			imgScrollTrack = new AImage(scrollTrack);

//			areaScrollPane.getVerticalScrollBar().setUI(
//					new AScrollBar(imgScrollThumb.getImgIcon().getImage(),
//							imgScrollTrack.getImgIcon().getImage()));
		}

		areaScrollPane.getVerticalScrollBar().setPreferredSize(
				new Dimension(4, 0));
	}

	public JTextArea getTextBox() {
		return textArea;
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

				setImageSize(getPreferredSize().width,
						getPreferredSize().height);
			} else {
				setImage(imagePath);

				setImageSize(getPreferredSize().width,
						getPreferredSize().height);
			}

		}

		@Override
		public void focusLost(FocusEvent arg0) {
			setImage(imagePath);
			setImageSize(getPreferredSize().width, getPreferredSize().height);
		}

	}

}
