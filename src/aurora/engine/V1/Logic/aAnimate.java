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


package aurora.engine.V1.Logic;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 *
 * @author Sammy
 * @version 0.2
 */
public class aAnimate implements Runnable {

    private Thread runner;
    private float Alpha;
    private int AnimationID;
    private Graphics2D g2d;
    private JComponent component;
    private JComponent container;
    private JFrame frame;
    private Graphics g;
    private aPostHandler e;
    private int x = 0;
    private int y = 0;
    private int XPos;
    private int speed;
    private int YPos;
    private int YSpeed;
    private int XSpeed;
    private boolean Animating = false;

    public aAnimate(JComponent component) {
        this.component = component;
        this.component.setDoubleBuffered(true);
        this.component.setVisible(false);
    }

    public aAnimate() {
    }

    public aAnimate(JComponent component, aPostHandler e) {
        this.component = component;
        this.component.setDoubleBuffered(true);
        this.e = e;
        this.component.setVisible(false);
    }

    public void setInitialLocation(int x, int y) {

        this.x = x;
        this.y = y;
        this.component.setBounds(x, y, component.getWidth(), component.getHeight());

    }
    public void addPostAnimationListener(aPostHandler e){
        this.e = e;
    }

    public boolean isAnimating() {
        return Animating;
    }

    public void setComponent(JComponent component) {
        this.component = component;
        this.component.setDoubleBuffered(true);
    }

    /**
     * Fades in a component
     *
     * EFFECT ID = 0;
     *
     */
    public void fadeIn(JComponent component) {
        this.component = component;


        Alpha = 0.2F;


        //Get Graphics from Commponent
        g = this.component.getGraphics();
        g2d = (Graphics2D) g;

        //Enable Anti-Alias
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);


        //start fade effect
        AnimationID = 0;
        start();

    }

    public void fadeIn(JComponent commponent, JFrame container) {
        this.component = commponent;
        this.frame = container;

        Alpha = 0.2F;


        //Get Graphics from Commponent
        g = this.component.getGraphics();
        //
        g2d = (Graphics2D) g;


        //Enable Anti-Alias and quality
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);


        //start fade effect
        AnimationID = 0;
        start();

    }

    /**
     * Fades out a component
     *
     * EFFECT ID = 1;
     *
     */
    public void fadeOut(JComponent component) {
        //Get Graphics from Commponent
        //TODO NEED TO SUBCLASS AND OVERRIDE TO MAKE FADE EFFECT

        this.component = component;

        g = this.component.getGraphics();

        g2d = (Graphics2D) g;
        Alpha = 1.0f;

        //Enable Anti-Alias
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        AnimationID = 1;
        start();
    }

    /**
     * Moves Component Horizontally Vertically and Diagonally
     *
     * EFFECT ID = 2,3,4;
     *
     */
    public void moveHorizontal(int XPosition, int MoveSpeed) {
        this.XPos = XPosition;
        this.speed = MoveSpeed - 20;
        if (XPos < 0) {
            speed = (-1 * speed) - 30;

        }
        AnimationID = 2;
        System.out.println("move horz with X at: " + x);
        start();
    }

    public void moveVertical(int YPosition, int MoveSpeed) {
        this.YPos = YPosition;
        this.speed = MoveSpeed - 10;

        AnimationID = 3;
        start();
    }

    public void moveDiagonal(int XPosition, int YPosition, int XMoveSpeed, int YMoveSpeed) {
        this.YPos = YPosition;
        this.XPos = XPosition;
        this.YSpeed = YMoveSpeed;
        this.XSpeed = XMoveSpeed;

        AnimationID = 4;
        start();
    }

    public void start() {
        runner = null;
        runner = new Thread(this);
        runner.start();


        System.out.println("Running Animation");

    }

    public void run() {

        while (runner == Thread.currentThread()) {
            Animating = true;

            if (AnimationID == 0) {
                g2d.setComposite(makeComposite(Alpha));


                System.out.println("Alpha " + Alpha);

                component.paintComponents(g2d);
                component.paint(g2d);
                component.update(g2d);
                component.repaint();
                component.revalidate();
                if (Alpha == 1.0F) {
                    break;
                }
                Alpha += 0.2F;

            } else if (AnimationID == 1) {
                System.out.println("Alpha of Animation: " + Alpha);
                g2d.setComposite(makeComposite(Alpha));
                //Decrease Alpha
                Alpha -= 0.1F;
                component.repaint();
                component.revalidate();



                //if alpha is nothing then render invisible
                if (Alpha < 0.05F) {
                    //commponent.setVisible(false);
                    Alpha = 0.0F;
                    g2d.setComposite(makeComposite(Alpha));

                    break;

                }

                //HORIZONTAL
            } else if (AnimationID == 2) {


                x = x + (speed);

                component.setBounds(x, component.getLocation().y, component.getWidth(), component.getHeight());

                if (speed > 0) {

                    if (component.getLocation().x >= XPos) {
                        // component.setVisible(false);
                        break;
                    }
                } else {
                    if (component.getLocation().x <= XPos) {
                        // component.setVisible(false);
                        break;
                    }
                }
                this.component.setVisible(true);


                //VERTICAL
            } else if (AnimationID == 3) {
                if (YPos >= 0) {
                    y = y + speed;
                    if (YPos <= y) {
                        break;
                    }
                    component.setBounds(component.getLocation().x, y, component.getWidth(), component.getHeight());
                } else {
                    y = y - speed;
                    if (YPos >= y) {
                        break;
                    }
                    component.setBounds(component.getLocation().x, y, component.getWidth(), component.getHeight());
                }

                
                component.repaint();
                System.out.println("bool : " + (YPos <= component.getLocation().y));




                //DIAGONAL
            } else if (AnimationID == 4) {
                if (YPos > 0 || XPos > 0) {
                    y = y + YSpeed;
                    x = x + XSpeed;
                } else {
                    y = y - YSpeed;
                    x = x - XSpeed;
                }


                //if one or the other positions attained wait untill other 
                //axis is attained       
                if (component.getLocation().y >= YPos) {
                    y = YPos;
                } else if (component.getLocation().x >= XPos) {
                    x = XPos;
                }

                if (component.getLocation().x >= YPos && component.getLocation().y >= YPos) {
                    break;
                }

                component.setBounds(x, y, component.getWidth(), component.getHeight());


            }

            //pause
            try {
                Thread.sleep(16);
            } catch (InterruptedException ex) {
                Logger.getLogger(aAnimate.class.getName()).log(Level.SEVERE, null, ex);
            }

            System.out.println("X Val: " + x);
            System.out.println("Y Val: " + y);
            this.component.repaint();
            this.component.setVisible(true);

        }
        Animating = false;
        runner = null;
        this.component.setLocation(x, y);
        this.component.revalidate();

        doneAnimation();

    }

    private AlphaComposite makeComposite(float alpha) {
        int type = AlphaComposite.SRC_OVER;
        return (AlphaComposite.getInstance(type, alpha));
    }

    ///Run when animation is complete
    private void doneAnimation() {
        if (e != null) {
            e.actionPerformed();
        }
    }

    public void removeAllListeners() {
        this.e = null;
    }
}
