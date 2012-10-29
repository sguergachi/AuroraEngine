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

import aurora.engine.V1.UI.ARadioButton;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * Manages a list of radio buttons to have only one
 * selected at a time
 * @author Sammy
 */
public class ARadioButtonManager {

    private ArrayList<ARadioButton> buttonList;

    public ARadioButtonManager() {

        buttonList = new ArrayList();

    }

    /*
     * Adds radio Button to list of Radio buttons
     *
     */
    public void addButton(ARadioButton radioButton) {
        buttonList.add(radioButton);
    }
    /*
     * Adds a Handler to all buttons added to the
     * Manager to be able to have only one selected
     */
    public void setRadioButton() {

        for (int i = 0; i < buttonList.size(); i++) {
            buttonList.get(i).addMouseListener(new RadioButtonHandler());
        }


    }

    public ArrayList<ARadioButton> getButtonList() {
        return buttonList;
    }

    private class RadioButtonHandler implements MouseListener {

        public void mouseClicked(MouseEvent e) {

        }

        public void mousePressed(MouseEvent e) {

        }

        public void mouseReleased(MouseEvent e) {
             ARadioButton btn = (ARadioButton) e.getSource();
            if (btn.isSelected) {
                for (int i = 0; i < buttonList.size(); i++) {
                    if (buttonList.get(i) != btn) {
                        buttonList.get(i).setUnSelected();
                    }
                }
            } else {
            }
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {

        }
    }
}
