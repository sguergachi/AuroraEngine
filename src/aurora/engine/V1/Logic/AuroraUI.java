/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aurora.engine.V1.Logic;

/**
 *
 * @author Sammy Guergachi <sguergachi at gmail.com>
 */
public interface AuroraUI {

    /**
     * .-----------------------------------------------------------------------.
     * | loadUI() Method
     * .-----------------------------------------------------------------------.
     * |
     * |
     * | This method performs the initial loading of all UI components in this
     * | screen. This is the first method that is called on creation of a
     * | screen.
     * |
     * | In this method the following should be
     * | allowed to happen:
     * | * Creation of UI components
     * | * Creation of UI components dependencies
     * | * In-line configuration of components only (via parameters)
     * | * Obligatory pre-configuration should visibly be created and
     * | configured first.
     * |
     * |
     * .
     */
    void loadUI();

    /**
     * .-----------------------------------------------------------------------.
     * | buildUI() Method
     * .-----------------------------------------------------------------------.
     * |
     * |
     * | This method creates the UI by first configuring it to the UI needs
     * | then it should add the UI components to the appropriate Canvas.
     * |
     * |
     * | The method will perform this after the loadUI() method has
     * | accomplished creating all necessary objects to allow for
     * | the building process to occur more then one time.
     * | Divide the method into two:
     * | 1. Configure components in the order they are created
     * | 2. Add the components to the canvas in the order they are seen
     * |
     * |
     * .
     */
    void buildUI();

    /**
     * .-----------------------------------------------------------------------.
     * | addToCanvas() Method
     * .-----------------------------------------------------------------------.
     * |
     * |
     * | This method adds all of the loaded, configured and built UI components
     * | to the Canvas of the Aurora Application.
     * |
     * |
     * | This method should be used to redraw and re-add to cleared canvas, not
     * | the buildUI() method.
     * |
     * .
     */
    void addToCanvas();
}
