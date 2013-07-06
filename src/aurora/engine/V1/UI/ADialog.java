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
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.apache.log4j.Logger;


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
    private JPanel paneDialogBG;

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

    static final Logger logger = Logger.getLogger(ADialog.class);

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
    public ADialog(int Type, String Text, Font font, ActionListener al) {
        setButtonListener(al);
        this.Type = Type;
        this.Text = Text;
        this.font = font;
        if (Type == aDIALOG_WARNING) {


            iconImg = new AImage("app_icon_dialog_warning.png", 100, 100);


        } else if (Type == aDIALOG_ERROR) {

            iconImg = new AImage("app_icon_dialog_error.png", 100, 100);

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
    public ADialog(int Type, String Text, ActionListener al) {
        setButtonListener(al);
        this.Type = Type;
        this.Text = Text;
        font = new Font("Arial", Font.BOLD, 20);

        if (Type == aDIALOG_WARNING) {

            iconImg = new AImage("app_icon_dialog_warning.png", 100, 100);



        } else if (Type == aDIALOG_ERROR) {


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

            iconImg = new AImage("app_icon_dialog_warning.png", 100, 100);



        } else if (Type == aDIALOG_ERROR) {

            iconImg = new AImage("app_icon_dialog_error.png", 100, 100);

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
        font = new Font("Arial", Font.BOLD, 20);

        if (Type == aDIALOG_WARNING) {


            iconImg = new AImage("app_icon_dialog_warning.png", 100, 100);


        } else if (Type == aDIALOG_ERROR) {

            iconImg = new AImage("app_icon_dialog_error.png", 100, 100);
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
     * .........................................................................
     *
     * @param listener ActionListener
     *
     */
    public void setOKButtonListener(ActionListener listener) {
        a = listener;
        btnOk.addActionListener(a);
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


        paneDialogBG = new AImagePane("app_dialog_bg.png",555,250);
        paneDialogBG.setLayout(new BorderLayout());
        add(BorderLayout.CENTER, paneDialogBG);

        //* Config Dialog *//
        setUndecorated(true);
        setSize(555, 250);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);

        iconImg.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        iconContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        iconContainer.setLayout(new BoxLayout(iconContainer, BoxLayout.Y_AXIS));
        iconContainer.setOpaque(false);
        iconContainer.add(Box.createVerticalStrut(15));
        iconContainer.add(iconImg);
        iconContainer.add(Box.createVerticalStrut(10));
        paneDialogBG.add(iconContainer, BorderLayout.NORTH);


        textContainer = new JPanel(new FlowLayout(FlowLayout.CENTER,5,12));
        textContainer.setOpaque(false);
        lblText = new ASlickLabel(Text);
        lblText.setFont(font);
        lblText.setForeground(Color.LIGHT_GRAY);
        textContainer.add(lblText);


        //BOTTOM

        paneDialogBG.add(textContainer, BorderLayout.CENTER);

        btnOk = new AButton("app_btn_okDialog_norm.png",
                "app_btn_okDialog_down.png", "app_btn_okDialog_over.png");
        btnCancel = new AButton("app_btn_cancelDialog_norm.png",
                "app_btn_cancelDialog_down.png", "app_btn_cancelDialog_over.png");
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


        Bottom = new JPanel(new BorderLayout(0, 0));
        pnlButtonContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        pnlButtonContainer.setOpaque(false);
        pnlButtonContainer.add(btnCancel);
        pnlButtonContainer.add(btnOk);


        Bottom.setOpaque(false);
        Bottom.add(pnlButtonContainer, BorderLayout.EAST);
        paneDialogBG.add(BorderLayout.PAGE_END, Bottom);

        btnCancel.addActionListener(new ExitListener());

        paneDialogBG.getInputMap().put(KeyStroke.getKeyStroke(
                java.awt.event.KeyEvent.VK_ENTER, 0), "enterDown");

        paneDialogBG.getActionMap().put("enterDown", new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                a.actionPerformed(null);
            }
        });



        requestFocusInWindow();

    }

    class ExitListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
            setLocationRelativeTo(null);
        }
    }
}
