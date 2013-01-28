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

import java.awt.*;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicScrollBarUI;

import org.apache.log4j.Logger;


/**
 *
 * @author Sammy
 */
public class AScrollPane extends JScrollPane {

    private final String scrollBarImgPath;
    static final Logger logger = Logger.getLogger(AScrollPane.class);

    public AScrollPane(Component component, int Policy, String ScrollBarImg) {
        super(component);
        this.setOpaque(false);
        this.scrollBarImgPath = ScrollBarImg;

        if (Policy == JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
                || Policy == JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
                || Policy == JScrollPane.VERTICAL_SCROLLBAR_NEVER) {

            this.setVerticalScrollBarPolicy(Policy);
            this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        } else if (Policy == JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
                || Policy == JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
                || Policy == JScrollPane.HORIZONTAL_SCROLLBAR_NEVER) {

            this.setHorizontalScrollBarPolicy(Policy);
            this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        }

        AImage img = new AImage(scrollBarImgPath);
        this.setVerticalScrollBar(new aScrollBar(img.getImgIcon().getImage()));

    }

    //TODO need to add more Constructors for Flexability

    public class aScrollBar extends JScrollBar {

        public aScrollBar(Image img) {
            super();
            this.setUI(new ScrollBarCustomUI(img));
        }

        public class ScrollBarCustomUI extends BasicScrollBarUI {

            private final Image image;

            public ScrollBarCustomUI(Image img) {
                this.image = img;
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                Graphics2D g2g = (Graphics2D) g;
                g2g.dispose();
                g2g.drawImage(image, 0, 0, null);
                super.paintThumb(g2g, c, thumbBounds);
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
                super.paintTrack(g, c, trackBounds);
            }


            @Override
            protected void setThumbBounds(int x, int y, int width, int height) {

                super.setThumbBounds(0, 0, 0, 0);
            }


            @Override
            protected Dimension getMinimumThumbSize() {
                return new Dimension(0, 0);
            }

            @Override
            protected Dimension getMaximumThumbSize() {
                return new Dimension(0, 0);
            }
        }
    }
}
