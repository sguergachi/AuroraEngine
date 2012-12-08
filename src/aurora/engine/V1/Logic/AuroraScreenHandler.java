/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aurora.engine.V1.Logic;

/**
 *
 * @author Sammy Guergachi <sguergachi at gmail.com>
 */
public interface AuroraScreenHandler {

    /**
     * .-----------------------------------------------------------------------.
     * | setLogic(DashboardLogic)
     * .-----------------------------------------------------------------------.
     * |
     * | Pass the Logic Instanced in the AuroraUI component of this Screen
     * | For the Handler to work the Logic *MUST* be passed using this method
     * |
     * |
     * .........................................................................
     * <p/>
     * @param logic AuroraScreenLogic
     */
    void setLogic(final AuroraScreenLogic logic);
}
