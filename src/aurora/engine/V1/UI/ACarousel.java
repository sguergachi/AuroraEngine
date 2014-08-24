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

import aurora.engine.V1.Logic.APostHandler;
import aurora.engine.V1.UI.ACarouselTitle.TitleType;
import java.awt.Point;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import javax.swing.JPanel;
import org.apache.log4j.Logger;

/**
 *
 * @author Sammy, Carlos
 * @version 0.4
 */
public class ACarousel extends JPanel implements Runnable {

    private String URL;

    private Point.Double centerPoint;

    private Point.Double leftPoint;

    private Point.Double rightPoint;

    private Point.Double sPoint;

    private double panelWidth;

    private int panelHeight;

    private int totalWidth;

    private Thread runLeft;

    private Thread runRight;

    private double centX;

    private double centY;

    private double leftX;

    private double leftY;

    private double rightX;

    private double rightY;

    private boolean DEBUG = false;

    private int counter;

    private ADialog err;

    //carlos
    private int numberOfPanes;

    private ArrayList<Point.Double> leftOffsets;

    private ArrayList<Point.Double> rightOffsets;

    private boolean isRunningLeft;

    private boolean isRunningRight;

    private ACarouselPane originalLeftPane;

    private ACarouselPane originalCenterPane;

    private ACarouselPane originalRightPane;

    private final double MAX_INCREMENT = 5.0;

    private final double MIN_INCREMENT = -5.0;

    private double multiplier = 3.6;

    private int delay = 16;

    private final static double MAX_Y_POINT = 200;	// maximum y-point for any pane

    private final static double MIN_Y_POINT = 0;	// minimum y-point for any pane

    static final Logger logger = Logger.getLogger(ACarousel.class);

    private APostHandler postLeftAnimate;
    private APostHandler postRightAnimate;
    private ACarouselPane nextCenterPane;
    private ACarouselPane centerPane;
    private ACarouselPane nextLeftPane;
    private ACarouselPane nextRightPane;

    public ACarousel(double panelWidth, int panelHeight, int totalWidth) {
        this.setLayout(null);
        this.setOpaque(false);
        this.setDoubleBuffered(true);
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
        this.totalWidth = totalWidth;
        numberOfPanes = 0;
        leftOffsets = new ArrayList<Double>();
        rightOffsets = new ArrayList<Double>();
        isRunningLeft = false;
        isRunningRight = false;
    }

    /**
     *
     * @param p
     */
    public void addPane(ACarouselPane p) {
        ACarouselPane pane = p;
        Point.Double point = calculatePosition();
        pane.setPoint(point.x, point.y);
        pane.setBounds((int) point.x, (int) point.y, (int) panelWidth,
                       panelHeight);
        numberOfPanes++;

        if (numberOfPanes == 1) {
            pane.setAsLeftPane();
            originalLeftPane = pane;
        } else if (numberOfPanes == 2) {
            pane.setAsCenterPane();
            originalCenterPane = pane;
        } else if (numberOfPanes == 3) {
            pane.setAsRightPane();
            originalRightPane = pane;
        }

        this.add(pane);
    }

