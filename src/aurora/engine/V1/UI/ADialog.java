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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Desktop.Action;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.AbstractButton;
import javax.swing.JMenu;
import javax.swing.JPanel;

/**
 *
 * @author Sammy
 * @version 0.2
 */
public final class ADialog extends ADragFrame {

    private int Type;
    private String Text;
    public static final int aDIALOG_WARNING = 2;
    public static final int aDIALOG_ERROR = 1;
    private JPanel img;
    private AButton exit;
    private ASlickLabel title;
    private ASlickLabel lblText;
    private JPanel Top;
    private AButton btnOk;
    private AButton btnCancel;
    private JPanel Bottom;
    private ActionListener a;
    //private aSound snd;
    private Font font;
    
    public ADialog(int Type, String Text, Font font, Object obj){
    	if(obj instanceof ActionListener){
    		setButtonListener((ActionListener) obj);
    		this.Type = Type;
            this.Text = Text;
            this.font = font;

            if (Type == aDIALOG_WARNING) {

                img = new AImagePane("Aurora_Dialog2.png");
                img.setLayout(new BorderLayout());
                add(BorderLayout.CENTER, img);

                title = new ASlickLabel("  Warning    ");


            } else if (Type == aDIALOG_ERROR) {

                img = new AImagePane("Aurora_Dialog1.png");
                img.setLayout(new BorderLayout());
                add(BorderLayout.CENTER, img);
                title = new ASlickLabel("  Error    ");

            }
            showDialog();
    	}
    }
    
    public ADialog(int Type, String Text, Object obj){
    	if(obj instanceof ActionListener){
    		setButtonListener((ActionListener) obj);
    		this.Type = Type;
            this.Text = Text;
            font = new Font("Arial", Font.PLAIN, 20);

            if (Type == aDIALOG_WARNING) {

                img = new AImagePane("Aurora_Dialog2.png");
                img.setLayout(new BorderLayout());
                add(BorderLayout.CENTER, img);

                title = new ASlickLabel("  Warning    ");


            } else if (Type == aDIALOG_ERROR) {


                img = new AImagePane("Aurora_Dialog1.png");
                img.setLayout(new BorderLayout());
                add(BorderLayout.CENTER, img);
                title = new ASlickLabel("  Error    ");
            }

            showDialog();
    	}
    }
    
    public ADialog(int Type, String Text, Font font) {


        this.Type = Type;
        this.Text = Text;
        this.font = font;

        if (Type == aDIALOG_WARNING) {

            img = new AImagePane("Aurora_Dialog2.png");
            img.setLayout(new BorderLayout());
            add(BorderLayout.CENTER, img);

            title = new ASlickLabel("  Warning    ");


        } else if (Type == aDIALOG_ERROR) {

            img = new AImagePane("Aurora_Dialog1.png");
            img.setLayout(new BorderLayout());
            add(BorderLayout.CENTER, img);
            title = new ASlickLabel("  Error    ");

        }
        showDialog();
    }

    public ADialog(int Type, String Text) {


        this.Type = Type;
        this.Text = Text;
        font = new Font("Arial", Font.PLAIN, 20);

        if (Type == aDIALOG_WARNING) {

            img = new AImagePane("Aurora_Dialog2.png");
            img.setLayout(new BorderLayout());
            add(BorderLayout.CENTER, img);

            title = new ASlickLabel("  Warning    ");


        } else if (Type == aDIALOG_ERROR) {


            img = new AImagePane("Aurora_Dialog1.png");
            img.setLayout(new BorderLayout());
            add(BorderLayout.CENTER, img);
            title = new ASlickLabel("  Error    ");
        }

        showDialog();


    }

    public void setButtonListener(ActionListener a) {

        this.a = a;
        //btnOk.addActionListener(a);
    }

    public void showDialog() {

        /// EXIT BUTTON
        //exit = new AButton("Aurora_Close_normal.png", "Aurora_Close_down.png", "Aurora_Close_over.png");
        //exit.addActionListener(new ExitListener());

        //TOP
        /*Top = new JPanel();
        Top.setOpaque(false);
        Top.setLayout(new BorderLayout());

        Top.add(BorderLayout.WEST, title);
        //Top.add(BorderLayout.EAST, exit);

        img.add(BorderLayout.PAGE_START, Top);*/



        /// Frame Config
        setUndecorated(true);
        setSize(490, 230);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        



        lblText = new ASlickLabel(Text + "   ");

        lblText.setFont(font.deriveFont(font.BOLD, 28));
        lblText.setForeground(Color.LIGHT_GRAY);


        title.setForeground(Color.LIGHT_GRAY);

        title.setFont(font.deriveFont(font.BOLD, 20));

        //BOTTOM

        img.add(BorderLayout.EAST, lblText);

        btnOk = new AButton("Aurora_dOk_normal.png", "Aurora_dOk_down.png", "Aurora_dOk_over.png");
        btnCancel = new AButton("Aurora_dOk_normal.png", "Aurora_dOk_down.png", "Aurora_dOk_over.png"); //must insert correct cancel button images
        if (a != null) {
            btnOk.addActionListener(a);


        } else {
            a = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
//                    try {
//                        aSound snd = new aSound(aSound.sfxButton, false);
//                        try {
//                            snd.Play();
//                        } catch (InterruptedException ex) {
//                            Logger.getLogger(ADialog.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                    } catch (MalformedURLException ex) {
//                        Logger.getLogger(ADialog.class.getName()).log(Level.SEVERE, null, ex);
//                    } catch (UnsupportedAudioFileException ex) {
//                        Logger.getLogger(ADialog.class.getName()).log(Level.SEVERE, null, ex);
//                    } catch (IOException ex) {
//                        Logger.getLogger(ADialog.class.getName()).log(Level.SEVERE, null, ex);
//                    } catch (LineUnavailableException ex) {
//                        Logger.getLogger(ADialog.class.getName()).log(Level.SEVERE, null, ex);
//                    }
                    setVisible(false);
                }
            };
            btnOk.addActionListener(a);

        }


        Bottom = new JPanel();
        Bottom.setOpaque(false);

        Bottom.add(btnOk);
        Bottom.add(btnCancel);
        img.add(BorderLayout.PAGE_END, Bottom);
        img.addKeyListener(new EnterKeyListener());
        Bottom.addKeyListener(new EnterKeyListener());
        btnOk.addKeyListener(new EnterKeyListener());
        btnCancel.addActionListener(new ExitListener());
        title.addKeyListener(new EnterKeyListener());
        lblText.addKeyListener(new EnterKeyListener());
        addKeyListener(new EnterKeyListener());
        //Top.addKeyListener(new EnterKeyListener());
        //exit.addKeyListener(new EnterKeyListener());

//        img.requestFocusInWindow();

    }    
    
    class EnterKeyListener extends KeyAdapter {


        @Override
        public void keyPressed(KeyEvent e) {
            System.out.println("Pressed Key");
              if (e.getKeyChar() == KeyEvent.VK_ENTER) {

                  a.actionPerformed(null);

              }
        }


    }

    class ExitListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
//            try {
//                snd = new aSound(aSound.sfxButton, false);
//                try {
//                    snd.Play();
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(ADialog.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            } catch (MalformedURLException ex) {
//                Logger.getLogger(ADialog.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (UnsupportedAudioFileException ex) {
//                Logger.getLogger(ADialog.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (IOException ex) {
//                Logger.getLogger(ADialog.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (LineUnavailableException ex) {
//                Logger.getLogger(ADialog.class.getName()).log(Level.SEVERE, null, ex);
//            }
            setVisible(false);
            setLocationRelativeTo(null);
        }
    }
}
