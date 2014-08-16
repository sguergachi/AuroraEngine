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

import java.awt.Graphics;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.swing.JComponent;
import javax.swing.Timer;
import org.apache.log4j.Logger;

/**
 *
 * @author Sammy
 * @version 0.2
 */
public class AAnimate implements Runnable {

    private Thread runner;

    private float Alpha;

    private int AnimationID;

    private JComponent component;

    private Graphics g;

    private APostHandler postAnimate;

    private final ArrayList<APostHandler> postListenerList;

    private int x = 0;

    private int y = 0;

    private int XPos;

    private int speed;

    private int YPos;

    private int YSpeed;

    private int XSpeed;

    private boolean animating = false;

    private int acc;

    private float alphaAcc;

    static final Logger logger = Logger.getLogger(AAnimate.class);

    private Window frame;
    private Timer animation;

    public static final int fps = 60;
    private boolean allowVisibleNow = true;
    private int tick;

    public AAnimate(JComponent component) {
        this.postListenerList = new ArrayList<>();
        this.component = component;
        this.component.setDoubleBuffered(true);
        this.component.setVisible(false);
    }

    public AAnimate() {
        this.postListenerList = new ArrayList<>();
    }

    public AAnimate(JComponent component, APostHandler e) {
        this.postListenerList = new ArrayList<>();
        this.component = component;
        this.component.setDoubleBuffered(true);
        this.postAnimate = e;
        this.component.setVisible(false);
    }

    public AAnimate(Window frame) {
        this.frame = frame;
        this.postListenerList = new ArrayList<>();
        this.frame.setVisible(false);
    }

    public void setInitialLocation(int x, int y) {

        this.x = x;
        this.y = y;
        if (component != null) {
            this.component.setBounds(x, y, component.getWidth(), component
                                     .getHeight());
            this.component.setLocation(x, y);

        } else {
            frame.setLocation(x, y);
        }

    }

    public void addPostAnimationListener(APostHandler e) {
        this.postAnimate = e;
    }

    public void appendPostAnimationListener(APostHandler e) {
        postListenerList.add(e);
    }

    public boolean isAnimating() {
        return animating;
    }

    public void setComponent(JComponent component) {
        this.component = component;
        this.component.setDoubleBuffered(true);
    }

    /**
     * Fades in a component. not functioning
     *
     * EFFECT ID = 0;
     *
     * @param component
     */
    public void fadeIn(Window component) {
        //Get Graphics from Commponent

        this.frame = component;

        Alpha = 0.0f;

        //start fade effect
        AnimationID = 0;
        start();

    }

    /**
     * Fades out a component. not functioning
     *
     * EFFECT ID = 1;
     *
     * @param component
     */
    public void fadeOut(Window component) {
        //Get Graphics from Commponent

        this.frame = component;

        Alpha = 1.0f;

        //Enable Anti-Alias
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
        this.speed = MoveSpeed;
        if (XPos < 0) {
            speed = (-1 * speed);

        }
        AnimationID = 2;

        if (logger.isDebugEnabled()) {
            logger.debug("move horz with X at: " + x);
        }

        start();
    }

    public void moveVertical(int YPosition, int MoveSpeed) {
        this.YPos = YPosition;
        this.speed = MoveSpeed - 10;

        AnimationID = 3;
        start();
    }

    public void moveDiagonal(int XPosition, int YPosition, int XMoveSpeed,
                             int YMoveSpeed) {
        this.YPos = YPosition;
        this.XPos = XPosition;
        this.YSpeed = YMoveSpeed;
        this.XSpeed = XMoveSpeed;

        AnimationID = 4;
        start();
    }

    public void start() {
        tick = 0;

        // Method A
        runner = null;
        runner = new Thread(this);
        runner.start();

        if (logger.isDebugEnabled()) {
            logger.debug("Running animation");
        }

        // Method B
//        runAnimation();
    }

    public void setAllowVisibleNow(boolean allowVisibleNow) {
        this.allowVisibleNow = allowVisibleNow;
    }

