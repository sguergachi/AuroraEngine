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

import aurora.engine.V1.Logic.aSurface;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author Sammy
 */
public class aImage extends JLabel {

    private String ImageURl;
    private ImageIcon image;
    private int w = 0;
    private int h = 0;
    private final aSurface ressource;

    public String getImgURl() {
        return ImageURl;
    }

    public int getImgHeight() {
        return image.getIconHeight();
    }

    public ImageIcon getImgIcon() {
        return image;
    }

    public int getImgWidth() {
        return image.getIconWidth();
    }

    public void setImgURl(String ImgURl) {
        this.ImageURl = ImgURl;
        createImage();
    }

    public void setH(int height) {
        this.h = height;
        createImage();
    }

    public void setImg(ImageIcon img) {
        this.image = img;
        createImage();
    }

    public void setW(int width) {
        this.w = width;
        createImage();
    }

    public void setImageSize(int w, int h) {

        this.w = w;
        this.h = h;
        createImage();

    }

    public aImage(String ImgURL) {
        ressource = new aSurface("");
        //System.out.println(ressource.getSurfacePath());
        this.ImageURl = ImgURL;
        this.setOpaque(false);

        createImage();
    }

    public aImage(String ImgURL, int Width, int Height) {
        ressource = new aSurface("");
        //System.out.println(ressource.getSurfacePath());
        this.ImageURl = ImgURL;
        this.w = Width;
        this.h = Height;
        this.setOpaque(false);

        createImage();
    }

    private void createImage() {




        try {

            image = new ImageIcon(new URL(ressource.getSurfacePath() + "/aurora/V1/resources/" + ImageURl));

        } catch (MalformedURLException ex) {
            //fallback
            try {
                image = new ImageIcon(getClass().getResource("/aurora/V1/resources/" + ImageURl));
            } catch (Exception exx) {
                Logger.getLogger(aImage.class.getName()).log(Level.SEVERE, null, exx);
            }
        }

        if (w == 0) {
            w = image.getImage().getWidth(this);
            h = image.getImage().getHeight(this);
        }


        Image img = (aImage.resizeImage(image.getImage(), w, h));
        image.setImage(img);
        img.flush();

        this.setIcon(image);
    }

    public static BufferedImage resizeBufferedImg(Image image, int width, int height) {

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE);
        Graphics2D g = bufferedImage.createGraphics();
        g.setComposite(AlphaComposite.Src);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

        g.drawImage(image, 0, 0, width, height, null);
        g.dispose();


        return bufferedImage;
    }

    public static Image resizeImage(Image image, int width, int height) {

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE);
        Graphics2D g = bufferedImage.createGraphics();
        g.setComposite(AlphaComposite.Src);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

        g.drawImage(image, 0, 0, width, height, null);
        g.dispose();


        return Toolkit.getDefaultToolkit().createImage(bufferedImage.getSource());
    }
}