    /**
     *
     * @return
     */
    private Point.Double calculatePosition() {

        Point.Double point = new Point.Double(0, 0);

        if (numberOfPanes == 0) {
            point.x = ((totalWidth - ((int) panelWidth * 3 + 25)) / 2) + 2;
            point.y = MAX_Y_POINT;
            leftX = point.x;
            if (logger.isDebugEnabled()) {
                logger.debug("leftX = " + leftX);
            }
        } else if (numberOfPanes == 1) {
            double prevPointX = ((ACarouselPane) this.getComponent(0))
                    .getPointX(); //getting previously added pane
            point.x = prevPointX + panelWidth;
            point.y = MIN_Y_POINT;
            centX = point.x;
            if (logger.isDebugEnabled()) {
                logger.debug("centX = " + centX);
            }
        } else if (numberOfPanes == 2) {
            double prevPointX = ((ACarouselPane) this.getComponent(1))
                    .getPointX();
            point.x = prevPointX + panelWidth;
            point.y = MAX_Y_POINT;
            rightX = point.x;
            if (logger.isDebugEnabled()) {
                logger.debug("rightX = " + rightX);
            }
        } else if (numberOfPanes == 3) {
            double prevPointX = ((ACarouselPane) this.getComponent(0))
                    .getPointX();
            point.x = prevPointX - panelWidth;
            point.y = MIN_Y_POINT;
            Point.Double rightOffsetPoint = new Point.Double(rightX + panelWidth,
                                                             point.y);
            leftOffsets.add(point);
            rightOffsets.add(rightOffsetPoint);
        } else if (numberOfPanes >= 4) {
            double prevPointX = ((ACarouselPane) this.getComponent(numberOfPanes
                                                                   - 1))
                    .getPointX();
            Point.Double lastRightOffsetPoint = rightOffsets.get((rightOffsets
                    .size() - 1));
            point.x = prevPointX - panelWidth;

            if (isOdd(numberOfPanes)) {
                point.y = MIN_Y_POINT;
            } else {
                point.y = MAX_Y_POINT;
            }

            Point.Double rightOffsetPoint = new Point.Double(lastRightOffsetPoint.x
                                                             + panelWidth,
                                                             point.y);
            leftOffsets.add(point);
            rightOffsets.add(rightOffsetPoint);
        }
        return point;
    }

    //CREATE AND SET OFF PANE
    /**
     *
     * @param p
     * @param x
     * @param i
     */
    private void setOffPane(ACarouselPane p, double x, double i) {

        ACarouselPane pane = p;
        double pointX = x;
        double pointY = i;

        Point.Double point = pane.getPoint();
        point.setLocation(pointX, pointY);
        pane.setPoint(pointX, pointY);
    }

    private void paint() {

        ACarouselPane cp = null;
        Point.Double p = null;

        for (int i = 0; i < this.getComponentCount(); i++) {
            cp = (ACarouselPane) this.getComponent(i);
            p = cp.getPoint();
            cp.setBounds((int) p.x, (int) p.y, (int) panelWidth, panelHeight);

        }
    }

    //MOVE METHODS
    public void MoveRight() {

        if (runLeft == null && runRight == null) {

            if (runRight == null) {
                runRight = new Thread(this);

            }

            isRunningRight = true;
            runLeft = null;
            try {
                if (logger.isDebugEnabled()) {
                    logger.debug("RUNNING RIGHT");
                }
                isRunningLeft = false;
                isRunningRight = true;
                runRight.start();
            } catch (IllegalThreadStateException e) {
                ADialog err = new ADialog(ADialog.aDIALOG_ERROR,
                                          "Animation Error!");
                err.setVisible(true);
            }
            if (postRightAnimate != null) {
                postRightAnimate.doAction();
            }
        }
    }

    public void MoveLeft() {


        if (runRight == null && runLeft == null) {
            if (runLeft == null) {
                runLeft = new Thread(this);
            }

            runRight = null;
            try {
                if (logger.isDebugEnabled()) {
                    logger.debug("RUNNING LEFT");
                }
                isRunningLeft = true;
                isRunningRight = false;
                runLeft.start();
            } catch (IllegalThreadStateException e) {
                err = new ADialog(ADialog.aDIALOG_ERROR, "Animation Error!");
                err.setVisible(true);
            }
            if (postLeftAnimate != null) {
                postLeftAnimate.doAction();
            }
        }
    }

    public void setPostLeftAnimation(APostHandler leftAction) {
        this.postLeftAnimate = leftAction;
    }

    public void setPostRightAnimation(APostHandler rightAction) {
        this.postRightAnimate = rightAction;
    }

