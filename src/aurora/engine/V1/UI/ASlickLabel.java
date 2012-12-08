/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package aurora.engine.V1.UI;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.Icon;
import javax.swing.JLabel;

/**
 * A Better Rendered Label
 * @author Sammy Guergachi <sguergachi at gmail.com>
 */
public class ASlickLabel extends JLabel{

    public ASlickLabel(Icon image) {
        super(image);
    }

    public ASlickLabel(String text) {
        super(text + "   ");
    }

    public ASlickLabel(String text, int horizontalAlignment) {
        super(text + "   ", horizontalAlignment);
    }

    public ASlickLabel(Icon image, int horizontalAlignment) {
        super(image, horizontalAlignment);
    }

    public ASlickLabel(String text, Icon icon, int horizontalAlignment) {
        super(text + "   ", icon, horizontalAlignment);
    }

    public ASlickLabel() {
    }

    @Override
    public void setText(String text) {
        super.setText(text + "  " );
    }




     @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

        super.paintComponent(g2d);
    }

}
