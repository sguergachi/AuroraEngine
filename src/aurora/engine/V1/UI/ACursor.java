/*
 * Copyright 2012 Sardonix Creative.
 *
 * This work is licensed under the
 * Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * To view a copy of this license, visit
 *
 *      http://creativecommons.org/licenses/by-nc-nd/3.0/
 *
 * or send a letter to Creative Commons, 444 Castro Street, Suite 900,
 * Mountain View, California, 94041, USA.
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package aurora.engine.V1.UI;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
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
