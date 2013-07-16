package aurora.engine.V1.UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.plaf.basic.BasicScrollBarUI;

import org.apache.log4j.Logger;


public class AScrollBar extends BasicScrollBarUI {

    private final Image imageThumb;

    private final Image imageTrack;

    static final Logger logger = Logger.getLogger(AScrollBar.class);

    public AScrollBar(String thumb, String track) {

        AImage thumbImg = new AImage(thumb);
        AImage trackImg = new AImage(track);
        this.imageThumb = thumbImg.getImgIcon().getImage();
        this.imageTrack = trackImg.getImgIcon().getImage();
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.translate(thumbBounds.x, thumbBounds.y);
        AffineTransform transform = AffineTransform.getScaleInstance(
                (double) thumbBounds.width / imageThumb.getWidth(null),
                (double) thumbBounds.height / imageThumb.getHeight(null));
        ((Graphics2D) g).drawImage(imageThumb, transform, null);
        g2d.translate(-thumbBounds.x, -thumbBounds.y);
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(0,0,0,0));
        g2d.translate(trackBounds.x, trackBounds.y);
        ((Graphics2D) g).drawImage(
                imageTrack,
                AffineTransform
                .getScaleInstance(1, (double) trackBounds.height
                                     / imageTrack.getHeight(null)), null);
        g2d.translate(-trackBounds.x, -trackBounds.y);
    }

    @Override
    protected void installComponents() {
        switch (scrollbar.getOrientation()) {
            case JScrollBar.VERTICAL:
                incrButton = createIncreaseButton(SOUTH);
                decrButton = createDecreaseButton(NORTH);
                break;

            case JScrollBar.HORIZONTAL:
                if (scrollbar.getComponentOrientation().isLeftToRight()) {
                    incrButton = createIncreaseButton(EAST);
                    decrButton = createDecreaseButton(WEST);
                } else {
                    incrButton = createIncreaseButton(WEST);
                    decrButton = createDecreaseButton(EAST);
                }
                break;
        }
        scrollbar.add(incrButton);
        scrollbar.add(decrButton);
        // Force the children's enabled state to be updated.
        scrollbar.setEnabled(scrollbar.isEnabled());
    }

    protected JButton createZeroButton() {
        JButton button = new JButton("zero button");
        Dimension zeroDim = new Dimension(0, 0);
        button.setPreferredSize(zeroDim);
        button.setMinimumSize(zeroDim);
        button.setMaximumSize(zeroDim);
        return button;
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return createZeroButton();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return createZeroButton();
    }
}