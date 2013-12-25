/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aurora.engine.V1.UI;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;

/**
 *
 * @author Sammy Guergachi <sguergachi at gmail.com>
 */
public class APopupMenu extends JPopupMenu {

    {
        // need to disable that to work
        setLightWeightPopupEnabled(false);
        UIManager.put("PopupMenu.background", new Color(0,0,0,0));
        UIManager.put("PopupMenu.border", BorderFactory.createEmptyBorder());
    }


}
