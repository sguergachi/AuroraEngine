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
import java.awt.Font;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;

/**
 *
 * @author Sammy
 * @version 0.2
 */
public class aInfoFeed extends aImagePane implements Runnable {

    private Thread runner;
    private int Xpos;
    private int defaultXpos = 0;
    private int defaultYpos = 0;
    private ArrayList<JLabel> DisplayLabels;
    int counter = 0;
    private boolean isRunning;
    private ArrayList<String> DisplayArray;
    private Font dFont;


    public aInfoFeed(ArrayList TextArray) {
        this.setLayout(null);
        this.DisplayArray = TextArray;
        isRunning = true;
        defaultXpos = this.getWidth();
        defaultYpos = this.getHeight();
    }

    public aInfoFeed(String BGImg, ArrayList TextArray) {
        super(BGImg);
        this.setLayout(null);
        this.DisplayArray = TextArray;
        isRunning = true;
        defaultXpos = this.getWidth();
        defaultYpos = this.getHeight();
    }

    public aInfoFeed(String BGImg, int Width, int Height, ArrayList TextArray) {
        super(BGImg, Width, Height);
        this.setLayout(null);
        this.DisplayArray = TextArray;
        isRunning = true;
        defaultXpos = Width;
        defaultYpos = Height / 2 - 22;
    }

    public void go() {
        if (runner == null) {
            runner = new Thread(this);
        } else {
            //runner = null;
        }
        isRunning = true;

        dFont = new Font("AgencyFB", Font.BOLD, 20);

        createLable();

        runner.start();

        //System.out.println("FINISHED pre-run");

    }

    private void createLable() {
        counter = 0;
        DisplayLabels = null;
        DisplayLabels = new ArrayList<JLabel>();
        for (int a = 0; a < DisplayArray.size() * 2; a++) {

            //add Label
            if (a % 2 == 0) {
                DisplayLabels.add(a, new JLabel());
                DisplayLabels.get(a).setText(DisplayArray.get(counter));
                DisplayLabels.get(a).setFont(dFont);
                DisplayLabels.get(a).setForeground(Color.green);
                DisplayLabels.get(a).setBounds(defaultXpos + 10, defaultYpos, 20 * DisplayLabels.get(a).getText().length(), 50);
                this.add(DisplayLabels.get(a));
                counter++;
            }
            //add Spacer
            else {

                DisplayLabels.add(a, new aImage("infoSeperator.png"));
                DisplayLabels.get(a).setBounds(defaultXpos + 25, defaultYpos + 10, 40, 40);
                this.add(DisplayLabels.get(a));

            }
        }
        // System.out.println(DisplayLabels);
//        testLabel = new JLabel();
//        testLabel.setFont(dFont);
//        testLabel.setOpaque(false);
//        testLabel.setText("Hello World!");
//        testLabel.setBounds(50, 10, 20*testLabel.getText().length(),50);
//
//        this.add( testLabel);

        //System.out.println(testLabel.getBounds());
        // System.out.println(testLabel.getSize());
    }

    private boolean canAnimate() {
        boolean canAnimate;

        if (!DisplayLabels.isEmpty()) {
            canAnimate = true;
        }else{
            canAnimate = false;
        }
        return canAnimate;
    }

    private void doAnimation() {


        for (int i = 0; i < DisplayLabels.size(); i++) {
            if (DisplayLabels.get(i) != null) {
                if (i % 2 == 0) {
                    //check if new lablel can be created, by seeing if previous lable has endered frame
                    if (i - 1 >= 0) {
//                        System.out.println("Lable: " + (i - 1));
//                        System.out.println(this.getWidth() - DisplayLabels.get(i - 1).getLocation().x);
//                        System.out.println(DisplayLabels.get(i - 1).getWidth());
                        if (this.getWidth() - DisplayLabels.get(i - 1).getLocation().x >= (10 * (DisplayLabels.get(i - 1).getText().length())) + 60) {

                            DisplayLabels.get(i).setBounds(DisplayLabels.get(i).getX() - 10, defaultYpos, 20 * DisplayLabels.get(i).getText().length(), 50);
                        }

                    } else {
                        DisplayLabels.get(i).setBounds(DisplayLabels.get(i).getX() - 10, defaultYpos, 20 * DisplayLabels.get(i).getText().length(), 50);

                    }
                    //Check if lable is out of frame
                    if (DisplayLabels.get(i).getLocation().x <= -(20 * DisplayLabels.get(i).getText().length())) {

                        DisplayLabels.remove(i);

                    }

                } else {
                     if (DisplayLabels.get(i).getLocation().x <= -50) {

                        //DisplayLabels.remove(i);
                         DisplayLabels.get(i).setBounds(DisplayLabels.get(i).getX() - 10, defaultYpos + 5, 40, 40);

                    }
                     else if (this.getWidth() - DisplayLabels.get(i - 1).getLocation().x >= (10 * (DisplayLabels.get(i - 1).getText().length())) + 40) {

                        DisplayLabels.get(i).setBounds(DisplayLabels.get(i).getX() - 10, defaultYpos, 50, 50);
                    }
                  
                }
            }
            // System.out.println("Lable: " + i);

        }



    }

    @Override
    public void run() {
       // System.out.println("Running");
        Xpos = defaultXpos;
        while (runner == Thread.currentThread() && isRunning) {

            if (canAnimate()) {
                doAnimation();
            } else {
                
                break;

            }
             try {
                Thread.sleep(90);
            } catch (InterruptedException ex) {
                Logger.getLogger(aInfoFeed.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        runner = null;
        go();
    }
}
