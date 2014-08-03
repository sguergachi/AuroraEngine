/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aurora.engine.V1.Logic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.logging.Level;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import org.apache.log4j.Logger;

/**
 *
 * @author Sammy Guergachi <sguergachi at gmail.com>
 */
public class AJinputController {

    static final Logger logger = Logger.getLogger(AJinputController.class);

    // Controllers
    private ArrayList<Controller> foundControllers = new ArrayList<>();

    //
    // Listener Arrays
    //
    private ArrayList<ActionListener> listener_a_button = new ArrayList<>();
    private ArrayList<ActionListener> listener_b_button = new ArrayList<>();
    private ArrayList<ActionListener> listener_x_button = new ArrayList<>();
    private ArrayList<ActionListener> listener_y_button = new ArrayList<>();
    private ArrayList<ActionListener> listener_rb_button = new ArrayList<>();
    private ArrayList<ActionListener> listener_lb_button = new ArrayList<>();
    private ActionListener listener_hat_up_button;
    private ActionListener listener_hat_down_button;
    private ActionListener listener_hat_right_button;
    private ActionListener listener_hat_left_button;

    public void loadControllers() {
        Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();

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
        AThreadWorker controllerListener = new AThreadWorker(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

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
                for (int i = 0; i < components.length; i++) {
                    Component component = components[i];
                    Component.Identifier componentIdentifier = component.getIdentifier();

                    // Buttons
                    if (componentIdentifier.getName().matches("^[0-9]*$")) { // If the component identifier name contains only numbers, then this is a button.
                        // Is button pressed?
                        boolean isItPressed = true;
                        if (component.getPollData() == 0.0f) {
                            isItPressed = false;
                        }

                        // Button index
                        int buttonIndex;
                        buttonIndex = Integer.parseInt(component.getIdentifier().toString().trim());

                        // Create and add new button to panel.
                        logger.info("button Press  " + buttonIndex + " isPressed? " + isItPressed);

                        // A Button Pressed
                        if (buttonIndex == 0 && isItPressed) {
                            listener_a_button.get(0).actionPerformed(new ActionEvent(this, 0, null));
                        } // B Button Pressed
                        else if (buttonIndex == 1 && isItPressed) {
//                            for (int a = 0; a < listener_b_button.size(); a++) {
                            listener_b_button.get(0).actionPerformed(new ActionEvent(this, 0, null));
//                            }
                        }// X Button Pressed
                        else if (buttonIndex == 2 && isItPressed) {
//                            for (int a = 0; a < listener_x_button.size(); a++) {
                            listener_x_button.get(0).actionPerformed(new ActionEvent(this, 0, null));
//                            }
                        }// Y Button Pressed
                        else if (buttonIndex == 2 && isItPressed) {
//                            for (int a = 0; a < listener_y_button.size(); a++) {
                            listener_y_button.get(0).actionPerformed(new ActionEvent(this, 0, null));
//                            }
                        }// Left Bumper Pressed
                        else if (buttonIndex == 4 && isItPressed) {
//                            for (int a = 0; a < listener_lb_button.size(); a++) {
                            listener_lb_button.get(0).actionPerformed(new ActionEvent(this, 0, null));
//                            }
                        }// Right Bumper Pressed
                        else if (buttonIndex == 5 && isItPressed) {
//                            for (int a = 0; a < listener_rb_button.size(); a++) {
                            listener_rb_button.get(0).actionPerformed(new ActionEvent(this, 0, null));
//                            }
                        }



                        // We know that this component was button so we can skip to next component.
                        continue;
                    }

                    // Hat switch
                    if (componentIdentifier == Component.Identifier.Axis.POV) {
                        float hatSwitchPosition = component.getPollData();
                        logger.info("Hatswitch Position " + hatSwitchPosition);

                        // Hat Right
                        if (hatSwitchPosition == 0.5 && listener_hat_right_button != null) {
                            listener_hat_right_button.actionPerformed(new ActionEvent(this, 0, null));
                        } // Hat Left
                        else if (hatSwitchPosition == 1.0 && listener_hat_left_button != null) {
                            listener_hat_left_button.actionPerformed(new ActionEvent(this, 0, null));
                        } // Hat Up
                        else if (hatSwitchPosition == 0.25 && listener_hat_up_button != null) {
                            listener_hat_up_button.actionPerformed(new ActionEvent(this, 0, null));
                        } // Hat Down
                        else if (hatSwitchPosition == 0.75 && listener_hat_down_button != null) {
                            listener_hat_down_button.actionPerformed(new ActionEvent(this, 0, null));
                        }

                        // We know that this component was hat switch so we can skip to next component.
                        continue;
                    }

                    // Axes
                    if (component.isAnalog()) {
                        float axisValue = component.getPollData();
                        int axisValueInPercentage = getAxisValueInPercentage(axisValue);

                        // X axis
                        if (componentIdentifier == Component.Identifier.Axis.X) {
                            xAxisPercentage = axisValueInPercentage;
                            logger.info("xAxisPercentage " + xAxisPercentage);
                            continue; // Go to next component.
                        }
                        // Y axis
                        if (componentIdentifier == Component.Identifier.Axis.Y) {
                            yAxisPercentage = axisValueInPercentage;
                            logger.info("yAxisPercentage " + yAxisPercentage);

                            continue; // Go to next component.
                        }

                        // Other axis
                        logger.info("axisValueInPercentage " + axisValueInPercentage);
                    }

                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ex) {
                        java.util.logging.Logger.getLogger(AJinputController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }



            }
        }, 50);

        controllerListener.start();
    }

    // A button
    public void addListener_A_Button(ActionListener listener) {
        listener_a_button.clear();
        listener_a_button.add(listener);
    }

    public void clearListener_A_Button() {
        listener_a_button.clear();
    }

    // B button
    public void addListener_B_Button(ActionListener listener) {
        listener_b_button.clear();
        listener_b_button.add(listener);
    }

    public void clearListener_B_Button() {
        listener_b_button.clear();
    }

    // X button
    public void addListener_X_Button(ActionListener listener) {
        listener_x_button.clear();
        listener_x_button.add(listener);
    }

    public void clearListener_X_Button() {
        listener_x_button.clear();
    }

    // Y button
    public void addListener_Y_Button(ActionListener listener) {
        listener_y_button.clear();
        listener_y_button.add(listener);
    }

    public void clearListener_Y_Button() {
        listener_y_button.clear();
    }

    // LB button
    public void addListener_LB_Button(ActionListener listener) {
        listener_lb_button.clear();
        listener_lb_button.add(listener);
    }

    public void clearListener_LB_Button() {
        listener_lb_button.clear();
    }

    // RB button
    public void addListener_RB_Button(ActionListener listener) {
        listener_rb_button.clear();
        listener_rb_button.add(listener);
    }

    public void clearListener_RB_Button() {
        listener_rb_button.clear();
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

    public void clearAllListeners() {
        clearListener_Y_Button();
        clearListener_X_Button();
        clearListener_B_Button();
        clearListener_A_Button();
        clearListener_LB_Button();
        clearListener_RB_Button();
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
