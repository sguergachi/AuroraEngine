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
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.AbstractButton;
import javax.swing.*;

/**
 * .------------------------------------------------------------------------.
 * | ADialog
 * .------------------------------------------------------------------------.
 * |
 * |The ADialog is a custom built Dialog which can represent either an error
 * |or a warning. 
 * |The ADialog accepts a custom ActionListener for the "OK" Button. 
 * |
 * .........................................................................
 *
 * @author Sammy Guergachi <sguergachi at gmail.com> & Marius Brandt <marius dot brandt at hotmail dot com>
 * 
 */
public final class ADialog extends ADragFrame {

	/**
	 * Type definition.
	 */
    private int Type;
    
    /**
     * Custom text.
     */
    private String Text;
    
    /**
     * Type Constant.
     */
    public static final int aDIALOG_WARNING = 2;
    
    /**
     * Type Constant.
     */
    public static final int aDIALOG_ERROR = 1;
    
    /**
     * Background.
     */
    private JPanel img;
    
    /**
     * Custom text label.
     */
    private ASlickLabel lblText;
    
    /**
     * OK button.
     */
    private AButton btnOk;
    
    /**
     * Cancel button.
     */
    private AButton btnCancel;
    
    /**
     * Panel for buttons.
     */
    private JPanel Bottom;
    
    /**
     * Button container.
     */
    private JPanel pnlButtonContainer;
    
    /**
     * Custom ActionListener.
     */
    private ActionListener a;
    
    /**
     * Sound.
     */
    //private aSound snd;
    
    /**
     * Custom Font.
     */
    private Font font;
    
    /**
     * Icon Panel.
     */
    private JLabel iconImg;
    
    /**
     * Icon container.
     */
    public JPanel iconContainer;
    
    /**
     * Text container.
     */
    public JPanel textContainer;
    
    
    /**
     * .-----------------------------------------------------------------------.
     * | ADialog(int Type, String Text, Font font, ActionListener al)
     * .-----------------------------------------------------------------------.
     * |
     * |This constructor takes the type of dialog that should be shown (error or warning),
     * |the text that the user wants to display,
     * |a custom font and an ActionListener. 
     * |The custom ActionListener is mapped to the OK button. 
     * |After the initialization, the dialog is finished and can be shown via 
     * |.setVisible(true) 
     * | 
     * .........................................................................
     *
     * @param Type int, Text String, font Font, al ActionListener
     *
     */
    
    public ADialog(int Type, String Text, Font font, ActionListener al){
    	setButtonListener(al);
    	this.Type = Type;
        this.Text = Text;
        this.font = font;
        if (Type == aDIALOG_WARNING) {

            img = new AImagePane("app_dialog_bg.png");
            img.setLayout(new BorderLayout());
            add(BorderLayout.CENTER, img);
                
            iconImg = new AImage("app_icon_dialog_warning.png");


        } else if (Type == aDIALOG_ERROR) {

            img = new AImagePane("app_dialog_bg.png");
            img.setLayout(new BorderLayout());
            add(BorderLayout.CENTER, img);
                
            iconImg = new AImage("app_icon_dialog_error.png");

        }
        showDialog();
    }
    
    /**
     * .-----------------------------------------------------------------------.
     * | ADialog(int Type, String Text, ActionListener al)
     * .-----------------------------------------------------------------------.
     * |
     * |This constructor takes the type of dialog that should be shown (error or warning),
     * |the text that the user wants to display,
     * |a default font will be used instead of a custom one, and an ActionListener. 
     * |The custom ActionListener is mapped to the OK button. 
     * |After the initialization, the dialog is finished and can be shown via 
     * |.setVisible(true) 
     * | 
     * .........................................................................
     *
     * @param Type int, Text String, al ActionListener
     *
     */
    
    public ADialog(int Type, String Text, ActionListener al){
    	setButtonListener(al);
    	this.Type = Type;
        this.Text = Text;
        font = new Font("Arial", Font.PLAIN, 20);

        if (Type == aDIALOG_WARNING) {
        	img = new AImagePane("app_dialog_bg.png");
        	img.setLayout(new BorderLayout());                
        	add(BorderLayout.CENTER, img);
                
        	iconImg = new AImage("app_icon_dialog_warning.png");



        } else if (Type == aDIALOG_ERROR) {


        	img = new AImagePane("app_dialog_bg.png");
        	img.setLayout(new BorderLayout());
        	add(BorderLayout.CENTER, img);
                
        	iconImg = new AImage("app_icon_dialog_error.png");
        }

        showDialog();

    }
    
    /**
     * .-----------------------------------------------------------------------.
     * | ADialog(int Type, String Text, Font font)
     * .-----------------------------------------------------------------------.
     * |
     * |This constructor takes the type of dialog that should be shown (error or warning),
     * |the text that the user wants to display and a custom font.
     * |However, a default ActionListener will be used (OK button closes the dialog).
     * |After the initialization, the dialog is finished and can be shown via 
     * |.setVisible(true) 
     * | 
     * .........................................................................
     *
     * @param Type int, Text String, font Font
     *
     */
    
