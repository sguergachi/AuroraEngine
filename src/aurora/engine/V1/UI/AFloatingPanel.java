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

import java.awt.event.FocusEvent;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

/**
 *
 * @author Sammy Guergachi <sguergachi at gmail.com>
 */
public class AFloatingPanel extends JPanel {

    private final JFrame frame;

    private ArrayList<JComponent> ignoreList = new ArrayList<>();

    private boolean shown;

    public AFloatingPanel(JFrame frame) {
        this.frame = frame;
        this.addFocusListener(new FocusListener());
        this.setVisible(false);
    }

    public void show(int x, int y) {

        shown = true;
        this.setLocation(x, y);
        frame.getLayeredPane().add(this, JLayeredPane.MODAL_LAYER);
        this.revalidate();
        this.requestFocusInWindow();
    }

    public void hide() {
        shown = false;
        frame.getLayeredPane().remove(this);
        this.revalidate();
        frame.getLayeredPane().revalidate();
        frame.getLayeredPane().repaint();
    }

    public void addToIgnoreFocusLost(JComponent component) {
        ignoreList.add(component);
    }

    public boolean isShown() {
        return shown;
    }

    public void setShown(boolean shown) {
        this.shown = shown;
    }

    private class FocusListener implements java.awt.event.FocusListener {

        public FocusListener() {
        }

        @Override
        public void focusGained(FocusEvent e) {
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (!ignoreList.contains(e.getOppositeComponent())) {
                hide();
            }
        }
    }

}
