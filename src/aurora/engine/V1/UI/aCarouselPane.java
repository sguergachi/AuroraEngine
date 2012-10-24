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

import aurora.engine.V1.UI.aCarouselTitle.Title;
import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Point;

/**
 * This class is a sub class of the aImagePane class to include additional
 * attributes associated with aCarousel
 *
 * @author cmachado
 *
 */
public class aCarouselPane extends aImagePane {

    private aCarouselTitle title;

    private Point.Double point;

    private boolean isCenterPane;

    private boolean isLeftPane;

    private boolean isRightPane;

    public aCarouselPane(String path, int W, int H, boolean DoubleBuffered,
                         aCarouselTitle title, String name) {

        //Create the Image
        super(path, W, H, DoubleBuffered);

        //ID Name
        this.setName(name);
        this.title = title;

        point = new Point.Double(0, 0);
        isCenterPane = false;
        isLeftPane = false;
        isRightPane = false;
        this.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
    }

    public aCarouselPane(String path, int W, int H, aCarouselTitle title,
                         String name) {

        //Create the Image
        super(path, W, H);

        //ID Name
        this.setName(name);
        this.title = title;

        point = new Point.Double(0, 0);
        isCenterPane = false;
        isLeftPane = false;
        isRightPane = false;
        this.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
    }

    /**
     *
     * @param title
     * @param image
     * @param titleType
     */
    public void addContent(aImage image, aCarouselTitle.Title titleType) {

        aImage titleImage = null;

        if (titleType == Title.NORMAL) {
            titleImage = title.getNormalImage();
        } else if (titleType == Title.GLOW) {
            titleImage = title.getGlowImage();
        }

        this.add(BorderLayout.PAGE_START, new aImage(titleImage.getImgURl()));
        this.add(BorderLayout.CENTER, image);

    }

    public void addContent(aImagePane image, aCarouselTitle.Title titleType) {

        aImage titleImage = null;

        if (titleType == Title.NORMAL) {
            titleImage = title.getNormalImage();
        } else if (titleType == Title.GLOW) {
            titleImage = title.getGlowImage();
        }

        this.add(BorderLayout.PAGE_START, new aImage(titleImage.getImgURl()));
        this.add(BorderLayout.CENTER, image);
    }

    public void setDisplayedImage(aImage image) {
        this.add(BorderLayout.CENTER, image);
    }

    public void setDisplayedImage(aImagePane image) {
        this.add(BorderLayout.CENTER, image);
    }

    public void setDisplayedTitle(aCarouselTitle.Title titleType) {

        aImage titleImage = null;

        if (titleType == Title.NORMAL) {
            titleImage = title.getNormalImage();
        } else if (titleType == Title.GLOW) {
            titleImage = title.getGlowImage();
        }
        this.add(BorderLayout.PAGE_START, new aImage(titleImage.getImgURl()));
    }

    public void setTitle(aCarouselTitle title) {
        this.title = title;
    }

    public aCarouselTitle getTitle() {
        return title;
    }

    public void setPoint(double x, double y) {
        this.point.x = x;
        this.point.y = y;
    }

    public Point.Double getPoint() {
        return point;
    }

    public double getPointX() {
        return point.x;
    }

    /**
     * Sets the image displayed in the carousel pane
     *
     * @param image image to display
     */
    public void setImage(aImage image) {
        this.add(BorderLayout.CENTER, image);
    }

    /**
     * Changes the title of the carousel pane to the title specified
     *
     * @param titleType Title to replace current title with
     */
    public void changeTitle(aCarouselTitle.Title titleType) {

        aImage image = (aImage) this.getComponent(0);
        aImage titleImage = null;

        if (titleType == Title.NORMAL) {
            titleImage = title.getNormalImage();
            image.setIcon(titleImage.getIcon());
        } else if (titleType == Title.GLOW) {
            titleImage = title.getGlowImage();
            image.setIcon(titleImage.getIcon());
        }
    }

    public void setAsLeftPane() {
        isLeftPane = true;
        isRightPane = false;
        isCenterPane = false;
    }

    public boolean isLeftPane() {
        return isLeftPane;
    }

    public void setAsCenterPane() {
        isCenterPane = true;
        isLeftPane = false;
        isRightPane = false;
    }

    public boolean isCenterPane() {
        return isCenterPane;
    }

    public void setAsRightPane() {
        isRightPane = true;
        isLeftPane = false;
        isCenterPane = false;
    }

    public boolean isRightPane() {
        return isRightPane;
    }

    void setAsOffPane() {
        isRightPane = false;
        isLeftPane = false;
        isCenterPane = false;
    }
}
