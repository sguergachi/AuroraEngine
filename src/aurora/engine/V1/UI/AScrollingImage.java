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

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.apache.log4j.Logger;

/**
 *
 * @author Sammy
 * @version 0.2
 */
public class AScrollingImage extends JPanel implements Runnable {

    private String imgURL;

    private AImage loopImage1;

    private AImage loopImage2;

    private AImage loopImage3;

    private double imageHeight;

    private int imageWidth;

    private Thread runner;

    private int Xpos;

    private int Xpos2;

    private int Xpos3;

    private double Ypos;

    private boolean isCentered;

    private boolean stop = false;

    private Graphics g;

    private int border = 25;

    private int block = 65;

    private boolean pause;

    private boolean lock;

    static final Logger logger = Logger.getLogger(AScrollingImage.class);

    public AScrollingImage(String URL, int ImageWidth, int ImageHeight) {

        super(true);

        imgURL = URL;

        loopImage1 = new AImage(imgURL);
        loopImage2 = new AImage(imgURL);
        loopImage3 = new AImage(imgURL);


        if (ImageWidth != 0 && ImageHeight != 0) {
            loopImage1.setImg(new ImageIcon(AImage.resizeImage(loopImage1
                    .getImgIcon().getImage(), ImageWidth, ImageHeight)));
            loopImage2.setImg(new ImageIcon(AImage.resizeImage(loopImage1
                    .getImgIcon().getImage(), ImageWidth, ImageHeight)));
            loopImage3.setImg(new ImageIcon(AImage.resizeImage(loopImage3
                    .getImgIcon().getImage(), ImageWidth, ImageHeight)));
        }
        imageHeight = loopImage1.getImgIcon().getIconHeight();
        imageWidth = loopImage1.getImgIcon().getIconWidth();



        Xpos2 = 0 - loopImage2.getImgIcon().getIconWidth();
        Xpos3 = 0 + loopImage3.getImgIcon().getIconWidth();
        Xpos = 0;
        setOpaque(false);

    }

    public void StartLoop(int height) {
        if (runner == null) {
            runner = new Thread(this);
        }
        runner.start();
        Ypos = (int) (height / 2 - imageHeight / 2);
    }

    @Override
    public void run() {
        while (runner == Thread.currentThread()) {
            if (!stop) {


                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        repaint();
                    }
                });

                try {

                    Thread.sleep(16);

                } catch (InterruptedException ex) {
                    logger.error(ex);
                }

            } else {
                break;
            }
        }
    }

    public boolean isCentered() {
        if (Xpos == 0 || Xpos2 == 0 - loopImage2.getImgIcon().getIconWidth()
            || Xpos3 == 0 + loopImage3.getImgIcon().getIconWidth()) {
            isCentered = true;
        } else {
            isCentered = false;
        }
        return isCentered;
    }

    public void stop() {
        stop = true;
    }

    public void pause() {
        pause = true;
    }

    public int getXpos() {
        return Xpos;


    }

    public void setXpos(int Xpos) {
        this.Xpos = Xpos;
    }

    public void setCenterToFrame(JFrame frame) {
        Ypos += 15;
        Xpos = (frame.getSize().width - loopImage1.getImgWidth()) / 2 - 15;
        loopImage2.setVisible(false);
        loopImage3.setVisible(false);

    }

    public void grow(int scale) {

        double ratio = ((double) imageWidth / imageHeight);
        imageWidth += scale;
        imageHeight = (imageHeight + ((double) scale / ratio));

        Xpos -= scale / 2;
        Ypos -= (((double) scale / ratio) / 2) - (scale / ratio) / 4;


    }

    @Override
    public void paintComponent(Graphics g) {

        update(g);
    }

    /*
     * Animates 3 identical images to seem like a single one, and Uses a Queue
     * System to seem like the image is going into a loop.
     */
    @Override
    public void update(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                             RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                             RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        if ((loopImage1.getImgIcon().getImage().getWidth(this) > 0)
            && (loopImage1.getImgIcon().getImage().getHeight(this) > 0)) {
            //Draw Left Image
            if (loopImage2.isVisible()) {
                g2d.drawImage(loopImage2.getImgIcon().getImage(), Xpos2 + border, (int) Ypos, imageWidth, (int) imageHeight, this);
            }
            if (loopImage1.isVisible()) //Draw setCenterToFrame Image
            {
                g2d.drawImage(loopImage1.getImgIcon().getImage(), Xpos, (int) Ypos,
                              imageWidth, (int) imageHeight, this);
            }
            if (loopImage3.isVisible()) //Draw Right Image
            {
                g2d.drawImage(loopImage3.getImgIcon().getImage(), Xpos3 - border, (int) Ypos, imageWidth, (int) imageHeight, this);
            }
        }


        animate();
        g2d.dispose();

    }

    private void animate() {
        //move X cordinate to animate
        if (!pause && !stop) {
            Xpos++;
            Xpos2++;
            Xpos3++;
        }

        //If Right image is off screen place it behind Left Image
        if (Xpos3 >= this.getWidth()) {
            Xpos3 = Xpos2 - loopImage3.getImgIcon().getIconWidth() + block;
        } //iF setCenterToFrame Image off screen place behind
        else if (Xpos >= this.getWidth()) {
            Xpos = Xpos3 - loopImage3.getImgIcon().getIconWidth();
        } //if Left Image off screen place behind setCenterToFrame Image
        else if (Xpos2 >= this.getWidth()) {
            Xpos2 = Xpos - loopImage3.getImgIcon().getIconWidth();
        }

    }

    public int getYpos() {
        return (int) Ypos;
    }

    public void setYpos(int ypos) {
        this.Ypos = ypos;
    }

    public double getImageHeight() {
        return imageHeight;
    }

    public int getImageWidth() {
        return imageWidth;
    }
}
