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

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

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

    private int imageHeight;

    private int imageWidth;

    private Thread runner;

    private int Xpos;

    private int Xpos2;

    private int Xpos3;

    private int Ypos;

    private boolean isCentered;

    private boolean stop = false;

    private Graphics g;

    private int border = 25;

    private int block = 65;

    private boolean pause;

    private boolean lock;

    public AScrollingImage(String URL, int ImageWidth, int ImageHeight) {

        super(true);

        imgURL = URL;

        loopImage1 = new AImage(imgURL);
        loopImage2 = new AImage(imgURL);
        loopImage3 = new AImage(imgURL);

//        loopImage1 = new ImageIcon(getClass().getResource("/aurora/V1/resources/" + imgURL));
//        loopImage2 = new ImageIcon(getClass().getResource("/aurora/V1/resources/" + imgURL));
//        loopImage3 = new ImageIcon(getClass().getResource("/aurora/V1/resources/" + imgURL));

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

    public void StartLoop() {
        if (runner == null) {
            runner = new Thread(this);
        }
        runner.start();
        Ypos = loopImage2.getImgIcon().getIconHeight();
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
                    Logger.getLogger(AScrollingImage.class.getName()).log(
                            Level.SEVERE, null, ex);
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

    public void grow(int size) {

        Xpos = Xpos - (size + 40) / 3 - 2;
        Ypos = Ypos - size / 7 + 1;
        imageHeight = imageHeight + size / 3 + 4;
        imageWidth = imageWidth + (size + 40) + 2;
        block = block + 2;
        border = border + imageWidth;

    }

    public void setCenterToFrame(JFrame frame) {

        Xpos = frame.getSize().width / 9;
        loopImage2.setVisible(false);
        loopImage3.setVisible(false);

        Xpos2 = Xpos2 - loopImage2.getImgWidth();
        Xpos3 = Xpos3 - loopImage3.getImgWidth();
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
            g2d.drawImage(loopImage2.getImgIcon().getImage(), Xpos2 + border,
                    Ypos, imageWidth, imageHeight, this);
            //Draw setCenterToFrame Image
            g2d.drawImage(loopImage1.getImgIcon().getImage(), Xpos, Ypos,
                    imageWidth, imageHeight, this);
            //Draw Right Image
            g2d.drawImage(loopImage3.getImgIcon().getImage(), Xpos3 - border,
                    Ypos, imageWidth, imageHeight, this);
        }


        animate();
        g2d.dispose();

    }

    private void animate() {
        //move X cordinate to animate
        if (!pause) {
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
        return Ypos;
    }

    public void setYpos(int ypos) {
        this.Ypos = ypos;
    }
}