    public ADialog(int Type, String Text, Font font) {


        this.Type = Type;
        this.Text = Text;
        this.font = font;

        if (Type == aDIALOG_WARNING) {

            img = new AImagePane("app_dialog_bg.png");
            img.setLayout(new BorderLayout());
            add(BorderLayout.CENTER, img);
            
            iconImg = new AImage("app_icon_dialog_warning.png");



        } else if (Type == aDIALOG_ERROR) {

            img = new AImagePane("app_dialog_bg.png");
            img.setLayout(new BorderLayout());
            add(BorderLayout.CENTER, img);
            
            iconImg = new AImage("app_icon_dialog_error.png");

        }
        showDialog();
    }
    
    /**
     * .-----------------------------------------------------------------------.
     * | ADialog(int Type, String Text)
     * .-----------------------------------------------------------------------.
     * |
     * |This constructor takes the type of dialog that should be shown (error or warning),
     * |and text that the user wants to display.
     * |However, a default font and a default ActionListener will be used (OK button closes the dialog).
     * |After the initialization, the dialog is finished and can be shown via 
     * |.setVisible(true) 
     * | 
     * .........................................................................
     *
     * @param Type int, Text String
     *
     */

    public ADialog(int Type, String Text) {


        this.Type = Type;
        this.Text = Text;
        font = new Font("Arial", Font.PLAIN, 20);

        if (Type == aDIALOG_WARNING) {

            img = new AImagePane("app_dialog_bg.png");
            img.setLayout(new BorderLayout());
            add(BorderLayout.CENTER, img);
            
            iconImg = new AImage("app_icon_dialog_warning.png");


        } else if (Type == aDIALOG_ERROR) {


            img = new AImagePane("app_dialog_bg.png");
            img.setLayout(new BorderLayout());
            add(BorderLayout.CENTER, img);
            
            iconImg = new AImage("app_icon_dialog_error.png");
        }

        showDialog();


    }
    
    /**
     * .-----------------------------------------------------------------------.
     * | setButtonListener(ActionListener a)
     * .-----------------------------------------------------------------------.
     * |
     * |This function is used by the constructors to assign a custom ActionListener.
     * |This function is not meant to be used from outside the class.
     * |To assign a custom handler manually, see setOKButtonListener.
     * | 
     * .........................................................................
     *
     * @param a ActionListener
     *
     */
    
    private void setButtonListener(ActionListener a) {
        this.a = a;
    }
    
    /**
     * .-----------------------------------------------------------------------.
     * | setOKButtonListener(ActionListener listener)
     * .-----------------------------------------------------------------------.
     * |
     * | This function requires a custom ActionListener that has to be passed like this
     * | .setOKButtonListener(new YourListenerClass());
     * | This function will also override any previous assigned ActionListeners
     * | AND the default listener.
     * | Include setVisible(false); in your listener to close the dialog.
     * | 
     * | 
     * | 
     * |
     * |
     * .........................................................................
     *
     * @param listener ActionListener
     *
     */
    
    public void setOKButtonListener(ActionListener listener){
    	btnOk.removeActionListener(a);
    	btnOk.addActionListener(listener);
    }

    /**
     * .-----------------------------------------------------------------------.
     * | showDialog
     * .-----------------------------------------------------------------------.
     * |
     * |This function prepares the dialog.
     * |Buttons, text etc. are added and the dialog is ready.
     * |But this function does not make the dialog visible (see constructor notice) 
     * | 
     * .........................................................................
     *
     */
    
    public void showDialog() {

        /// Frame Config
        setUndecorated(true);
        setSize(490, 230);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        
        iconContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        iconContainer.setOpaque(false);
        iconContainer.add(iconImg);
        img.add(iconContainer,BorderLayout.NORTH);
        
        
        textContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        textContainer.setOpaque(false);
        lblText = new ASlickLabel(Text + "   ");
        lblText.setFont(font);
        lblText.setForeground(Color.LIGHT_GRAY);
        textContainer.add(lblText);


        //BOTTOM

        img.add(textContainer,BorderLayout.CENTER);

        btnOk = new AButton("app_btn_okDialog_norm.png", "app_btn_okDialog_down.png", "app_btn_okDialog_over.png");
        btnCancel = new AButton("app_btn_cancelDialog_norm.png", "app_btn_cancelDialog_down.png", "app_btn_cancelDialog_over.png"); //must insert correct cancel button images
        if (a != null) {
            btnOk.addActionListener(a);


        } else {
            a = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setVisible(false);
                }
            };
            btnOk.addActionListener(a);

        }


        Bottom = new JPanel(new BorderLayout());
        pnlButtonContainer = new JPanel();
        pnlButtonContainer.setOpaque(false);
        pnlButtonContainer.add(btnOk);
        pnlButtonContainer.add(btnCancel);
        
        Bottom.setOpaque(false);
        Bottom.add(pnlButtonContainer, BorderLayout.EAST);
        
        img.add(BorderLayout.PAGE_END, Bottom);
        img.addKeyListener(new EnterKeyListener());
        Bottom.addKeyListener(new EnterKeyListener());
        btnOk.addKeyListener(new EnterKeyListener());
        btnCancel.addActionListener(new ExitListener());
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
