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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import javax.swing.Timer;

/**
 *
 * A Slick Label that fades in and out with each Text change.
 *
 * @author Sammy Guergachi <sguergachi at gmail.com>
 */
public class AFadeLabel extends ASlickLabel {

    private String currentString = this.getText();

    private String nextString;

    private float Alpha = 1.0F;

    private boolean faded = false;

    private boolean fadedOut = false;

    private boolean fadedIn = true;

    private boolean stop;

    private int count;

    private Color currentColor;

    private Color nextColor;

    public AFadeLabel() {

        this.setIgnoreRepaint(true);

    }

    public AFadeLabel(String text) {
        setText(text);
    }

    @Override
    public void setText(String text) {
        if (currentString == null) {
            currentString = text;
            count++;
            super.setText(text);
        } else {
            nextString = text;
            count = 0;
            fade();
        }
    }

    @Override
    public void setForeground(Color clr) {
        if (currentColor == null) {
            currentColor = clr;
        } else {
            nextColor = clr;
        }
    }

    private void fade() {
        if (!nextString.equals(currentString)) {
            stop = false;
            Timer fadeOutAnimation = new Timer(16, new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    repaint();


                    // If currentString is still visible then repaint

                    if (!fadedOut) {

                        if (Alpha > 0.09F && count > 0) {
                            ((Timer) e.getSource()).stop();
                            stop = true;
                            setFadedOut();
                        }
                    }

                }
            });


            fadeOutAnimation.start();


        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);


        //User JAVA 2D Graphics not just Graphics
        Graphics2D g2d = (Graphics2D) g;



        //Make Text Render Beautifuly
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
                RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);




        //Animate the Text
        if (!stop) {
            fadeText(g2d);
        }
        FontMetrics fm = getFontMetrics(getFont());
        //Get Width Of Text
        Rectangle2D rect = fm.getStringBounds(currentString, g2d);
        int textWidth = (int) rect.getWidth();
        //Constant X
        int Xpos = (this.getWidth() - textWidth) / 2;

        if(currentColor == null){
            currentColor = this.getForeground();
        }

        g2d.setComposite(makeComposite(Alpha));
        g2d.setFont(this.getFont());
        g2d.setColor(currentColor);
        g2d.drawString(currentString, Xpos,
                this.getPreferredSize().height);



    }

    private void fadeText(Graphics2D g2d) {

        if (Alpha > 0.05F && !faded) {
            Alpha -= 0.05F;
        } else if (Alpha <= 0.9F && faded) {
            Alpha += 0.015F;
        }

        if (Alpha < 0.05F && !faded) {

            Alpha = 0.00F;
            faded = true;

            setFadedOut();

        }


        if (Alpha > 0.85F && faded) {

//            Alpha = 1.0F;
            faded = false;

            setFadedIn();

        }


    }

    private AlphaComposite makeComposite(float alpha) {

        int type = AlphaComposite.SRC_OVER;
        return (AlphaComposite.getInstance(type, alpha));

    }

    private void setFadedOut() {


        fadedOut = true;
        fadedIn = false;

        if (nextString != null || Alpha < 0.05F) {
            currentString = nextString;

            nextString = null;
        }

        if (nextColor != null) {
            currentColor = nextColor;
            nextColor = null;
        }

    }

    private void setFadedIn() {

        count++;
        fadedIn = true;
        fadedOut = false;


    }
}
