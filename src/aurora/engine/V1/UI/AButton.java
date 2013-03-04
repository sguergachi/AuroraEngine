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
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import org.apache.log4j.Logger;

/**
 * Extention of JButton to allow custom Image states (regardless of Look & Feel)
 *
 * @author Sammy
 * @version 0.2
 */
public class AButton extends JButton {

    String urlNormal;

    String urlOver;

    String urlDown;

    private ImageIcon btnNormal;

    private ImageIcon btnOver;

    private ImageIcon btnDown;

    private int w;

    private int h;

    static final Logger logger = Logger.getLogger(AButton.class);
    //////////////////////
    ///  Constructor    //
    //////////////////////

    private ASurface ressource;

    /**
     * Sets up the imageIcons to be painted on button and allows a string to be
     * placed on top of the button
     *
     * @param text
     * @param ImgUp
     * @param ImgDwn
     * @param ImgOvr
     *
     */
    public AButton(String text, String ImgUp, String ImgDwn, String ImgOvr) {
        super(text);
        this.urlNormal = ImgUp;
        this.urlOver = ImgOvr;
        this.urlDown = ImgDwn;
        ressource = new ASurface("");
        try {
            btnNormal = new ImageIcon(new URL(ressource.getSurfacePath()
                                              + "/aurora/V1/resources/" + ImgUp));
            btnOver = new ImageIcon(new URL(ressource.getSurfacePath()
                                            + "/aurora/V1/resources/" + ImgOvr));
            btnDown = new ImageIcon(new URL(ressource.getSurfacePath()
                                            + "/aurora/V1/resources/" + ImgDwn));

        } catch (MalformedURLException ex) {
            //Fallback
            try {
                btnNormal = new ImageIcon(getClass()
                                                                 .getResource(
                                                                 "/aurora/V1/resources/"
                                                                 + ImgUp));
                btnOver = new ImageIcon(getClass()
                                                               .getResource(
                                                               "/aurora/V1/resources/"
                                                               + ImgOvr));
                btnDown = new ImageIcon(getClass()
                                                               .getResource(
                                                               "/aurora/V1/resources/"
                                                               + ImgDwn));
            } catch (Exception exx) {
                logger.error(exx);
            }
        }

        setButton();
    }

    /**
     * Sets up the imageIcons to be painted on button and allows a string to be
     * placed on top of the button
     *
     * @param name
     * @param ImgUp
     * @param ImgDwn
     * @param ImgOvr
     *
     */
    public AButton(String ImgUp, String ImgDwn, String ImgOvr) {
        ressource = new ASurface("");
        this.urlNormal = ImgUp;
        this.urlOver = ImgOvr;
        this.urlDown = ImgDwn;
        try {
            btnNormal = new ImageIcon(new URL(ressource.getSurfacePath()
                                              + "/aurora/V1/resources/" + ImgUp));
            btnOver = new ImageIcon(new URL(ressource.getSurfacePath()
                                            + "/aurora/V1/resources/" + ImgOvr));
            btnDown = new ImageIcon(new URL(ressource.getSurfacePath()
                                            + "/aurora/V1/resources/" + ImgDwn));
        } catch (MalformedURLException ex) {
            try {
                btnNormal = new ImageIcon(getClass()
                                                                 .getResource(
                                                                 "/aurora/V1/resources/"
                                                                 + ImgUp));
                btnOver = new ImageIcon(getClass()
                                                               .getResource(
                                                               "/aurora/V1/resources/"
                                                               + ImgOvr));
                btnDown = new ImageIcon(getClass()
                                                               .getResource(
                                                               "/aurora/V1/resources/"
                                                               + ImgDwn));
            } catch (Exception exx) {
                logger.error(exx);
            }
        }


        setButton();
    }

