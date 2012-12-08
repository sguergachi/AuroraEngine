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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 *
 * @author Sammy
 */
public class AProgressWheel extends JPanel implements Runnable {

    private Thread runner;
    private String URL;
    private AImage img;
    private int rotate = 0;
    private int speed = 15;
    private boolean isClockwiseRotating = true;

    ;

    public AImage getImg() {
        return img;
    }

    public AProgressWheel(String URL) {

        this.URL = URL;
        img = new AImage(this.URL);
        setOpaque(false);

        this.setPreferredSize(new Dimension(img.getImgIcon().getIconWidth(), img.getImgIcon().getIconHeight()));
        start();

    }

    public AProgressWheel(String URL, int speed) {

        this.URL = URL;
        img = new AImage(this.URL);
        setOpaque(false);

        this.speed = speed;
        this.setPreferredSize(new Dimension(img.getImgIcon().getIconWidth(), img.getImgIcon().getIconHeight()));
        start();

    }

    @Override
    public void run() {
        while (runner == Thread.currentThread()) {

            this.repaint();
            try {
                Thread.sleep(16);
            } catch (InterruptedException ex) {
                Logger.getLogger(AProgressWheel.class.getName()).log(Level.SEVERE, null, ex);
            }

        }


    }

    public void stop() {
        this.setVisible(false);

    }

    public void setSpeed(int Speed) {
        this.speed = Speed;
    }

    public void setClockwise(boolean Clockwise) {

        this.isClockwiseRotating = Clockwise;

    }

    @Override
    public void paint(Graphics g) {
        super.paintComponent(g);

        rotate += speed - 8;

        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);


        AffineTransform origXform = g2d.getTransform();
        AffineTransform newXform = (AffineTransform) (origXform.clone());


        //center of rotation is center of the panel
        int xRot = this.getWidth() / 2;
        int yRot = this.getHeight() / 2;
        if (isClockwiseRotating && rotate > 0) {
            newXform.rotate(Math.toRadians(rotate), xRot, yRot);
        } else if (!isClockwiseRotating && rotate > 0) {
            newXform.rotate(-Math.toRadians(rotate), xRot, yRot);
        }
        g2d.setTransform(newXform);

        //draw image centered in panel
        int x = (getWidth() - img.getImgIcon().getIconWidth()) / 2;
        int y = (getHeight() - img.getImgIcon().getIconHeight()) / 2;
        g2d.drawImage(img.getImgIcon().getImage(), x, y, this);
        g2d.setTransform(origXform);

    }

    private void start() {
        if (runner == null) {
            runner = new Thread(this);
        }
        runner.start();
    }
}
