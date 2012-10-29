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

import aurora.engine.V1.UI.ACarouselTitle.Title;
import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Point;

/**
 * This class is a sub class of the AImagePane class to include additional
 * attributes associated with aCarousel
 *
 * @author cmachado
 *
 */
public class ACarouselPane extends AImagePane {

    private ACarouselTitle title;

    private Point.Double point;

    private boolean isCenterPane;

    private boolean isLeftPane;

    private boolean isRightPane;

    public ACarouselPane(String path, int W, int H, boolean DoubleBuffered,
                         ACarouselTitle title, String name) {

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

    public ACarouselPane(String path, int W, int H, ACarouselTitle title,
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
    public void addContent(AImage image, ACarouselTitle.Title titleType) {

        AImage titleImage = null;

        if (titleType == Title.NORMAL) {
            titleImage = title.getNormalImage();
        } else if (titleType == Title.GLOW) {
            titleImage = title.getGlowImage();
        }

        this.add(BorderLayout.PAGE_START, new AImage(titleImage.getImgURl()));
        this.add(BorderLayout.CENTER, image);

    }

    public void addContent(AImagePane image, ACarouselTitle.Title titleType) {

        AImage titleImage = null;

        if (titleType == Title.NORMAL) {
            titleImage = title.getNormalImage();
        } else if (titleType == Title.GLOW) {
            titleImage = title.getGlowImage();
        }

        this.add(BorderLayout.PAGE_START, new AImage(titleImage.getImgURl()));
        this.add(BorderLayout.CENTER, image);
    }

    public void setDisplayedImage(AImage image) {
        this.add(BorderLayout.CENTER, image);
    }

    public void setDisplayedImage(AImagePane image) {
        this.add(BorderLayout.CENTER, image);
    }

    public void setDisplayedTitle(ACarouselTitle.Title titleType) {

        AImage titleImage = null;

        if (titleType == Title.NORMAL) {
            titleImage = title.getNormalImage();
        } else if (titleType == Title.GLOW) {
            titleImage = title.getGlowImage();
        }
        this.add(BorderLayout.PAGE_START, new AImage(titleImage.getImgURl()));
    }

    public void setTitle(ACarouselTitle title) {
        this.title = title;
    }

    public ACarouselTitle getTitle() {
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
    public void setImage(AImage image) {
        this.add(BorderLayout.CENTER, image);
    }

    /**
     * Changes the title of the carousel pane to the title specified
     *
     * @param titleType Title to replace current title with
     */
    public void changeTitle(ACarouselTitle.Title titleType) {

        AImage image = (AImage) this.getComponent(0);
        AImage titleImage = null;

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
