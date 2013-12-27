/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aurora.engine.V1.UI;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import org.apache.log4j.Logger;


/**
 * .-----------------------------------------------------------------------.
 * | ACursor
 * .-----------------------------------------------------------------------.
 * |
 * | A Class to generate a custom Cursor with any transparencies omitted
 * |
 * |
 * .........................................................................
 *
 * @author Sammy Guergachi <sguergachi at gmail.com>
 *
 */
public class ACursor {

    private final AImage cursorImage;

    private Cursor Cursor;
    
    static final Logger logger = Logger.getLogger(ACursor.class);

    public ACursor(AImage aCursorImage) {

        this.cursorImage = aCursorImage;
        generateCursor();
    }

    /**
     * .-----------------------------------------------------------------------.
     * | generateCursor()
     * .-----------------------------------------------------------------------.
     * |
     * | generates the cursor by taking the AImage and converting it to a
     * | BufferedImage to be manipulated
     * | Checks for any transparency values and eliminates them then creates
     * | the cursor.
     * |
     * | use getCursor() to get the generated cursor
     * |
     * .........................................................................
     */
    private void generateCursor() {
        try {
            BufferedImage bufferedImage = AImage.resizeBufferedImage2(cursorImage
                    .getImgIcon().getImage(), cursorImage
                    .getImgIcon().getIconWidth(), cursorImage.getImgIcon()
                    .getIconHeight());
            for (int i = 0; i < bufferedImage.getHeight(); i++) {
                int[] rgb = bufferedImage.getRGB(0, i, bufferedImage.getWidth(),
                        1, null, 0, bufferedImage.getWidth() * 4);
                for (int j = 0; j < rgb.length; j++) {
                    int alpha = (rgb[j] >> 24) & 255;
                    if (alpha < 128) {
                        alpha = 0;
                    } else {
                        alpha = 255;
                    }
                    rgb[j] &= 0x00ffffff;
                    rgb[j] = (alpha << 24) | rgb[j];
                }
                bufferedImage.setRGB(0, i, bufferedImage.getWidth(), 1, rgb, 0,
                        cursorImage.getWidth() * 4);
            }
            Cursor = Toolkit.getDefaultToolkit()
                    .createCustomCursor(
                    bufferedImage, new Point(11, 8), "CustomCursor");



        } catch (Exception exp) {
        	logger.error(exp, exp);
        }
    }

    public Cursor getCursor() {
        return Cursor;
    }
}
