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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;

import org.apache.log4j.Logger;


/**
 *
 * @author Sammy
 */
public class ARadioButton extends AImagePane {

    private String btnNormal;
    private String btnOver;
    public boolean isSelected = false;
    static final Logger logger = Logger.getLogger(ARadioButton.class);

    public ARadioButton(String ImgUp, String ImgOvr) {

        btnNormal = ImgUp; //Normal State of Radio button
        btnOver = ImgOvr; // Selected state of radio button

        setImage(btnNormal); //Set inital image as normal
        //Set size and add mouse handler
        setPreferredSize(new Dimension(getImageWidth(), getImageHeight()));
        addMouseListener(new Click());
    }

    public void setSelected() {
        isSelected = true;
        setImage(btnOver);
    }

    public void setUnSelected() {
        isSelected = false;
        setImage(btnNormal);
    }

    private class Click implements MouseListener {

        public void mouseClicked(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {

          if (isSelected) setUnSelected();
          if (!isSelected) setSelected();

        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }
    }
}
