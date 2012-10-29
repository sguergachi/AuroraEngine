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

import aurora.engine.V1.Logic.ASurface;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * Image on a panel
 *
 * @author Sammy
 * @version 1.0
 */
public class AImagePane extends JPanel {

    private String ImageURL;
    private ImageIcon image;
    private int imageHeight = 0;
    private int imageWidth = 0;
    private ASurface ressource;

    //////////////
    //Constructors
    //////////////
    public AImagePane(String path, int W, int H) {

        ressource = new ASurface("");
        ImageURL = path;
        imageHeight = H;
        imageWidth = W;
        this.setOpaque(false);

        try {
            image = new ImageIcon(new URL(ressource.getSurfacePath() + "/aurora/V1/resources/" + ImageURL));
        } catch (MalformedURLException ex) {

            try {
                image = new ImageIcon(getClass().getResource("/aurora/V1/resources/" + ImageURL));
            } catch (Exception exx) {
                Logger.getLogger(AImagePane.class.getName()).log(Level.SEVERE, null, exx);
            }
        }

    }

    public AImagePane() {
        ressource = new ASurface("");
    }

    public AImagePane(String path) {
        ressource = new ASurface("");
        ImageURL = path;
        imageHeight = 0;
        imageWidth = 0;
        this.setOpaque(false);

        try {
            image = new ImageIcon(new URL(ressource.getSurfacePath() + "/aurora/V1/resources/" + ImageURL));
        } catch (MalformedURLException ex) {
            try {
                image = new ImageIcon(getClass().getResource("/aurora/V1/resources/" + ImageURL));
            } catch (Exception exx) {
                Logger.getLogger(AImagePane.class.getName()).log(Level.SEVERE, null, exx);
            }
        }
    }

    public AImagePane(String path, LayoutManager layout) {

        super(layout, true);
        ressource = new ASurface("");
        ImageURL = path;
        imageHeight = 0;
        imageWidth = 0;
        this.setOpaque(false);
        try {
            image = new ImageIcon(new URL(ressource.getSurfacePath() + "/aurora/V1/resources/" + ImageURL));
        } catch (MalformedURLException ex) {
            try {
                image = new ImageIcon(getClass().getResource("/aurora/V1/resources/" + ImageURL));
            } catch (Exception exx) {
                Logger.getLogger(AImagePane.class.getName()).log(Level.SEVERE, null, exx);
            }
        }
    }

    public AImagePane(String path, int W, int H, LayoutManager layout) {
        super(layout, true);
        ressource = new ASurface("");
        ImageURL = path;
        imageHeight = H;
        imageWidth = W;
        this.setOpaque(false);
        try {
            image = new ImageIcon(new URL(ressource.getSurfacePath() + "/aurora/V1/resources/" + ImageURL));
        } catch (MalformedURLException ex) {
            try {
                image = new ImageIcon(getClass().getResource("/aurora/V1/resources/" + ImageURL));
            } catch (Exception exx) {
                Logger.getLogger(AImagePane.class.getName()).log(Level.SEVERE, null, exx);
            }
        }
    }

    public AImagePane(String path, int W, int H, LayoutManager layout, boolean DoubleBuffered) {
        super(layout, DoubleBuffered);
        ressource = new ASurface("");
        ImageURL = path;
        imageHeight = H;
        imageWidth = W;
        this.setOpaque(false);
        try {
            image = new ImageIcon(new URL(ressource.getSurfacePath() + "/aurora/V1/resources/" + ImageURL));
        } catch (MalformedURLException ex) {
            try {
                image = new ImageIcon(getClass().getResource("/aurora/V1/resources/" + ImageURL));
            } catch (Exception exx) {
                Logger.getLogger(AImagePane.class.getName()).log(Level.SEVERE, null, exx);
            }
        }
    }

    public AImagePane(String path, int W, int H, boolean DoubleBuffered) {
        super(DoubleBuffered);
        ressource = new ASurface("");
        ImageURL = path;
        imageHeight = H;
        imageWidth = W;
        this.setOpaque(false);

        try {
            image = new ImageIcon(new URL(ressource.getSurfacePath() + "/aurora/V1/resources/" + ImageURL));
        } catch (MalformedURLException ex) {
            try {
                image = new ImageIcon(getClass().getResource("/aurora/V1/resources/" + ImageURL));
            } catch (Exception exx) {
                Logger.getLogger(AImagePane.class.getName()).log(Level.SEVERE, null, exx);
            }
        }

    }