    private void resetPanePositions() {

        ACarouselPane cp = null;
        Point.Double p = null;

        for (int i = 0; i < this.getComponentCount(); i++) {
            cp = (ACarouselPane) this.getComponent(i);
            p = cp.getPoint();
            cp.setBounds((int) p.x, (int) p.y, (int) panelWidth, panelHeight);

            if (p.x == leftX) {
                cp.setAsLeftPane();
            } else if (p.x == centX) {
                cp.setAsCenterPane();
            } else if (p.x == rightX) {
                cp.setAsRightPane();
            }
        }
    }

    double Incr1;
    double Incr2;

    @Override
    public void run() {

        counter = 0;

        centerPane = getCenterPane();

        if (centerPane.equals(originalCenterPane)) {
            Incr1 = MAX_INCREMENT;
            Incr2 = MIN_INCREMENT;
        } else if (centerPane.equals(originalLeftPane) || centerPane.equals(
                originalRightPane)) {
            Incr1 = MIN_INCREMENT;
            Incr2 = MAX_INCREMENT;
        } else {
            Incr1 = MAX_INCREMENT;
            Incr2 = MIN_INCREMENT;
        }

        Incr1 *= multiplier;
        Incr2 *= multiplier;

        if (isRunningLeft) {
            int index = rightOffsets.size() - 1; //getting last index in the rightOffsets array list

            ACarouselPane pane;

            for (int i = 0; i < numberOfPanes; i++) {
                pane = (ACarouselPane) this.getComponent(i);
                if (pane.getPointX() < leftX) { //check to see if there are any panes to the left of the left pane
                    Point.Double rightOffsetPoint = rightOffsets.get(index);
                    setOffPane(pane, rightOffsetPoint.x, rightOffsetPoint.y);
                    index = index - 1;
                    break;
                }
            }
        }

        if (isRunningRight) {
            int index = leftOffsets.size() - 1; //getting last index in the leftOffsets array list

            ACarouselPane pane;

            for (int i = 0; i < numberOfPanes; i++) {
                pane = (ACarouselPane) this.getComponent(i);
                if (pane.getPointX() > rightX) { //check to see if there are any panes to the left of the left pane
                    Point.Double leftOffsetPoint = leftOffsets.get(index);
                    setOffPane(pane, leftOffsetPoint.x, leftOffsetPoint.y);
                    index = index - 1;
                    break;
                }
            }
        }



        ///////.....LEFT
        boolean isCarouselMoving = false;
        double xMov = 0;
        double yMov = 0;
        while (runLeft == Thread.currentThread()) {
            centerPane = getCenterPane();
            centerPane.changeTitle(TitleType.NORMAL);

            if (nextCenterPane == null) {
                nextCenterPane = getNextCenterPane();
            }

            if (nextLeftPane == null) {
                nextLeftPane = getNextLeftPane();
            }

            if (nextRightPane == null) {
                nextRightPane = getNextRightPane();
            }

            try {
                Thread.sleep(delay);
            } catch (InterruptedException ex) {
                err.setVisible(true);
                counter = 0;
                break;
            }


            DEBUG();

            if (isCenterPointReached(0, isCarouselMoving)) {
                break;
            }

            // go through and set the new points for each pane
            for (int i = 0; i < this.getComponentCount(); i++) {
                ACarouselPane cPane = (ACarouselPane) this.getComponent(i);
                Point.Double point = cPane.getPoint();
                double increment = 0.0;

                //use increment of 14.5 for low resolutions else increment by 16 for high resolutions
                if ((panelWidth % 14.5) == 0) {
                    increment = 14.5;
                } else if ((panelWidth % 16.0) == 0) {
                    increment = (16.0);
                }

                xMov = (increment) * multiplier;


                if (cPane == nextCenterPane && nextCenterPane.getPointX() - xMov < centX) {
                    xMov = nextCenterPane.getPointX() - centX;
                } else if (cPane == nextRightPane && nextRightPane.getPointX() - xMov < rightX) {
                    xMov = nextRightPane.getPointX() - rightX;
                } else if (cPane == nextLeftPane && nextLeftPane.getPointX() - xMov < leftX) {
                    xMov = nextLeftPane.getPointX() - leftX;
                }

                if (isOdd(i)) {
                    yMov = Incr1;
                    if (cPane == nextCenterPane && cPane.getPointY() + yMov < centY) {
                        yMov = centY - cPane.getPointY();
                    } else if (cPane == nextRightPane && cPane.getPointY() + yMov < rightY) {
                        yMov = rightY - cPane.getPointY();
                    } else if (cPane == nextLeftPane && cPane.getPointY() + yMov < leftY) {
                        yMov = leftY - cPane.getPointY();
                    }
                    cPane.setPoint(point.x - xMov, point.y + yMov);
                } else {
                    yMov = Incr2;
                    if (cPane == nextCenterPane && cPane.getPointY() + yMov < centY) {
                        yMov = centY - cPane.getPointY();
                    } else if (cPane == nextRightPane && cPane.getPointY() + yMov < rightY) {
                        yMov = rightY - cPane.getPointY();
                    } else if (cPane == nextLeftPane && cPane.getPointY() + yMov < leftY) {
                        yMov = leftY - cPane.getPointY();
                    }
                    cPane.setPoint(point.x - xMov, point.y + yMov);
                }
            }

            isCarouselMoving = true;
            paint();

        }

        ///....Done
        //


        ///////.....RIGHT
        xMov = 0;
        yMov = 0;
        while (runRight == Thread.currentThread()) {
            centerPane = getCenterPane();
            centerPane.changeTitle(TitleType.NORMAL);

            if (nextCenterPane == null) {
                nextCenterPane = getNextCenterPane();
            }

            if (nextLeftPane == null) {
                nextLeftPane = getNextLeftPane();
            }

            if (nextRightPane == null) {
                nextRightPane = getNextRightPane();
            }

            try {
                Thread.sleep(delay);
            } catch (InterruptedException ex) {
                err.setVisible(true);
                counter = 0;
                break;
            }

            if (isCenterPointReached(1, isCarouselMoving)) {
                break;
            }

            for (int i = 0; i < this.getComponentCount(); i++) {
                ACarouselPane cPane = (ACarouselPane) this.getComponent(i);
                Point.Double point = cPane.getPoint();
                double increment = 0.0;

                //use increment of 14.5 for low resolutions else increment by 16 for high resolutions
                if ((panelWidth % 14.5) == 0) {
                    increment = 14.5;
                } else if ((panelWidth % 16.0) == 0) {
                    increment = (16.0);

                }

                xMov = (increment) * multiplier;


                if (cPane == nextCenterPane && nextCenterPane.getPointX() + xMov > centX) {
                    xMov = centX - nextCenterPane.getPointX();
                } else if (cPane == nextRightPane && nextRightPane.getPointX() + xMov > rightX) {
                    xMov = rightX - nextRightPane.getPointX();
                } else if (cPane == nextLeftPane && nextLeftPane.getPointX() + xMov > leftX) {
                    xMov = leftX - nextLeftPane.getPointX();
                }

                if (isOdd(i)) {
                    yMov = Incr1;
                    if (cPane == nextCenterPane && cPane.getPointY() + yMov < centY) {
                        yMov = centY - cPane.getPointY();
                    } else if (cPane == nextRightPane && cPane.getPointY() + yMov < rightY) {
                        yMov = rightY - cPane.getPointY();
                    } else if (cPane == nextLeftPane && cPane.getPointY() + yMov < leftY) {
                        yMov = leftY - cPane.getPointY();
                    }
                    cPane.setPoint(point.x + xMov, point.y + yMov);
                } else {
                    yMov = Incr2;
                    if (cPane == nextCenterPane && cPane.getPointY() + yMov < centY) {
                        yMov = centY - cPane.getPointY();
                    } else if (cPane == nextRightPane && cPane.getPointY() + yMov < rightY) {
                        yMov = rightY - cPane.getPointY();
                    } else if (cPane == nextLeftPane && cPane.getPointY() + yMov < leftY) {
                        yMov = leftY - cPane.getPointY();
                    }
                    cPane.setPoint(point.x + xMov, point.y + yMov);
                }
            }

            isCarouselMoving = true;
            paint();
        }

        ///....Done

//        nextRightPane.setPoint(rightX, nextRightPane.getPoint().y);
//        nextLeftPane.setPoint(leftX, nextLeftPane.getPoint().y);
//        nextCenterPane.setPoint(centX, centY);

        resetPanePositions();
        centerPane = getCenterPane();
        centerPane.changeTitle(TitleType.GLOW);

        nextCenterPane = null;
        nextRightPane = null;
        nextLeftPane = null;

    }

