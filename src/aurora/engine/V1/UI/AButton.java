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

    String urlNorm;

    String urlOver;

    String urlDown;

    private ImageIcon btnNormal;

    private ImageIcon btnOver;

    private ImageIcon btnDown;

    private int btnWidth;

    private int btnHeight;

    static final Logger logger = Logger.getLogger(AButton.class);

    private ASurface ressource;

    //////////////////////
    ///  Constructor    //
    //////////////////////
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
        this.urlNorm = ImgUp;
        this.urlOver = ImgOvr;
        this.urlDown = ImgDwn;

        // uses default image size
        this.btnWidth = 0;
        this.btnHeight = 0;

        setButton(true);
    }

    /**
     * Sets up the imageIcons to be painted on button and allows a string to be
     * placed on top of the button
     *
     * @param name
     * @param ImgNorm
     * @param ImgDwn
     * @param ImgOvr
     *
     */
    public AButton(String ImgNorm, String ImgDwn, String ImgOvr) {
        this.urlNorm = ImgNorm;
        this.urlOver = ImgOvr;
        this.urlDown = ImgDwn;

        // uses default image size
        this.btnWidth = 0;
        this.btnHeight = 0;

        setButton(false);
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

        this.btnWidth = width;
        this.btnHeight = height;


        this.urlNorm = ImgUp;
        this.urlOver = ImgOvr;
        this.urlDown = ImgDwn;


        setButton(true);
    }

    /**
     *
     * Prepares the button by setting the images to the proper states as well as
     * setting the Button to fully transparent with no borders.
     *
     * @param useSize Boolean
     */
    private void setButton(final Boolean useSize) {

        // Resource Manager //
        ressource = new ASurface("");

        // Set up images for buttons //



        try {

            btnNormal = new ImageIcon(new URL(ressource.getSurfacePath()
                                              + "/aurora/V1/resources/"
                                              + urlNorm));
            btnOver = new ImageIcon(new URL(ressource.getSurfacePath()
                                            + "/aurora/V1/resources/"
                                            + urlOver));
            btnDown = new ImageIcon(new URL(ressource.getSurfacePath()
                                            + "/aurora/V1/resources/"
                                            + urlDown));
            if (useSize) {

                // width or height of 0 means use default values //
                if (btnWidth == 0) {
                    btnWidth = btnNormal.getIconWidth();

                }
                if (btnHeight == 0) {
                    btnHeight = btnNormal.getIconHeight();
                }

                btnNormal.setImage(AImage
                        .resizeBufferedImg((new ImageIcon(new URL(ressource
                        .getSurfacePath() + "/aurora/V1/resources/" + urlNorm))
                        .getImage()), btnWidth, btnHeight));
                btnOver.setImage(AImage
                        .resizeBufferedImg((new ImageIcon(new URL(ressource
                        .getSurfacePath() + "/aurora/V1/resources/" + urlOver))
                        .getImage()), btnWidth, btnHeight));
                btnDown.setImage(AImage
                        .resizeBufferedImg((new ImageIcon(new URL(ressource
                        .getSurfacePath() + "/aurora/V1/resources/" + urlDown))
                        .getImage()), btnWidth, btnHeight));
            }



        } catch (MalformedURLException ex) {
            try {
                btnNormal = new ImageIcon(getClass()
                        .getResource("/aurora/V1/resources/"
                                     + urlNorm));
                btnOver = new ImageIcon(getClass()
                        .getResource("/aurora/V1/resources/"
                                     + urlOver));
                btnDown = new ImageIcon(getClass()
                        .getResource("/aurora/V1/resources/"
                                     + urlDown));
                if (useSize) {

                    // width or height of 0 means use default values //
                    if (btnWidth == 0) {
                        btnWidth = btnNormal.getIconWidth();

                    }
                    if (btnHeight == 0) {
                        btnHeight = btnNormal.getIconHeight();
                    }

                    btnNormal.setImage(AImage
                            .resizeBufferedImg((new ImageIcon(getClass()
                            .getResource("/aurora/V1/resources/" + urlNorm))
                            .getImage()), btnWidth, btnHeight));
                    btnOver.setImage(AImage
                            .resizeBufferedImg((new ImageIcon(getClass()
                            .getResource("/aurora/V1/resources/" + urlOver))
                            .getImage()), btnWidth, btnHeight));

                    btnDown.setImage(AImage
                            .resizeBufferedImg((new ImageIcon(getClass()
                            .getResource("/aurora/V1/resources/" + urlDown))
                            .getImage()), btnWidth, btnHeight));
                }


            } catch (Exception exx) {
                logger.error(exx);
            }

            this.revalidate();
        }



        // Set image states //

        this.setIcon(btnNormal);
        this.setRolloverIcon(btnOver);
        this.setSelectedIcon(btnDown);
        this.setPressedIcon(btnDown);

        // Remove backround and borders //
        this.setBorderPainted(false);
        this.setFocusPainted(false);
        this.setOpaque(false);
        this.setContentAreaFilled(false);
    }

    public void setButtonStates(String ImgUp, String ImgDwn, String ImgOvr) {

        this.urlNorm = ImgUp;
        this.urlOver = ImgOvr;
        this.urlDown = ImgDwn;

        setButton(false);
    }

    public void setButtonSize(int width, int height) {
        this.btnWidth = width;
        this.btnHeight = height;
        try {
            btnOver.setImage(AImage
                    .resizeBufferedImg((new ImageIcon(new URL(ressource
                    .getSurfacePath() + "/aurora/V1/resources/" + urlOver))
                    .getImage()), btnWidth, btnHeight));
            btnNormal.setImage(AImage
                    .resizeBufferedImg((new ImageIcon(new URL(ressource
                    .getSurfacePath() + "/aurora/V1/resources/" + urlNorm))
                    .getImage()), btnWidth, btnHeight));
            btnDown.setImage(AImage
                    .resizeBufferedImg((new ImageIcon(new URL(ressource
                    .getSurfacePath() + "/aurora/V1/resources/" + urlDown))
                    .getImage()), btnWidth, btnHeight));

        } catch (MalformedURLException ex) {
            try {
                btnOver.setImage(AImage
                        .resizeBufferedImg((new ImageIcon(getClass()
                        .getResource("/aurora/V1/resources/" + urlOver))
                        .getImage()), btnWidth, btnHeight));
                btnNormal.setImage(AImage
                        .resizeBufferedImg((new ImageIcon(getClass()
                        .getResource("/aurora/V1/resources/" + urlNorm))
                        .getImage()), btnWidth, btnHeight));
                btnDown.setImage(AImage
                        .resizeBufferedImg((new ImageIcon(getClass()
                        .getResource("/aurora/V1/resources/" + urlDown))
                        .getImage()), btnWidth, btnHeight));

            } catch (Exception exx) {
                logger.error(exx);
            }
        }

    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
                RenderingHints.VALUE_COLOR_RENDER_QUALITY);

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        super.paintComponent(g2d);
    }
}