    @Override
    protected void paintComponent(Graphics g) {
        //  Dispaly  image on Panel

        if (image != null) {
            if (imageWidth == 0 && imageHeight == 0) {
                imageWidth = image.getIconWidth();
                imageHeight = image.getIconHeight();
            }
            g.drawImage(image.getImage(), 0, 0, imageWidth, imageHeight, this);
        } else {
            g.clearRect(0, 0, imageWidth, imageHeight);

        }


    }

    public void setURL(String URL) {
        ImageURL = URL;
        imageWidth = 0;
        imageHeight = 0;
        try {
            image = new ImageIcon(new URL(ressource.getSurfacePath() + "/aurora/V1/resources/" + ImageURL));
        } catch (MalformedURLException ex) {
            try {
                image = new ImageIcon(getClass().getResource("/aurora/V1/resources/" + ImageURL));
            } catch (Exception exx) {
                Logger.getLogger(AImagePane.class.getName()).log(Level.SEVERE, null, exx);
            }
        }
        this.revalidate();
        this.repaint();
    }

    public void setImageSize(int width, int height) {
        imageHeight = height;
        imageWidth = width;
        revalidate();
        repaint();
    }

    public void setImage(ImageIcon img, int H, int W) {
        this.image = null;
        this.repaint();
        this.revalidate();
        image = img;
        imageHeight = H;
        imageWidth = W;
        this.repaint();
        this.revalidate();
    }

    public void setImage(String ImageURL, int H, int W) {
        this.image = null;
        this.repaint();
        this.revalidate();

        this.ImageURL = ImageURL;
        imageHeight = H;
        imageWidth = W;
        this.setOpaque(false);
        try {
            image = new ImageIcon(new URL(ressource.getSurfacePath() + "/aurora/V1/resources/" + this.ImageURL));
        } catch (MalformedURLException ex) {
            try {
                image = new ImageIcon(getClass().getResource("/aurora/V1/resources/" + ImageURL));
            } catch (Exception exx) {
                Logger.getLogger(AImagePane.class.getName()).log(Level.SEVERE, null, exx);
            }
        }
        this.revalidate();
        this.repaint();
    }

    public void setImage(String ImageURL) {
        this.image = null;


        this.ImageURL = ImageURL;
        try {
            this.image = new ImageIcon(new URL(ressource.getSurfacePath() + "/aurora/V1/resources/" + ImageURL));
        } catch (MalformedURLException ex) {
            try {
                image = new ImageIcon(getClass().getResource("/aurora/V1/resources/" + ImageURL));
            } catch (Exception exx) {
                Logger.getLogger(AImagePane.class.getName()).log(Level.SEVERE, null, exx);
            }
        }
        imageWidth = image.getIconWidth();
        imageHeight = image.getIconHeight();

        this.revalidate();
        this.repaint();

    }

    public void setImage(AImagePane image) {
        if (image != null) {
            this.image = image.getImgIcon();
            imageWidth = image.getImageWidth();
            imageHeight = image.getImageHeight();
        } else {
            this.image = image.getImgIcon();
        }

        this.revalidate();
        this.repaint();

    }

    public void setImageURL(String ImageURL) throws MalformedURLException {
        this.image = null;


        System.out.println("URL " + ImageURL);
        this.ImageURL = ImageURL;
        this.image = new ImageIcon(new URL(ImageURL));

        imageWidth = image.getIconWidth();
        imageHeight = image.getIconHeight();

        this.revalidate();
        this.repaint();

    }

    public void clearImage() {
        this.image = null;
        this.revalidate();
        this.repaint();
    }

    public ImageIcon getImgIcon() {
        return image;
    }

    public String getImageName() {
        return ImageURL;
    }

    //TODO GET REAL SIZE AND WIDTH using get getImgIcon().getIconWidth()
    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
        this.repaint();
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
        this.repaint();
    }

    public int getImageHeight() {
        return imageHeight;

    }

    public int getImageWidth() {
        return imageWidth;
    }
}
