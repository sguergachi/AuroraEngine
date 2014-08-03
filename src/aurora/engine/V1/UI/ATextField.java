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

import aurora.engine.V1.Logic.AContextMenuListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.BorderFactory;
import javax.swing.JTextField;
import org.apache.log4j.Logger;

public class ATextField extends AImagePane {

    private String imagePath;

    private JTextField textBox;

    private String activeImagePath;

    static final Logger logger = Logger.getLogger(ATextField.class);

    public ATextField() {
        makeTextBox();
    }

    public ATextField(String backgroundImage) {
        this.imagePath = backgroundImage;

        this.setImage(imagePath);
        this.setLayout(new BorderLayout());
        makeTextBox();

    }

    public ATextField(String backgroundImage_INACTIVE,
                      String backgroundImage_ACTIVE) {
        this.imagePath = backgroundImage_INACTIVE;
        this.activeImagePath = backgroundImage_ACTIVE;

        this.setImage(imagePath);
        this.setLayout(new BorderLayout());
        makeTextBox();

    }

    public ATextField(String backgroundImage_INACTIVE,
                      String backgroundImage_ACTIVE, String SurfaceName) {
        this.imagePath = backgroundImage_INACTIVE;
        this.activeImagePath = backgroundImage_ACTIVE;
        this.setSurface(SurfaceName);
        this.setLayout(new BorderLayout());
        this.setImage(imagePath);
        makeTextBox();

    }

    private void makeTextBox() {

        textBox = new JTextField("");
        textBox.setOpaque(false);
        textBox.setBorder(null);
        textBox.setColumns(20);
        textBox.setBorder(BorderFactory.createEmptyBorder());
        textBox.setBackground(new Color(0, 0, 0, 0));
        textBox.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        textBox.addMouseListener(new AContextMenuListener());
        textBox.addFocusListener(new textFocusListener());

        this.add(textBox, BorderLayout.CENTER);

        this.setOpaque(false);
        this.setBorder(BorderFactory.createEmptyBorder());
        this.setBackground(new Color(0, 0, 0, 0));

    }

    public void setText(String text) {
        textBox.setText(text);
    }

    public String getText() {
        return textBox.getText();
    }

    public JTextField getTextBox() {
        return textBox;
    }

    public void setTextboxSize(int Width, int Height) {

        int width = Width;
        int height = Height;

        if (width == 0) {
            width = this.getImageWidth();
        }
        if (height == 0) {
            height = this.getImageHeight();
        }
        this.setPreferredSize(new Dimension(width, height));
        textBox.setPreferredSize(new Dimension(width, height));
        this.setImageSize(width, height);
        this.revalidate();
    }

    public void setBackgroundImage(String backgroundImage) {
        this.imagePath = backgroundImage;
        this.setImageSize(this.getPreferredSize().width,
                this.getPreferredSize().height);
    }

    public class textFocusListener implements FocusListener {

        @Override
        public void focusGained(FocusEvent arg0) {
            if (activeImagePath != null) {
                setImage(activeImagePath);
                setImageSize(getPreferredSize().width, getPreferredSize().height);
            } else {
                setImage(imagePath);

                setImageSize(getPreferredSize().width, getPreferredSize().height);
            }

        }

        @Override
        public void focusLost(FocusEvent arg0) {
            setImage(imagePath);
            setImageSize(getPreferredSize().width, getPreferredSize().height);
        }
    }
}
