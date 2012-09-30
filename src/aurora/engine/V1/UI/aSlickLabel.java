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
public class aSlickLabel extends JLabel{

    public aSlickLabel(Icon image) {
        super(image);
    }

    public aSlickLabel(String text) {
        super(text + "   ");
    }

    public aSlickLabel(String text, int horizontalAlignment) {
        super(text + "   ", horizontalAlignment);
    }

    public aSlickLabel(Icon image, int horizontalAlignment) {
        super(image, horizontalAlignment);
    }

    public aSlickLabel(String text, Icon icon, int horizontalAlignment) {
        super(text + "   ", icon, horizontalAlignment);
    }

    public aSlickLabel() {
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
