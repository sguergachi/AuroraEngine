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

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sammy
 */
public class aHoverButton extends aImage implements Runnable {

    private final int time;
    private final String imgNorm;
    private MouseEvent e;


    public boolean isHovering() {
        return hovering;
    }
    private final String imgOver;
    private Thread runner;
    private boolean hovering = false;
    private MouseListener handler;
    private boolean done = false;

    public aHoverButton(MouseListener handler, int miliseconds, String imgNorm, String imgOver) {
        super(imgNorm);
        super.addMouseListener(handler);
        this.handler = handler;
        this.time = miliseconds;
        this.imgNorm = imgNorm;
        this.imgOver = imgOver;


    }

    public aHoverButton(int miliseconds, String imgNorm, String imgOver) {
        super(imgNorm);

        this.time = miliseconds;
        this.imgNorm = imgNorm;
        this.imgOver = imgOver;

    }

    /**
     * Button Activates on hover over certain amount of time
     *
     * Call This method in MouseEnter actionPerformed Method
     **/
    public void mouseHover(MouseEvent e) {
        this.e = e;
       
        runner = null;
        if (runner == null) {
            runner = new Thread(this);
            hovering = true;
        } 

        super.setImgURl(imgOver);
        runner.start();

    }

    public void mouseExit() {
        super.setImgURl(imgNorm);
        hovering = false;
    }



    @Override
    public void run() {
        int counter = 0;
        while (runner == Thread.currentThread()) {
            System.out.println(counter);
            if (counter < time) {
                counter++;

            } else {
                System.out.println("DONE!");
               
                handler.mouseClicked(e);
                
                break;
            }
            if (!hovering) {
                System.out.println("STOPED!");
                break;
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(aHoverButton.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        runner = null;
        if (hovering) {
            System.out.println(hovering);
            runner = null;
            if (runner == null) {
                runner = new Thread(this);
                hovering = true;
            }

            runner.start();
        }
    }
}
