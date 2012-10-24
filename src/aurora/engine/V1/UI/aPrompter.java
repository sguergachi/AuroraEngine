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

import aurora.engine.V1.Logic.aPostHandler;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 *
 * @author Sammy
 */
public class aPrompter extends JPanel implements Runnable {

    private Thread runner;
    private ArrayList<String> toDisplayList;
    private ArrayList<String> updateList;
    private float Alpha = 0.0F;
    private int Ypos;
    private int Xpos;
    //private int Size = 25;
    private int Counter = 0;
    private Color color;
    private boolean stop = false;
    private FontMetrics fm;
    private Rectangle2D rect;
    private int textWidth;
    private boolean running = true;
    private Font font;
    private int arrayIndex = 0;
    private boolean needUpdate;
    private aPostHandler postHandler;
    private ArrayList<Color> listUpdateColor;

    public aPrompter(Font font) {
        super(true);
        this.font = font;
        setOpaque(false);
        setLayout(null);

        updateList = new ArrayList();
        toDisplayList = new ArrayList();
        start();
    }

    public aPrompter(Color clr, Font font) {
        super(true);
        this.font = font;
        this.color = clr;
        setOpaque(false);
        setLayout(null);
        updateList = new ArrayList();
        toDisplayList = new ArrayList();
        listUpdateColor = new ArrayList();
        //add(initial);
        start();
    }

    /**
     * Add text to start
     */
    public void add(String text) {
        updateList.add(text);
        listUpdateColor.add(color);
    }

    public void add(String text, Color clr) {
        updateList.add(text);
        listUpdateColor.add(clr);
    }

    public void add(ArrayList<String> text) {
        updateList.addAll(text);
        for (int i = 0; i < text.size(); i++) {
            listUpdateColor.add(color);
        }
    }

    public void setUp(int Ypos, int Xpos) {

        this.Ypos = Ypos * 2;
        this.Xpos = Xpos / 2;

//        start();

    }

    public boolean isRunning() {
        return running;
    }

    private void updateList() {

        if (!updateList.isEmpty() && !toDisplayList.containsAll(updateList)) {
            //Transfer updateList to Display List
            //Then Clear updateList
            System.out.println("updating prompt...");
            toDisplayList = (ArrayList<String>) updateList.clone();
            updateList.clear();

            needUpdate = false;
            stop = false;

        } else if (updateList.size() == toDisplayList.size()) {
            needUpdate = true;
        }
    }

    private void start() {
        updateList();

        if (runner == null) {
            runner = new Thread(this);
            running = true;
        }
        runner.setName("aPromterThread");
        if (color == null) {
            color = Color.BLACK;
        }

        runner.start();

    }

    @Override
    public void run() {

        while (runner == Thread.currentThread()) {



            //If Stop Flag, then stop animation

            //System.out.println("Counter " + Counter + " arr " + arrayIndex);

            if (!stop) {


                //if (Counter > 0) {


                if (Counter == 1) {
                    //Wait Longer for first text

                    //System.out.println("Waiting 2000 ");
                    try {
                        Thread.sleep(1600);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(aProgressWheel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } 
                if (Ypos == (this.getHeight() / 2)) {
                    //Wait to allow for reading

                    //System.out.println("Waiting 1000 ");

                    try {
                        Thread.sleep(1200);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(aProgressWheel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                //}
                this.repaint();

            } else {

                break;
            }

            while (updateList.size() == toDisplayList.size()) {


                updateList();

//                try {
//                    Thread.sleep(10);
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(aProgressWheel.class.getName()).log(Level.SEVERE, null, ex);
//                }

            }

            try {
                Thread.sleep(16);
            } catch (InterruptedException ex) {
                Logger.getLogger(aProgressWheel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        //Animation Complete!
        running = false;
        //Execute what ever is in postHandler Method
        if (postHandler != null) {
            postHandler.actionPerformed();
        }
    }

    @Override
    public void paintComponent(Graphics g) {

        update(g);

    }

    @Override
    public void update(Graphics g) {



        //User JAVA 2D Graphics not just Graphics
        Graphics2D g2d = (Graphics2D) g;


        g2d.setColor(listUpdateColor.get(arrayIndex));

        //Make Text Render Beautifuly
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);




        //If Already Fully trans, Turn Opaque first
        //System.out.println("Alpha1 " + Alpha);


//        if (Size == 0) {
//            Size = 35;
//        }

        //Animate the Text
        paintText(g2d);
        //System.out.println("Alpha2 " + Alpha);

        //Count the number of updates
        Counter++;

        if (Alpha != 0.0F) {
            Alpha -= 0.05F;
        } else if (Alpha == 0.0F) {
            Alpha = 1.0F;
        }

        //System.out.println("Alpha3 " + Alpha);

    }

    public void paintText(Graphics2D g2d) {


        if (fm == null) {
            fm = g2d.getFontMetrics(font);
        }

        //When Opacity is Close to 0 Then Move On To Next Text To Display
        if (Alpha <= 0.05F) {

            Alpha = 0.0F;

            try {
                Thread.sleep(20);
            } catch (InterruptedException ex) {
                Logger.getLogger(aProgressWheel.class.getName()).log(Level.SEVERE, null, ex);
            }

            Xpos = this.getWidth() / 2;
            Ypos = this.getHeight() / 2 + 6;

            //If no more to add then check for more
           // System.out.println("size " + toDisplayList.size());
           // System.out.println("size arry " + (String.valueOf(arrayIndex + 1)));

            if (arrayIndex + 1 > toDisplayList.size() - 1) {
                stop = true;
                arrayIndex = 0; //Reset ArrayIndex
                updateList();
            } else {
                arrayIndex++;
            }
        }

        //This Is Where The Animation Really Happens

        if (!stop && !needUpdate) {
            //Get Width Of Text
            rect = fm.getStringBounds(toDisplayList.get(arrayIndex), g2d);
            textWidth = (int) rect.getWidth();
            //Move
            Ypos--;
            //Constant X
            Xpos = (this.getWidth() - textWidth) / 2;
            g2d.setFont(font);
            //Set Opacity
            g2d.setComposite(makeComposite(Alpha));
            //And DRAW!!
            
            g2d.drawString(toDisplayList.get(arrayIndex), Xpos, Ypos);
        }
    }

    private AlphaComposite makeComposite(float alpha) {
        int type = AlphaComposite.SRC_OVER;
        return (AlphaComposite.getInstance(type, alpha));
    }

    public void addPost(aPostHandler postAnimationHandler) {
        postHandler = postAnimationHandler;
    }
}
