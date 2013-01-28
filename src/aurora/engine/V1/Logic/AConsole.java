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


package aurora.engine.V1.Logic;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.log4j.Logger;


/**
 *
 * @author Sammy
 */
public class AConsole extends JFrame{
private static JTextArea txtBox;
private JScrollPane scroller;
private static int counter = 0;
static final Logger logger = Logger.getLogger(AConsole.class);

public AConsole(){
    setTitle("Aurora -- Console");
    txtBox = new JTextArea(10,20);
    scroller = new JScrollPane(txtBox);
    txtBox.setLineWrap(true);
    scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    add(BorderLayout.CENTER, scroller);
    setAlwaysOnTop(true);
    setSize(300,400);
    setVisible(true);
    setLocation(55, 200);
}

    public static void print(Object txt){
        counter++;
        txtBox.append(counter + ":   " + txt.toString() + "\n");

    }

}