    /**
     *
     * @param n
     *          <
     *          p/>
     * <p/>
     * @return
     */
    private boolean isOdd(int n) {

        boolean isOdd = false;

        if (n % 2 != 0) {
            isOdd = true;
        }
        return isOdd;
    }

    /**
     *
     * @return
     */
    public ACarouselPane getCenterPane() {

        boolean found = false;
        ACarouselPane pane = null;

        for (int i = 0; i < numberOfPanes && !found; i++) {
            pane = (ACarouselPane) this.getComponent(i);
            if (pane.getPointX() == centX) {
                found = true;
            }
        }
        return pane;
    }

    public ACarouselPane getNextCenterPane() {

        boolean found = false;
        int index = 0;
        ACarouselPane pane = null;

        if (isRunningRight) {

            for (int i = 0; i < numberOfPanes && !found; i++) {
                pane = (ACarouselPane) this.getComponent(i);
                if (pane.getPointX() == centX) {
                    index = i - 1;
                }
            }

            if (index < 0) {
                index = this.getComponentCount() - 1;
            }

            return (ACarouselPane) this.getComponent(index);
        } else {
            for (int i = 0; i < numberOfPanes && !found; i++) {
                pane = (ACarouselPane) this.getComponent(i);
                if (pane.getPointX() == centX) {
                    index = i + 1;
                }
            }
            if (this.getComponentCount() <= index) {
                index = 0;
            }
            return (ACarouselPane) this.getComponent(index);
        }
    }