    /**
     * Sets up the imageIcons to be painted on button
     *
     * @param name
     * @param ImgUp
     * @param ImgDwn
     * @param ImgOvr
     *
     */
    public AButton(String ImgUp, String ImgDwn, String ImgOvr, int width,
                   int height) {

        this.w = width;
        this.h = height;

        ressource = new ASurface("");

        this.urlNormal = ImgUp;
        this.urlOver = ImgOvr;
        this.urlDown = ImgDwn;
        try {
            btnNormal = new ImageIcon(new URL(ressource.getSurfacePath()
                                              + "/aurora/V1/resources/" + ImgUp));
            btnOver = new ImageIcon(new URL(ressource.getSurfacePath()
                                            + "/aurora/V1/resources/" + ImgOvr));
            btnDown = new ImageIcon(new URL(ressource.getSurfacePath()
                                            + "/aurora/V1/resources/" + ImgDwn));
        } catch (MalformedURLException ex) {
            try {
                btnNormal = new ImageIcon(getClass()
                                                                 .getResource(
                                                                 "/aurora/V1/resources/"
                                                                 + ImgUp));
                btnOver = new ImageIcon(getClass()
                                                               .getResource(
                                                               "/aurora/V1/resources/"
                                                               + ImgOvr));
                btnDown = new ImageIcon(getClass()
                                                               .getResource(
                                                               "/aurora/V1/resources/"
                                                               + ImgDwn));
            } catch (Exception exx) {
                logger.error(exx);
            }
        }





        if (w == 0) {

            w = btnNormal.getIconWidth();

        }
        if (h == 0) {
            h = btnNormal.getIconHeight();
        }
        try {
            //System.out.println("BUTTON "  + ressource.getSurfacePath() + "/aurora/V1/resources/"+ urlDown);
            btnDown.setImage(AImage
                    .resizeBufferedImg((new ImageIcon(new URL(ressource
                    .getSurfacePath() + "/aurora/V1/resources/" + urlDown))
                    .getImage()), w, h));
            btnOver.setImage(AImage
                    .resizeBufferedImg((new ImageIcon(new URL(ressource
                    .getSurfacePath() + "/aurora/V1/resources/" + urlOver))
                    .getImage()), w, h));
            btnNormal.setImage(AImage
                    .resizeBufferedImg((new ImageIcon(new URL(ressource
                    .getSurfacePath() + "/aurora/V1/resources/" + urlNormal))
                    .getImage()), w, h));


        } catch (MalformedURLException ex) {
            try {
                btnOver.setImage(AImage
                        .resizeBufferedImg((new ImageIcon(getClass()
                        .getResource("/aurora/V1/resources/" + urlOver))
                        .getImage()), w, h));
                btnNormal.setImage(AImage
                        .resizeBufferedImg((new ImageIcon(getClass()
                        .getResource("/aurora/V1/resources/" + urlNormal))
                        .getImage()), w, h));
                btnDown.setImage(AImage
                        .resizeBufferedImg((new ImageIcon(getClass()
                        .getResource("/aurora/V1/resources/" + urlDown))
                        .getImage()), w, h));

            } catch (Exception exx) {
                logger.error(exx);
            }
        }

        setButton();
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
                RenderingHints.VALUE_COLOR_RENDER_QUALITY);

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        super.paintComponent(g2d);
    }

    /**
     * Prepares the button by setting the images to the proper states as well as
     * setting the Button to fully transparent with no borders
     *
     */
    private void setButton() {

        //Resource Manager

        ressource = new ASurface("");

        //set image states

        this.setIcon(btnNormal);
        this.setRolloverIcon(btnOver);
        this.setSelectedIcon(btnDown);
        this.setPressedIcon(btnDown);

        //remove backround and borders
        this.setBorderPainted(false);
        this.setFocusPainted(false);
        this.setOpaque(false);
        this.setContentAreaFilled(false);
    }

    public void setButtonSize(int width, int height) {
        this.w = width;
        this.h = height;
        try {
            btnOver.setImage(AImage
                    .resizeBufferedImg((new ImageIcon(new URL(ressource
                    .getSurfacePath() + "/aurora/V1/resources/" + urlOver))
                    .getImage()), w, h));
            btnNormal.setImage(AImage
                    .resizeBufferedImg((new ImageIcon(new URL(ressource
                    .getSurfacePath() + "/aurora/V1/resources/" + urlNormal))
                    .getImage()), w, h));
            btnDown.setImage(AImage
                    .resizeBufferedImg((new ImageIcon(new URL(ressource
                    .getSurfacePath() + "/aurora/V1/resources/" + urlDown))
                    .getImage()), w, h));

        } catch (MalformedURLException ex) {
            try {
                btnOver.setImage(AImage
                        .resizeBufferedImg((new ImageIcon(getClass()
                        .getResource("/aurora/V1/resources/" + urlOver))
                        .getImage()), w, h));
                btnNormal.setImage(AImage
                        .resizeBufferedImg((new ImageIcon(getClass()
                        .getResource("/aurora/V1/resources/" + urlNormal))
                        .getImage()), w, h));
                btnDown.setImage(AImage
                        .resizeBufferedImg((new ImageIcon(getClass()
                        .getResource("/aurora/V1/resources/" + urlDown))
                        .getImage()), w, h));

            } catch (Exception exx) {
                logger.error(exx);
            }
        }

    }
}
