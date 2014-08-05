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

import aurora.engine.V1.UI.ACarouselTitle.TitleType;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import javax.swing.Box;
import org.apache.log4j.Logger;

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

    static final Logger logger = Logger.getLogger(ACarouselPane.class);

    public ACarouselPane(String path, int W, int H, boolean DoubleBuffered,
                         ACarouselTitle title, String name) {

        //Create the Image
        super(path, W, H, DoubleBuffered);
        setPreferredSize(new Dimension(W, H));

        //ID Name
        this.setName(name);
        this.title = title;

        point = new Point.Double(0, 0);
        isCenterPane = false;
        isLeftPane = false;
        isRightPane = false;

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

    }

    /**
     *
     * @param title
     * @param image
     * @param titleType
     */
    public void addContent(AImage image, ACarouselTitle.TitleType titleType) {

        this.setLayout(new FlowLayout(FlowLayout.CENTER, 0, -image.getPreferredSize().height / 10));
        AImage titleImage = null;

        if (titleType == TitleType.NORMAL) {
            titleImage = title.getNormalImage();
        } else if (titleType == TitleType.GLOW) {
            titleImage = title.getGlowImage();
        }


        this.add(Box.createRigidArea(new Dimension(image.getPreferredSize().width,
                                                   image.getPreferredSize().height / 7)));
        this.add(new AImage(titleImage.getImgURl()));
        this.add(image);

    }

    public void addContent(AImagePane image, ACarouselTitle.TitleType titleType) {
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 0, -image.getPreferredSize().height / 8));
        AImage titleImage = null;

        if (titleType == TitleType.NORMAL) {
            titleImage = title.getNormalImage();
        } else if (titleType == TitleType.GLOW) {
            titleImage = title.getGlowImage();
        }

        this.add(Box.createRigidArea(new Dimension(image.getPreferredSize().width,
                                                   image.getPreferredSize().height / 5)));
        this.add(new AImage(titleImage.getImgURl()));
        this.add(image);
    }

    public void setDisplayedImage(AImage image) {
        this.add(image);
    }

    public void setDisplayedImage(AImagePane image) {
        this.add(image);
    }

    public void setDisplayedTitle(ACarouselTitle.TitleType titleType) {

        AImage titleImage = null;

        if (titleType == TitleType.NORMAL) {
            titleImage = title.getNormalImage();
        } else if (titleType == TitleType.GLOW) {
            titleImage = title.getGlowImage();
        }
        this.add(new AImage(titleImage.getImgURl()));
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
        this.add(image);
    }

    /**
     * Changes the title of the carousel pane to the title specified
     *
     * @param titleType TitleType to replace current title with
     */
    public void changeTitle(ACarouselTitle.TitleType titleType) {

        AImage image = (AImage) this.getComponent(1);
        AImage titleImage = null;

        if (titleType == TitleType.NORMAL) {
            titleImage = title.getNormalImage();
            image.setIcon(titleImage.getIcon());
        } else if (titleType == TitleType.GLOW) {
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