    public ACarouselPane getNextLeftPane() {

        boolean found = false;
        int index = 0;
        ACarouselPane pane = null;

        if (isRunningRight) {

            for (int i = 0; i < numberOfPanes && !found; i++) {
                pane = (ACarouselPane) this.getComponent(i);
                if (pane.getPointX() < leftX) {
                    index = i; // left offset pane becomes left when moving right
                }
            }

            if (index < 0) {
                index = this.getComponentCount();
            }

            return (ACarouselPane) this.getComponent(index);
        } else {
            for (int i = 0; i < numberOfPanes && !found; i++) {
                pane = (ACarouselPane) this.getComponent(i);
                if (pane.getPointX() == centX) {
                    index = i; // center pane becomes left when moving left
                }
            }
            if (this.getComponentCount() <= index) {
                index = 0;
            }
            return (ACarouselPane) this.getComponent(index);
        }
    }

    public ACarouselPane getNextRightPane() {

        boolean found = false;
        int index = 0;
        ACarouselPane pane = null;

        if (isRunningRight) {

            for (int i = 0; i < numberOfPanes && !found; i++) {
                pane = (ACarouselPane) this.getComponent(i);
                if (pane.getPointX() == centX) {
                    index = i; // center pane becomes right when moving right
                }
            }

            if (index < 0) {
                index = this.getComponentCount();
            }

            return (ACarouselPane) this.getComponent(index);
        } else {
            for (int i = 0; i < numberOfPanes && !found; i++) {
                pane = (ACarouselPane) this.getComponent(i);
                if (pane.getPointX() > rightX) {
                    index = i; // right offset pane becomes right when moving left
                }
            }
            if (this.getComponentCount() <= index) {
                index = 0;
            }
            return (ACarouselPane) this.getComponent(index);
        }
    }

