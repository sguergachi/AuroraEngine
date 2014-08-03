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

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.JComponent;
import javax.swing.JTextPane;
import javax.swing.Painter;
import javax.swing.UIManager;
import org.apache.log4j.Logger;

/**
 *
 * @author Sammy Guergachi <sguergachi at gmail.com>
 */
public class ASlickTextPane extends JTextPane implements MouseListener {

    private String url;

    private Color prevColor;

    private Cursor prevCursor;

    static final Logger logger = Logger.getLogger(ASlickLabel.class);

    private Color linkColor;

    public ASlickTextPane(String text) {
        super.setText(text + "");
        UIManager.getLookAndFeelDefaults().put(
                "TextPane[Enabled].backgroundPainter", new FillPainter());
        this.setOpaque(false);
        this.setEditable(false);
        setEnabled(false);
    }

    public ASlickTextPane() {
        UIManager.getLookAndFeelDefaults().put(
                "TextPane[Enabled].backgroundPainter", new FillPainter());
        this.setEditable(false);
        setEnabled(false);
    }

    @Override
    public void setText(String text) {
        super.setText(text);
        this.setEditable(false);
        UIManager.getLookAndFeelDefaults().put(
                "TextPane[Enabled].backgroundPainter", new FillPainter());
        this.setOpaque(false);
        setEnabled(false);

    }

    @Override
    public void setForeground(Color color) {
        setDisabledTextColor(color);
    }

    public void setLink(String URL) {
        this.url = URL;
        this.setToolTipText(URL);
        this.addMouseListener(this);
        this.linkColor = Color.green;
        this.setOpaque(false);
    }

    public void setLink(String URL, Color linkColor) {
        this.url = URL;
        this.setToolTipText(URL);
        this.addMouseListener(this);
        this.linkColor = linkColor;
        this.setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);

        super.paintComponent(g2d);
    }

    public void mouseClicked(MouseEvent e) {

        if (url != null) {
            try {
                try {
                    Desktop.getDesktop().browse(new URI(url));
                } catch (URISyntaxException ex) {
                    logger.error(ex);
                }
            } catch (IOException ex) {
                logger.error(ex);
            }
        }
    }

    public void mousePressed(MouseEvent e) {
        System.out.println("Mouse Clicked Link!");
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {

        if (url != null) {
            prevColor = this.getForeground();
            prevCursor = this.getCursor();

            ACursor selectCursor = new ACursor(new AImage("cursor_select.png"));
            setCursor(selectCursor.getCursor());
            this.setForeground(linkColor);
        }

    }

    public void mouseExited(MouseEvent e) {
        if (url != null) {
            this.setForeground(prevColor);
            setCursor(prevCursor);
        }
    }

    public class FillPainter implements Painter<JComponent> {

        @Override
        public void paint(Graphics2D g, JComponent object, int width, int height) {
            g.setColor(object.getBackground());
        }
    }
}
