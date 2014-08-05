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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.logging.Level;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;
import org.apache.log4j.Logger;

/**
 * Code derived from "http://theuzo007.wordpress.com/2013/10/26/joystick-in-java-with-jinput-v2/"
 * <p>
 * @author Sammy Guergachi <sguergachi at gmail.com>
 */
public class AJinputController {

    static final Logger logger = Logger.getLogger(AJinputController.class);

    // Controllers
    private ArrayList<Controller> foundControllers = new ArrayList<>();

    //
    // Listener Arrays
    //
    private ActionListener listener_a_button;

    private ActionListener listener_b_button;

    private ActionListener listener_x_button;

    private ActionListener listener_y_button;

    private ActionListener listener_rb_button;

    private ActionListener listener_lb_button;

    private ActionListener listener_hat_up_button;

    private ActionListener listener_hat_down_button;

    private ActionListener listener_hat_right_button;

    private ActionListener listener_hat_left_button;

    private ActionListener listener_lanalog_right;

    private ActionListener listener_lanalog_left;

    private ActionListener listener_lanalog_up;

    private ActionListener listener_lanalog_down;

    public void loadControllers() {
        Controller[] controllers = ControllerEnvironment.getDefaultEnvironment()
                .getControllers();

        for (int i = 0; i < controllers.length; i++) {
            Controller controller = controllers[i];

            if (controller.getType() == Controller.Type.STICK
                        || controller.getType() == Controller.Type.GAMEPAD
                        || controller.getType() == Controller.Type.WHEEL
                        || controller.getType() == Controller.Type.FINGERSTICK) {

                logger.info("Controller Type: " + controller.getType());
                logger.info("Controller Name: " + controller.getName());

                // Add new controller to the list of all controllers.
                foundControllers.add(controller);

            }
        }

        startListeningToControllers();
    }