    /**
     *
     * @param runDirection
     * @param increment1
     * @param increment2
     *                     <p>
     * @return
     */
    private boolean isCenterPointReached(int runDirection, boolean b) {

        boolean centerPointReached = false;
        boolean isCarouselMoving = b;

        if (isCarouselMoving) {
            for (int i = 0; i < numberOfPanes; i++) {
                ACarouselPane pane = (ACarouselPane) this.getComponent(i);
                if (pane.getPointX() == centX) {
                    if (runDirection == 0) {
                        runLeft = null;
                    } else if (runDirection == 1) {
                        runRight = null;
                    }
                    centerPointReached = true;
                    break;
                }
            }
        }

        return centerPointReached;
    }

    public boolean isDEBUG() {
        return DEBUG;
    }

    public void setDEBUG(boolean DEBUG) {
        this.DEBUG = DEBUG;
    }

    public double getIncr1() {
        return Incr1;
    }

    public void setIncr1(double Incr1) {
        this.Incr1 = Incr1;
    }

    public double getIncr2() {
        return Incr2;
    }

    public void setIncr2(double Incr2) {
        this.Incr2 = Incr2;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public double getCentX() {
        return centX;
    }

    public double getCentY() {
        return centY;
    }

    public Double getCenterPoint() {
        return centerPoint;
    }

    public void setCenterPoint(Double centerPoint) {
        this.centerPoint = centerPoint;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public ADialog getErr() {
        return err;
    }

    public void setErr(ADialog err) {
        this.err = err;
    }

    public ACarouselPane getLeftPane() {

        ACarouselPane pane = null;
        boolean found = false;

        for (int i = 0; i < numberOfPanes && !found; i++) {
            pane = (ACarouselPane) this.getComponent(i);
            if (pane.isLeftPane()) {
                found = true;
            }
        }
        return pane;
    }

    public double getLeftX() {
        return leftX;
    }

    public int getPanelHeight() {
        return panelHeight;
    }

    public void setPanelHeight(int panelHeight) {
        this.panelHeight = panelHeight;
    }

    public double getPanelWidth() {
        return panelWidth;
    }

    public void setPanelWidth(int panelWidth) {
        this.panelWidth = panelWidth;
    }

    public double getRightX() {
        return rightX;
    }

    public Thread getRunLeft() {
        return runLeft;
    }

    public void setRunLeft(Thread runLeft) {
        this.runLeft = runLeft;
    }

    public Thread getRunRight() {
        return runRight;
    }

    public void setRunRight(Thread runRight) {
        this.runRight = runRight;
    }

    public int getTotalWidth() {
        return totalWidth;
    }

    public void setTotalWidth(int totalWidth) {
        this.totalWidth = totalWidth;
    }

    public void DEBUG() {
        if (DEBUG) {

            counter++;

            if (logger.isDebugEnabled()) {
                logger.debug(" --------- " + counter + " --------------");
                logger.debug("cent X (def) : " + centX);
                logger.debug("cent Y (def) : " + centY);
                logger.debug("centerPoint.x (chg) : " + centerPoint.x);
                logger.debug("centerPoint.y (chg) : " + centerPoint.y);
                logger.debug("rightX (def) : " + rightX);
                logger.debug("rightY (def) : " + rightY);
                logger.debug("rightPoint.x (chg) : " + rightPoint.x);
                logger.debug("rightPoint.y (chg) : " + rightPoint.y);
                logger.debug(" leftX (def) : " + leftX);
                logger.debug(" leftY (def) : " + leftY);
                logger.debug("leftPoint.x (chg) : " + leftPoint.x);
                logger.debug("leftPoint.y (chg) : " + leftPoint.y);
                logger.debug("sPoint.x (chg) : " + sPoint.x);
                logger.debug("sPoint.y (chg) : " + sPoint.y);
                logger.debug("this.height " + this.getHeight());
                logger.debug("this.width " + this.getWidth());
            }


        }
    }
}
