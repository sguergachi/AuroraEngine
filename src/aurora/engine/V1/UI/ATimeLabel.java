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

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JLabel;

/**
 *
 * @author Sammy
 * @version 0.2
 */
public final class ATimeLabel extends JLabel implements Runnable {

    private Thread runner;

    public static final String FULL_DATE = "EEEEE, MMMMM dd \n hh:mm a";

    public static final String TIME = "hh:mm a";

    public static final String TIME_PLUS = "HH:mm:ss a";

    public static final String DATE_NUM = "MMddyy";

     public static final String DATE_LETTERS = "EEEEE, MMMMM dd";

    public static final String YEAR = "yyyy";

    public static final String TIME_24HOUR = "kk:mm a";

    public static final String MONTH = "MM-dd";

    private static String[] WEEK_DATE = {"Sunday", "Monday", "Tuesday",
        "Wednesday", "Thursday", "Friday", "Saturday"};

    public static final String[] MONTH_DATE = {" January", " February", " March",
        " April", " May", " June", " July", " August", " September", " October",
        " November", " December"};

    public String DATE;

    private final String timeType;

    public ATimeLabel() {
        timeType = ATimeLabel.FULL_DATE;
        start();
    }

    public ATimeLabel(String format) {
        timeType = format;
        start();
    }

    public static String current(String format) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format);

        return sdf.format(cal.getTime());
    }

    public void start() {
        repaint();
        if (runner == null) {
            runner = new Thread(this);
        }
        runner.start();
        runner.setName("Time Checker Thread");


    }

    @Override
    public void run() {
        while (runner == Thread.currentThread()) {



            setText(current(timeType) + "   ");


            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                System.out.println("Time Thread failed");
            }

        }
    }

        @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

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

}