    public void runAnimation() {
        int DELAY = 1000 / fps;
        if (animation == null) {
            animation = new Timer(DELAY, new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (animating == false) {
                        Timer timer = ((Timer) e.getSource());
                        timer.stop();
                        runner = null;
                        if (component != null) {
                            component.setLocation(x, y);
                            component.revalidate();
                        } else {
                            frame.setLocation(x, y);
                            frame.revalidate();
                        }
                        doneAnimation();
                    }

                } // animation loop
            }); // end timer action listener
        }
        animation.start();
        animation.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {


                Timer timer = ((Timer) e.getSource());
                if (timer.isRunning()) {
                    animating = true;
                }
                tick++;
                if (AnimationID == 0) {


                    //Decrease Alpha
                    Alpha += (0.05F + alphaAcc);
                    frame.repaint();
                    frame.revalidate();

                    frame.setOpacity(Alpha);

                    if (Alpha > 0.9F) {
                        Alpha = 1F;
                        frame.setOpacity(Alpha);
                        animating = false;


                    }

                    alphaAcc = alphaAcc + 0.01f;

                } else if (AnimationID == 1) {


                    //Decrease Alpha
                    Alpha -= (0.05F + alphaAcc);
                    frame.repaint();
                    frame.revalidate();

                    frame.setOpacity(Alpha);

                    if (Alpha < 0.05F) {
                        Alpha = 0.0F;
                        frame.setOpacity(Alpha);
                        animating = false;

                    }

                    //HORIZONTAL
                } else if (AnimationID == 2) {

                    x += (speed) + acc;
                    y = component.getLocation().y;
                    component.setBounds(x, y, component
                                        .getWidth(), component.getHeight());


                    if (speed > 0) {

                        if (component.getLocation().x >= XPos) {
                            animating = false;


                        }

                        if (component.getLocation().x > XPos / 2) {

                            acc--;
                            acc--;
                        } else if (component.getLocation().x < XPos / 2) {

                            acc++;
                        }

                    } else {
                        if (component.getLocation().x <= XPos) {
                            animating = false;


                        }

                        if (component.getLocation().x <= XPos / 2) {

                            acc++;
                            acc++;
                        } else if (component.getLocation().x >= XPos / 2) {

                            acc--;
                        }
                    }

                    //VERTICAL
                } else if (AnimationID == 3) {
                    if (YPos >= 0 && speed > 0) {
                        y = y + speed;
                        x = component.getLocation().x;
                        if (YPos <= y) {
                            animating = false;

                        }
                        if (component != null) {
                            component.setBounds(x, y,
                                                component
                                                .getWidth(), component.getHeight());
                        } else {
                            frame.setLocation(x, y);
                        }
                    } else {
                        if (speed < 0) {
                            y = y + speed;
                        } else {
                            y = y - speed;
                        }

                        if (component != null) {
                            component.setBounds(x, y,
                                                component
                                                .getWidth(), component.getHeight());
                        } else {
                            frame.setLocation(x, y);
                        }

                        if (YPos >= y) {
                            animating = false;


                        }

                        if (speed < 0) {
                            if (YPos >= y + speed) {
                                speed = -(y - YPos);
                            }
                        } else {
                            if (YPos >= y - speed) {
                                speed = YPos - y;
                            }
                        }

                    }
                    if (component != null) {
                        component.repaint();
                    } else {
                        frame.repaint();
                    }

                    //DIAGONAL
                } else if (AnimationID == 4) {
                    if (YPos > 0 || XPos > 0) {
                        y += YSpeed;
                        x += XSpeed;
                    } else {
                        y -= YSpeed;
                        x -= XSpeed;
                    }

                    //if one or the other positions attained wait untill other
                    //axis is attained
                    if (component.getLocation().y >= YPos) {
                        y = YPos;
                    } else if (component.getLocation().x >= XPos) {
                        x = XPos;
                    }

                    if (component.getLocation().x >= YPos
                        && component.getLocation().y >= YPos) {
                        animating = false;

                    }

                    component.setBounds(x, y, component.getWidth(), component
                                        .getHeight());
                }


                alphaAcc += 0.01f;


                if (component != null) {
                    component.revalidate();
                    if ((tick == 2 || allowVisibleNow)) {
                        component.setVisible(true);
                    }

                    if (!allowVisibleNow && tick == 2) {
                        AThreadWorker repaint = new AThreadWorker(new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {

                                component.setBounds(x, y,
                                                    component
                                                    .getWidth(), component.getHeight());
                                component.paintImmediately(component.getBounds());
                                component.repaint();

                                component.setVisible(true);


                                try {
                                    Thread.sleep(tick);
                                } catch (InterruptedException ex) {
                                    java.util.logging.Logger.getLogger(AAnimate.class.getName()).log(Level.SEVERE, null, ex);
                                }


                                component.setBounds(x, y,
                                                    component
                                                    .getWidth(), component.getHeight());
                                component.paintImmediately(component.getBounds());
                                component.repaint();
                            }
                        });

                        repaint.startOnce();
                    }

                } else {
                    frame.repaint();
                    frame.setVisible(true);
                }



            }

        });




    }

    public void run() {
        int DELAY = 1000 / fps;

        while (runner == Thread.currentThread()) {
            animating = true;
            tick++;
            if (AnimationID == 0) {

                if (logger.isDebugEnabled()) {
                    logger.debug("Alpha of Animation: " + Alpha);
                }

                //Decrease Alpha
                Alpha += (0.05F + alphaAcc);
                frame.repaint();
                frame.revalidate();

                frame.setOpacity(Alpha);

                if (Alpha > 0.9F) {
                    Alpha = 1F;
                    frame.setOpacity(Alpha);
                    break;
                }

                alphaAcc = alphaAcc + 0.01f;

            } else if (AnimationID == 1) {

                if (logger.isDebugEnabled()) {
                    logger.debug("Alpha of Animation: " + Alpha);
                }

                //Decrease Alpha
                Alpha -= (0.05F + alphaAcc);
                frame.repaint();
                frame.revalidate();

                frame.setOpacity(Alpha);

                if (Alpha < 0.05F) {
                    Alpha = 0.0F;
                    frame.setOpacity(Alpha);
                    break;
                }

                //HORIZONTAL
            } else if (AnimationID == 2) {

                x += (speed) + acc;
                y = component.getLocation().y;
                component.setBounds(x, component.getLocation().y, component
                                    .getWidth(), component.getHeight());

                if (speed > 0) {

                    if (component.getLocation().x >= XPos) {
                        break;
                    }

                    if (component.getLocation().x > XPos / 2) {

                        acc--;
                        acc--;
                    } else if (component.getLocation().x < XPos / 2) {

                        acc++;
                    }

                } else {
                    if (component.getLocation().x <= XPos) {
                        break;
                    }

                    if (component.getLocation().x <= XPos / 2) {

                        acc++;
                        acc++;
                    } else if (component.getLocation().x >= XPos / 2) {

                        acc--;
                    }
                }

                //VERTICAL
            } else if (AnimationID == 3) {
                if (YPos >= 0 && speed > 0) {
                    y = y + speed;
                    x = component.getLocation().x;
                    if (YPos <= y) {
                        break;
                    }
                    if (component != null) {
                        component.setBounds(x, y,
                                            component
                                            .getWidth(), component.getHeight());
                    } else {
                        frame.setLocation(x, y);
                    }
                } else {
                    if (speed < 0) {
                        y = y + speed;
                    } else {
                        y = y - speed;
                    }

                    if (component != null) {
                        component.setBounds(component.getLocation().x, y,
                                            component
                                            .getWidth(), component.getHeight());
                    } else {
                        frame.setLocation(x, y);
                    }

                    if (YPos >= y) {
                        break;
                    }

                    if (speed < 0) {
                        if (YPos >= y + speed) {
                            speed = -(y - YPos);
                        }
                    } else {
                        if (YPos >= y - speed) {
                            speed = YPos - y;
                        }
                    }

                }
                if (component != null) {
                    component.repaint();
                } else {
                    frame.repaint();
                }

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

                if (component.getLocation().x >= YPos
                    && component.getLocation().y >= YPos) {
                    break;
                }

                component.setBounds(x, y, component.getWidth(), component
                                    .getHeight());

            }



            alphaAcc = alphaAcc + 0.01f;


            if (component != null) {
                this.component.repaint();
                if (!component.isVisible() && (tick == 3 || allowVisibleNow)) {
                    component.setVisible(true);
                }

                if (!allowVisibleNow && tick == 3) {
                    AThreadWorker repaint = new AThreadWorker(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {

                            component.setBounds(x, component.getLocation().y,
                                                component
                                                .getWidth(), component.getHeight());
                            component.paintImmediately(component.getBounds());
                            component.repaint();

                            component.setVisible(true);


                            try {
                                Thread.sleep(tick);
                            } catch (InterruptedException ex) {
                                java.util.logging.Logger.getLogger(AAnimate.class.getName()).log(Level.SEVERE, null, ex);
                            }


                            component.setBounds(x, component.getLocation().y,
                                                component
                                                .getWidth(), component.getHeight());
                            component.paintImmediately(component.getBounds());
                            component.repaint();
                        }
                    });

                    repaint.startOnce();
                }

            } else {
                frame.repaint();
                frame.setVisible(true);
            }


            //pause
            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException ex) {
                logger.error(ex);
            }
        }


        animating = false;
        runner = null;
        if (component != null) {
            this.component.setLocation(x, y);
            component.revalidate();

        } else {
            this.frame.setLocation(x, y);
            this.frame.revalidate();
        }
        doneAnimation();

    } // end run()

    ///Run when animation is complete
    private void doneAnimation() {
        if (postAnimate != null) {
            postAnimate.doAction();
        }

        if (postListenerList.size() != 0) {
            for (int i = 0; i < postListenerList.size(); i++) {
                postListenerList.get(i).doAction();
            }

            postListenerList.clear();
        }
    }

    public void removeAllListeners() {
        this.postAnimate = null;
    }
}