    void startListeningToControllers() {
        AThreadWorker controllerListener = new AThreadWorker(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (foundControllers.size() > 0) {
                            // Currently selected controller.
                            Controller controller = foundControllers.get(0);

                            // Pull controller for current data, and break loop if controller is disconnected.
                            if (!controller.poll()) {
                                ((AThreadWorker) e.getSource()).stop();
                            }

                            // X axis and Y axis
                            int xAxisPercentage = 0;
                            int yAxisPercentage = 0;

                            // Go trough all components of the controller.
                            Component[] components = controller.getComponents();
                            net.java.games.input.EventQueue queue = controller
                            .getEventQueue();
                            Event event = new Event();


                            while (queue.getNextEvent(event)) {
                                Component component = event.getComponent();
                                Component.Identifier componentIdentifier
                                                     = component.getIdentifier();

                                // Buttons
                                if (componentIdentifier.getName().matches(
                                        "^[0-9]*$")) { // If the component identifier name contains only numbers, then this is a button.
                                    // Is button pressed?
                                    boolean isItPressed = true;
                                    if (component.getPollData() == 0.0f) {
                                        isItPressed = false;
                                    }

                                    // Button index
                                    int buttonIndex;
                                    buttonIndex = Integer.parseInt(component
                                            .getIdentifier().toString().trim());

                                    // Create and add new button to panel.
                                    logger.info("button Press  " + buttonIndex
                                                + " isPressed? " + isItPressed);

                                    // A Button Pressed
                                    if (listener_a_button != null && buttonIndex
                                                                     == 0
                                        && isItPressed) {
                                        listener_a_button.actionPerformed(
                                                new ActionEvent(this, 0, null));
                                    } // B Button Pressed
                                    else if (listener_a_button != null
                                             && buttonIndex == 1 && isItPressed) {
                                        listener_b_button.actionPerformed(
                                                new ActionEvent(this, 0, null));
                                    }// X Button Pressed
                                    else if (listener_a_button != null
                                             && buttonIndex == 2 && isItPressed) {
                                        listener_x_button.actionPerformed(
                                                new ActionEvent(this, 0, null));
                                    }// Y Button Pressed
                                    else if (listener_a_button != null
                                             && buttonIndex == 3 && isItPressed) {
                                        listener_y_button.actionPerformed(
                                                new ActionEvent(this, 0, null));
                                    }// Left Bumper Pressed
                                    else if (listener_lb_button != null
                                             && buttonIndex == 4 && isItPressed) {
                                        listener_lb_button.actionPerformed(
                                                new ActionEvent(this, 0, null));
                                    }// Right Bumper Pressed
                                    else if (listener_rb_button != null
                                             && buttonIndex == 5 && isItPressed) {
                                        listener_rb_button.actionPerformed(
                                                new ActionEvent(this, 0, null));
                                    }



                                    // We know that this component was button so we can skip to next component.
                                    continue;
                                }

                                // Hat switch
                                if (componentIdentifier
                                    == Component.Identifier.Axis.POV) {
                                    float hatSwitchPosition = component
                                    .getPollData();
                                    logger.info("Hatswitch Position "
                                                + hatSwitchPosition);

                                    // Hat Right
                                    if (hatSwitchPosition == 0.5
                                        && listener_hat_right_button != null) {
                                        listener_hat_right_button
                                        .actionPerformed(
                                                new ActionEvent(this, 0, null));
                                    } // Hat Left
                                    else if (hatSwitchPosition == 1.0
                                             && listener_hat_left_button != null) {
                                        listener_hat_left_button
                                        .actionPerformed(
                                                new ActionEvent(this, 0, null));
                                    } // Hat Up
                                    else if (hatSwitchPosition == 0.25
                                             && listener_hat_up_button != null) {
                                        listener_hat_up_button.actionPerformed(
                                                new ActionEvent(this, 0, null));
                                    } // Hat Down
                                    else if (hatSwitchPosition == 0.75
                                             && listener_hat_down_button != null) {
                                        listener_hat_down_button
                                        .actionPerformed(
                                                new ActionEvent(this, 0, null));
                                    }

                                    // We know that this component was hat switch so we can skip to next component.
                                    continue;
                                }




                            }

                            for (int i = 0; i < components.length; i++) {
                                Component component = components[i];
                                Component.Identifier componentIdentifier
                                                     = component.getIdentifier();
                                // Axes
                                if (component.isAnalog()) {
                                    float axisValue = component.getPollData();
                                    int axisValueInPercentage
                                        = getAxisValueInPercentage(axisValue);

                                    // X axis
                                    if (componentIdentifier
                                        == Component.Identifier.Axis.X) {
                                        xAxisPercentage = axisValueInPercentage;
//                            logger.info("xAxisPercentage " + xAxisPercentage);
                                        if (listener_lanalog_right != null
                                            && xAxisPercentage > 75) {
                                            listener_lanalog_right
                                            .actionPerformed(new ActionEvent(
                                                            this, 0, null));
                                        } else if (listener_lanalog_left != null
                                                   && xAxisPercentage < 25) {
                                            listener_lanalog_left
                                            .actionPerformed(new ActionEvent(
                                                            this, 0, null));
                                        }
                                        continue; // Go to next component.
                                    }
                                    // Y axis
                                    if (componentIdentifier
                                        == Component.Identifier.Axis.Y) {
                                        yAxisPercentage = axisValueInPercentage;
//                            logger.info("yAxisPercentage " + yAxisPercentage);
                                        if (listener_lanalog_up != null
                                            && yAxisPercentage > 75) {
                                            listener_lanalog_up.actionPerformed(
                                                    new ActionEvent(this, 0,
                                                                    null));
                                        } else if (listener_lanalog_down != null
                                                   && yAxisPercentage < 25) {
                                            listener_lanalog_down
                                            .actionPerformed(new ActionEvent(
                                                            this, 0, null));
                                        }
                                        continue; // Go to next component.
                                    }

                                    // Other axis
//                        logger.info("axisValueInPercentage " + axisValueInPercentage);


                                    try {
                                        Thread.sleep(15);
                                    } catch (InterruptedException ex) {
                                        java.util.logging.Logger.getLogger(
                                                AJinputController.class
                                                .getName()).log(Level.SEVERE,
                                                                null, ex);
                                    }
                                }

                            }


                            try {
                                Thread.sleep(30);
                            } catch (InterruptedException ex) {
                                java.util.logging.Logger.getLogger(
                                        AJinputController.class.getName()).log(
                                        Level.SEVERE, null, ex);
                            }


                        }

                    }
                }, 50);

        controllerListener.start();
    }

    // A button
    public void setListener_A_Button(ActionListener listener) {
        listener_a_button = (listener);
    }

    public void clearListener_A_Button() {
        listener_a_button = null;
    }

    // B button
    public void setListener_B_Button(ActionListener listener) {
        listener_b_button = listener;
    }

    public void clearListener_B_Button() {
        listener_b_button = null;
    }

    // X button
    public void setListener_X_Button(ActionListener listener) {
        listener_x_button = (listener);
    }

    public void clearListener_X_Button() {
        listener_x_button = null;
    }

    // Y button
    public void setListener_Y_Button(ActionListener listener) {
        listener_y_button = (listener);
    }

    public void clearListener_Y_Button() {
        listener_y_button = null;
    }

    // LB button
    public void setListener_LB_Button(ActionListener listener) {
        listener_lb_button = (listener);
    }

    public void clearListener_LB_Button() {
        listener_lb_button = null;
    }

    // RB button
    public void setListener_RB_Button(ActionListener listener) {
        listener_rb_button = (listener);
    }

    public void clearListener_RB_Button() {
        listener_rb_button = null;
    }

    // Hat Right
    public void setListener_HAT_Right_Button(ActionListener listener) {
        listener_hat_right_button = (listener);
    }

    public void clearListener_HAT_Right_Button() {
        listener_hat_right_button = null;
    }

    // Hat Left
    public void setListener_HAT_Left_Button(ActionListener listener) {
        listener_hat_left_button = (listener);
    }

    public void clearListener_HAT_Left_Button() {
        listener_hat_left_button = null;
    }

    // Hat Up
    public void setListener_HAT_Up_Button(ActionListener listener) {
        listener_hat_up_button = (listener);
    }

    public void clearListener_HAT_Up_Button() {
        listener_hat_up_button = null;
    }

    // Hat Down
    public void setListener_HAT_Down_Button(ActionListener listener) {
        listener_hat_down_button = (listener);
    }

    public void clearListener_HAT_Down_Button() {
        listener_hat_down_button = null;
    }

    // Analog Down
    public void setListener_ANALOG_Down_Button(ActionListener listener) {
        listener_lanalog_down = (listener);
    }

    public void clearListener_ANALOG_Down_Button() {
        listener_lanalog_down = null;
    }

    // Analog Up
    public void setListener_ANALOG_Up_Button(ActionListener listener) {
        listener_lanalog_up = (listener);
    }

    public void clearListener_ANALOG_Up_Button() {
        listener_lanalog_up = null;
    }

    // Analog Left
    public void setListener_ANALOG_Left_Button(ActionListener listener) {
        listener_lanalog_left = (listener);
    }

    public void clearListener_ANALOG_Left_Button() {
        listener_lanalog_left = null;
    }

    // Analog Right
    public void setListener_ANALOG_Right_Button(ActionListener listener) {
        listener_lanalog_right = (listener);
    }

    public void clearListener_ANALOG_Right_Button() {
        listener_lanalog_right = null;
    }

    public void clearAllListeners() {
        clearListener_Y_Button();
        clearListener_X_Button();
        clearListener_B_Button();
        clearListener_A_Button();
        clearListener_LB_Button();
        clearListener_RB_Button();
    }

    public ArrayList<Controller> getFoundControllers() {
        return foundControllers;
    }

    public ActionListener getListener_a_button() {
        return listener_a_button;
    }

    public ActionListener getListener_b_button() {
        return listener_b_button;
    }

    public ActionListener getListener_x_button() {
        return listener_x_button;
    }

    public ActionListener getListener_y_button() {
        return listener_y_button;
    }

    public ActionListener getListener_rb_button() {
        return listener_rb_button;
    }

    public ActionListener getListener_lb_button() {
        return listener_lb_button;
    }

    public ActionListener getListener_hat_up_button() {
        return listener_hat_up_button;
    }

    public ActionListener getListener_hat_down_button() {
        return listener_hat_down_button;
    }

    public ActionListener getListener_hat_right_button() {
        return listener_hat_right_button;
    }

    public ActionListener getListener_hat_left_button() {
        return listener_hat_left_button;
    }

    /**
     * Given value of axis in percentage.
     * Percentages increases from left/top to right/bottom.
     * If idle (in center) returns 50, if joystick axis is pushed to the left/top
     * edge returns 0 and if it's pushed to the right/bottom returns 100.
     * <p>
     * @return value of axis in percentage.
     */
    public static int getAxisValueInPercentage(float axisValue) {
        return (int) (((2 - (1 - axisValue)) * 100) / 2);
    }

}
