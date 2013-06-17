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

import aurora.engine.V1.Logic.AThreadWorker;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JLabel;

import org.apache.log4j.Logger;

/**
 *
 * @author Sammy
 * @version 0.2
 */
public class ATimeLabel extends ASlickLabel {

    private Thread runner;

    public static final String FULL_DATE = "EEEEE, MMMMM dd hh:mm:ss a";

    public static final String TIME = "hh:mm a";

    public static final String TIME_PLUS = "HH:mm:ss a";

    public static final String DATE_NUM = "MMddyy";

    public static final String DATE = "MM/dd/yy";

    public static final String DATE_LETTERS = "EEEEE, MMMMM dd";

    public static final String YEAR = "yyyy";

    public static final String TIME_24HOUR = "kk:mm a";

    public static final String MONTH = "MM-dd";

    private static String[] WEEK_ARRAY = {"Sunday", "Monday", "Tuesday",
        "Wednesday", "Thursday", "Friday", "Saturday"};

    public static final String[] MONTH_ARRAY = {" January", " February",
        " March",
        " April", " May", " June", " July", " August", " September", " October",
        " November", " December"};

    private final String timeType;

    static final Logger logger = Logger.getLogger(ATimeLabel.class);

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

        AThreadWorker time = new AThreadWorker(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setText(current(timeType) + "   ");
                repaint();
                logger.info("Time thread refhreshing");
            }
        }, 60000);

        time.start();


    }


}
