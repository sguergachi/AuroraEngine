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

import java.io.File;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Aurora Surface Implementation through the Engine
 * @author Sammy
 */
public class aSurface {

    private String toLookFor;
    private String locationPath;
    private String SurfaceLocation;
    private File[] list;
    private StringBuffer s;


    /**
     * Used as part of the Aurora Surface technology the Resource SMnager finds
     * the Surface and allows Aurora engine powered apps to utilize it
     *
     * String locationPath, the location of the Directory of the Surface
     */
    public aSurface(String locationPath) {
        this.locationPath = locationPath;
       
        findSurface();
    }

    /**
     * Used as part of the Aurora Surface technology the Resource SMnager finds
     * the Surface and allows Aurora engine powered apps to utilize it
     *
     * String locationPath, the location of the Directory of the Surface
     *
     * String SurfaceName, if you know the specific Surface you want to use THIS
     * WILL FALL BACK TO NORMAL SEARCH IF IT DOES NOT FIND THE SURFACE YOU
     * REQUESED
     */
    public aSurface(String SurfaceName, String locationPath) {
        toLookFor = SurfaceName;
        this.locationPath = locationPath;

        findSurface();
    }

    public String getSurfacePath() {

        return SurfaceLocation;

    }

    /**
     * look in folder for a surface file
     *
     * @return Surface Path
     */
    private void findSurface() {
        try {
            s = new StringBuffer(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath().toString());
        } catch (URISyntaxException ex) {
            Logger.getLogger(aSurface.class.getName()).log(Level.SEVERE, null, ex);
        }

        //System.out.println("pre parssed path " + s.toString());
        s.delete(0,1);
        s.delete(s.length() - "/AuroraEngine.jar".length(), s.length());
        s.append(locationPath);

        //System.out.println("path " + s.toString());

        aFileManager mngr = new aFileManager(s.toString(), true);

        

        //If check if we know what Surface we are looking for
        if (toLookFor == null) {
            list = mngr.finder("_Surface");
            //If not then look for first Surface and use it
            if ( list != null && list.length > 0) {
                //System.out.println(" Files" + getClass().getResource(locationPath).getPath().toString());
                SurfaceLocation = list[0].toString();
            } else {
                SurfaceLocation = null;
            }


        } else {

            list = mngr.finder(toLookFor + "_Surface");

            //If yes then look for that Surface and use it
            if (list.length > 0) {

                SurfaceLocation = list[0].toString();

            } else {

                list = mngr.finder("_Surface");

                //If specific surface does not exist, fallback to finding first surface and using it
                if (list.length > 0) {

                    SurfaceLocation = list[0].toString();
                } else {
                    SurfaceLocation = null;
                }
            }

        }

        if(SurfaceLocation != null){
        SurfaceLocation = "jar:file:/" + SurfaceLocation.replace("\\", "/")+"!";
        
        }
        
        //System.out.println("surface location " + SurfaceLocation);
    }

}
