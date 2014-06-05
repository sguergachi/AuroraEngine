/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aurora.engine.V1.UI;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.JLabel;
import org.apache.log4j.Logger;

/**
 * A Better Rendered Label
 * <p/>
 * @author Sammy Guergachi <sguergachi at gmail.com>
 */
public class ASlickLabel extends JLabel implements MouseListener {

    private String url;

    private Color prevColor;

    private Cursor prevCursor;

    static final Logger logger = Logger.getLogger(ASlickLabel.class);

    private Color linkColor;

    public ASlickLabel(String text) {
        super(text + "");
    }

    public ASlickLabel(String text, int horizontalAlignment) {
        super(text + "", horizontalAlignment);
    }

    public ASlickLabel() {
    }

    @Override
    public void setText(String text) {
        super.setText(text + "  ");
    }

    public void superSetText(String text) {
        super.setText(text);
    }

    public void setLink(String URL) {
        this.url = URL;
        this.addMouseListener(this);
        this.linkColor = Color.green;
    }

    public void setLink(String URL, Color linkColor) {
        this.url = URL;
        this.addMouseListener(this);
        this.linkColor = linkColor;
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
                RenderingHints.VALUE_STROKE_NORMALIZE);

        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);

        super.paintComponent(g2d);
    }

    public void mouseClicked(MouseEvent e) {

        if (url != null) {
            try {
                try {
                    Desktop.getDesktop().browse(new URI(url));
                } catch (URISyntaxException ex) {
                    logger.error(ex);
                }
            } catch (IOException ex) {
                logger.error(ex);
            }
        }
    }

    public void mousePressed(MouseEvent e) {
        System.out.println("Mouse Clicked Link!");
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {

        if (url != null) {
            prevColor = this.getForeground();
            prevCursor = this.getCursor();

            ACursor selectCursor = new ACursor(new AImage("cursor_select.png"));
            setCursor(selectCursor.getCursor());
            this.setForeground(linkColor);
        }

    }

    public void mouseExited(MouseEvent e) {
        if (url != null) {
            this.setForeground(prevColor);
            setCursor(prevCursor);
        }
    }
}
